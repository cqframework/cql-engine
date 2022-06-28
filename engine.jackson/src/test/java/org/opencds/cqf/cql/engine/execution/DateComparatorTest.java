package org.opencds.cqf.cql.engine.execution;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DateComparatorTest extends CqlExecutionTestBase {
    @Test
    public void testInterval() {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("Date Comparator Test").getExpression().evaluate(context);
        assertThat(result, is(true));
    }
}
