package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.IntervalTypeSpecifier;
import org.cqframework.cql.elm.execution.NamedTypeSpecifier;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.List;
import java.util.stream.StreamSupport;

/*
*** LIST NOTES ***
Length(argument List<T>) Integer

The Length operator returns the number of elements in a list.
If the argument is null, the result is 0.

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
        if (operand instanceof String) {
            return stringLength((String) operand);
        }

        if (operand instanceof Iterable) {
            return listLength((Iterable) operand);
        }

        throw new IllegalArgumentException(String.format("Cannot perform Length operator on type '%s'.", operand.getClass().getName()));
    }

    public static Integer stringLength(String operand) {
        if (operand == null) {
            return null;
        }

        return operand.length();
    }

    public static Integer listLength(Iterable operand) {
        if (operand == null) {
            return 0;
        }

        return (int) StreamSupport.stream(((Iterable<?>) operand).spliterator(), false).count();
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        // null operand case
        if (getOperand() instanceof AsEvaluator) {
            if (((AsEvaluator) getOperand()).getAsTypeSpecifier() instanceof NamedTypeSpecifier) {
                return stringLength((String) operand);
            }
            else {
                return listLength((Iterable) operand);
            }
        }

        return length(operand);
    }
}
