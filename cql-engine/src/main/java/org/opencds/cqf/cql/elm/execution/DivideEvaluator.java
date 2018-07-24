package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Quantity;
import org.opencds.cqf.cql.runtime.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
/(left Decimal, right Decimal) Decimal
/(left Quantity, right Decimal) Quantity
/(left Quantity, right Quantity) Quantity

The divide (/) operator performs numeric division of its arguments.
Note that this operator is Decimal division; for Integer division, use the truncated divide (div) operator.
When invoked with Integer arguments, the arguments will be implicitly converted to Decimal.
TODO: For division operations involving quantities, the resulting quantity will have the appropriate unit. For example:
12 'cm2' / 3 'cm'
In this example, the result will have a unit of 'cm'.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class DivideEvaluator extends org.cqframework.cql.elm.execution.Divide {

    private static BigDecimal divideHelper(BigDecimal left, BigDecimal right) {
        if (EqualEvaluator.equal(right, new BigDecimal("0.0"))) {
            return null;
        }

        try {
            return Value.verifyPrecision(left.divide(right));
        } catch (ArithmeticException e) {
            return left.divide(right, 8, RoundingMode.FLOOR);
        }
    }

    public static Object divide(Object left, Object right) {

        if (left == null || right == null) {
            return null;
        }

        if (left instanceof BigDecimal && right instanceof BigDecimal) {
            return divideHelper((BigDecimal) left, (BigDecimal) right);
        }

        else if (left instanceof Quantity && right instanceof Quantity) {
            BigDecimal value = divideHelper(((Quantity) left).getValue(), ((Quantity) right).getValue());
            return new Quantity().withValue(Value.verifyPrecision(value)).withUnit(((Quantity) left).getUnit());
        }

        else if (left instanceof Quantity && right instanceof BigDecimal) {
            BigDecimal value = divideHelper(((Quantity) left).getValue(), (BigDecimal) right);
            return new Quantity().withValue(Value.verifyPrecision(value)).withUnit(((Quantity)left).getUnit());
        }

        else if (left instanceof Interval && right instanceof Interval) {
            Interval leftInterval = (Interval)left;
            Interval rightInterval = (Interval)right;

            return new Interval(divide(leftInterval.getStart(), rightInterval.getStart()), true, divide(leftInterval.getEnd(), rightInterval.getEnd()), true);
        }

        throw new IllegalArgumentException(
                String.format("Cannot Divide arguments of type '%s' and '%s'.",
                        left.getClass().getName(), right.getClass().getName())
        );
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), divide(left, right), left, right);
    }
}
