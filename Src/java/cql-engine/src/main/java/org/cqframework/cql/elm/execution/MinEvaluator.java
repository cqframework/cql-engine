package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.Iterator;

/*
The Min operator returns the minimum element in the source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
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

        if (value == null) { continue; } // skip null

        if ((Boolean)LessEvaluator.less(value, min)) { min = value; }

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
