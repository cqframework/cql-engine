package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Value;

import java.util.ArrayList;
import java.util.List;

/*
except(left List<T>, right List<T>) List<T>

The except operator for intervals returns the set difference of two intervals.
  More precisely, this operator returns the portion of the first interval that does not overlap with the second.
Note that to avoid returning an improper interval, if the second argument is properly contained within the first and
  does not start or end it, this operator returns null.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class ExceptEvaluator extends org.cqframework.cql.elm.execution.Except {

    public static Object except(Object left, Object right) {
        if (left == null) {
            return null;
        }

        if (right == null) {
            return left;
        }

        if (left instanceof Interval) {
            Object leftStart = ((Interval)left).getStart();
            Object leftEnd = ((Interval)left).getEnd();
            Object rightStart = ((Interval)right).getStart();
            Object rightEnd = ((Interval)right).getEnd();

            if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

            if (GreaterEvaluator.greater(rightStart, leftEnd)) { return left; }

            else if (LessEvaluator.less(leftStart, rightStart)
                    && GreaterEvaluator.greater(leftEnd, rightEnd)) { return null; }

            // left interval starts before right interval
            if ((LessEvaluator.less(leftStart, rightStart) && LessOrEqualEvaluator.lessOrEqual(leftEnd, rightEnd))) {
                Object min = LessEvaluator.less(Value.predecessor(rightStart), leftEnd) ? Value.predecessor(rightStart) : leftEnd;
                return new Interval(leftStart, true, min, true);
            }
            // right interval starts before left interval
            else if (GreaterEvaluator.greater(leftEnd, rightEnd)
                    && GreaterOrEqualEvaluator.greaterOrEqual(leftStart, rightStart))
            {
                Object max = GreaterEvaluator.greater(Value.successor(rightEnd), leftStart) ? Value.successor(rightEnd) : leftStart;
                return new Interval(max, true, leftEnd, true);
            }

            throw new IllegalArgumentException(String.format("The following interval values led to an undefined Except result: leftStart: %s, leftEnd: %s, rightStart: %s, rightEnd: %s", leftStart.toString(), leftEnd.toString(), rightStart.toString(), rightEnd.toString()));
        }

        else if (left instanceof Iterable) {
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

        return context.logTrace(this.getClass(), except(left, right), left, right);
    }
}
