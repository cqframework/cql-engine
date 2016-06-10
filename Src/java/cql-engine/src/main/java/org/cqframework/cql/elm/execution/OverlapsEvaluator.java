package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
* Created by Chris Schuler on 6/8/2016
*/

public class OverlapsEvaluator extends Overlaps {

  public static boolean overlaps(Interval left, Interval right) {
    Object leftStart = left.getStart();
    Object leftEnd = left.getEnd();
    Object rightStart = right.getStart();
    Object rightEnd = right.getEnd();

    if (leftStart instanceof Integer) {
      return ((Integer)leftStart <= (Integer)rightEnd && (Integer)rightStart <= (Integer)leftEnd);
    }
    else if (leftStart instanceof BigDecimal) {
      return (((BigDecimal)leftStart).compareTo((BigDecimal)rightEnd) <= 0 && ((BigDecimal)rightStart).compareTo((BigDecimal)leftEnd) <= 0);
    }
    else if (leftStart instanceof Quantity) {
      return ((((Quantity)leftStart).getValue()).compareTo(((Quantity)rightEnd).getValue()) <= 0 && (((Quantity)rightStart).getValue()).compareTo(((Quantity)leftEnd).getValue()) <= 0);
    }
    else {
      throw new IllegalArgumentException(String.format("Cannot Overlaps arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }
  }

  @Override
  public Object evaluate(Context context) {
    Interval left = (Interval)getOperand().get(0).evaluate(context);
    Interval right = (Interval)getOperand().get(1).evaluate(context);

    if (left != null && right != null) {
      return overlaps(left, right);
    }
    return null;
  }
}
