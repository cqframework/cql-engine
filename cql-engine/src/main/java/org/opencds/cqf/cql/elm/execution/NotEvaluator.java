package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
not (argument Boolean) Boolean

The not operator returns true if the argument is false and false if the argument is true. Otherwise, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class NotEvaluator extends org.cqframework.cql.elm.execution.Not {

    public static Boolean not(Boolean operand) {
        return operand == null ? null : !operand;
    }

    @Override
    public Object evaluate(Context context) {
        Boolean operand = (Boolean) getOperand().evaluate(context);

        return context.logTrace(this.getClass(), not(operand), operand);
    }
}
