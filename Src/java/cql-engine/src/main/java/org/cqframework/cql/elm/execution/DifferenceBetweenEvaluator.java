package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.DateTime;

import java.util.Arrays;
import java.util.ArrayList;

/**
* Created by Chris Schuler on 6/22/2016
*/
public class DifferenceBetweenEvaluator extends DifferenceBetween {

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);
    String precision = getPrecision().value();

    if (precision == null) {
      throw new IllegalArgumentException("Precision must be specified.");
    }

    if (left == null || right == null) { return null; }

    if (left instanceof DateTime && right instanceof DateTime) {
      DateTime leftDT = (DateTime)left;
      DateTime rightDT = (DateTime)right;

      ArrayList<String> indexes = new ArrayList<>(Arrays.asList("Year", "Month", "Day", "Hour", "Minute", "Second", "Millisecond"));
      int idx = indexes.indexOf(precision);

      if (idx != -1) {
        // check level of precision
        if (idx + 1 > leftDT.getPartial().size() || idx + 1 > rightDT.getPartial().size()) {
          return null;
        }

        return rightDT.getPartial().getValue(idx) - leftDT.getPartial().getValue(idx);
      }

      else {
        throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
      }
    }
    throw new IllegalArgumentException(String.format("Cannot DifferenceBetween arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
  }
}
