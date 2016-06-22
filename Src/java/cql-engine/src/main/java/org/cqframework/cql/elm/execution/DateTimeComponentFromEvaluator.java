package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.DateTime;

import java.util.Arrays;
import java.util.ArrayList;

/**
* Created by Chris Schuler on 6/22/2016
*/
public class DateTimeComponentFromEvaluator extends DateTimeComponentFrom {

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);
    String precision = getPrecision().value();

    if (precision == null) {
      throw new IllegalArgumentException("Precision must be specified.");
    }

    if (operand instanceof DateTime) {
      DateTime dateTime = (DateTime)operand;

      // DateTimePrecision Enum represents precision as Titlecase Strings
      ArrayList<String> indexes = new ArrayList<>(Arrays.asList("Year", "Month", "Day", "Hour", "Minute", "Second", "Millisecond"));
      int idx = indexes.indexOf(precision);

      if (idx != -1) {
        // check level of precision
        if (idx + 1 > dateTime.getPartial().size()) {
          return null;
        }

        return dateTime.getPartial().getValue(idx);
      }

      else {
        throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
      }
    }
    throw new IllegalArgumentException(String.format("Cannot DateTimeComponentFrom arguments of type '%s'.", operand.getClass().getName()));
  }
}
