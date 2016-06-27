package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;

/*
The properly included in operator for intervals returns true if the first interval is completely included in the second and
  the first interval is strictly smaller than the second.
  More precisely, if the starting point of the first interval is greater than or equal to the starting point of the second interval,
    and the ending point of the first interval is less than or equal to the ending point of the second interval,
      and they are not the same interval.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.
Note that during is a synonym for included in.
*/

/**
* Created by Chris Schuler on 6/8/2016
*/
public class ProperlyIncludedInEvaluator extends ProperIncludedIn {

  @Override
  public Object evaluate(Context context) {
    Interval left = (Interval)getOperand().get(0).evaluate(context);
    Interval right = (Interval)getOperand().get(1).evaluate(context);

    if (left != null && right != null) {
      Object leftStart = left.getStart();
      Object leftEnd = left.getEnd();
      Object rightStart = right.getStart();
      Object rightEnd = right.getEnd();

      return (Value.compareTo(Interval.getSize(leftStart, leftEnd), Interval.getSize(rightStart, rightEnd), "!=")
              && Value.compareTo(rightStart, leftStart, "<=") && Value.compareTo(rightEnd, leftEnd, ">="));
    }
    return null;
  }
}
