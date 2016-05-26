package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class FirstEvaluator extends First {

    @Override
    public Object evaluate(Context context) {
        Object value = getSource().evaluate(context);

        if (value == null) {
            return null;
        }

        for (Object element : (Iterable)value) {
            return element;
        }

        return null;
    }
}
