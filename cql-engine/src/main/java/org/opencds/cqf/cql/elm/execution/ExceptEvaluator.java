package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
except(left Interval<T>, right Interval<T>) Interval<T>
The except operator for intervals returns the set difference of two intervals.
  More precisely, this operator returns the portion of the first interval that does not overlap with the second.
  Note that to avoid returning an improper interval, if the second argument is properly contained within the first and
    does not start or end it, this operator returns null.
If either argument is null, the result is null.

except(left List<T>, right List<T>) List<T>
The except operator returns the set difference of two lists. More precisely, the operator returns a list with the
    elements that appear in the first operand that do not appear in the second operand.
This operator uses the notion of equivalence to determine whether two elements are the same for the purposes of computing the difference.
If the left argument is null, the result is null. else if the right argument is null, the result is the left argument.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class ExceptEvaluator extends org.cqframework.cql.elm.execution.Except {

    public static Object except(Object left, Object right) {
        if (left == null) {
            return null;
        }

        if (left instanceof Interval) {
            if (right == null) {
                return null;
            }

            Object leftStart = ((Interval)left).getStart();
            Object leftEnd = ((Interval)left).getEnd();
            Object rightStart = ((Interval)right).getStart();
            Object rightEnd = ((Interval)right).getEnd();

            if (leftStart == null || leftEnd == null
                    || rightStart == null || rightEnd == null)
            {
                return null;
            }

            // Return null when:
            // left and right are equal
            // right properly includes left
            // left properly includes right and right doesn't start or end left
            String precision = null;
            if (leftStart instanceof BaseTemporal && rightStart instanceof BaseTemporal) {
                precision = BaseTemporal.getHighestPrecision((BaseTemporal) leftStart, (BaseTemporal) leftEnd, (BaseTemporal) rightStart, (BaseTemporal) rightEnd);
            }
            Boolean leftEqualRight = EqualEvaluator.equal(left, right);
            Boolean rightProperlyIncludesLeft = ProperlyIncludesEvaluator.properlyIncludes(right, left, precision);
            Boolean leftProperlyIncludesRight = ProperlyIncludesEvaluator.properlyIncludes(left, right, precision);
            Boolean rightStartsLeft = StartsEvaluator.starts(right, left, precision);
            Boolean rightEndsLeft = EndsEvaluator.ends(right, left, precision);
            Boolean isUndefined = AnyTrueEvaluator.anyTrue(
                    Arrays.asList(
                            leftEqualRight,
                            rightProperlyIncludesLeft,
                            AndEvaluator.and(
                                    leftProperlyIncludesRight,
                                    AndEvaluator.and(
                                            NotEvaluator.not(rightStartsLeft),
                                            NotEvaluator.not(rightEndsLeft)
                                    )
                            )
                    )
            );
            if (isUndefined != null && isUndefined) {
                return null;
            }

            if (GreaterEvaluator.greater(rightStart, leftEnd)) {
                return left;
            }

            else if (AndEvaluator.and(LessEvaluator.less(leftStart, rightStart), GreaterEvaluator.greater(leftEnd, rightEnd)))
            {
                return null;
            }

            // left interval starts before right interval
            if (AndEvaluator.and(LessEvaluator.less(leftStart, rightStart), LessOrEqualEvaluator.lessOrEqual(leftEnd, rightEnd)))
            {
                Object min = LessEvaluator.less(PredecessorEvaluator.predecessor(rightStart), leftEnd) ? PredecessorEvaluator.predecessor(rightStart) : leftEnd;
                return new Interval(leftStart, true, min, true);
            }

            // right interval starts before left interval
            else if (AndEvaluator.and(GreaterEvaluator.greater(leftEnd, rightEnd), GreaterOrEqualEvaluator.greaterOrEqual(leftStart, rightStart)))
            {
                Object max = GreaterEvaluator.greater(SuccessorEvaluator.successor(rightEnd), leftStart) ? SuccessorEvaluator.successor(rightEnd) : leftStart;
                return new Interval(max, true, leftEnd, true);
            }

            throw new IllegalArgumentException(String.format("The following interval values led to an undefined Except result: leftStart: %s, leftEnd: %s, rightStart: %s, rightEnd: %s", leftStart.toString(), leftEnd.toString(), rightStart.toString(), rightEnd.toString()));
        }

        else if (left instanceof Iterable) {
            if (right == null) {
                return left;
            }

            Iterable leftArr = (Iterable)left;
            Iterable rightArr = (Iterable)right;

            List<Object> result = new ArrayList<>();
            for (Object leftItem : leftArr) {
                if (!InEvaluator.in(leftItem, rightArr, null)) {
                    result.add(leftItem);
                }
            }
            return result;
        }
        throw new IllegalArgumentException(String.format("Cannot Except arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return except(left, right);
    }
}
