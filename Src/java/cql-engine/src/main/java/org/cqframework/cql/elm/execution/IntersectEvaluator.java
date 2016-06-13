package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Quantity;
import java.math.BigDecimal;
import java.lang.Math;

import java.util.ArrayList;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/8/2016 - added Interval Logic
 */
public class IntersectEvaluator extends Intersect {

  @Override
  public Object evaluate(Context context) {
    Object test = getOperand().get(0).evaluate(context);
    if (test instanceof Interval) {
      Interval leftInterval = (Interval)test;
      Interval rightInterval = (Interval)getOperand().get(1).evaluate(context);

      if (!OverlapsEvaluator.overlaps(leftInterval, rightInterval)) { return null; }

      if (leftInterval != null && rightInterval != null) {
        Object leftStart = leftInterval.getStart();
        Object leftEnd = leftInterval.getEnd();
        Object rightStart = rightInterval.getStart();
        Object rightEnd = rightInterval.getEnd();

        if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

        if (leftStart instanceof Integer) {
          return (new Interval(Math.max((Integer)leftStart, (Integer)rightStart), true, Math.min((Integer)leftEnd, (Integer)rightEnd), true));
        }

        else if (leftStart instanceof BigDecimal) {
          return (new Interval(((BigDecimal)leftStart).max((BigDecimal)rightStart), true, ((BigDecimal)leftEnd).min((BigDecimal)rightEnd), true));
        }

        else if (leftStart instanceof Quantity) {
          String unit = ((Quantity)leftStart).getUnit();
          return (new Interval(new Quantity().withValue((((Quantity)leftStart).getValue()).max(((Quantity)rightStart).getValue())).withUnit(unit), true, new Quantity().withValue((((Quantity)leftEnd).getValue()).min(((Quantity)rightEnd).getValue())).withUnit(unit), true));
        }

        else {
          throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s' and '%s'.", this.getClass().getSimpleName(), leftStart.getClass().getName(), rightStart.getClass().getName()));
        }
      }
      return null;
    }

    else {
      Iterable<Object> left = (Iterable<Object>)getOperand().get(0).evaluate(context);
      Iterable<Object> right = (Iterable<Object>)getOperand().get(1).evaluate(context);

      if (left == null || right == null) {
          return null;
      }

      java.util.List<Object> result = new ArrayList<Object>();
      for (Object leftItem : left) {
          if (InEvaluator.in(leftItem, right)) {
              result.add(leftItem);
          }
      }

      return result;
    }
  }
}
