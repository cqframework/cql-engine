package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/8/2016 - added Interval Logic
 */
public class ContainsEvaluator extends Contains {

  @Override
  public Object evaluate(Context context) {
    Object test = getOperand().get(0).evaluate(context);

    if (test instanceof Interval) {
      Interval left = (Interval)test;
      Object right = getOperand().get(1).evaluate(context);

      if (left != null && right != null) {
        Object leftStart = left.getStart();
        Object leftEnd = left.getEnd();

        return (Value.compareTo(right, leftStart, ">=") && Value.compareTo(right, leftEnd, "<="));
      }
    }

    else {
      Iterable<Object> list = (Iterable<Object>)test;
      Object testElement = this.getOperand().get(1).evaluate(context);
      return InEvaluator.in(testElement, list);
    }
    return null;
  }
}
