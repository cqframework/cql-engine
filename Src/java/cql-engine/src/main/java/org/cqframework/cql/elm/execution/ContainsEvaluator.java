package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;

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
        if (right instanceof Integer) {
          return ((Integer)right >= (Integer)leftStart && (Integer)right <= (Integer)leftEnd);
        }
        else if (right instanceof BigDecimal) {
          return (((BigDecimal)right).compareTo((BigDecimal)leftStart) >= 0 && ((BigDecimal)right).compareTo((BigDecimal)leftEnd) <= 0);
        }
        else if (right instanceof Quantity) {
          return ((((Quantity)right).getValue()).compareTo(((Quantity)leftStart).getValue()) >= 0 && (((Quantity)right).getValue()).compareTo(((Quantity)leftEnd).getValue()) <= 0); 
        }
        else {
          throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), right.getClass().getName(), leftStart.getClass().getName()));
        }
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
