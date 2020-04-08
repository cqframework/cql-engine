package org.opencds.cqf.cql.elm.execution;

import java.math.BigDecimal;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Value;

/*
structured type Quantity
{
  value Decimal
  unit String
}

The Quantity type represents quantities with a specified unit within CQL.
*/

public class QuantityEvaluator extends org.cqframework.cql.elm.execution.Quantity {

    @Override
    protected Object internalEvaluate(Context context) {
        BigDecimal value = Value.verifyPrecision(this.getValue());
        return new org.opencds.cqf.cql.runtime.Quantity().withValue(value).withUnit(this.getUnit());
    }
}
