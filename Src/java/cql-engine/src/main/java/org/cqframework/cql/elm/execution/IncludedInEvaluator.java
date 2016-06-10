package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/8/2016 - added Interval Logic
 */
public class IncludedInEvaluator extends IncludedIn {

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left != null || right != null) {
      if (left instanceof Interval) {
        Object leftStart = ((Interval)left).getStart();
        Object leftEnd = ((Interval)left).getEnd();
        Object rightStart = ((Interval)right).getStart();
        Object rightEnd = ((Interval)right).getEnd();

        if (leftStart instanceof Integer) {
          return ((Integer)rightStart <= (Integer)leftStart && (Integer)rightEnd >= (Integer)leftEnd);
        }

        else if (leftStart instanceof BigDecimal) {
          return (((BigDecimal)rightStart).compareTo((BigDecimal)leftStart) <= 0 && ((BigDecimal)rightEnd).compareTo((BigDecimal)leftEnd) >= 0);
        }

        else if (leftStart instanceof Quantity) {
          return ((((Quantity)rightStart).getValue()).compareTo(((Quantity)leftStart).getValue()) <= 0 && (((Quantity)rightEnd).getValue()).compareTo(((Quantity)leftEnd).getValue()) >= 0);
        }

        else {
          throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
        }
      }

      else {
        for (Object element : (Iterable)left) {
            if (!InEvaluator.in(element, (Iterable)right)) {
                return false;
            }
        }
        return true;
      }
    }
   return null;
  }
}
