package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.TraceExecution;
import org.opencds.cqf.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
Abs(argument Integer) Integer
Abs(argument Decimal) Decimal
Abs(argument Quantity) Quantity

The Abs operator returns the absolute value of its argument.
When taking the absolute value of a quantity, the unit is unchanged.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/24/2016.
 */
public class AbsEvaluator extends org.cqframework.cql.elm.execution.Abs {

    public static Object abs(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof Integer) {
            return  Math.abs((Integer)operand);
        }

        else if (operand instanceof BigDecimal) {
            return ((BigDecimal)operand).abs();
        }

        else if (operand instanceof Quantity) {
            return new Quantity().withValue((((Quantity)operand).getValue()).abs()).withUnit(((Quantity)operand).getUnit());
        }

        throw new IllegalArgumentException(String.format("Cannot evaluate the Abs operator with an argument of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), abs(operand), operand);
    }
}
