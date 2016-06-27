package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/*
The Now operator returns the date and time of the start timestamp associated with the evaluation request.
Now is defined in this way for two reasons:
1.	The operation will always return the same value within any given evaluation, ensuring that the result of
      an expression containing Now will always return the same result.
2.	The operation will return the timestamp associated with the evaluation request, allowing the evaluation to
      be performed with the same timezone information as the data delivered with the evaluation request.
*/

/**
* Created by Chris Schuler on 6/21/2016 (v1)
*/
public class NowEvaluator extends Now {

  @Override
  public Object evaluate(Context context) {
    return org.cqframework.cql.runtime.DateTime.getNow();
  }
}
