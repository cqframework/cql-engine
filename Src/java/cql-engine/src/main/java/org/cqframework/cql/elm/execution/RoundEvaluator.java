package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.*;

/**
 * Created by Bryn on 5/25/2016.
 */
public class RoundEvaluator extends Round {

    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);
        Object precisionValue = getPrecision() == null ? null : getPrecision().evaluate(context);
        //BigDecimal precision = new BigDecimal((precisionValue == null ? 0 : (Integer)precisionValue));
        RoundingMode rm = RoundingMode.HALF_UP;

        if (value == null) { return null; }

        if (((BigDecimal)value).compareTo(new BigDecimal(0)) < 0) { rm = RoundingMode.HALF_DOWN; }

        if (value instanceof BigDecimal){
            if (precisionValue == null || ((Integer)precisionValue == 0)) {
                return ((BigDecimal)value).setScale(0, rm);
            }
            else {
                return ((BigDecimal)value).setScale((Integer)precisionValue, rm);
            }
        }

        throw new IllegalArgumentException(String.format("Cannot %s with argument of type '%s'.", this.getClass().getSimpleName(), value.getClass().getName()));
    }
}
