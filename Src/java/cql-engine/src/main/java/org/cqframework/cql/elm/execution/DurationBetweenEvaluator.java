package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Interval;

import java.util.Arrays;
import java.util.ArrayList;

/**
* Created by Chris Schuler on 6/22/2016
*/
public class DurationBetweenEvaluator extends DurationBetween {

  private static final Integer MONTH_OFFSET = 12;
  private static final Integer HOUR_OFFSET = 23;
  private static final Integer MINUTE_OFFSET = 59;
  private static final Integer SECOND_OFFSET = 59;
  private static final Integer MILLI_OFFSET = 999;

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
      int offset = 0;

      if (idx != -1) {
        // check for uncertainty
        if (idx + 1 > leftDT.getPartial().size()) {
          Integer max = rightDT.getPartial().property(DateTime.getField(idx)).withMaximumValue().getValue(idx);
          Integer low = rightDT.getPartial().getValue(idx);
          Integer high = low + max;
          return new Interval(low, true, high, true);
        }
        else if (idx + 1 > rightDT.getPartial().size()) {
          Integer max = leftDT.getPartial().property(DateTime.getField(idx)).withMaximumValue().getValue(idx);
          Integer low = max + 1 - leftDT.getPartial().getValue(idx);
          Integer high = low + max;
          return new Interval(low, true, high, true);
        }
        // check that the element after idx for rightDT is greater than or equal to leftDT element - if not, minus 1 from result
        if (leftDT.getPartial().size() > idx + 1 && rightDT.getPartial().size() > idx + 1) {
          if (rightDT.getPartial().getValue(idx+1) < leftDT.getPartial().getValue(idx+1)) {
            offset = -1;
          }
        }
        return rightDT.getPartial().getValue(idx) - leftDT.getPartial().getValue(idx) + offset;
        }
      }

      else {
        throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
      }
    throw new IllegalArgumentException(String.format("Cannot DifferenceBetween arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
  }
}
