package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class RoundEvaluator extends Round {

    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);
        Object precisionValue = getPrecision() == null ? null : getPrecision().evaluate(context);
        BigDecimal precision = new BigDecimal((precisionValue == null ? 0 : (Integer)precisionValue));

        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal){
            if (precisionValue == null || ((Integer)precisionValue == 0)) {
                return new BigDecimal(Math.round(((BigDecimal)value).doubleValue()));
            }
            else {
                return new BigDecimal(Math.round(((BigDecimal)value).multiply(precision).doubleValue())).divide(precision);
            }
        }

        throw new IllegalArgumentException(String.format("Cannot %s with argument of type '%s'.", this.getClass().getSimpleName(), value.getClass().getName()));
    }
}
