package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class InEvaluator extends In {

    public static Boolean in(Object testElement, Iterable<? extends Object> list) {
        if (list == null) {
            return null;
        }

        for (Object element : list) {
            if (org.cqframework.cql.runtime.Value.equivalent(testElement, element)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object evaluate(Context context) {
        Object testElement = getOperand().get(0).evaluate(context);
        Iterable<Object> list = (Iterable<Object>)getOperand().get(1).evaluate(context);
        return in(testElement, list);
    }
}
