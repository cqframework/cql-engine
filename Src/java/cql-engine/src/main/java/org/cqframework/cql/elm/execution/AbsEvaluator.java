package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/24/2016.
 */
public class AbsEvaluator extends Abs {
    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);

        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return Math.abs((Integer)value);
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).abs();
        }

        // TODO: Finish implementation
        // +(Quantity, Quantity)
        // +(DateTime, Quantity)
        // +(Time, Quantity)

        throw new IllegalArgumentException(String.format("Cannot %s with argument of type '%s'.",this.getClass().getSimpleName(), value.getClass().getName()));
    }
}
