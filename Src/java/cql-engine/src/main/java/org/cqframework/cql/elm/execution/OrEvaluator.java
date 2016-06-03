package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class OrEvaluator extends Or {

    @Override
    public Object evaluate(Context context) {
        Boolean left = (Boolean) getOperand().get(0).evaluate(context);
        Boolean right = (Boolean) getOperand().get(1).evaluate(context);

        if (left == null || right == null) {
            if ((left != null && left == true) || (right != null && right == true)) {
                return true;
            }

            return null;
        }

        return (left || right);
    }
}
