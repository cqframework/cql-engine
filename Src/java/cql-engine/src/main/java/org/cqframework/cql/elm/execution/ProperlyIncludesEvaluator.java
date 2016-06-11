package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;

/**
* Created by Chris Schuler on 6/8/2016
*/
public class ProperlyIncludesEvaluator extends ProperIncludes {

  @Override
  public Object evaluate(Context context) {
    Interval left = (Interval)getOperand().get(0).evaluate(context);
    Interval right = (Interval)getOperand().get(1).evaluate(context);

    if (left != null && right != null) {
      Object leftStart = left.getStart();
      Object leftEnd = left.getEnd();
      Object rightStart = right.getStart();
      Object rightEnd = right.getEnd();

      return (Value.compareTo(Interval.getSize(leftStart, leftEnd), Interval.getSize(rightStart, rightEnd), "!=") && Value.compareTo(leftStart, rightStart, "<=") && Value.compareTo(leftEnd, rightEnd, ">="));
    }
    return null;
  }
}
