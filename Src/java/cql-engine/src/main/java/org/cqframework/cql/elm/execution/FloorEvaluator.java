package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class FloorEvaluator extends Floor {

    @Override
    public Object evaluate(Context context) {
        Object value = getOperand().evaluate(context);

        if (value == null) {
            return null;
        }

        if(value instanceof BigDecimal){
            return new BigDecimal(Math.floor(((BigDecimal)value).doubleValue()));
        }

        throw new IllegalArgumentException(String.format("Cannot do an Abs with argument of type '%s'.", value.getClass().getName()));
    }
}
