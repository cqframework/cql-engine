package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.runtime.*;
import org.testng.Assert;
import org.testng.annotations.Test;


public class Issue218 extends CqlExecutionTestBase {
    @Test
    public void testFromWithLet() {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("From").getExpression().evaluate(context);
    }

    public static void main(String[] args) {
        Issue218 i218 = new Issue218();
        try
        {
            i218.beforeEachTestMethod();
            i218.testFromWithLet();
            i218.oneTimeTearDown();
        }    
        catch (Exception e) {
            //
        }
        
    }
}
