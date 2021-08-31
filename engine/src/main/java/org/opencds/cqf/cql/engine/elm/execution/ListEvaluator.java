package org.opencds.cqf.cql.engine.elm.execution;

import java.util.ArrayList;

import org.cqframework.cql.elm.execution.Expression;
import org.opencds.cqf.cql.engine.execution.Context;

public class ListEvaluator extends org.cqframework.cql.elm.execution.List {

    @Override
    public void prepare(Context context) {
        for (Expression element : getElement()) {
            element.prepare(context);
        }
    }

    @Override
    protected Object internalEvaluate(Context context) {
        ArrayList<Object> result = new ArrayList<Object>();
        for (org.cqframework.cql.elm.execution.Expression element : this.getElement()) {
            result.add(element.evaluate(context));
        }
        return result;
    }
}
