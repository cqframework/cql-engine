package org.hl7.fhirpath;

import static org.opencds.cqf.cql.engine.elm.execution.ToQuantityEvaluator.toQuantity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXB;

import org.cqframework.cql.elm.execution.Library;
import org.fhir.ucum.UcumException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhirpath.tests.InvalidType;
import org.hl7.fhirpath.tests.Tests;
import org.opencds.cqf.cql.engine.data.CompositeDataProvider;
import org.opencds.cqf.cql.engine.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.ExistsEvaluator;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.fhir.model.FhirModelResolver;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Date;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Time;

import ca.uhn.fhir.context.FhirContext;

public abstract class TestFhirPath {

    public static Tests loadTestsFile(String testsFilePath) {
        try {
            InputStream testsFileRaw = TestFhirPath.class.getResourceAsStream(testsFilePath);
            return JAXB.unmarshal(testsFileRaw, Tests.class);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new IllegalArgumentException("Couldn't load tests file [" + testsFilePath + "]: " + e.toString());
        }
    }

    private TranslatorHelper translator = new TranslatorHelper();

    private IBaseResource loadResourceFile(String resourceFilePath, FhirContext context) {
        return context.newXmlParser()
            .parseResource(new InputStreamReader(TestFhirPath.class.getResourceAsStream(resourceFilePath)));
    }

    private Iterable<Object> loadExpectedResults(org.hl7.fhirpath.tests.Test test, boolean isExpressionOutputTest) {
        List<Object> results = new ArrayList<>();
        if (isExpressionOutputTest) {
            results.add(true);
        }
        else {
            if (test.getOutput() != null) {
                for (org.hl7.fhirpath.tests.Output output : test.getOutput()) {
                    if (output.getType() != null) {
                        switch (output.getType()) {
                            case BOOLEAN:
                                results.add(Boolean.valueOf(output.getValue()));
                                break;
                            case DECIMAL:
                                results.add(new BigDecimal(output.getValue()));
                                break;
                            case DATE:
                                results.add(new Date(output.getValue()));
                                break;
                            case DATE_TIME:
                                results.add(new DateTime(output.getValue(),
                                    ZoneOffset.systemDefault().getRules().getOffset(Instant.now())));
                                break;
                            case TIME:
                                results.add(new Time(output.getValue()));
                                break;
                            case INTEGER:
                                results.add(Integer.valueOf(output.getValue()));
                                break;
                            case STRING:
                                results.add(output.getValue());
                                break;
                            case CODE:
                                results.add(output.getValue());
                                break;
                            case QUANTITY:
                                results.add(toQuantity(output.getValue()));
                                break;
                            default:
                                throw new IllegalArgumentException(String.format("Unknown output type: %s", output.getType()));
                        }
                    }
                    else {
                        throw new IllegalArgumentException("Output type is not specified and the test is not expressed as an expression-output test");
                    }
                }
            }
        }

        return results;
    }

    abstract Boolean compareResults(Object expectedResult, Object actualResult, Context context, FhirModelResolver<?,?,?,?,?,?,?,?> resolver);

    @SuppressWarnings("unchecked")
    private Iterable<Object> ensureIterable(Object result) {
        Iterable<Object> actualResults;
        if (result instanceof Iterable) {
            actualResults = (Iterable<Object>) result;
        } else {
            List<Object> results = new ArrayList<Object>();
            results.add(result);
            actualResults = results;
        }
        return actualResults;
    }

