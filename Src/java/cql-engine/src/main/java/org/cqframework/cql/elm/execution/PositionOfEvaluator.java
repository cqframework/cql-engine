package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class PositionOfEvaluator extends PositionOf {

    @Override
    public Object evaluate(Context context) {
        Object pattern = getPattern().evaluate(context);
        Object string = getString().evaluate(context);

        if (pattern == null || string == null) {
            return null;
        }

        if (pattern instanceof String) {
            return ((String)string).indexOf((String)pattern);
        }

        throw new IllegalArgumentException(String.format("Cannot %s arguments of type '%s'.", this.getClass().getSimpleName(), pattern.getClass().getName()));
    }
}
