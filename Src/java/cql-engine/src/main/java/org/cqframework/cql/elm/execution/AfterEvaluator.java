package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Value;

/**
* Created by Chris Schuler on 6/7/2016
*/
public class AfterEvaluator extends After {

  @Override
  public Object evaluate(Context context) {
    Object testLeft = getOperand().get(0).evaluate(context);
    Object testRight = getOperand().get(1).evaluate(context);

    if (testLeft == null || testRight == null) { return null; }

    if (testLeft instanceof Interval && testRight instanceof Interval){
      Interval leftInterval = (Interval)testLeft;
      Interval rightInterval = (Interval)testRight;
      Object left = leftInterval.getStart();
      Object right = rightInterval.getEnd();

      return Value.compareTo(left, right, ">");
    }

    else if (testLeft instanceof Interval && !(testRight instanceof Interval)) {
      Interval leftInterval = (Interval)testLeft;
      Object right = testRight;

      return Value.compareTo(leftInterval.getStart(), right, ">");
    }

    else if (!(testLeft instanceof Interval) && testRight instanceof Interval) {
      Object left = testLeft;
      Interval rightInterval = (Interval)testRight;

      return Value.compareTo(left, rightInterval.getEnd(), ">");
    }

    return null;
  }
}
