package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/*
end of(argument Interval<T>) T

Notes for this class operation are in the Interval class - getEnd() method
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class EndEvaluator extends End {

    @Override
    public Object evaluate(Context context) {
        org.cqframework.cql.runtime.Interval argument = (org.cqframework.cql.runtime.Interval)this.getOperand().evaluate(context);
        if (argument != null) {
            return argument.getEnd();
        }

        return null;
    }
}
