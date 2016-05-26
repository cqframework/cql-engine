package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class ContainsEvaluator extends Contains {
    @Override
    public Object evaluate(Context context) {
        Iterable<Object> list = (Iterable<Object>)this.getOperand().get(0).evaluate(context);
        Object testElement = this.getOperand().get(1).evaluate(context);
        return InEvaluator.in(testElement, list);
    }
}
