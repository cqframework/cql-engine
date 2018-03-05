package org.opencds.cqf.cql.execution;

import org.joda.time.Partial;
import org.opencds.cqf.cql.runtime.*;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Issue33 extends CqlExecutionTestBase {
    @Test
    public void testInterval() {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("Issue33").getExpression().evaluate(context);
        assertThat(((DateTime)((Interval)result).getStart()).getPartial(), is(new Partial(DateTime.getFields(6), new int[] {2017, 12, 20, 11, 0, 0})));
        assertThat(((DateTime)((Interval)result).getEnd()).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2017, 12, 20, 23, 59, 59, 999})));
    }
}
