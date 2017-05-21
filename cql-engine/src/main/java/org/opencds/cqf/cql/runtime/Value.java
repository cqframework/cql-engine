package org.opencds.cqf.cql.runtime;

import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.Partial;
import org.opencds.cqf.cql.elm.execution.GreaterOrEqualEvaluator;
import org.opencds.cqf.cql.elm.execution.LessOrEqualEvaluator;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
/**
 * Created by Bryn on 5/2/2016.
 */
public class Value {

  public static BigDecimal verifyPrecision(BigDecimal value) {
    // at most 8 decimal places
    if (value.precision() > 8) {
      return value.setScale(8, RoundingMode.FLOOR);
    }

    else if (value.precision() < 2) {
      return value.setScale(1, RoundingMode.FLOOR);
    }

    return value;
  }

  public static Object successor(Object value) {
    if (value == null) {
      return null;
    }

    else if (value instanceof Integer) {
      return ((Integer)value) + 1;
    }
    else if (value instanceof BigDecimal) {
      return ((BigDecimal)value).add(new BigDecimal("0.00000001"));
    }
    else if (value instanceof Quantity) {
      Quantity quantity = (Quantity)value;
      return new Quantity().withValue((BigDecimal)successor(quantity.getValue())).withUnit(quantity.getUnit());
    }
    else if (value instanceof DateTime) {
      if (GreaterOrEqualEvaluator.greaterOrEqual(value, maxValue(DateTime.class))) {
        throw new RuntimeException("The result of the successor operation exceeds the maximum value allowed for type DateTime.");
      }
      DateTime dt = (DateTime)value;
      return new DateTime()
              .withPartial(dt.getPartial().property(DateTime.getField(dt.getPartial().size() - 1)).addToCopy(1))
              .withTimezoneOffset(dt.getTimezoneOffset());
    }
    else if (value instanceof Time) {
      if (GreaterOrEqualEvaluator.greaterOrEqual(value, maxValue(Time.class))) {
        throw new RuntimeException("The result of the successor operation exceeds the maximum value allowed for type Time.");
      }
      Time t = (Time)value;
      return new Time()
              .withPartial(t.getPartial().property(Time.getField(t.getPartial().size() - 1)).addToCopy(1))
              .withTimezoneOffset(t.getTimezoneOffset());
    }

    throw new NotImplementedException(String.format("Successor is not implemented for type %s", value.getClass().getName()));
  }

  public static Object predecessor(Object value) {
    if (value == null) {
      return null;
    }
    else if (value instanceof Integer) {
      return ((Integer)value) - 1;
    }
    else if (value instanceof BigDecimal) {
      return ((BigDecimal)value).subtract(new BigDecimal("0.00000001"));
    }
    else if (value instanceof Quantity) {
      Quantity quantity = (Quantity)value;
      return new Quantity().withValue((BigDecimal)predecessor(quantity.getValue())).withUnit(quantity.getUnit());
    }
    else if (value instanceof DateTime) {
      if (LessOrEqualEvaluator.lessOrEqual(value, minValue(DateTime.class))) {
        throw new RuntimeException("The result of the predecessor operation exceeds the minimum value allowed for type DateTime.");
      }
      DateTime dt = (DateTime)value;
      return new DateTime()
              .withPartial(dt.getPartial().property(DateTime.getField(dt.getPartial().size() - 1)).addToCopy(-1))
              .withTimezoneOffset(dt.getTimezoneOffset());
    }
    else if (value instanceof Time) {
      if (LessOrEqualEvaluator.lessOrEqual(value, minValue(Time.class))) {
        throw new RuntimeException("The result of the predecessor operation exceeds the minimum value allowed for type Time.");
      }
      Time t = (Time)value;
      return new Time()
              .withPartial(t.getPartial().property(Time.getField(t.getPartial().size() - 1)).addToCopy(-1))
              .withTimezoneOffset(t.getTimezoneOffset());
    }

    throw new NotImplementedException(String.format("Predecessor is not implemented for type %s", value.getClass().getName()));
  }

  public static Object minValue(Type type) {
    if (type == Integer.class) {
      return Integer.MIN_VALUE;
    }
    else if (type == BigDecimal.class) {
      return new BigDecimal("-9999999999999999999999999999.99999999");
    }
    else if (type == Quantity.class) {
      return new Quantity().withValue((BigDecimal)minValue(BigDecimal.class));
    }
    else if (type == DateTime.class) {
      return new DateTime().withPartial(new Partial(DateTime.fields, new int[] {0001, 1, 1, 0, 0, 0, 0}));
    }
    else if (type == Time.class) {
      return new Time().withPartial(new Partial(Time.fields, new int[] {0, 0, 0, 0}));
    }

    throw new NotImplementedException(String.format("MinValue is not implemented for type %s.", type.getTypeName()));
  }

  public static Object maxValue(Type type) {
    if (type == Integer.class) {
      return Integer.MAX_VALUE;
    }
    else if (type == BigDecimal.class) {
      return new BigDecimal("9999999999999999999999999999.99999999");
    }
    else if (type == Quantity.class) {
      return new Quantity().withValue((BigDecimal)maxValue(BigDecimal.class));
    }
    else if (type == DateTime.class) {
      return new DateTime().withPartial(new Partial(DateTime.fields, new int[] {9999, 12, 31, 23, 59, 59, 999}));
    }
    else if (type == Time.class) {
      return new Time().withPartial(new Partial(Time.fields, new int[] {23, 59, 59, 999}));
    }

    throw new NotImplementedException(String.format("MaxValue is not implemented for type %s.", type.getTypeName()));
  }
}
