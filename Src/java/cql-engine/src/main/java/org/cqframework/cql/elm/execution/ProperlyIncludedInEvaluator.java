package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
* Created by Chris Schuler on 6/8/2016
*/

public class ProperlyIncludedInEvaluator extends ProperIncludedIn {

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
        return (leftSize != rightSize && (Integer)rightStart <= (Integer)leftStart && (Integer)rightEnd >= (Integer)leftEnd);
      }

      else if (leftStart instanceof BigDecimal) {
        BigDecimal leftSize = ((BigDecimal)leftEnd).subtract((BigDecimal)leftStart);
        BigDecimal rightSize = ((BigDecimal)rightEnd).subtract((BigDecimal)rightStart);
        return (leftSize.compareTo(rightSize) != 0 && ((BigDecimal)rightStart).compareTo((BigDecimal)leftStart) <= 0 && ((BigDecimal)rightEnd).compareTo((BigDecimal)leftEnd) >= 0);
      }

      else if (leftStart instanceof Quantity) {
        BigDecimal leftSize = (((Quantity)leftEnd).getValue()).subtract(((Quantity)leftStart).getValue());
        BigDecimal rightSize = (((Quantity)rightEnd).getValue()).subtract(((Quantity)rightStart).getValue());
        return (leftSize.compareTo(rightSize) != 0 && (((Quantity)rightStart).getValue()).compareTo(((Quantity)leftStart).getValue()) <= 0 && (((Quantity)rightEnd).getValue()).compareTo(((Quantity)leftEnd).getValue()) >= 0);
      }

      else {
        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), left.getClass().getName(), right.getClass().getName()));
      }
    }
    return null;
  }
}
