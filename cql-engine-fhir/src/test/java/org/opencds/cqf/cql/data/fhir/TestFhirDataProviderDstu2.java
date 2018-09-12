package org.opencds.cqf.cql.data.fhir;

import ca.uhn.fhir.model.dstu2.resource.Encounter;
import org.opencds.cqf.cql.execution.Context;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Created by Christopher on 5/3/2017.
 */
public class TestFhirDataProviderDstu2 extends FhirExecutionTestBase {

    private Context context;

    @BeforeMethod
    public void before() {
        context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", dstu2Provider);
        BaseFhirDataProvider provider = new FhirDataProviderDstu2().setEndpoint("http://fhirtest.uhn.ca/baseDstu2");
//        FhirDataProviderDstu2 primitiveProvider = new FhirDataProviderDstu2().withEndpoint("http://fhirtest.uhn.ca/baseDstu2").withPackageName("ca.uhn.fhir.model.primitive");
//        context.registerDataProvider("http://hl7.org/fhir", primitiveProvider);
//        FhirDataProviderDstu2 compositeProvider = new FhirDataProviderDstu2().withEndpoint("http://fhirtest.uhn.ca/baseDstu2").withPackageName("ca.uhn.fhir.model.dstu2.composite");
//        context.registerDataProvider("http://hl7.org/fhir", compositeProvider);
    }

//    TODO - DSTU2 endpoint throwing 403 status - fix
    @Test
    public void testDstu2ProviderRetrieve() {
        FhirBundleCursorDstu2 results = (FhirBundleCursorDstu2) dstu2Provider.retrieve("Patient", "2822", "Encounter", null, "code", null, null, null, null, null, null);

        for (Object result : results) {
            Encounter e = (Encounter) result;
            if (!e.getPatient().getReference().getIdPart().equals("2822")) {
                Assert.fail("Invalid patient id in Resource");
            }
        }

        assertTrue(true);
    }

    @Test
    public void testDstu2ProviderString() {
        Object result = context.resolveExpressionRef("testString").getExpression().evaluate(context);
        assertTrue(result != null);
    }

    @Test
    public void testDstu2ProviderCode() {
        Object result = context.resolveExpressionRef("testCode").getExpression().evaluate(context);
        assertTrue(result != null);
    }

    @Test
    public void testDstu2ProviderDate() {
        Object result = context.resolveExpressionRef("testDate").getExpression().evaluate(context);
        assertTrue(result != null);
    }

    @Test
    public void testDstu2ProviderDecimal() {
        Object result = context.resolveExpressionRef("testDecimal").getExpression().evaluate(context);
        assertTrue(result != null);
    }

    @Test
    public void testDstu2ProviderID() {
        Object result = context.resolveExpressionRef("testID").getExpression().evaluate(context);
        assertTrue(result != null);
    }
}
