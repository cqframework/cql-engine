package org.hl7.fhirpath;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.fhir.ucum.UcumException;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhirpath.tests.Group;
import org.opencds.cqf.cql.engine.data.CompositeDataProvider;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.fhir.model.Dstu3FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.model.FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.retrieve.RestFhirRetrieveProvider;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.testng.ITest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class CQLOperationsDstu3Test extends TestFhirPath implements ITest {
    private static FhirContext fhirContext = FhirContext.forCached(FhirVersionEnum.DSTU3);
    private static Dstu3FhirModelResolver fhirModelResolver = new Dstu3FhirModelResolver(fhirContext);
    private static RestFhirRetrieveProvider retrieveProvider = new RestFhirRetrieveProvider(
        new SearchParameterResolver(fhirContext),
        fhirModelResolver,
        fhirContext.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3")
    );
    private static CompositeDataProvider provider = new CompositeDataProvider(fhirModelResolver, retrieveProvider);

    private final String file;
    private final org.hl7.fhirpath.tests.Test test;
    private final Group group;

    @Factory(dataProvider = "dataMethod")
    public CQLOperationsDstu3Test(String file, Group group, org.hl7.fhirpath.tests.Test test) {
        this.file = file;
        this.group = group;
        this.test = test;
    }

    @DataProvider
    public static Object[][] dataMethod() {
        String[] listOfFiles = {
            "stu3/tests-fhir-r3.xml"
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
        runStu3Test(test, fhirContext, provider, fhirModelResolver);
    }

    protected Boolean compareResults(Object expectedResult, Object actualResult, Context context, FhirModelResolver<?,?,?,?,?,?,?,?> resolver) {
        if (actualResult instanceof Enumeration) {
            actualResult = ((Enumeration<?>) actualResult).getValueAsString();
        } else if (actualResult instanceof Quantity) {
            Quantity quantity = (Quantity) actualResult;
            actualResult = new org.opencds.cqf.cql.engine.runtime.Quantity()
                .withValue(quantity.getValue())
                .withUnit(quantity.getUnit());
        } else if (actualResult instanceof Coding) {
            Coding coding = (Coding) actualResult;
            actualResult = new Code()
                .withCode(coding.getCode())
                .withDisplay(coding.getDisplay())
                .withSystem(coding.getSystem())
                .withVersion(coding.getVersion());
        }

        return super.compareResults(expectedResult, actualResult, context, resolver);
    }
}