    protected void runTest(org.hl7.fhirpath.tests.Test test, String basePathInput, FhirContext fhirContext, CompositeDataProvider provider, FhirModelResolver<?,?,?,?,?,?,?,?> resolver) throws UcumException {
        String cql = null;
        IBaseResource resource = null;
        if (test.getInputfile() != null) {
            String resourceFilePath = basePathInput + test.getInputfile();
            resource = loadResourceFile(resourceFilePath, fhirContext);
            cql = String.format(
                "library TestFHIRPath using FHIR version '4.0.1' include FHIRHelpers version '4.0.1' called FHIRHelpers parameter %s %s context %s define Test:",
                resource.fhirType(), resource.fhirType(), resource.fhirType());
        }
        else {
            cql = "library TestFHIRPath using FHIR version '4.0.1' include FHIRHelpers version '4.0.1' called FHIRHelpers define Test:";
        }

        String testExpression = test.getExpression().getValue();
        boolean isExpressionOutputTest = test.getOutput().size() == 1 && test.getOutput().get(0).getType() == null;
        if (isExpressionOutputTest) {
            String outputExpression = test.getOutput().get(0).getValue();
            if ("null".equals(outputExpression)) {
                cql = String.format("%s (%s) is %s", cql, testExpression, outputExpression);
            }
            else {
                cql = String.format("%s (%s) = %s", cql, testExpression, outputExpression);
            }
        } else {
            cql = String.format("%s %s", cql, testExpression);
        }

        Library library = null;
        // If the test expression is invalid, expect an error during translation and
        // fail if we don't get one
        InvalidType invalidType = test.getExpression().getInvalid();
        if (invalidType == null) {
            invalidType = InvalidType.FALSE;
        }

        if (invalidType.equals(InvalidType.SEMANTIC)) {
            boolean testPassed = false;
            try {
                library = translator.translate(cql);
            } catch (Exception e) {
                testPassed = true;
            }

            if (!testPassed) {
                throw new RuntimeException(String.format("Expected exception not thrown for test %s.", test.getName()));
            }
        } else {
            try {
                library = translator.translate(cql);
            } catch (IllegalArgumentException e) {
                // if it crashes and didn't have an expected output, assume the test was supposed to fail.
                if (test.getOutput() == null || test.getOutput().isEmpty()) {
                    return;
                } else {
                    e.printStackTrace();
                    throw new RuntimeException(String.format("Couldn't translate library and was expencting a result. %s.", test.getName()));
                }
            }

            Context context = new Context(library);
            context.registerLibraryLoader(translator.getLibraryLoader());
            context.registerDataProvider("http://hl7.org/fhir", provider);
            if (resource != null) {
                context.setParameter(null, resource.fhirType(), resource);
            }

            Object result = null;
            boolean testPassed = false;
            String message = null;
            try {
                result = context.resolveExpressionRef("Test").evaluate(context);
                testPassed = invalidType.equals(InvalidType.FALSE);
            } catch (Exception e) {
                testPassed = invalidType.equals(InvalidType.TRUE);
                message = e.getMessage();
            }

            if (!testPassed) {
                if (invalidType.equals(InvalidType.TRUE)) {
                    throw new RuntimeException(String.format("Expected exception not thrown for test %s.", test.getName()));
                } else {
                    throw new RuntimeException(String.format("Unexpected exception thrown for test %s: %s.", test.getName(), message));
                }
            }

            if (test.isPredicate() != null && test.isPredicate().booleanValue()) {
                result = ExistsEvaluator.exists(ensureIterable(result));
            }

            Iterable<Object> actualResults = ensureIterable(result);
            Iterable<Object> expectedResults = loadExpectedResults(test, isExpressionOutputTest);
            Iterator<Object> actualResultsIterator = actualResults.iterator();
            for (Object expectedResult : expectedResults) {
                if (actualResultsIterator.hasNext()) {
                    Object actualResult = actualResultsIterator.next();
                    System.out.println("Test: " + test.getName());
                    System.out.println("- Expected Result: " + expectedResult + " (" + expectedResult.getClass() +")");
                    System.out.println("- Actual Result: " + actualResult + " (" + expectedResult.getClass() +")");
                    Boolean comparison = compareResults(expectedResult, actualResult, context, resolver);
                    if (comparison == null || !comparison) {
                        throw new RuntimeException("Actual result is not equal to expected result.");
                    }
                } else {
                    throw new RuntimeException("Actual result is not equal to expected result.");
                }
            }
        }
    }
}
