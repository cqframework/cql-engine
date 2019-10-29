package org.opencds.cqf.cql.data.fhir;

import org.hl7.fhir.instance.model.Encounter;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.retrieve.FhirBundleCursor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.assertTrue;

public class TestFhirDataProviderHL7 extends FhirExecutionTestBase {

    private Context context;

    @BeforeMethod
    public void before() {
        context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", hl7Provider);
    }

//    @Test
    public void testHL7ProviderRetrieve() {
		String contextPath = hl7ModelResolver.getContextPath("Patient", "Encounter").toString();
        FhirBundleCursor results = (FhirBundleCursor) hl7Provider.retrieve("Patient", contextPath, "2822", "Encounter", null, "code", null, null, null, null, null, null);

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
