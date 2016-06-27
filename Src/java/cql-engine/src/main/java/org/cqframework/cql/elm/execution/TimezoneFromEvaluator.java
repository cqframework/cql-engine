package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.DateTime;

/*
NOTE: this is within the purview of DateTimeComponentFrom
*/

/**
* Created by Chris Schuler on 6/22/2016
*/
public class TimezoneFromEvaluator extends TimezoneFrom {

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);

    if (operand instanceof DateTime) {
      return ((DateTime)operand).getTimezoneOffset();
    }
    throw new IllegalArgumentException(String.format("Cannot TimezoneFrom arguments of type '%s'.", operand.getClass().getName()));
  }
}
