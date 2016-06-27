package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.Uncertainty;
import org.cqframework.cql.runtime.Value;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class GreaterEvaluator extends Greater {

  public static Object greater(Object left, Object right) {

    if (left == null || right == null) {
        return null;
    }

    else if (left instanceof Integer && right instanceof Integer) {
        return Integer.compare((Integer)left, (Integer)right) > 0;
    }

    else if (left instanceof BigDecimal && right instanceof BigDecimal) {
        return ((BigDecimal)left).compareTo((BigDecimal)right) > 0;
    }

    else if (left instanceof String) {
        return ((String)left).compareTo((String)right) > 0;
    }

    // >(Quantity, Quantity)
    else if (left instanceof Quantity && right instanceof Quantity) {
      return (((Quantity)left).getValue()).compareTo(((Quantity)right).getValue()) > 0;
    }

    // >(DateTime, DateTime)
    else if (left instanceof DateTime && right instanceof DateTime) {
      DateTime leftDT = (DateTime)left;
      DateTime rightDT = (DateTime)right;
      int size = 0;

      // Uncertainty detection
      if (leftDT.getPartial().size() != rightDT.getPartial().size()) {
        size = leftDT.getPartial().size() > rightDT.getPartial().size() ? rightDT.getPartial().size() : leftDT.getPartial().size();
      }
      else { size =leftDT.getPartial().size(); }

      for (int i = 0; i < size; ++i) {
        if (leftDT.getPartial().getValue(i) > rightDT.getPartial().getValue(i)) {
          return true;
        }
        else if (leftDT.getPartial().getValue(i) < rightDT.getPartial().getValue(i)) {
          return false;
        }
      }
      // Uncertainty wrinkle
      if (leftDT.getPartial().size() != rightDT.getPartial().size()) { return null; }
      return false;
    }

    else if (left instanceof Uncertainty && right instanceof Uncertainty) {

    }

    else if (left instanceof Uncertainty || right instanceof Uncertainty) {
      Interval leftU = new Interval(0, true, 0, true);
      Interval rightU = new Interval(0, true, 0, true);

      if (left instanceof Uncertainty && right instanceof Uncertainty) {
        leftU = ((Uncertainty)left).getUncertaintyInterval();
        rightU = ((Uncertainty)right).getUncertaintyInterval();
      }
      else if (left instanceof Uncertainty) {
        leftU = ((Uncertainty)left).getUncertaintyInterval();
        rightU = Uncertainty.toUncertainty(right);
      }
      else {
        leftU = Uncertainty.toUncertainty(left);
        rightU = ((Uncertainty)right).getUncertaintyInterval();
      }

      if (Value.compareTo(leftU.getStart(), rightU.getEnd(), ">")) { return true; }
      if (Value.compareTo(leftU.getEnd(), rightU.getStart(), "<")) { return false; }
      return null;
    }
    // TODO: Finish implementation
    // >(Time, Time)

    throw new IllegalArgumentException(String.format("Cannot Greater arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
  }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return greater(left, right);
    }
}
