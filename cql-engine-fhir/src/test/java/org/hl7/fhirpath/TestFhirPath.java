package org.hl7.fhirpath;

import ca.uhn.fhir.context.FhirContext;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhirpath.tests.Group;
import org.hl7.fhirpath.tests.OutputType;
import org.hl7.fhirpath.tests.Tests;
import org.opencds.cqf.cql.data.fhir.FhirDataProvider;
import org.opencds.cqf.cql.data.fhir.TestFhirLibrary;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.CqlLibraryReader;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Value;
import org.testng.annotations.Test;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Bryn on 12/14/2016.
 */
public class TestFhirPath {

    private FhirContext fhirContext = FhirContext.forDstu3();

    private Tests loadTests() {
        return JAXB.unmarshal(TestFhirPath.class.getResourceAsStream("stu3/tests-fhir-r3.xml"), Tests.class);
    }

    private Resource loadResource(String resourcePath) {
        return (Resource)fhirContext.newXmlParser().parseResource(
                new InputStreamReader(TestFhirPath.class.getResourceAsStream("stu3/input/" + resourcePath)));
    }

    private Iterable<Object> loadExpectedResults(org.hl7.fhirpath.tests.Test test) {
        List<Object> results = new ArrayList<>();
        if (test.getOutput() != null) {
            for (org.hl7.fhirpath.tests.Output output : test.getOutput()) {
                switch (output.getType()) {
                    case BOOLEAN:
                        results.add(Boolean.valueOf(output.getValue()));
                        break;
                    case DATE:
                        results.add(DateTime.fromJodaDateTime(org.joda.time.DateTime.parse(output.getValue())));
                        break;
                    case INTEGER:
                        results.add(Integer.valueOf(output.getValue()));
                        break;
                    case STRING:
                        results.add(output.getValue());
                        break;
                    case CODE:
                        results.add(output.getValue()); // TODO: This is probably wrong...
                        break;
                }
            }
        }

        return results;
    }

    private Library translate(String cql) {
//        try {
            ArrayList<CqlTranslator.Options> options = new ArrayList<>();
            options.add(CqlTranslator.Options.EnableDateRangeOptimization);
            CqlTranslator translator = CqlTranslator.fromText(cql, new LibraryManager(), options.toArray(new CqlTranslator.Options[options.size()]));
            if (translator.getErrors().size() > 0) {
                System.err.println("Translation failed due to errors:");
                ArrayList<String> errors = new ArrayList<>();
                for (CqlTranslatorException error : translator.getErrors()) {
                    TrackBack tb = error.getLocator();
                    String lines = tb == null ? "[n/a]" : String.format("[%d:%d, %d:%d]",
                            tb.getStartLine(), tb.getStartChar(), tb.getEndLine(), tb.getEndChar());
                    errors.add(lines + error.getMessage());
                }
                throw new IllegalArgumentException(errors.toString());
            }

//            // output translated library for review
//            xmlFile = new File("response.xml");
//            xmlFile.createNewFile();
//            PrintWriter pw = new PrintWriter(xmlFile, "UTF-8");
//            pw.println(translator.toXml());
//            pw.println();
//            pw.close();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

        Library library = null;
        try {
            library = CqlLibraryReader.read(new StringReader(translator.toXml()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return library;
    }

    private void runTest(org.hl7.fhirpath.tests.Test test) {
        Resource resource = loadResource(test.getInputfile());
        String cql = String.format("using FHIR version '1.8' parameter %s %s define Test: %s",
                resource.fhirType(), resource.fhirType(), test.getExpression().getValue());

        Library library = null;
        // If the test expression is invalid, expect an error during translation and fail if we don't get one
        boolean isInvalid = test.getExpression().isInvalid() != null && test.getExpression().isInvalid();

        if (isInvalid) {
            boolean testPassed = false;
            try {
                library = translate(cql);
            }
            catch (CqlTranslatorException e) {
                testPassed = true;
            }

            if (!testPassed) {
                throw new RuntimeException(String.format("Expected exception not thrown for test %s.", test.getName()));
            }
        }
        else {
            library = translate(cql);

            Context context = new Context(library);

            FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhirtest.uhn.ca/baseDstu3");
            //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://fhir3.healthintersections.com.au/open/");
            //FhirDataProvider provider = new FhirDataProvider().withEndpoint("http://wildfhir.aegis.net/fhir");
            context.registerDataProvider("http://hl7.org/fhir", provider);

            context.setParameter(null, resource.fhirType(), resource);

            Object result = context.resolveExpressionRef("Test").evaluate(context);
            Iterable<Object> actualResults;
            if (result instanceof Iterable) {
                actualResults = (Iterable<Object>) result;
            } else {
                List results = new ArrayList<>();
                results.add(result);
                actualResults = results;
            }

            Iterable<Object> expectedResults = loadExpectedResults(test);
            Iterator<Object> actualResultsIterator = actualResults.iterator();
            for (Object expectedResult : expectedResults) {
                if (actualResultsIterator.hasNext()) {
                    Object actualResult = actualResultsIterator.next();
                    Boolean comparison = Value.equals(expectedResult, actualResult);
                    if (comparison == null || !comparison) {
                        throw new RuntimeException("Actual result is not equal to expected result.");
                    }
                } else {
                    throw new RuntimeException("Actual result is not equal to expected result.");
                }
            }
        }
    }

    @Test
    public void testFhirPath() {
        // Load Test cases from org/hl7/fhirpath/stu3/tests-fhir-r3.xml
        // foreach test group:
        // foreach test case:
        // load the resource from inputFile
        // create a parameter named the resource type with the value of the resource
        // create a CQL library with the expression
        // evaluate the expression
        // validate that the result is equal to the output elements of the test
        Tests tests = loadTests();
        int testCounter = 0;
        int passCounter = 0;
        for (Group group : tests.getGroup()) {
            System.out.println(String.format("Running test group %s...", group.getName()));
            for (org.hl7.fhirpath.tests.Test test : group.getTest()) {
                testCounter += 1;
                try {
                    System.out.println(String.format("Running test %s...", test.getName()));
                    runTest(test);
                    passCounter += 1;
                    System.out.println(String.format("Test %s passed.", test.getName()));
                }
                catch (Exception e) {
                    System.out.println(String.format("Test %s failed with exception: %s", test.getName(), e.getMessage()));
                }
            }
            System.out.println(String.format("Finished test group %s.", group.getName()));
        }
        System.out.println(String.format("Passed %s of %s tests.", passCounter, testCounter));
    }
}
