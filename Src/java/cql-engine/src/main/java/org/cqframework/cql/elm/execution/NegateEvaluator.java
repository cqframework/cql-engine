package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class NegateEvaluator extends Negate {

    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);

        if (value instanceof Integer) {
            return -(int) value;
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).negate();
        }

        if (value instanceof org.cqframework.cql.runtime.Quantity) {
            org.cqframework.cql.runtime.Quantity quantity = (org.cqframework.cql.runtime.Quantity) value;
            return new org.cqframework.cql.runtime.Quantity()
                    .withValue(quantity.getValue().negate())
                    .withUnit(quantity.getUnit());
        }

        return value;
    }
}
