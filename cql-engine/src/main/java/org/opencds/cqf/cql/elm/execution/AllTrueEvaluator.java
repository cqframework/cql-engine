package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.util.Iterator;

/*
AllTrue(argument List<Boolean>) Boolean

The AllTrue operator returns true if all the non-null elements in the source are true.
If the source contains no non-null elements, true is returned.
If the source is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class AllTrueEvaluator extends org.cqframework.cql.elm.execution.AllTrue {

    public static Boolean allTrue(Object src) {
        if (src == null) {
            return null;
        }

        if (src instanceof Iterable) {
            Iterable element = (Iterable)src;
            Iterator elemsItr = element.iterator();

            if (!elemsItr.hasNext()) { // empty list
                return true;
            }

            while (elemsItr.hasNext()) {
                Object exp = elemsItr.next();

                if (exp == null) { // skip null
                    continue;
                }

                if (exp instanceof Boolean) {
                    Boolean boolVal = (Boolean) exp;

                    if (!boolVal) {
                        return false;
                    }
                }
                else {
                    throw new IllegalArgumentException(String.format("Cannot perform AllTrue operator on List with type %s", exp.getClass().getSimpleName()));
                }
            }
            return true;
        }

        throw new IllegalArgumentException(String.format("Cannot perform AllTrue operator on type %s", src.getClass().getSimpleName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object src = getSource().evaluate(context);
        return context.logTrace(this.getClass(), allTrue(src), src);
    }
}
