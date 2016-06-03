package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class LowerEvaluator extends Lower {

    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);

        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return ((String) value).toLowerCase();
        }

        throw new IllegalArgumentException(String.format("Cannot %s with argument of type '%s'.", this.getClass().getSimpleName(), value.getClass().getName()));
    }
}
