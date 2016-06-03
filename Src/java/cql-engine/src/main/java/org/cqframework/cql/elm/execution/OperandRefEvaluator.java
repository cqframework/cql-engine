package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class OperandRefEvaluator extends OperandRef {

    @Override
    public Object evaluate(Context context) {
        return context.resolveVariable(this.getName(), true).getValue();
    }
}
