package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
Floor(argument Decimal) Integer

The Floor operator returns the first integer less than or equal to the argument.
When invoked with an Integer argument, the argument will be implicitly converted to Decimal.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class FloorEvaluator extends org.cqframework.cql.elm.execution.Floor {

    public static Object floor(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof BigDecimal) {
            return BigDecimal.valueOf(Math.floor(((BigDecimal) operand).doubleValue())).intValue();
        }

        else if (operand instanceof Quantity) {
            return BigDecimal.valueOf(Math.floor(((Quantity) operand).getValue().doubleValue())).intValue();
        }

        throw new IllegalArgumentException(String.format("Cannot perform Floor operation with argument of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), floor(operand), operand);
    }
}
