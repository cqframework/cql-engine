package org.opencds.cqf.cql.engine.elm.execution;

import org.opencds.cqf.cql.engine.execution.Context;

public class ValueSetRefEvaluator extends org.cqframework.cql.elm.execution.ValueSetRef {

  @Override
  protected Object internalEvaluate(Context context) {
    return context.resolveValueSetRef(this.getLibraryName(), this.getName());
  }
}
