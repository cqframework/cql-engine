package edu.utah.cql.elm.execution;

import edu.utah.cql.execution.Context;
import edu.utah.cql.runtime.Interval;
import edu.utah.cql.runtime.Value;

/*
meets before(left Interval<T>, right Interval<T>) Boolean

The meets before operator returns true if the first interval ends immediately before the second interval starts.
If either argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/8/2016
*/
public class MeetsBeforeEvaluator extends org.cqframework.cql.elm.execution.MeetsBefore {

  @Override
  public Object evaluate(Context context) {
    Interval left = (Interval)getOperand().get(0).evaluate(context);
    Interval right = (Interval)getOperand().get(1).evaluate(context);

    if (left != null && right != null) {
      Object leftEnd = left.getEnd();
      Object rightStart = right.getStart();

      if (leftEnd == null || rightStart == null) { return null; }

      return Value.compareTo(rightStart, Interval.successor(leftEnd), "==");
    }
    return null;
  }
}
