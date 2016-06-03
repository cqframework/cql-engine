package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class QuantityEvaluator extends Quantity {

    @Override
    public Object evaluate(Context context) {
        return new org.cqframework.cql.runtime.Quantity().withValue(this.getValue()).withUnit(this.getUnit());
    }
}
