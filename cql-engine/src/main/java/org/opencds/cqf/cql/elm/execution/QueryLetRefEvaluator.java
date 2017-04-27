package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/**
 * Created by Chris Schuler on 11/22/2016.
 */
public class QueryLetRefEvaluator  extends org.cqframework.cql.elm.execution.QueryLetRef {

    @Override
    public Object evaluate (Context context) {
        return context.resolveLetExpressionRef(this.getName()).evaluate(context);
    }
}
