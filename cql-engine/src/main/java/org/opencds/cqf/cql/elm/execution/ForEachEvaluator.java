package org.opencds.cqf.cql.elm.execution;


import org.opencds.cqf.cql.execution.Context;

import java.util.ArrayList;
import java.util.List;

public class ForEachEvaluator extends org.cqframework.cql.elm.execution.ForEach {

    public Object forEach(Iterable<Object> source, Object element, Context context) {
        if (source == null || element == null) {
            return null;
        }

        List<Object> retVal = new ArrayList<>();
        for (Object o : source) {
            retVal.add(context.resolvePath(o, element.toString()));
        }
        return retVal;
    }

    @Override
    public Object evaluate(Context context) {
        return forEach((Iterable<Object>) getSource().evaluate(context), getElement().evaluate(context), context);
    }
}
