package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.Iterator;

/**
 * Created by Bryn on 5/25/2016.
 */
public class CoalesceEvaluator extends Coalesce {
    @Override
    public Object evaluate(Context context) {
        java.util.List<Expression> operands = getOperand();

        Iterator<Expression> expressions = operands.iterator();
        while (expressions.hasNext()) {
            Expression expression = expressions.next();
            Object tmpVal = expression.evaluate(context);
            if (tmpVal != null) {
                if (tmpVal instanceof Iterable && operands.size() == 1) {
                    Iterator<Object> elemsItr = ((Iterable) tmpVal).iterator();
                    while (elemsItr.hasNext()) {
                        Object obj = elemsItr.next();
                        if (obj != null) {
                            return obj;
                        }
                    }

                    return null;
                }

                return tmpVal;
            }
        }

        return null;
    }
}
