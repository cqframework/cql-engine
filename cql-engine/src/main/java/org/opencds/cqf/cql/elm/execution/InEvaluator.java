package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Time;

/*
*** NOTES FOR INTERVAL ***
in(point T, argument Interval<T>) Boolean

The in operator for intervals returns true if the given point is greater than or equal to the
    starting point of the interval, and less than or equal to the ending point of the interval.
    For open interval boundaries, exclusive comparison operators are used.
    For closed interval boundaries, if the interval boundary is null, the result of the boundary comparison is considered true.
If precision is specified and the point type is a date/time type, comparisons used in the
    operation are performed at the specified precision.
If either argument is null, the result is null.

*/

/*
*** NOTES FOR LIST ***
in(element T, argument List<T>) Boolean

The in operator for lists returns true if the given element is in the given list.
This operator uses the notion of equivalence to determine whether or not the element being searched for
    is equivalent to any element in the list. In particular this means that if the list contains a null,
    and the element being searched for is null, the result will be true.
If the left argument is null, the result is null. If the right argument is null, the result is false.

*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class InEvaluator extends org.cqframework.cql.elm.execution.In {

    public static Boolean in(Object left, Object right, String precision) {

        if (right == null) {
            return null;
        }

        if (right instanceof Iterable) {
            for (Object element : (Iterable) right) {
                if (EquivalentEvaluator.equivalent(left, element)) {
                    return true;
                }
            }
            return false;
        }

        else if (right instanceof Interval) {
            Object rightStart = ((Interval) right).getStart();
            Object rightEnd = ((Interval) right).getEnd();

            if (rightStart == null && ((Interval) right).getLowClosed()) {
                return true;
            }

            else if (rightEnd == null && ((Interval) right).getHighClosed()) {
                return true;
            }

            else if (rightStart == null || rightEnd == null || left == null) {
                return null;
            }

            else if (rightStart instanceof BaseTemporal) {
                Boolean sameOrAfter = SameOrAfterEvaluator.sameOrAfter(left, rightStart, precision);
                Boolean sameOrBefore = SameOrBeforeEvaluator.sameOrBefore(left, rightEnd, precision);
                if (sameOrAfter == null || sameOrBefore == null) {
                    return null;
                }
                return sameOrAfter && sameOrBefore;
            }

            Boolean greaterOrEqual = GreaterOrEqualEvaluator.greaterOrEqual(left, rightStart);
            Boolean lessOrEqual = LessOrEqualEvaluator.lessOrEqual(left, rightEnd);
            if (greaterOrEqual == null || lessOrEqual == null) {
                return null;
            }
            return greaterOrEqual && lessOrEqual;
        }

        throw new IllegalArgumentException(String.format("Cannot In arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), in(left, right, precision), left, right);
    }
}
