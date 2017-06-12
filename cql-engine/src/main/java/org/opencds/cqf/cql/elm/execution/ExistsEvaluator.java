package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.util.Iterator;

/*
exists(argument List<T>) Boolean

The exists operator returns true if the list contains any elements, including null elements.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class ExistsEvaluator extends org.cqframework.cql.elm.execution.Exists {

    public static Object exists(Object operand) {
        Iterable value = (Iterable) operand;

        if (value == null) {
            return false;
        }

        Iterator iterator = value.iterator();

        return iterator.hasNext();
    }

    @Override
    public Object evaluate(Context context) {
        Object operand= getOperand().evaluate(context);

        return context.logTrace(this.getClass(), exists(operand), operand);
    }
}
