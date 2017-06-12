package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.util.Iterator;

/*
AnyTrue(argument List<Boolean>) Boolean

The AnyTrue operator returns true if any non-null element in the source is true.
If the source contains no non-null elements, false is returned.
If the source is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class AnyTrueEvaluator extends org.cqframework.cql.elm.execution.AnyTrue {

    public static Object anyTrue(Object src) {

        if (src == null) {
            return false;
        }

        if(src instanceof Iterable) {
            Iterable<Object> element = (Iterable<Object>)src;
            Iterator<Object> elemsItr = element.iterator();

            if (!elemsItr.hasNext()) { // empty list
                return false;
            }

            while (elemsItr.hasNext()) {
                Object exp = elemsItr.next();

                if (exp == null) { // skip null
                    continue;
                }

                Boolean boolVal = (Boolean) exp;

                if (Boolean.TRUE == boolVal) {
                    return true;
                }
            }
        }

        else {
            return null;
        }

        return false; // all null or all false
    }

    @Override
    public Object evaluate(Context context) {

        Object src = getSource().evaluate(context);

        return context.logTrace(this.getClass(), anyTrue(src), src);
    }
}
