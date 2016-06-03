package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class IndexOfEvaluator extends IndexOf {

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);
        Object elementToFind = getElement().evaluate(context);

        if (source == null) {
            return null;
        }

        int index = -1;
        for (Object element : (Iterable)source) {
            index++;
            if (org.cqframework.cql.runtime.Value.equivalent(element, elementToFind)) {
                return index;
            }
        }

        return -1;
    }
}
