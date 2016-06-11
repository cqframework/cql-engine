package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;

/**
* Created by Chris Schuler 6/8/2016
*/
public class WidthEvaluator extends Width {

  @Override
  public Object evaluate(Context context) {

    Interval argument = (Interval)getOperand().evaluate(context);

    if (argument != null) {
      Object start = argument.getStart();
      Object end = argument.getEnd();
      return Interval.getSize(start, end);
    }
    return null;
  }
}
