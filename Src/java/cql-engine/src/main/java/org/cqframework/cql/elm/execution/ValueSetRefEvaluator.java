package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

public class ValueSetRefEvaluator extends ValueSetRef {

  @Override
  public Object evaluate(Context context) {
    return context.resolveValueSetRef(this.getLibraryName(), this.getName());    
  }
}
