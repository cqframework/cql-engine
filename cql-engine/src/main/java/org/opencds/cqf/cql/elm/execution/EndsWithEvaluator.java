package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/**
 * Created by Christopher Schuler on 6/20/2017.
 */
public class EndsWithEvaluator extends org.cqframework.cql.elm.execution.EndsWith {

    public static Object endsWith(String argument, String suffix) {
        if (argument == null || suffix == null) {
            return null;
        }
        return argument.endsWith(suffix);
    }

    @Override
    public Object evaluate(Context context) {
        String argument = (String) getOperand().get(0).evaluate(context);
        String suffix = (String) getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), endsWith(argument, suffix), argument, suffix);
    }
}
