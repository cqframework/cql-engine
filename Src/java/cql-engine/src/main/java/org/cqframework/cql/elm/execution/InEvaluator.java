package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;

/*
*** NOTES FOR INTERVAL ***
The in operator for intervals returns true if the given point is greater than or equal to the starting point of the interval,
  and less than or equal to the ending point of the interval.
For open interval boundaries, exclusive comparison operators are used.
For closed interval boundaries, if the interval boundary is null, the result of the boundary comparison is considered true.
If either argument is null, the result is null. 
*/

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/8/2016 - added Interval Logic
 */
public class InEvaluator extends In {

  public static Boolean in(Object testElement, Iterable<? extends Object> list) {
      if (list == null) {
          return null;
      }

      for (Object element : list) {
          if (Value.equivalent(testElement, element)) {
              return true;
          }
      }

      return false;
  }

  @Override
  public Object evaluate(Context context) {
    Object test = getOperand().get(1).evaluate(context);

    if (test instanceof Interval) {
      Object left = getOperand().get(0).evaluate(context);
      Interval right = (Interval)test;

      if (left != null && right != null) {
        Object rightStart = right.getStart();
        Object rightEnd = right.getEnd();

        if (rightStart == null || rightEnd == null) { return null; }

        return (Value.compareTo(left, rightStart, ">=") && Value.compareTo(left, rightEnd, "<="));
      }
      return null;
    }

    else {
      Object testElement = getOperand().get(0).evaluate(context);
      Iterable<Object> list = (Iterable<Object>)getOperand().get(1).evaluate(context);
      return in(testElement, list);
    }
  }
}
