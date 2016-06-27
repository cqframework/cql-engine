package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/*
The Today operator returns the date (with no time component) of the start timestamp associated with the evaluation request.
See the Now operator for more information on the rationale for defining the Today operator in this way.
*/

/**
* Created by Chris Schuler on 6/21/2016 (v1)
*/
public class TodayEvaluator extends Today {

  @Override
  public Object evaluate(Context context) {
    return org.cqframework.cql.runtime.DateTime.getToday();
  }
}
