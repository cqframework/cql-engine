package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.ArrayList;

/**
 * Created by Bryn on 5/25/2016.
 */
public class ListEvaluator extends List {

    @Override
    public Object evaluate(Context context) {
        ArrayList<Object> result = new ArrayList<Object>();
        for (Expression element : this.getElement()) {
            result.add(element.evaluate(context));
        }
        return result;
    }
}
