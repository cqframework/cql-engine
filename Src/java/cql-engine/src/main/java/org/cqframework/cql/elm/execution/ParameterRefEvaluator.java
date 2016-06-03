package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class ParameterRefEvaluator extends ParameterRef {

    @Override
    public Object evaluate(Context context) {
        return context.resolveParameterRef(this.getLibraryName(), this.getName());
    }
}
