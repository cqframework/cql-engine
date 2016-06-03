package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.ArrayList;

/**
 * Created by Bryn on 5/25/2016.
 */
public class DistinctEvaluator extends Distinct {

    public static java.util.List<Object> distinct(Iterable<Object> source) {
        ArrayList<Object> result = new ArrayList<Object>();
        for (Object element : source) {
            if (!InEvaluator.in(element, result)) {
                result.add(element);
            }
        }
        return result;
    }

    @Override
    public Object evaluate(Context context) {
        Object value = this.getOperand().evaluate(context);
        return distinct((Iterable<Object>)value);
    }
}
