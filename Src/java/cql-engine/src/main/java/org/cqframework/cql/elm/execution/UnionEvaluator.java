package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.ArrayList;

/**
 * Created by Bryn on 5/25/2016.
 */
public class UnionEvaluator extends Union {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }

        ArrayList result = new ArrayList();
        for (Object leftElement : (Iterable)left) {
            result.add(leftElement);
        }

        for (Object rightElement : (Iterable)right) {
            result.add(rightElement);
        }

        return result;
    }
}
