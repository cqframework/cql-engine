package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class SuccessorEvaluator extends Successor {

    @Override
    public Object evaluate(Context context) {
        Object argument = this.getOperand().evaluate(context);
        return org.cqframework.cql.runtime.Interval.successor(argument);
    }
}
