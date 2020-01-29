package org.opencds.cqf.cql.data.fhir;


//import org.hl7.fhir.instance.model.Encounter;
import org.hl7.fhir.dstu2016may.model.Encounter;
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
