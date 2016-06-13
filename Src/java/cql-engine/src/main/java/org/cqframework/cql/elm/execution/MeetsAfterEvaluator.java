package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;

/**
* Created by Chris Schuler on 6/8/2016
*/
public class MeetsAfterEvaluator extends MeetsAfter {

  @Override
  public Object evaluate(Context context) {
    Interval left = (Interval)getOperand().get(0).evaluate(context);
    Interval right = (Interval)getOperand().get(1).evaluate(context);

    if (left != null && right != null) {
      Object leftStart = left.getStart();
      Object rightEnd = right.getEnd();

      if (leftStart == null || rightEnd == null) { return null; }

      return Value.compareTo(leftStart, Interval.successor(rightEnd), "==");
    }
    return null;
  }
}
