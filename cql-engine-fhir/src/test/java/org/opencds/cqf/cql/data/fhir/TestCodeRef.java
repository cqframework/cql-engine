package org.opencds.cqf.cql.data.fhir;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.terminology.fhir.FhirTerminologyProvider;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by Christopher on 4/25/2017.
 */
public class TestCodeRef extends FhirExecutionTestBase {

    private FhirTerminologyProvider terminologyProvider =
            new FhirTerminologyProvider().setEndpoint("http://measure.eval.kanvix.com/cqf-ruler/baseDstu3", false);

    @Test
    public void CodeRefTest1() {
        Context context = new Context(library);
        context.registerTerminologyProvider(terminologyProvider);

        Object result = context.resolveExpressionRef("CodeRef1").getExpression().evaluate(context);
        assertTrue(result != null);
    }

    @Test
    public void CodeRefTest2() {
        Context context = new Context(library);
        context.registerTerminologyProvider(terminologyProvider);

        Object result = context.resolveExpressionRef("CodeRef2").getExpression().evaluate(context);
        assertTrue(result != null);
    }
}
