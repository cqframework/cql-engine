package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Christopher Schuler on 6/20/2017.
 */
public class MatchesEvaluator extends org.cqframework.cql.elm.execution.Matches {

    public static Object matches(String argument, String pattern) {
        if (argument == null || pattern == null) {
            return null;
        }

        return argument.matches(pattern);
    }

    @Override
    public Object evaluate(Context context) {
        String argument = (String) getOperand().get(0).evaluate(context);
        String pattern = (String) getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), matches(argument, pattern), argument, pattern);
    }
}
