package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.*;

/**
 * Created by Bryn on 5/25/2016.
 */
public class ExceptEvaluator extends Except {

    @Override
    public Object evaluate(Context context) {
        Iterable<Object> left = (Iterable<Object>)getOperand().get(0).evaluate(context);
        Iterable<Object> right = (Iterable<Object>)getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }

        java.util.List<Object> result = new ArrayList<Object>();
        for (Object leftItem : left) {
            if (!InEvaluator.in(leftItem, right)) {
                result.add(leftItem);
            }
        }

        return result;
    }
}
