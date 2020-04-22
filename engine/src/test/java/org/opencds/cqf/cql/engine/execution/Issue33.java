package org.opencds.cqf.cql.engine.execution;

import java.math.BigDecimal;

import org.opencds.cqf.cql.engine.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.runtime.TemporalHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Issue33 extends CqlExecutionTestBase {
    @Test
    public void testInterval() {
        Context context = new Context(library);
        BigDecimal offset = TemporalHelper.getDefaultOffset();
        Object result = context.resolveExpressionRef("Issue33").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(((Interval)result).getStart(), new DateTime(offset, 2017, 12, 20, 11, 0, 0)));
        Assert.assertTrue(EquivalentEvaluator.equivalent(((Interval)result).getEnd(), new DateTime(offset, 2017, 12, 20, 23, 59, 59, 999)));
    }
}
