package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.Iterator;

/**
 * Created by Bryn on 5/25/2016.
 */
public class AnyTrueEvaluator extends AnyTrue {
    @Override
    public Object evaluate(Context context) {
        // TODO: Fix this
        Object src = getSource();

        if(src instanceof List) {
            java.util.List<Expression> element = ((List)src).getElement();
            Iterator<Expression> elemsItr = element.iterator();
            while (elemsItr.hasNext()) {
                Expression exp = elemsItr.next();
                Boolean boolVal = (Boolean) exp.evaluate(context);

                if (Boolean.TRUE == boolVal) return true;
            }
        }else{
            return null;
        }

        return false;
    }
}
