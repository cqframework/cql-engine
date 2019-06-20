package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

public class ParameterRefEvaluator extends org.cqframework.cql.elm.execution.ParameterRef {

    @Override
    public Object evaluate(Context context) {
        return context.resolveParameterRef(this.getLibraryName(), this.getName());
    }
}
