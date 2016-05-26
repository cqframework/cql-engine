package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class IsTrueEvaluator extends IsTrue {

    @Override
    public Object evaluate(Context context) {
        return Boolean.TRUE == (Boolean) getOperand().evaluate(context);
    }
}
