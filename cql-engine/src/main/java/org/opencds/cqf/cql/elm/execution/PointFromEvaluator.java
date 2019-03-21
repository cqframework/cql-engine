package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.execution.Context;

/*
point from(argument Interval<T>) : T
The point from operator extracts the single point from a unit interval. If the argument is not a unit interval, a run-time error is thrown.

If the argument is null, the result is null.
* */
public class PointFromEvaluator extends org.cqframework.cql.elm.execution.PointFrom {

    public static Object pointFrom(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof Interval) {
            Object start = ((Interval) operand).getStart();
            Object end = ((Interval) operand).getEnd();

            Boolean equal = EqualEvaluator.equal(start, end);
            if (equal != null && equal) {
                return start;
            }

            throw new IllegalArgumentException("Cannot perform PointFrom operation on intervals that are not unit intervals.");
        }

        throw new IllegalArgumentException(String.format("Cannot perform PointFrom operator with arguments of type: %s", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), pointFrom(operand), operand);
    }
}
