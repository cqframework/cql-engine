package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class CeilingEvaluator extends Ceiling {
    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);

        if (value == null) {
            return null;
        }

        if(value instanceof BigDecimal){
            return BigDecimal.valueOf(Math.ceil(((BigDecimal)value).doubleValue()));
        }

        // TODO: Finish implementation
        // +(Quantity, Quantity)

        throw new IllegalArgumentException(String.format("Cannot %s with argument of type '%s'.",this.getClass().getSimpleName(), value.getClass().getName()));
    }
}
