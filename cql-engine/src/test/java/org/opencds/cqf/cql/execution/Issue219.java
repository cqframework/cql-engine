package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.runtime.*;
import org.testng.Assert;
import org.testng.annotations.Test;


public class Issue219 extends CqlExecutionTestBase {
    @Test
    public void testFromWithLet() {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("From").getExpression().evaluate(context);
    }

    public static void main(String[] args) {
        Issue219 i219 = new Issue219();
        try
        {
            i219.beforeEachTestMethod();
            i219.testFromWithLet();
            i219.oneTimeTearDown();
        }    
        catch (Exception e) {
            //
        }
        
    }
}
