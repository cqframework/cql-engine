package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
* StartsWith(argument String, prefix String) Boolean
*
* The StartsWith operator returns true if the given string starts with the given prefix.
*
* If the prefix is the empty string, the result is true.
*
* If either argument is null, the result is null.
*/

/**
 * Created by Christopher Schuler on 6/12/2017.
 */
public class StartsWithEvaluator extends org.cqframework.cql.elm.execution.StartsWith {

    public static Object startsWith(Object argument, Object prefix) {
        if (argument == null || prefix == null) {
            return null;
        }

        return ((String) argument).startsWith((String) prefix);
    }

    @Override
    public Object evaluate(Context context) {
        Object argument = getOperand().get(0).evaluate(context);
        Object prefix = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), startsWith(argument, prefix), argument, prefix);
    }
}
