package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;

import java.math.BigDecimal;

/**
 * Created by Christopher Schuler on 6/12/2017.
 */
public class PointFromEvaluator extends org.cqframework.cql.elm.execution.PointFrom {

    public static Object pointFrom(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof Interval) {
            Object start = ((Interval) operand).getStart();
            Object end = ((Interval) operand).getEnd();

            Object width = Interval.getSize(start, end);

            if (width instanceof Integer) {
                if ((Integer) width == 0) {
                    return start;
                }
            }

            else if (width instanceof BigDecimal) {
                if (EqualEvaluator.equal(width, new BigDecimal("0.0"))) {
                    return start;
                }
            }

            else if (width instanceof Quantity) {
                if (EqualEvaluator.equal(((Quantity) width).getValue(), new BigDecimal("0.0"))) {
                    return start;
                }
            }

            throw new IllegalArgumentException("Cannot perform PointFrom operation on Interval with a width greater than one.");
        }

        throw new IllegalArgumentException(String.format("Cannot perform PointFrom operator with arguments of type: %s", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), pointFrom(operand), operand);
    }
}
