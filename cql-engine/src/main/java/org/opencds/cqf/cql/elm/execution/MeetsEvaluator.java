package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

/*
meets _precision_ (left Interval<T>, right Interval<T>) Boolean

The meets operator returns true if the first interval ends immediately before the second interval starts,
    or if the first interval starts immediately after the second interval ends.
    In other words, if the ending point of the first interval is equal to the predecessor of the starting point of the second,
    or if the starting point of the first interval is equal to the successor of the ending point of the second.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed at the specified precision.
If either argument is null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/8/2016
 */
public class MeetsEvaluator extends org.cqframework.cql.elm.execution.Meets {

    public static Boolean meetsOperation(Object left, Object right, String precision) {
        if (left == null && right == null) {
            return null;
        }
        Object maxValue = MaxValueEvaluator.maxValue(left != null ? left.getClass().getName() : right.getClass().getName());
        if (left instanceof BaseTemporal && right instanceof BaseTemporal) {
            Boolean isMax = SameAsEvaluator.sameAs(left, maxValue, precision);
            if (isMax != null && isMax) {
                return false;
            }
            if (precision == null && ((BaseTemporal) left).isUncertain(Precision.MILLISECOND)) {
                return SameAsEvaluator.sameAs(SuccessorEvaluator.successor(left), right, "millisecond");
            }
            else if (precision != null && ((BaseTemporal) left).isUncertain(Precision.fromString(precision))) {
                return SameAsEvaluator.sameAs(left, right, precision);
            }

            if (precision == null) {
                precision = "millisecond";
            }

            if (left instanceof DateTime && right instanceof DateTime) {
                DateTime dt = new DateTime(((DateTime) left).getDateTime().plus(1, Precision.fromString(precision).toChronoUnit()), ((BaseTemporal) left).getPrecision());
                return SameAsEvaluator.sameAs(dt, right, precision);
            }
            else if (left instanceof Time) {
                Time t = new Time(((Time) left).getTime().plus(1, Precision.fromString(precision).toChronoUnit()), ((BaseTemporal) left).getPrecision());
                return SameAsEvaluator.sameAs(t, right, precision);
            }
        }
        Boolean isMax = EqualEvaluator.equal(left, maxValue);
        if (isMax != null && isMax) {
            return false;
        }
        return EqualEvaluator.equal(SuccessorEvaluator.successor(left), right);
    }

    public static Boolean meets(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Interval && right instanceof Interval) {
            Object leftStart = ((Interval) left).getStart();
            Object leftEnd = ((Interval) left).getEnd();

            Boolean in = InEvaluator.in(leftStart, right, precision);
            if (in != null && in) {
                return false;
            }
            in = InEvaluator.in(leftEnd, right, precision);
            if (in != null && in) {
                return false;
            }

            return OrEvaluator.or(
                        MeetsBeforeEvaluator.meetsBefore(left, right, precision),
                        MeetsAfterEvaluator.meetsAfter(left, right, precision)
            );
        }

        throw new IllegalArgumentException(String.format("Cannot Meets arguments of type '%s' and %s.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return meets(left, right, precision);
    }
}
