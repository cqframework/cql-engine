package org.opencds.cqf.cql.execution;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CqlErrorsAndMessagingOperatorsTest extends CqlExecutionTestBase {
    @Test
    public void TestMessage() {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("TestMessageInfo").evaluate(context);
        Assert.assertEquals(result.toString(), "100: Test Message");

        result = context.resolveExpressionRef("TestMessageWarn").evaluate(context);
        Assert.assertEquals(result.toString(), "200: You have been warned!");

        result = context.resolveExpressionRef("TestMessageTrace").evaluate(context);
        Assert.assertEquals(result.toString(), "300: This is a trace\n[3, 4, 5]");

        try {
            result = context.resolveExpressionRef("TestMessageError").evaluate(context);
        } catch (RuntimeException re) {
            Assert.assertEquals(re.getMessage(), "400: This is an error!\n4");
        }
    }
}
