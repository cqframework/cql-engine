package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class ToConceptEvaluator extends ToConcept {

    @Override
    public Object evaluate(Context context) {
        org.cqframework.cql.runtime.Concept result = new org.cqframework.cql.runtime.Concept();
        Object source = getOperand().evaluate(context);
        if (source instanceof Iterable) {
            for (Object code : (Iterable<Object>)source) {
                result.withCode((org.cqframework.cql.runtime.Code)code);
            }
        }
        else {
            result.withCode((org.cqframework.cql.runtime.Code)source);
        }
        return result;
    }
}
