package org.opencds.cqf.cql.engine.elm.execution;

import org.opencds.cqf.cql.engine.execution.Context;

// References a code system by its previously defined name

public class CodeSystemRefEvaluator extends org.cqframework.cql.elm.execution.CodeSystemRef {

  @Override
  protected Object internalEvaluate(Context context) {
    return context.resolveCodeSystemRef(this.getLibraryName(), this.getName()).evaluate(context);
  }
}
