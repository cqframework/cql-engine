package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;

/*
overlaps(left Interval<T>, right Interval<T>) Boolean

The overlaps operator returns true if the first interval overlaps the second.
  More precisely, if the ending point of the first interval is greater than or equal to the starting point of the second interval,
    and the starting point of the first interval is less than or equal to the ending point of the second interval.
If either argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/8/2016
*/
public class OverlapsEvaluator extends Overlaps {

  public static Boolean overlaps(Interval left, Interval right) {
    if (left == null || right == null) { return null; }

    Object leftStart = left.getStart();
    Object leftEnd = left.getEnd();
    Object rightStart = right.getStart();
    Object rightEnd = right.getEnd();

    if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

    return (Value.compareTo(leftStart, rightEnd, "<=") && Value.compareTo(rightStart, leftEnd, "<="));
  }

  @Override
  public Object evaluate(Context context) {
    Interval left = (Interval)getOperand().get(0).evaluate(context);
    Interval right = (Interval)getOperand().get(1).evaluate(context);

    return overlaps(left, right);
  }
}
