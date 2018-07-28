package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Quantity;
import org.opencds.cqf.cql.runtime.Value;

import java.math.BigDecimal;

/*
*(left Integer, right Integer) Integer
*(left Decimal, right Decimal) Decimal
*(left Decimal, right Quantity) Quantity
*(left Quantity, right Decimal) Quantity
*(left Quantity, right Quantity) Quantity

The multiply (*) operator performs numeric multiplication of its arguments.
When invoked with mixed Integer and Decimal arguments, the Integer argument will be implicitly converted to Decimal.
TODO: For multiplication operations involving quantities, the resulting quantity will have the appropriate unit. For example:
12 'cm' * 3 'cm'
3 'cm' * 12 'cm2'
In this example, the first result will have a unit of 'cm2', and the second result will have a unit of 'cm3'.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class MultiplyEvaluator extends org.cqframework.cql.elm.execution.Multiply {

  public static Object multiply(Object left, Object right) {
    if (left == null || right == null) {
        return null;
    }

    // *(Integer, Integer)
    if (left instanceof Integer) {
        return (Integer)left * (Integer)right;
    }

    // *(Decimal, Decimal)
    else if (left instanceof BigDecimal && right instanceof BigDecimal) {
        return Value.verifyPrecision(((BigDecimal)left).multiply((BigDecimal)right));
    }

    // *(Quantity, Quantity)
    else if (left instanceof Quantity && right instanceof Quantity) {
      // TODO: unit multiplication i.e. cm*cm = cm^2
      String unit = ((Quantity)left).getUnit();
      BigDecimal value = Value.verifyPrecision((((Quantity)left).getValue()).multiply(((Quantity)right).getValue()));
      return new Quantity().withValue(value).withUnit(unit);
    }

    // *(Decimal, Quantity)
    else if (left instanceof BigDecimal && right instanceof Quantity) {
      BigDecimal value = Value.verifyPrecision(((BigDecimal)left).multiply(((Quantity)right).getValue()));
      return ((Quantity) right).withValue(value);
    }

    // *(Quantity, Decimal)
    else if (left instanceof Quantity && right instanceof BigDecimal) {
      BigDecimal value = Value.verifyPrecision((((Quantity)left).getValue()).multiply((BigDecimal)right));
      return ((Quantity) left).withValue(value);
    }

    // *(Uncertainty, Uncertainty)
    else if (left instanceof Interval && right instanceof Interval) {
      Interval leftInterval = (Interval)left;
      Interval rightInterval = (Interval)right;
      return new Interval(multiply(leftInterval.getStart(), rightInterval.getStart()), true, multiply(leftInterval.getEnd(), rightInterval.getEnd()), true);
    }

    throw new IllegalArgumentException(String.format("Cannot Multiply arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
  }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), multiply(left, right), left, right);
    }
}
