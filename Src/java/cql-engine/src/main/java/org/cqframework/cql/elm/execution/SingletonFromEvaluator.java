package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class SingletonFromEvaluator extends SingletonFrom {

    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);

        if (value == null) {
            return null;
        }

        Object result = null;
        boolean first = true;
        for (Object element : (Iterable)value) {
            if (first) {
                result = element;
                first = false;
            }
            else {
                throw new IllegalArgumentException("Expected a list with at most one element, but found a list with multiple elements.");
            }
        }

        return result;
    }
}
