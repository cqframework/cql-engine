package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class IncludedInEvaluator extends IncludedIn {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }

        for (Object element : (Iterable)left) {
            if (!InEvaluator.in(element, (Iterable)right)) {
                return false;
            }
        }

        return true;
    }
}
