package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.Value;
import java.math.BigDecimal;
import java.util.Iterator;

/*
* The Max operator returns the maximum element in the source.
* If the source contains no non-null elements, null is returned.
* If the source is null, the result is null.
* Possible return types include: Integer, BigDecimal, Quantity, DateTime, Time, String
*/

/**
* Created by Chris Schuler on 6/13/2016
*/
public class MinEvaluator extends Min {

  public static Object min(Object source) {
    if (source instanceof Iterable) {
      Iterable<Object> element = (Iterable<Object>)source;
      Iterator<Object> itr = element.iterator();

      if (!itr.hasNext()) { return null; } // empty list
      Object min = itr.next();
      while (min == null && itr.hasNext()) { min = itr.next(); }
      while (itr.hasNext()) {
        Object value = itr.next();

        if (value instanceof Integer || value instanceof BigDecimal || value instanceof Quantity) {
          if (Value.compareTo(min, value, ">")) { min = value; }
        }
        else if (value instanceof String) {
          if (((String)min).compareTo((String)value) > 0) { min = value; }
        }
        // TODO: implement DateTime and Time
        else {
          throw new IllegalArgumentException(String.format("Cannot Min arguments of type '%s'.", value.getClass().getName()));
        }
      }
      return min;
    }
    else { return null; }
  }

  @Override
  public Object evaluate(Context context) {

    Object source = getSource().evaluate(context);
    if (source == null) { return null; }

    return min(source);
  }
}
