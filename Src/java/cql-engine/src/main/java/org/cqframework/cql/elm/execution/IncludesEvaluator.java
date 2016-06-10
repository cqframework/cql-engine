package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/8/2016 - added Interval Logic
 */
public class IncludesEvaluator extends Includes {

  @Override
  public Object evaluate(Context context) {
      Object left = getOperand().get(0).evaluate(context);
      Object right = getOperand().get(1).evaluate(context);

      if (left != null || right != null) {
        if (left instanceof Interval) {
          Interval leftInterval = (Interval)left;
          Interval rightInterval = (Interval)right;
          Object leftStart = leftInterval.getStart();
          Object leftEnd = leftInterval.getEnd();
          Object rightStart = rightInterval.getStart();
          Object rightEnd = rightInterval.getEnd();

          if (leftStart instanceof Integer) {
            return ((Integer)leftStart <= (Integer)rightStart && (Integer)leftEnd >= (Integer)rightEnd);
          }
          else if (leftStart instanceof BigDecimal) {
            return (((BigDecimal)leftStart).compareTo((BigDecimal)rightStart) <= 0 && ((BigDecimal)leftEnd).compareTo((BigDecimal)rightEnd) >= 0);
          }
          else if (leftStart instanceof Quantity) {
            return ((((Quantity)leftStart).getValue()).compareTo(((Quantity)rightStart).getValue()) <= 0 && (((Quantity)leftEnd).getValue()).compareTo(((Quantity)rightEnd).getValue()) >= 0);
          }
          else {
            throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
          }
        }

        else {
          for (Object rightElement : (Iterable)right) {
              if (!InEvaluator.in(rightElement, (Iterable)left)) {
                  return false;
              }
          }
          return true;
        }
      }
    return null;
  }
}
