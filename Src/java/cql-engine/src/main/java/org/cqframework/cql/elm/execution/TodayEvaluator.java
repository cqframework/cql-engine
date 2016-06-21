package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

public class TodayEvaluator extends Today {

  @Override
  public Object evaluate(Context context) {
    return org.cqframework.cql.runtime.DateTime.getToday();
  }
}
