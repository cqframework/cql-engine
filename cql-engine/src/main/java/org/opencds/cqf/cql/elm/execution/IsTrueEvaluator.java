package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
is true(argument Boolean) Boolean

The is true operator determines whether or not its argument evaluates to true.
If the argument evaluates to true, the result is true; otherwise, the result is false.
*/

public class IsTrueEvaluator extends org.cqframework.cql.elm.execution.IsTrue {

    public static Object isTrue(Boolean operand) {
        return Boolean.TRUE == operand;
    }

    @Override
    public Object evaluate(Context context) {
        Boolean operand = (Boolean) getOperand().evaluate(context);

        return isTrue(operand);
    }
}
