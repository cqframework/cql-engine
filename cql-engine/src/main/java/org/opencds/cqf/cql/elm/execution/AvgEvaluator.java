package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import org.opencds.cqf.cql.runtime.Value;

import java.util.Iterator;
import java.math.BigDecimal;

/*
Avg(argument List<Decimal>) Decimal
Avg(argument List<Quantity>) Quantity

* The Avg operator returns the average of the non-null elements in the source.
* If the source contains no non-null elements, null is returned.
* If the source is null, the result is null.
* Returns values of type BigDecimal or Quantity
*/

/**
* Created by Chris Schuler on 6/13/2016
*/
public class AvgEvaluator extends org.cqframework.cql.elm.execution.Avg {

  public static Object avg(Object src) {

    if (src == null) {
      return null;
    }

    BigDecimal avg = new BigDecimal(0);
    int size = 0;

    if (src instanceof Iterable) {
      Iterable<Object> element = (Iterable<Object>)src;
      Iterator<Object> itr = element.iterator();

      if (!itr.hasNext()) { // empty list
        return null;
      }

      while (itr.hasNext()) {
        Object value = itr.next();

        if (value == null) {
          continue;
        }

        ++size;

        if (value instanceof BigDecimal) {
          avg = avg.add((BigDecimal)value);
        }

        else if (value instanceof Quantity) {
          avg = avg.add(((Quantity)value).getValue());
        }

        else {
          throw new IllegalArgumentException(String.format("Cannot Average arguments of type '%s'.", value.getClass().getName()));
        }
      }
    }

    else { // TODO: maybe throw exception here?
      return null;
    }

    if (size == 0) { // all elements null
      return null;
    }

    return Value.verifyPrecision((BigDecimal) DivideEvaluator.divide(avg, new BigDecimal(size)));
  }

  @Override
  public Object evaluate(Context context) {

    Object src = getSource().evaluate(context);

    return context.logTrace(this.getClass(), avg(src), src);
  }
}
