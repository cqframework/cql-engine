package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
Lower(argument String) String

The Lower operator returns the lower case of its argument.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class LowerEvaluator extends org.cqframework.cql.elm.execution.Lower {

    public static Object lower(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof String) {
            return ((String) operand).toLowerCase();
        }
        throw new IllegalArgumentException(String.format("Cannot perform Lower operation with argument of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), lower(operand), operand);
    }
}
