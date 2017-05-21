package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
overlaps after(left Interval<T>, right Interval<T>) Boolean

The overlaps after operator returns true if the first interval overlaps the second and ends after it.
If either argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/8/2016
*/
public class OverlapsAfterEvaluator extends org.cqframework.cql.elm.execution.OverlapsAfter {

  @Override
  public Object evaluate(Context context) {
    Interval left = (Interval)getOperand().get(0).evaluate(context);
    Interval right = (Interval)getOperand().get(1).evaluate(context);

    if (left != null && right != null) {
      Object leftEnd = left.getEnd();
      Object rightEnd = right.getEnd();

      return (GreaterEvaluator.greater(leftEnd, rightEnd) && OverlapsEvaluator.overlaps(left, right));
    }

    return null;
  }
}
