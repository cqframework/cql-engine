package org.opencds.cqf.cql.data.fhir;

import org.hl7.fhir.instance.model.Encounter;
import org.opencds.cqf.cql.execution.Context;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Created by Christopher Schuler on 6/20/2017.
 */
public class TestFhirDataProviderHL7 extends FhirExecutionTestBase {

    private Context context;

    @BeforeMethod
    public void before() {
        context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", hl7Provider);
    }

//    TODO - DSTU2 endpoint throwing 403 status - fix
//    @Test
    public void testHL7ProviderRetrieve() {
        FhirBundleCursorHL7 results = (FhirBundleCursorHL7) hl7Provider.retrieve("Patient", "2822", "Encounter", null, "code", null, null, null, null, null, null);

        for (Object result : results) {
            Encounter e = (Encounter) result;
            if (!e.getPatient().getReference().contains("2822")) {
                Assert.fail("Invalid patient id in Resource");
            }
        }

        assertTrue(true);
    }

//    @Test
    public void testHL7ProviderString() {
        Object result = context.resolveExpressionRef("testString").getExpression().evaluate(context);
        assertTrue(result != null);
    }

//    @Test
    public void testHL7ProviderCode() {
        Object result = context.resolveExpressionRef("testCode").getExpression().evaluate(context);
        assertTrue(result != null);
    }

//    @Test
    public void testHL7ProviderDate() {
        Object result = context.resolveExpressionRef("testDate").getExpression().evaluate(context);
        assertTrue(result != null);
    }

//    @Test
    public void testHL7ProviderDecimal() {
        Object result = context.resolveExpressionRef("testDecimal").getExpression().evaluate(context);
        assertTrue(result != null);
    }

//    @Test
    public void testHL7ProviderBaseDataElement() {
        Object result = context.resolveExpressionRef("testIBaseDataElement").getExpression().evaluate(context);
        assertTrue(result != null);
    }
}
