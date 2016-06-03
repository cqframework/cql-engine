package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class LengthEvaluator extends Length {

    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);

        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return ((String) value).length();
        }

        if (value instanceof Iterable) {
            if (value instanceof java.util.List) {
                return ((java.util.List) value).size();
            } else {
                int size = 0;
                for(Object curr : (Iterable) value)
                {
                    size++;
                }
                return size;
            }
        }

        throw new IllegalArgumentException(String.format("Cannot %s of type '%s'.", this.getClass().getSimpleName(), value.getClass().getName()));
    }
}
