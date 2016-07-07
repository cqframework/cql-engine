package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Value;

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
        boolean nullSwitch = false;
        for (Object element : (Iterable)source) {
            index++;
            Boolean equiv = Value.equivalent(element, elementToFind);
            if (equiv == null) { nullSwitch = true; }
            else if (equiv) {
                return index;
            }
        }
        if (nullSwitch) { return null; }
        return -1;
    }
}
