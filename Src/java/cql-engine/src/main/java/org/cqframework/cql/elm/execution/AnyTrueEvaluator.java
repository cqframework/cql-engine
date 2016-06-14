package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.Iterator;

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler on 6/13/2016
 */
public class AnyTrueEvaluator extends AnyTrue {
    @Override
    public Object evaluate(Context context) {

        Object source = getSource().evaluate(context);
        if (source == null) { return null; }

        if(source instanceof Iterable) {
            Iterable<Object> element = (Iterable<Object>)source;
            Iterator<Object> elemsItr = element.iterator();
            if (!elemsItr.hasNext()) { return null; } // empty list
            while (elemsItr.hasNext()) {
                Object exp = elemsItr.next();
                if (exp == null) { continue; } // skip null
                Boolean boolVal = (Boolean) exp;

                if (Boolean.TRUE == boolVal) return true;
            }
        }else{
            return null;
        }

        return false; // all null or all false
    }
}
