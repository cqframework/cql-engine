package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;
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
public class FloorEvaluator extends Floor {

    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);

        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return BigDecimal.valueOf(Math.floor(((BigDecimal)value).doubleValue())).intValue();
        }

        else if (value instanceof Quantity) {
          return BigDecimal.valueOf(Math.floor(((Quantity)value).getValue().doubleValue())).intValue();
        }

        throw new IllegalArgumentException(String.format("Cannot perform Floor operation with argument of type '%s'.", value.getClass().getName()));
    }
}
