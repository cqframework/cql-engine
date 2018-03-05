package org.opencds.cqf.cql.execution;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Issue39 extends CqlExecutionTestBase {
    @Test
    public void testInterval() {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("EquivalentIntervals").getExpression().evaluate(context);
        assertThat(result, is(true));
    }
}
