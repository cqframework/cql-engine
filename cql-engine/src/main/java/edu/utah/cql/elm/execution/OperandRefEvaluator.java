package edu.utah.cql.elm.execution;

import edu.utah.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class OperandRefEvaluator extends org.cqframework.cql.elm.execution.OperandRef {

    @Override
    public Object evaluate(Context context) {
        return context.resolveVariable(this.getName(), true).getValue();
    }
}
