package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.Value;
import java.math.BigDecimal;
import java.lang.Math.*;

import java.util.ArrayList;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/8/2016 - added Interval Logic and union() method
 */
public class UnionEvaluator extends Union {

  public static Interval union(Object left, Object right) {
    Interval leftInterval = (Interval)left;
    Interval rightInterval = (Interval)right;
    Object leftStart = leftInterval.getStart();
    Object leftEnd = leftInterval.getEnd();
    Object rightStart = rightInterval.getStart();
    Object rightEnd = rightInterval.getEnd();

    if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

    if (Value.compareTo(leftEnd, rightStart, "<") || Value.compareTo(rightEnd, leftStart, "<")) { return null; }

    if (leftStart instanceof Integer) {
      return new Interval(Math.min((Integer)leftStart, (Integer)rightStart), true, Math.max((Integer)leftEnd, (Integer)rightEnd), true);
    }

    else if (leftStart instanceof BigDecimal) {
      return new Interval(((BigDecimal)leftStart).min((BigDecimal)rightStart), true, ((BigDecimal)leftEnd).max((BigDecimal)rightEnd), true);
    }

    else if (leftStart instanceof Quantity) {
      String unit = ((Quantity)leftStart).getUnit();
      return new Interval(new Quantity().withValue((((Quantity)leftStart).getValue()).min(((Quantity)rightStart).getValue())).withUnit(unit), true, new Quantity().withValue((((Quantity)leftEnd).getValue()).max(((Quantity)rightEnd).getValue())).withUnit(unit), true);
    }

    else {
      throw new IllegalArgumentException(String.format("Cannot UnionEvaluator arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) {
        return null;
    }

    if (left instanceof Interval) {
      return union(left, right);
    }

    else {
      // List Logic
      ArrayList result = new ArrayList();
      for (Object leftElement : (Iterable)left) {
          result.add(leftElement);
      }

      for (Object rightElement : (Iterable)right) {
          result.add(rightElement);
      }

      return result;
    }
  }
}
