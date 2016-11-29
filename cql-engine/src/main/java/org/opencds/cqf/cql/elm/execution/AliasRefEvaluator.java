package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/**
 * Created by Christopher on 11/22/2016.
 */
public class AliasRefEvaluator extends org.cqframework.cql.elm.execution.AliasRef {

    @Override
    public Object evaluate(Context context) {
        return context.resolveAlias(this.getName());
    }
}
