package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
* LastPositionOf(pattern String, argument String) Integer
*
* The LastPositionOf operator returns the 0-based index of the last appearance of the given pattern in the given string.
*
* If the pattern is not found, the result is -1.
*
* If either argument is null, the result is null.
*/

/**
 * Created by Christopher Schuler on 6/12/2017.
 */
public class LastPositionOfEvaluator extends org.cqframework.cql.elm.execution.LastPositionOf {

    public static Object lastPositionOf(Object string, Object pattern) {
        if (pattern == null || string == null) {
            return null;
        }

        if (pattern instanceof String) {
            return ((String)string).lastIndexOf((String) pattern);
        }

        throw new IllegalArgumentException(String.format("Cannot perform LastPositionOf operation with arguments of type '%s' and '%s'.", pattern.getClass().getName(), string.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object string = getString().evaluate(context);
        Object pattern = getPattern().evaluate(context);

        return context.logTrace(this.getClass(), lastPositionOf(string, pattern), string, pattern);
    }
}
