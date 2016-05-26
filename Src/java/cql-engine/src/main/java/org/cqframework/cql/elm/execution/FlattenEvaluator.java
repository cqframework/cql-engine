package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.ArrayList;

/**
 * Created by Bryn on 5/25/2016.
 */
public class FlattenEvaluator extends Flatten {

    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);
        if (value == null) {
            return null;
        }

        ArrayList resultList = new ArrayList();
        for (Object element : (Iterable)value) {
            for (Object subElement : (Iterable)element) {
                resultList.add(subElement);
            }
        }

        return resultList;
    }
}
