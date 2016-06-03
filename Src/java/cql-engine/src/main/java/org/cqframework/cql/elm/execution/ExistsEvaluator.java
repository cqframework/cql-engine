package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class ExistsEvaluator extends Exists {

    @Override
    public Object evaluate(Context context) {
        Iterable<Object> value = (Iterable<Object>)getOperand().evaluate(context);
        java.util.Iterator<Object> iterator = value.iterator();
        if (iterator.hasNext()) {
            return true;
        }

        return false;
    }
}
