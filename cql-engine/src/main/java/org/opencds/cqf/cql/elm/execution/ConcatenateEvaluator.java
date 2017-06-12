package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
+(left String, right String) String

The concatenate (+) operator performs string concatenation of its arguments.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class ConcatenateEvaluator extends org.cqframework.cql.elm.execution.Concatenate {

    public static Object concatenate(Object left, Object right) {
        if (left == null || right == null) {
            return null;
        }

        if(left instanceof String && right instanceof String){
            return ((String)left).concat((String)right);
        }

        throw new IllegalArgumentException(String.format("Cannot Concatenate arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), concatenate(left, right), left, right);
    }
}
