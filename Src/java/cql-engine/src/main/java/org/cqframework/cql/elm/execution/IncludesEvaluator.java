package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;

/*
*** NOTES FOR INTERVAL ***
The includes operator for intervals returns true if the first interval completely includes the second.
  More precisely, if the starting point of the first interval is less than or equal to the starting point of the second interval,
    and the ending point of the first interval is greater than or equal to the ending point of the second interval.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.

*/

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/8/2016 - added Interval Logic
 */
public class IncludesEvaluator extends Includes {

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left != null || right != null) {
      if (left instanceof Interval) {
        Object leftStart = ((Interval)left).getStart();
        Object leftEnd = ((Interval)left).getEnd();
        Object rightStart = ((Interval)right).getStart();
        Object rightEnd = ((Interval)right).getEnd();

        if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

        return (Value.compareTo(leftStart, rightStart, "<=") && Value.compareTo(leftEnd, rightEnd, ">="));
      }

      else {
        for (Object rightElement : (Iterable)right) {
            if (!InEvaluator.in(rightElement, (Iterable)left)) {
                return false;
            }
        }
        return true;
      }
    }
    return null;
  }
}
