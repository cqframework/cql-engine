package org.opencds.cqf.cql.data.fhir;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.terminology.fhir.Dstu3FhirTerminologyProvider;

import static org.testng.AssertJUnit.assertTrue;

public class TestCodeRef extends FhirExecutionTestBase {

    private Dstu3FhirTerminologyProvider terminologyProvider =
            new Dstu3FhirTerminologyProvider().setEndpoint("http://measure.eval.kanvix.com/cqf-ruler/baseDstu3", false);

    // @Test
    public void CodeRefTest1() {
        Context context = new Context(library);
        context.registerTerminologyProvider(terminologyProvider);

        Object result = context.resolveExpressionRef("CodeRef1").getExpression().evaluate(context);
        assertTrue(result != null);
    }

    // @Test
    public void CodeRefTest2() {
        Context context = new Context(library);
        context.registerTerminologyProvider(terminologyProvider);

        Object result = context.resolveExpressionRef("CodeRef2").getExpression().evaluate(context);
        assertTrue(result != null);
    }
}
