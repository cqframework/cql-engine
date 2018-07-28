package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.IntervalTypeSpecifier;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

/*
contains(argument List<T>, element T) Boolean

The contains operator for lists returns true if the given element is in the list.
This operator uses the notion of equivalence to determine whether or not the element being searched for is equivalent to any element
    in the list. In particular this means that if the list contains a null, and the element being searched for is null, the result will be true.
If the list argument is null, the result is false.

contains _precision_ (argument Interval<T>, point T) Boolean
The contains operator for intervals returns true if the given point is greater than or equal to the starting point of the interval,
    and less than or equal to the ending point of the interval. For open interval boundaries, exclusive comparison operators are used.
    For closed interval boundaries, if the interval boundary is null, the result of the boundary comparison is considered true.
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed at the specified precision.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016
 */
public class ContainsEvaluator extends org.cqframework.cql.elm.execution.Contains {

    public static Object contains(Object left, Object right, String precision) {
        if (left instanceof Interval) {
            return intervalContains((Interval) left, right, precision);
        }

        else if (left instanceof Iterable) {
            return listContains((Iterable) left, right);
        }

        throw new IllegalArgumentException(String.format("Cannot Contains arguments of type '%s'.", left.getClass().getName()));
    }

    private static Object intervalContains(Interval left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        if (right instanceof BaseTemporal) {
            Boolean pointSameOrAfterStart ;
            if (left.getStart() == null) {
                pointSameOrAfterStart = true;
            }
            else {
                pointSameOrAfterStart = SameOrAfterEvaluator.sameOrAfter(right, left.getStart(), precision);
            }

            Boolean pointSamedOrBeforeEnd;
            if (left.getEnd() == null) {
                pointSamedOrBeforeEnd = true;
            }
            else {
                pointSamedOrBeforeEnd = SameOrBeforeEvaluator.sameOrBefore(right, left.getEnd(), precision);
            }

            return AndEvaluator.and(pointSameOrAfterStart, pointSamedOrBeforeEnd);
        }
        else {
            return AndEvaluator.and(
                    left.getStart() == null ? true : GreaterOrEqualEvaluator.greaterOrEqual(right, left.getStart()),
                    left.getEnd() == null ? true : LessOrEqualEvaluator.lessOrEqual(right, left.getEnd())
            );
        }
    }

    private static Object listContains(Iterable left, Object right) {
        if (left == null) {
            return false;
        }

        return InEvaluator.in(right, left, null);
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        // null left operand case
        if (getOperand().get(0) instanceof AsEvaluator) {
            if (((AsEvaluator) getOperand().get(0)).getAsTypeSpecifier() instanceof IntervalTypeSpecifier) {
                return context.logTrace(this.getClass(), intervalContains((Interval) left, right, precision), left, right, precision);
            }
            else {
                return context.logTrace(this.getClass(), listContains((Iterable) left, right), left, right);
            }
        }

        return contains(left, right, precision);
    }
}
