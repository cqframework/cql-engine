package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class IndexerEvaluator extends Indexer {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }

        if (left instanceof String) {
            if (right instanceof Integer) {
                if((int)right < 0 || (int)right >= ((String)left).length()){
                    return null;
                }

                return "" + ((String) left).charAt((int) right);
            }
        }

        if (left instanceof Iterable) {
            if (right instanceof Integer) {
                int index = -1;
                for (Object element : (Iterable)left) {
                    index++;
                    if ((Integer)right == index) {
                        return element;
                    }
                }

                return null;
            }
        }

        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
    }
}
