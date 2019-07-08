package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

public class ReplaceMatchesEvaluator extends org.cqframework.cql.elm.execution.ReplaceMatches {

    public static Object replaceMatches(String argument, String pattern, String substitution) {
        if (argument == null || pattern == null || substitution == null) {
            return null;
        }

        return argument.replaceAll(pattern, substitution);
    }

    @Override
    protected Object internalEvaluate(Context context) {
        String argument = (String) getOperand().get(0).evaluate(context);
        String pattern = (String) getOperand().get(1).evaluate(context);
        String substitution = (String) getOperand().get(2).evaluate(context);

        return replaceMatches(argument, pattern, substitution);
    }
}
