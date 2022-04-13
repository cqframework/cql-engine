package org.opencds.cqf.cql.engine.elm.execution;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opencds.cqf.cql.engine.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.runtime.Quantity;
import org.opencds.cqf.cql.engine.runtime.Value;

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

public class DivideEvaluator extends org.cqframework.cql.elm.execution.Divide {

    private static BigDecimal divideHelper(BigDecimal left, BigDecimal right) {
        if (EqualEvaluator.equal(right, new BigDecimal("0.0"))) {
            return null;
        }

        try {
            return Value.verifyPrecision(left.divide(right), null);
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
        	//@@@CQF-1348 unit calculation in division
            String unit = "";
            String unitLeft = ((Quantity) left).getUnit();
            String unitRight = ((Quantity) right).getUnit();
            if (unitLeft.equals("1") && !unitRight.equals("1") ) {
                throw new InvalidOperatorArgument(
                    "Dividend and divisor must have the same unit",
                    String.format("Divide(%s, %s)", ((Quantity) left).getUnit(), ((Quantity) right).getUnit())
                );
            }
            else if (!unitLeft.equals("1") && unitRight.equals("1")) {
                unit = unitLeft;
            }
            else if (!unitLeft.equals("1") && !unitRight.equals("1")) {
                unit = unitCalculator(unitLeft, unitRight);
            }
            BigDecimal value = divideHelper(((Quantity) left).getValue(), ((Quantity) right).getValue());
            return new Quantity().withValue(Value.verifyPrecision(value, null)).withUnit(unit);
        }

        else if (left instanceof Quantity && right instanceof BigDecimal) {
            BigDecimal value = divideHelper(((Quantity) left).getValue(), (BigDecimal) right);
            return new Quantity().withValue(Value.verifyPrecision(value, null)).withUnit(((Quantity)left).getUnit());
        }

        else if (left instanceof Interval && right instanceof Interval) {
            Interval leftInterval = (Interval)left;
            Interval rightInterval = (Interval)right;

            return new Interval(
                    divide(leftInterval.getStart(), rightInterval.getStart()), true,
                    divide(leftInterval.getEnd(), rightInterval.getEnd()), true
            );
        }

        throw new InvalidOperatorArgument(
                "Divide(Decimal, Decimal), Divide(Quantity, Decimal), Divide(Quantity, Quantity)",
                String.format("Divide(%s, %s)", left.getClass().getName(), right.getClass().getName())
        );
    }

    public static String unitCalculator(String s1, String s2) {
        Pattern integerPattern = Pattern.compile("-?\\d+");
        Matcher matcher1 = integerPattern.matcher(s1);
        Matcher matcher2 = integerPattern.matcher(s2);
        int exp1 = 1, exp2 = 1;
        String root = s1;
        if (matcher1.find()) {
            exp1 = Integer.parseInt(matcher1.group());
            root = s1.substring(0, s1.indexOf(matcher1.group()));
        }
        if (matcher2.find()) {
            exp2 = Integer.parseInt(matcher2.group());
        }
        int exp = exp1 - exp2;
        if (exp == 0)
            root = "1";
        else if (exp > 1 || exp < 0)
            root += String.valueOf(exp);
        return root;
    }

    @Override
    protected Object internalEvaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return divide(left, right);
    }
}
