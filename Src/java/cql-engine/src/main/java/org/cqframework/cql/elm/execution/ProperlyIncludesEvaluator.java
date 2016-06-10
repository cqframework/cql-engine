package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
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

      if (leftStart instanceof Integer) {
        Integer leftSize = (Integer)leftEnd - (Integer)leftStart;
        Integer rightSize = (Integer)rightEnd - (Integer)rightStart;
        return (leftSize != rightSize && (Integer)leftStart <= (Integer)rightStart && (Integer)leftEnd >= (Integer)rightEnd);
      }

      else if (leftStart instanceof BigDecimal) {
        BigDecimal leftSize = ((BigDecimal)leftEnd).subtract((BigDecimal)leftStart);
        BigDecimal rightSize = ((BigDecimal)rightEnd).subtract((BigDecimal)rightStart);
        return (leftSize.compareTo(rightSize) != 0 && ((BigDecimal)leftStart).compareTo((BigDecimal)rightStart) <= 0 && ((BigDecimal)leftEnd).compareTo((BigDecimal)rightEnd) >= 0);
      }

      else if (leftStart instanceof Quantity) {
        BigDecimal leftSize = (((Quantity)leftEnd).getValue()).subtract(((Quantity)leftStart).getValue());
        BigDecimal rightSize = (((Quantity)rightEnd).getValue()).subtract(((Quantity)rightStart).getValue());
        return (leftSize.compareTo(rightSize) != 0 && (((Quantity)leftStart).getValue()).compareTo(((Quantity)rightStart).getValue()) <= 0 && (((Quantity)leftEnd).getValue()).compareTo(((Quantity)rightEnd).getValue()) >= 0);
      }

      else {
        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
      }
    }
    return null;
  }
}
