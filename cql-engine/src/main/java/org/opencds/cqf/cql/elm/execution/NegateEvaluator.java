package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
-(argument Integer) Integer
-(argument Decimal) Decimal
-(argument Quantity) Quantity

The negate (-) operator returns the negative of its argument.
When negating quantities, the unit is unchanged.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class NegateEvaluator extends org.cqframework.cql.elm.execution.Negate {

    public static Object negate(Object source) {
        if (source == null) {
            return null;
        }

        if (source instanceof Integer) {
            return -(int) source;
        }

        if (source instanceof BigDecimal) {
            return ((BigDecimal) source).negate();
        }

        if (source instanceof Quantity) {
            Quantity quantity = (Quantity) source;
            return new Quantity().withValue(quantity.getValue().negate()).withUnit(quantity.getUnit());
        }

        return source;
    }

    @Override
    public Object evaluate(Context context) {
        Object source = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), negate(source), source);
    }
}
