package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.runtime.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class LetClauseOutsideQueryContextTest extends CqlExecutionTestBase {
    @Test
    public void testInterval() {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("First Position of list").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(((List)result).get(0), 1));
        
        result = context.resolveExpressionRef("Third Position of list With Same Name of Let As First").getExpression().evaluate(context);
        Assert.assertTrue(EquivalentEvaluator.equivalent(((List)result).get(0), 3));
    }
}