package org.opencds.cqf.cql.elm.execution;

import org.apache.commons.lang3.ArrayUtils;
import org.opencds.cqf.cql.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.execution.Context;

/*

    ConvertsToBoolean(argument String) Boolean

    The ConvertsToBoolean operator returns true if its argument can be converted to a Boolean value. See the ToBoolean
        operator for a description of the supported conversions.

    If the input cannot be interpreted as a valid Boolean value, the result is false.

    If the argument is null, the result is null.

*/

public class ConvertsToBooleanEvaluator extends org.cqframework.cql.elm.execution.ConvertsToBoolean {

    private static String[] validTrueValues = new String[]{ "true", "t", "yes", "y", "1" };
    private static String[] validFalseValues = new String[]{ "false", "f", "no", "n", "0" };

    public static Boolean convertsToBoolean(Object argument) {
        if (argument == null) {
            return null;
        }

        if (argument instanceof String) {
            return ArrayUtils.contains(validTrueValues, ((String) argument).toLowerCase())
                    || ArrayUtils.contains(validFalseValues, ((String) argument).toLowerCase());
        }

        throw new InvalidOperatorArgument(
                "ConvertsToBoolean(String)",
                String.format("ConvertsToBoolean(%s)", argument.getClass().getName())
        );
    }

    @Override
    protected Object internalEvaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        return convertsToBoolean(operand);
    }

}
