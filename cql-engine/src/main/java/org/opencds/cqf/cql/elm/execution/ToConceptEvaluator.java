package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Concept;
import org.opencds.cqf.cql.runtime.Code;

/*
ToConcept(argument Code) Concept

The ToConcept operator converts a value of type Code to a Concept value with the given Code as its primary and only Code.
If the Code has a display value, the resulting Concept will have the same display value.
If the argument is null, the result is null.
*/

public class ToConceptEvaluator extends org.cqframework.cql.elm.execution.ToConcept {

    public static Object toConcept(Object operand) {
        if (operand == null) {
            return null;
        }

        Concept result = new Concept();

        if (operand instanceof Iterable) {
            for (Object code : (Iterable) operand) {
                result.withCode((Code)code);
            }
            return result;
        }
        else if (operand instanceof Code) {
            result.withCode((Code) operand);
            return result;
        }

        throw new InvalidOperatorArgument(
                "ToConcept(Code)",
                String.format("ToConcept(%s)", operand.getClass().getName())
        );
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        return toConcept(operand);
    }
}
