package edu.utah.cql.elm.execution;

import edu.utah.cql.execution.Context;
import edu.utah.cql.runtime.Time;

/*
TimeOfDay() Time

The TimeOfDay operator returns the time of day of the start timestamp associated with the evaluation request.
See the Now operator for more information on the rationale for defining the TimeOfDay operator in this way.
*/

/**
* Created by Chris Schuler on 7/1/2016
*/
public class TimeOfDayEvaluator extends org.cqframework.cql.elm.execution.TimeOfDay {

  @Override
  public Object evaluate(Context context) {
    return Time.getTimeOfDay();
  }
}
