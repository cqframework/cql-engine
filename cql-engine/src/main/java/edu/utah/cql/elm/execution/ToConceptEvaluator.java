package edu.utah.cql.elm.execution;

import edu.utah.cql.execution.Context;
import edu.utah.cql.runtime.Concept;
import edu.utah.cql.runtime.Code;

/*
ToConcept(argument Code) Concept

The ToConcept operator converts a value of type Code to a Concept value with the given Code as its primary and only Code.
If the Code has a display value, the resulting Concept will have the same display value.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class ToConceptEvaluator extends org.cqframework.cql.elm.execution.ToConcept {

    @Override
    public Object evaluate(Context context) {
        Concept result = new Concept();
        Object source = getOperand().evaluate(context);
        if (source instanceof Iterable) {
            for (Object code : (Iterable<Object>)source) {
                result.withCode((Code)code);
            }
        }
        else {
            result.withCode((Code)source);
        }
        return result;
    }
}
