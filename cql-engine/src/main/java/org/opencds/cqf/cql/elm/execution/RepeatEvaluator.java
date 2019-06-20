package org.opencds.cqf.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.opencds.cqf.cql.execution.Context;

public class RepeatEvaluator extends org.cqframework.cql.elm.execution.Repeat {

    public static Object repeat(Object source, Object element, String scope) {
        // TODO
        throw new NotImplementedException("Repeat operation not yet implemented");
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);
        Object element = getElement().evaluate(context);
        String scope = getScope();

        return repeat(source, element, scope);
    }
}
