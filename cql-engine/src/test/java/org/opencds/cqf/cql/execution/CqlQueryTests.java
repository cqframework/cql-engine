package org.opencds.cqf.cql.execution;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

public class CqlQueryTests extends CqlExecutionTestBase
{
    @Test
    public void TestLet()
    {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("RightShift").getExpression().evaluate(context);
        Assert.assertEquals(result, Arrays.asList(null, "A", "B", "C"));
        result = context.resolveExpressionRef("LeftShift").getExpression().evaluate(context);
        Assert.assertEquals(result, Arrays.asList("B", "C", "D", null));
        result = context.resolveExpressionRef("LeftShift2").getExpression().evaluate(context);
        Assert.assertEquals(result, Arrays.asList("B", "C", "D", null));
    }

    @Test
    public void TestWithout()
    {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("ClaimWithQualifiyingPOSWithoutEncounter").getExpression().evaluate(context);
        Assert.assertTrue(result == null);
    }
}
