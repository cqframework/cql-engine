package edu.utah.cql.elm.execution;

import edu.utah.cql.execution.Context;

/**
* Created by Chris Schuler on 8/20/2016
*/
public class ValueSetRefEvaluator extends org.cqframework.cql.elm.execution.ValueSetRef {

  @Override
  public Object evaluate(Context context) {
    return context.resolveValueSetRef(this.getLibraryName(), this.getName());
  }
}
