package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class MultiplyEvaluator extends Multiply {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            return null;
        }

        // *(Integer, Integer)
        if (left instanceof Integer) {
            return (Integer)left * (Integer)right;
        }

        // *(Decimal, Decimal)
        if (left instanceof BigDecimal) {
            return ((BigDecimal)left).multiply((BigDecimal)right);
        }

        // TODO: Finish implementation of Multiply
        // *(Decimal, Quantity)
        // *(Quantity, Decimal)
        // *(Quantity, Quantity)

        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
    }
}
