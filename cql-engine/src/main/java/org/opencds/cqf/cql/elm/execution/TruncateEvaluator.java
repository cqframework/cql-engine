package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.math.BigDecimal;

/*
Truncate(argument Decimal) Integer

The Truncate operator returns the integer component of its argument.
When invoked with an Integer argument, the argument will be implicitly converted to Decimal.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class TruncateEvaluator extends org.cqframework.cql.elm.execution.Truncate {

    public static Object truncate(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof BigDecimal) {
            Double val = ((BigDecimal) operand).doubleValue();
            if (val < 0){
                return ((BigDecimal) operand).setScale(0, BigDecimal.ROUND_CEILING).intValue();
            }
            else {
                return ((BigDecimal) operand).setScale(0, BigDecimal.ROUND_FLOOR).intValue();
            }
        }

        throw new IllegalArgumentException(String.format("Cannot Truncate with argument of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), truncate(operand), operand);
    }
}
