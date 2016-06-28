package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.Value;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Uncertainty;

import java.math.BigDecimal;

import java.util.ArrayList;

/**
 * Created by Bryn on 5/25/2016 (v1), edited by Chris Schuler on 6/28/2016 (v2)
 */
public class LessEvaluator extends Less {

  public static Object less(Object left, Object right) {
    if (left == null || right == null) {
        return null;
    }

    if (left instanceof Integer && right instanceof Integer) {
        return Integer.compare((Integer)left, (Integer)right) < 0;
    }

    else if (left instanceof BigDecimal && right instanceof BigDecimal) {
        return ((BigDecimal)left).compareTo((BigDecimal)right) < 0;
    }

    else if (left instanceof String && right instanceof String) {
        return ((String)left).compareTo((String)right) < 0;
    }

    // TODO: Finish implementation
    // <(Quantity, Quantity)
    else if (left instanceof Quantity && right instanceof Quantity) {
      return Value.compareTo(left, right, "<");
    }
    // <(DateTime, DateTime)
    else if (left instanceof DateTime && right instanceof DateTime) {
      DateTime leftDT = (DateTime)left;
      DateTime rightDT = (DateTime)right;
      int size = 0;

      // Uncertainty detection
      if (leftDT.getPartial().size() != rightDT.getPartial().size()) {
        size = leftDT.getPartial().size() > rightDT.getPartial().size() ? rightDT.getPartial().size() : leftDT.getPartial().size();
      }
      else { size = leftDT.getPartial().size(); }

      for (int i = 0; i < size; ++i) {
        if (leftDT.getPartial().getValue(i) < rightDT.getPartial().getValue(i)) {
          return true;
        }
        else if (leftDT.getPartial().getValue(i) > rightDT.getPartial().getValue(i)) {
          return false;
        }
      }
      // Uncertainty wrinkle
      if (leftDT.getPartial().size() != rightDT.getPartial().size()) { return null; }
      return false;
    }

    else if (left instanceof Uncertainty || right instanceof Uncertainty) {
      ArrayList<Interval> intervals = Uncertainty.getLeftRightIntervals(left, right);
      Interval leftU = intervals.get(0);
      Interval rightU = intervals.get(1);

      if (Value.compareTo(leftU.getEnd(), rightU.getStart(), "<")) { return true; }
      if (Value.compareTo(leftU.getStart(), rightU.getEnd(), ">=")) { return false; }
      return null;
    }

    // <(Time, Time)

    throw new IllegalArgumentException(String.format("Cannot Less arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
  }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return less(left, right);
    }
}
