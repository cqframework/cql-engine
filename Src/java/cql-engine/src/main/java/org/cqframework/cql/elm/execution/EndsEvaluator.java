package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;

/**
* Created by Chris Schuler on 6/7/2016
*/
public class EndsEvaluator extends Ends {

  @Override
  public Object evaluate(Context context) {
    Interval leftInterval = (Interval)getOperand().get(0).evaluate(context);
    Interval rightInterval = (Interval)getOperand().get(1).evaluate(context);

    if (leftInterval != null && rightInterval != null) {
      Object leftStart = leftInterval.getStart();
      Object leftEnd = leftInterval.getEnd();
      Object rightStart = rightInterval.getStart();
      Object rightEnd = rightInterval.getEnd();

      return (Value.compareTo(leftStart, rightStart, ">=") && Value.compareTo(leftEnd, rightEnd, "=="));
    }
    return null;
  }
}
