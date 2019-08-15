package org.opencds.cqf.cql.execution;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Issue223 extends CqlExecutionTestBase {
    @Test
    public void testInterval() {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("Access Flattened List of List Items in a Single Query").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("Access Flattened List of List Items").getExpression().evaluate(context);
        assertThat(result, is(true));
    }
}