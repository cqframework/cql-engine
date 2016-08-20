package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

public class CodeSystemRefEvaluator extends CodeSystemRef {

  @Override
  public Object evaluate(Context context) {
    return context.resolveCodeSystemRef(this.getLibraryName(), this.getName());    
  }
}
