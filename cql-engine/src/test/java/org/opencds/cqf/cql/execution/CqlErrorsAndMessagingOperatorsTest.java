package org.opencds.cqf.cql.execution;

import java.util.*;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CqlErrorsAndMessagingOperatorsTest extends CqlExecutionTestBase {
    @Test
    public void TestMessage() {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("TestMessageInfo").evaluate(context);
        assertThat(result, is(1));
        //Assert.assertEquals(result.toString(), "100: Test Message");

        result = context.resolveExpressionRef("TestMessageWarn").evaluate(context);
        assertThat(result, is(2));
        //Assert.assertEquals(result.toString(), "200: You have been warned!");

        result = context.resolveExpressionRef("TestMessageTrace").evaluate(context);
        assertThat(result, is(new ArrayList<Object>(Arrays.asList(3, 4, 5))));
        //Assert.assertEquals(result.toString(), "300: This is a trace\n[3, 4, 5]");

        try {
            result = context.resolveExpressionRef("TestMessageError").evaluate(context);
        } catch (RuntimeException re) {
            Assert.assertEquals(re.getMessage(), "400: This is an error!\n4");
        }
    }
}
