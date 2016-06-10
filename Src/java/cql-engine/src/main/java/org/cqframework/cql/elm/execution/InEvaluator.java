package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/8/2016 - added Interval Logic
 */
public class InEvaluator extends In {

  public static Boolean in(Object testElement, Iterable<? extends Object> list) {
      if (list == null) {
          return null;
      }

      for (Object element : list) {
          if (org.cqframework.cql.runtime.Value.equivalent(testElement, element)) {
              return true;
          }
      }

      return false;
  }

  @Override
  public Object evaluate(Context context) {
    Object test = getOperand().get(1).evaluate(context);
    if (test instanceof Interval) {
      Object left = getOperand().get(0).evaluate(context);
      Interval right = (Interval)test;

      if (left != null && right != null) {
        Object rightStart = right.getStart();
        Object rightEnd = right.getEnd();
        if (left instanceof Integer) {
          return ((Integer)left >= (Integer)rightStart && (Integer)left <= (Integer)rightEnd);
        }
        else if (left instanceof BigDecimal) {
          return (((BigDecimal)left).compareTo((BigDecimal)rightStart) >= 0 && ((BigDecimal)left).compareTo((BigDecimal)rightEnd) <= 0);
        }
        else if (left instanceof Quantity) {
          return ((((Quantity)left).getValue()).compareTo(((Quantity)rightStart).getValue()) >= 0 && (((Quantity)left).getValue()).compareTo(((Quantity)rightEnd).getValue()) <= 0);
        }
        else {
          throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), rightStart.getClass().getName()));
        }
      }
      return null;
    }

    else {
      Object testElement = getOperand().get(0).evaluate(context);
      Iterable<Object> list = (Iterable<Object>)getOperand().get(1).evaluate(context);
      return in(testElement, list);
    }
  }
}
