package org.opencds.cqf.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.opencds.cqf.cql.execution.Context;

/**
 * Created by Christopher Schuler on 6/13/2017.
 */
public class DescendentsEvaluator extends org.cqframework.cql.elm.execution.Descendents {

    public static Object descendents(Object source) {
        // TODO
        throw new NotImplementedException("Descendents operation not yet implemented");
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);

        return context.logTrace(this.getClass(), descendents(source), source);
    }
}
