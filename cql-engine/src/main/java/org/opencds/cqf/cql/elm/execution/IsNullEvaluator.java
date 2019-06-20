package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
is null(argument Any) Boolean

The is null operator determines whether or not its argument evaluates to null.
If the argument evaluates to null, the result is true; otherwise, the result is false.
*/

public class IsNullEvaluator extends org.cqframework.cql.elm.execution.IsNull {

    public static Object isNull(Object operand) {
        return operand == null;
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return isNull(operand);
    }
}
