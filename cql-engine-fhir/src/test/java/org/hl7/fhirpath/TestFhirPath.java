package org.hl7.fhirpath;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;

import ca.uhn.fhir.context.FhirContext;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhirpath.tests.Group;
import org.hl7.fhirpath.tests.Tests;
import org.opencds.cqf.cql.data.fhir.BaseFhirDataProvider;
import org.opencds.cqf.cql.data.fhir.FhirDataProviderDstu2;
import org.opencds.cqf.cql.data.fhir.FhirDataProviderHL7;
import org.opencds.cqf.cql.data.fhir.FhirDataProviderStu3;
import org.opencds.cqf.cql.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.CqlLibraryReader;
import org.opencds.cqf.cql.execution.LibraryLoader;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.DateTime;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Bryn on 12/14/2016.
 */
public class TestFhirPath {

    private FhirContext fhirContext = FhirContext.forDstu3();

    private Tests loadTestsFile(String testsFilePath) {
        try {
            InputStream testsFileRaw = TestFhirPath.class.getResourceAsStream(testsFilePath);
            return JAXB.unmarshal(testsFileRaw, Tests.class);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Couldn't load tests file ["+testsFilePath+"]: " + e.toString());
        }
    }

    private Resource loadResourceFile(String resourceFilePath) {
        return (Resource)fhirContext.newXmlParser().parseResource(
            new InputStreamReader(TestFhirPath.class.getResourceAsStream(resourceFilePath)));
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
                        results.add(new DateTime(output.getValue(), ZoneOffset.systemDefault().getRules().getOffset(Instant.now())));
                        break;
                    case INTEGER:
                        results.add(Integer.valueOf(output.getValue()));
                        break;
                    case STRING:
                        results.add(output.getValue());
                        break;
                    case CODE:
                        results.add(new Code().withCode(output.getValue()));
                        break;
                }
            }
        }

        return results;
    }

    private ModelManager modelManager;
    private ModelManager getModelManager() {
        if (modelManager == null) {
            modelManager = new ModelManager();
        }

        return modelManager;
    }

    private LibraryManager libraryManager;
    private LibraryManager getLibraryManager() {
        if (libraryManager == null) {
            libraryManager = new LibraryManager(getModelManager());
            libraryManager.getLibrarySourceLoader().clearProviders();
            libraryManager.getLibrarySourceLoader().registerProvider(new TestLibrarySourceProvider());
        }
        return libraryManager;
    }

    private LibraryLoader libraryLoader;
    private LibraryLoader getLibraryLoader() {
        if (libraryLoader == null) {
            libraryLoader = new TestLibraryLoader(libraryManager);
        }
        return libraryLoader;
    }

    private Library translate(String cql) throws UcumException {
        ArrayList<CqlTranslator.Options> options = new ArrayList<>();
        options.add(CqlTranslator.Options.EnableDateRangeOptimization);
        UcumService ucumService = new UcumEssenceService(UcumEssenceService.class.getResourceAsStream("/ucum-essence.xml"));
        CqlTranslator translator = CqlTranslator.fromText(cql, getModelManager(), getLibraryManager(), ucumService, options.toArray(new CqlTranslator.Options[options.size()]));
        if (translator.getErrors().size() > 0) {
            ArrayList<String> errors = new ArrayList<>();
            for (CqlTranslatorException error : translator.getErrors()) {
                TrackBack tb = error.getLocator();
                String lines = tb == null ? "[n/a]" : String.format("[%d:%d, %d:%d]",
                        tb.getStartLine(), tb.getStartChar(), tb.getEndLine(), tb.getEndChar());
                errors.add(lines + error.getMessage());
            }
            throw new IllegalArgumentException(errors.toString());
        }

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

    private Boolean compareResults(Object expectedResult, Object actualResult) {
        // Perform FHIR system-defined type conversions
        if (actualResult instanceof Enumeration) {
            actualResult = new Code().withCode(((Enumeration)actualResult).getValueAsString());
        }
        else if (actualResult instanceof BooleanType) {
            actualResult = ((BooleanType)actualResult).getValue();
        }
        else if (actualResult instanceof IntegerType) {
            actualResult = ((IntegerType)actualResult).getValue();
        }
        else if (actualResult instanceof DecimalType) {
            actualResult = ((DecimalType)actualResult).getValue();
        }
        else if (actualResult instanceof StringType) {
            actualResult = ((StringType)actualResult).getValue();
        }
        else if (actualResult instanceof BaseDateTimeType) {
            actualResult = DateTime.fromJavaDate(((BaseDateTimeType)actualResult).getValue());
        }
        else if (actualResult instanceof Quantity) {
            Quantity quantity = (Quantity)actualResult;
            actualResult = new org.opencds.cqf.cql.runtime.Quantity()
                    .withValue(quantity.getValue())
                    .withUnit(quantity.getUnit());
        }
        else if (actualResult instanceof Coding) {
            Coding coding = (Coding)actualResult;
            actualResult = new Code()
                    .withCode(coding.getCode())
                    .withDisplay(coding.getDisplay())
                    .withSystem(coding.getSystem())
                    .withVersion(coding.getVersion());
        }
        return EqualEvaluator.equal(expectedResult, actualResult);
    }

    private void runStu3Test(org.hl7.fhirpath.tests.Test test) throws UcumException {
        String resourceFilePath = "stu3/input/" + test.getInputfile();
        Resource resource = loadResourceFile(resourceFilePath);
        String cql = String.format("library TestFHIRPath using FHIR version '3.0.0' include FHIRHelpers version '3.0.0' called FHIRHelpers parameter %s %s define Test: %s",
                resource.fhirType(), resource.fhirType(), test.getExpression().getValue());

        Library library = null;
        // If the test expression is invalid, expect an error during translation and fail if we don't get one
        boolean isInvalid = test.getExpression().isInvalid() != null && test.getExpression().isInvalid();

        if (isInvalid) {
            boolean testPassed = false;
            try {
                library = translate(cql);
            }
            catch (Exception e) {
                testPassed = true;
            }

            if (!testPassed) {
                throw new RuntimeException(String.format("Expected exception not thrown for test %s.", test.getName()));
            }
        }
        else {
            library = translate(cql);

            Context context = new Context(library);

            context.registerLibraryLoader(getLibraryLoader());

            BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://fhirtest.uhn.ca/baseDstu3");
            //BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://fhir3.healthintersections.com.au/open/");
            //BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://wildfhir.aegis.net/fhir");
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
                    Boolean comparison = compareResults(expectedResult, actualResult);
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
        String testsFilePath = "stu3/tests-fhir-r3.xml";
        System.out.println(String.format("Running test file %s...", testsFilePath));
        Tests tests = loadTestsFile(testsFilePath);
        int testCounter = 0;
        int passCounter = 0;
        for (Group group : tests.getGroup()) {
            System.out.println(String.format("Running test group %s...", group.getName()));
            for (org.hl7.fhirpath.tests.Test test : group.getTest()) {
                testCounter += 1;
                try {
                    //System.out.println(String.format("Running test %s...", test.getName()));
                    runStu3Test(test);
                    passCounter += 1;
                    System.out.println(String.format("Test %s passed.", test.getName()));
                }
                catch (Exception e) {
                    System.out.println(String.format("Test %s failed with exception: %s", test.getName(), e.toString()));
                }
            }
            //System.out.println(String.format("Finished test group %s.", group.getName()));
        }
        System.out.println(String.format("Tests file %s passed %s of %s tests.", testsFilePath, passCounter, testCounter));
    }

    private String getStringFromResourceStream(String resourceName) {
        java.io.InputStream input = TestFhirPath.class.getResourceAsStream(resourceName);
        try (BufferedReader stringReader = new BufferedReader(new InputStreamReader(input))) {
            String line = null;
            StringBuilder source = new StringBuilder();
            while ((line = stringReader.readLine()) != null) {
                source.append(line);
                source.append("\n");
            }
            return source.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // TODO: Resolve Error: Could not load model information for model FHIR, version 3.0.0 because version 1.0.2 is already loaded
    @Test
    public void testFhirHelpersStu3() throws UcumException {
        String cql = getStringFromResourceStream("stu3/TestFHIRHelpers.cql");
        Library library = translate(cql);
        Context context = new Context(library);
        context.registerLibraryLoader(getLibraryLoader());

        BaseFhirDataProvider provider = new FhirDataProviderStu3().setEndpoint("http://fhirtest.uhn.ca/baseDstu3");
        context.registerDataProvider("http://hl7.org/fhir", provider);

        Object result = context.resolveExpressionRef("TestPeriodToInterval").getExpression().evaluate(context);
        // TODO - fix
//        Assert.assertEquals(((DateTime)((Interval) result).getStart()).getPartial(), new Partial(DateTime.getFields(6), new int[] {2017, 5, 6, 18, 8, 0}));
//        Assert.assertEquals(((DateTime)((Interval) result).getEnd()).getPartial(), new Partial(DateTime.getFields(6), new int[] {2017, 5, 6, 19, 8, 0}));
        result = context.resolveExpressionRef("TestToQuantity").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestRangeToInterval").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToCode").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToConcept").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToString").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestRequestStatusToString").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToDateTime").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToTime").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToInteger").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToDecimal").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToBoolean").getExpression().evaluate(context);
    }

    //@Test
    public void testFhirHelpersDstu2() throws UcumException {
        String cql = getStringFromResourceStream("Dstu2/TestFHIRHelpersDstu2.cql");
        Library library = translate(cql);
        Context context = new Context(library);
        context.registerLibraryLoader(getLibraryLoader());

        BaseFhirDataProvider provider = new FhirDataProviderDstu2();
        context.registerDataProvider("http://hl7.org/fhir", provider);

        Object result = context.resolveExpressionRef("TestPeriodToInterval").getExpression().evaluate(context);
        // TODO - millis shouldn't be populated - issue with DateTime.fromJavaDate(Date date)
//        Assert.assertEquals(((DateTime)((Interval) result).getStart()).getPartial(), new Partial(DateTime.getFields(7), new int[] {2017, 5, 6, 18, 8, 0, 0}));
//        Assert.assertEquals(((DateTime)((Interval) result).getEnd()).getPartial(), new Partial(DateTime.getFields(7), new int[] {2017, 5, 6, 19, 8, 0, 0}));
        result = context.resolveExpressionRef("TestToQuantity").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestRangeToInterval").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToCode").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToConcept").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToString").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestRequestStatusToString").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToDateTime").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToTime").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToInteger").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToDecimal").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToBoolean").getExpression().evaluate(context);
    }

    //@Test
    public void testFhirHelpersHL7() throws UcumException {
        String cql = getStringFromResourceStream("Dstu2/TestFHIRHelpersDstu2.cql");
        Library library = translate(cql);
        Context context = new Context(library);
        context.registerLibraryLoader(getLibraryLoader());

        BaseFhirDataProvider provider = new FhirDataProviderHL7();
        context.registerDataProvider("http://hl7.org/fhir", provider);

        Object result = context.resolveExpressionRef("TestPeriodToInterval").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToQuantity").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestRangeToInterval").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToCode").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToConcept").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToString").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestRequestStatusToString").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToDateTime").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToTime").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToInteger").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToDecimal").getExpression().evaluate(context);
        result = context.resolveExpressionRef("TestToBoolean").getExpression().evaluate(context);
    }

    @Test
    public void testDateType() {
        // DateType Month is zero-based (11 == December)
        DateType birthDate = new DateType(1974, 11, 25);
        assertThat(birthDate.getYear(), is(1974));
        assertThat(birthDate.getMonth(), is(11));
        assertThat(birthDate.getDay(), is(25));
    }

    @Test
    public void testDate() {
        // NOTE: DateType uses default GMT
        Date birthDate = new DateType(1974, 11, 25).getValue();
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        calendar.setTime(birthDate);
        assertThat(calendar.get(Calendar.YEAR), is(1974));
        assertThat(calendar.get(Calendar.MONTH), is(11));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(25));
    }
}
