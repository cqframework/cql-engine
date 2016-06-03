package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.util.Iterator;

/**
 * Created by Bryn on 5/25/2016.
 */
public class AllTrueEvaluator extends AllTrue {
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

                if (boolVal == null || Boolean.FALSE == boolVal) return false;
            }
        }else{
            return null;
        }

        return true;
    }
}
