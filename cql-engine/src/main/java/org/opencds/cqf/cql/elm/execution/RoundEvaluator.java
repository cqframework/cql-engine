package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.math.BigDecimal;
import java.math.RoundingMode;

/*
Round(argument Decimal) Decimal
Round(argument Decimal, precision Integer) Decimal

The Round operator returns the nearest whole number to its argument. The semantics of round are defined as a traditional
  round, meaning that a decimal value of 0.5 or higher will round to 1.
When invoked with an Integer argument, the argument will be implicitly converted to Decimal.
If the argument is null, the result is null.
Precision determines the decimal place at which the rounding will occur.
If precision is not specified or null, 0 is assumed.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class RoundEvaluator extends org.cqframework.cql.elm.execution.Round {

    public static Object round(Object operand, Object precision) {
        RoundingMode rm = RoundingMode.HALF_UP;

        if (operand == null) {
            return null;
        }

        if (operand instanceof BigDecimal){
            if (((BigDecimal) operand).compareTo(new BigDecimal(0)) < 0) {
                rm = RoundingMode.HALF_DOWN;
            }

            if (precision == null || ((Integer) precision == 0)) {
                return ((BigDecimal) operand).setScale(0, rm);
            }

            else {
                return ((BigDecimal) operand).setScale((Integer)precision, rm);
            }
        }

        throw new IllegalArgumentException(String.format("Cannot Round with argument of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        Object precision = getPrecision() == null ? null : getPrecision().evaluate(context);
        //BigDecimal precision = new BigDecimal((precisionValue == null ? 0 : (Integer)precisionValue));

        return context.logTrace(this.getClass(), round(operand, precision), operand, precision);
    }
}
