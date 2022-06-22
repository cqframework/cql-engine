package org.hl7.fhirpath;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.fhir.ucum.UcumException;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhirpath.tests.Group;
import org.opencds.cqf.cql.engine.data.CompositeDataProvider;
import org.opencds.cqf.cql.engine.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.fhir.model.FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.model.R4FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.retrieve.RestFhirRetrieveProvider;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.testng.ITest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class CQLOperationsR4Test extends TestFhirPath implements ITest {

    private static FhirContext fhirContext = FhirContext.forCached(FhirVersionEnum.R4);
    private static R4FhirModelResolver fhirModelResolver = new R4FhirModelResolver(fhirContext);
    private static RestFhirRetrieveProvider retrieveProvider = new RestFhirRetrieveProvider(
        new SearchParameterResolver(fhirContext),
        fhirModelResolver,
        fhirContext.newRestfulGenericClient("http://fhirtest.uhn.ca/baseR4")
    );
    private static CompositeDataProvider provider = new CompositeDataProvider(fhirModelResolver, retrieveProvider);

    private final String file;
    private final org.hl7.fhirpath.tests.Test test;
    private final org.hl7.fhirpath.tests.Group group;

    @Factory(dataProvider = "dataMethod")
    public CQLOperationsR4Test(String file, Group group, org.hl7.fhirpath.tests.Test test) {
        this.file = file;
        this.group = group;
        this.test = test;
    }

    @DataProvider
    public static Object[][] dataMethod() {
        String[] listOfFiles = {
            "r4/tests-fhir-r4.xml",
            "cql/CqlAggregateFunctionsTest.xml",
            "cql/CqlAggregateTest.xml",
            "cql/CqlArithmeticFunctionsTest.xml",
            "cql/CqlComparisonOperatorsTest.xml",
            "cql/CqlConditionalOperatorsTest.xml",
            "cql/CqlDateTimeOperatorsTest.xml",
            "cql/CqlErrorsAndMessagingOperatorsTest.xml",
            "cql/CqlIntervalOperatorsTest.xml",
            "cql/CqlListOperatorsTest.xml",
            "cql/CqlLogicalOperatorsTest.xml",
            "cql/CqlNullologicalOperatorsTest.xml",
            "cql/CqlStringOperatorsTest.xml",
            "cql/CqlTypeOperatorsTest.xml",
            "cql/CqlTypesTest.xml",
            "cql/ValueLiteralsAndSelectors.xml"
        };

        List<Object[]> testsToRun = new ArrayList<>();
        for (String file: listOfFiles) {
            for (Group group : loadTestsFile(file).getGroup()) {
                for (org.hl7.fhirpath.tests.Test test : group.getTest()) {
                    if (!"2.1.0".equals(test.getVersion())) { // unsupported version
                        testsToRun.add(new Object[] {
                            file,
                            group,
                            test
                        });
                    }
                }
            }
        }
        return testsToRun.toArray(new Object[testsToRun.size()][]);
    }

    @Override
    public String getTestName() {
        return file.replaceAll(".xml", "") +"/"+ group.getName() +"/"+ test.getName();
    }

    @Test
    public void test() throws UcumException {
        runTest(test, "r4/input/", fhirContext, provider, fhirModelResolver);
    }

    @Override
    public Boolean compareResults(Object expectedResult, Object actualResult, Context context, FhirModelResolver<?, ?, ?, ?, ?, ?, ?, ?> resolver) {
        // Perform FHIR system-defined type conversions
        if (actualResult instanceof Enumeration) {
            actualResult = ((Enumeration<?>) actualResult).getValueAsString();
        } else if (actualResult instanceof BooleanType) {
            actualResult = ((BooleanType) actualResult).getValue();
        } else if (actualResult instanceof IntegerType) {
            actualResult = ((IntegerType) actualResult).getValue();
        } else if (actualResult instanceof DecimalType) {
            actualResult = ((DecimalType) actualResult).getValue();
        } else if (actualResult instanceof StringType) {
            actualResult = ((StringType) actualResult).getValue();
        } else if (actualResult instanceof BaseDateTimeType) {
            actualResult = resolver.toJavaPrimitive(actualResult, actualResult);
        } else if (actualResult instanceof Quantity) {
            Quantity quantity = (Quantity) actualResult;
            actualResult = new org.opencds.cqf.cql.engine.runtime.Quantity().withValue(quantity.getValue())
                .withUnit(quantity.getUnit());
        } else if (actualResult instanceof Coding) {
            Coding coding = (Coding) actualResult;
            actualResult = new Code().withCode(coding.getCode()).withDisplay(coding.getDisplay())
                .withSystem(coding.getSystem()).withVersion(coding.getVersion());
        }
        return EqualEvaluator.equal(expectedResult, actualResult, context);
    }
}
