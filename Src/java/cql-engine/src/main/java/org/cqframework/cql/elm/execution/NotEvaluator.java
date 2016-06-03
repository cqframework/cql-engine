package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class NotEvaluator extends Not {

    @Override
    public Object evaluate(Context context) {
        Boolean val = (Boolean) getOperand().evaluate(context);
        return val == null ? null : !val;
    }
}
