package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class TruncateEvaluator extends Truncate {

    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);

        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            Double val = ((BigDecimal)value).doubleValue();
            if (val < 0){
                return ((BigDecimal)value).setScale(0, BigDecimal.ROUND_CEILING).intValue();
            }
            else {
                return ((BigDecimal)value).setScale(0, BigDecimal.ROUND_FLOOR).intValue();
            }
        }

        throw new IllegalArgumentException(String.format("Cannot %s with argument of type '%s'.", this.getClass().getSimpleName(), value.getClass().getName()));
    }
}
