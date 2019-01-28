package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
Today() Date

The Today operator returns the date (with no time component) of the start timestamp associated with the evaluation request.
See the Now operator for more information on the rationale for defining the Today operator in this way.
*/

public class TodayEvaluator extends org.cqframework.cql.elm.execution.Today {

  @Override
  public Object evaluate(Context context) {
    return DateFromEvaluator.dateFrom(context.getEvaluationDateTime());
  }
}
