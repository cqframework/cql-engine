package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;
import org.cqframework.cql.runtime.Value;
import java.util.*;
import java.math.BigDecimal;

/*
* The Mode operator returns the statistical mode of the elements in source.
* If the source contains no non-null elements, null is returned.
* If the source is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/13/2016
*/
public class ModeEvaluator extends Mode {

  public static Object mode(Object source) {

    if (source instanceof Iterable) {
      Iterable<Object> element = (Iterable<Object>)source;
      Iterator<Object> itr = element.iterator();

      if (!itr.hasNext()) { return null; } // empty list
      Object mode = new Object();
      ArrayList<Object> values = new ArrayList<>();
      while (itr.hasNext()) {
        Object value = itr.next();
        if (value != null) { values.add(value); }
      }

      if (values.isEmpty()) { return null; } // all null
      values = MedianEvaluator.sortList(values);

      int max = 0;
      for (int i = 0; i < values.size(); ++i) {
        int count = (values.lastIndexOf(values.get(i)) - i) + 1;
        if (count > max) {
          mode = values.get(i);
          max = count;
        }
      }
      return mode;
    }
    else { return null; }
  }

  @Override
  public Object evaluate(Context context) {
    Object source = getSource().evaluate(context);
    if (source == null) { return null; }

    return mode(source);
  }
}
