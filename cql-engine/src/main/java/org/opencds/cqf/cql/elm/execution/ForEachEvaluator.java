package org.opencds.cqf.cql.elm.execution;


import org.opencds.cqf.cql.execution.Context;

import java.util.ArrayList;
import java.util.List;

/**
*   Created by Chris Schuler on 9/25/2016
*/
public class ForEachEvaluator extends org.cqframework.cql.elm.execution.ForEach {

    // TODO: create tests

    public Object forEach(Object source, Object element, Context context) {
        if (source == null || element == null) {
            return null;
        }

        List<Object> retVal = new ArrayList<>();
        for (Object o : (Iterable) source) {
            retVal.add(context.resolvePath(o, element.toString()));
        }
        return retVal;
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getSource().evaluate(context);
        Object element = getSource().evaluate(context);

        return context.logTrace(this.getClass(), forEach(source, element, context), source, element);
    }
}
