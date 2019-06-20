package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

public class OperandRefEvaluator extends org.cqframework.cql.elm.execution.OperandRef {

    @Override
    public Object evaluate(Context context) {
        return context.resolveVariable(this.getName(), true).getValue();
    }
}
