package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.util.List;

/*
*** LIST NOTES ***
Length(argument List<T>) Integer

The Length operator returns the number of elements in a list.
If the argument is null, the result is null.

*** STRING NOTES ***
Length(argument String) Integer

The Length operator returns the number of characters in a string.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class LengthEvaluator extends org.cqframework.cql.elm.execution.Length {

    public static Object length(Object operand) {
        if (operand == null) {
            return 0;
        }

        if (operand instanceof String) {
            return ((String) operand).length();
        }

        if (operand instanceof Iterable) {
            if (operand instanceof List) {
                return ((List) operand).size();
            }

            else {
                int size = 0;
                for(Object curr : (Iterable) operand) {
                    size++;
                }

                return size;
            }
        }

        throw new IllegalArgumentException(String.format("Cannot perform Length operator on type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), length(operand), operand);
    }
}
