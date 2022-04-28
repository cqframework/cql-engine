package org.opencds.cqf.cql.engine.elm.execution;

import java.math.BigDecimal;

import org.opencds.cqf.cql.engine.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.runtime.Quantity;

/*
div(left Integer, right Integer) Integer
div(left Decimal, right Decimal) Decimal
div(left Quantity, right Quantity) Quantity

The div operator performs truncated division of its arguments.
When invoked with mixed Integer and Decimal arguments, the Integer argument will be implicitly converted to Decimal.
If either argument is null, the result is null.
*/

public class TruncatedDivideEvaluator extends org.cqframework.cql.elm.execution.TruncatedDivide {

    public static Object div(Object left, Object right) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Integer) {
            if ((Integer)right == 0) {
                return null;
            }

            return (Integer)left / (Integer)right;
        }

        //@@@CQF-1348 handle Long data type
        if (left instanceof Long) {
            if ((Long)right == 0) {
                return null;
            }

            return (Long)left / (Long)right;
        }

        else if (left instanceof BigDecimal) {
            if (EqualEvaluator.equal(right, new BigDecimal("0.0"))) {
                return null;
            }

            return ((BigDecimal)left).divideAndRemainder((BigDecimal)right)[0];
        }

        else if (left instanceof Quantity) {
            if (EqualEvaluator.equal(((Quantity) right).getValue(), new BigDecimal("0.0"))) {
                return null;
            }
            //@@@CQF-1348 unit calculation in division
            String unit = ((Quantity) left).getUnit();
            if (right instanceof Quantity) {
                String unitLeft = ((Quantity) left).getUnit();
                String unitRight = ((Quantity) right).getUnit();
                if (unitLeft.equals("1") && !unitRight.equals("1")) {
                    throw new InvalidOperatorArgument(
                        "Dividend and divisor must have the same unit",
                        String.format("Divide(%s, %s)", ((Quantity) left).getUnit(), ((Quantity) right).getUnit())
                    );
                } else if (!unitLeft.equals("1") && unitRight.equals("1")) {
                    unit = unitLeft;
                } else if (!unitLeft.equals("1") && !unitRight.equals("1")) {
                    unit = DivideEvaluator.unitCalculator(unitLeft, unitRight);
                }
            }
            return new Quantity()
                .withUnit(unit)
                .withValue(((Quantity) left).getValue().divideAndRemainder(((Quantity) right).getValue())[0]);
        }

        else if (left instanceof Interval && right instanceof Interval) {
            Interval leftInterval = (Interval)left;
            Interval rightInterval = (Interval)right;

            return new Interval(div(leftInterval.getStart(), rightInterval.getStart()), true, div(leftInterval.getEnd(), rightInterval.getEnd()), true);
        }

        throw new InvalidOperatorArgument(
            "TruncatedDivide(Integer, Integer), TruncatedDivide(Decimal, Decimal),  TruncatedDivide(Quantity, Quantity)",
            String.format("TruncatedDivide(%s, %s)", left.getClass().getName(), right.getClass().getName())
        );
    }

    @Override
    protected Object internalEvaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        return div(left, right);
    }
}
