package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.execution.Context;
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

        throw new InvalidOperatorArgument("Abs", operand);
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return super.execute(x -> abs(operand));
    }
}
