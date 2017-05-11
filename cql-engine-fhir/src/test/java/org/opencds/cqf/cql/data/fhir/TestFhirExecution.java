package org.opencds.cqf.cql.data.fhir;

import org.opencds.cqf.cql.execution.Context;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by Christopher on 5/11/2017.
 */
public class TestFhirExecution extends FhirExecutionTestBase {

    @Test
    public void testCoalesce() {
        Context context = new Context(library);
        context.registerDataProvider("http://hl7.org/fhir", dstu3Provider);
        Object result = context.resolveExpressionRef("testCoalesce").getExpression().evaluate(context);
        Assert.assertTrue((Integer)((List) result).get(0) == 72);
    }
}
