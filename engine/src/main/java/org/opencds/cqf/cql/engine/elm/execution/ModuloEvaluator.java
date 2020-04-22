package org.opencds.cqf.cql.engine.elm.execution;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.opencds.cqf.cql.engine.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.engine.execution.Context;

/*
mod(left Integer, right Integer) Integer
mod(left Decimal, right Decimal) Decimal

The mod operator computes the remainder of the division of its arguments.
When invoked with mixed Integer and Decimal arguments, the Integer argument will be implicitly converted to Decimal.
If either argument is null, the result is null.
*/

public class ModuloEvaluator extends org.cqframework.cql.elm.execution.Modulo {

    public static Object modulo(Object left, Object right) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Integer) {
            if ((Integer)right == 0) {
                return null;
            }
            return (Integer)left % (Integer)right;
        }

        if (left instanceof BigDecimal) {
            if (right == new BigDecimal("0.0")) {
                return null;
            }
            return ((BigDecimal)left).remainder((BigDecimal)right).setScale(8, RoundingMode.FLOOR);
        }

        throw new InvalidOperatorArgument(
                "Modulo(Integer, Integer) or Modulo(Decimal, Decimal)",
                String.format("Modulo(%s, %s)", left.getClass().getName(), right.getClass().getName())
        );
    }

    @Override
    protected Object internalEvaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        return modulo(left, right);
    }
}
