package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/**
 * Created by Christopher on 4/25/2017.
 */
public class CodeRefEvaluator extends org.cqframework.cql.elm.execution.CodeRef {

    @Override
    public Object evaluate(Context context) {
        return context.resolveCodeRef(this.getName()).evaluate(context);
    }
}
