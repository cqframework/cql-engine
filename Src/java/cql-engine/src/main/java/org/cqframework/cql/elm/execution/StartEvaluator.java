package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class StartEvaluator extends Start {

    @Override
    public Object evaluate(Context context) {
        org.cqframework.cql.runtime.Interval argument = (org.cqframework.cql.runtime.Interval)this.getOperand().evaluate(context);
        if (argument != null) {
            return argument.getStart();
        }

        return null;
    }
}
