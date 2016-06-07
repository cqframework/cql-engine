package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class DivideEvaluator extends Divide {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null || (Integer)right == 0) {
            return null;
        }

        // /(Decimal, Decimal)
        if (left instanceof BigDecimal) {
            return ((BigDecimal)left).divide((BigDecimal)right);
        }

        // TODO: Finish implementation of Divide
        // /(Quantity, Decimal)
        // /(Quantity, Quantity)

        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
    }
}
