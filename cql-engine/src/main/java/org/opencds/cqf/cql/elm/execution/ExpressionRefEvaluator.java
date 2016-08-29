package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class ExpressionRefEvaluator extends org.cqframework.cql.elm.execution.ExpressionRef {

    @Override
    public Object evaluate(Context context) {
        return context.resolveExpressionRef(this.getLibraryName(), this.getName()).evaluate(context);
    }
}
