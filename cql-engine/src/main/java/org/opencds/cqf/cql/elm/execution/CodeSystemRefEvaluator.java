package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

// References a code system by its previously defined name

public class CodeSystemRefEvaluator extends org.cqframework.cql.elm.execution.CodeSystemRef {

  @Override
  public Object evaluate(Context context) {
    return context.resolveCodeSystemRef(this.getLibraryName(), this.getName()).evaluate(context);
  }
}
