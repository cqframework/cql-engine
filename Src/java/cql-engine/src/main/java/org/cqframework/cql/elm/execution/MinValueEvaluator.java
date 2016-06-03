package org.cqframework.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.cqframework.cql.execution.Context;
import org.joda.time.Partial;

import java.math.BigDecimal;

/**
 * Created by Bryn on 5/25/2016.
 */
public class MinValueEvaluator extends MinValue {

    @Override
    public Object evaluate(Context context) {
        switch (valueType.getLocalPart()) {
            case "Integer": return org.cqframework.cql.runtime.Interval.minValue(Integer.class);
            case "Decimal": return org.cqframework.cql.runtime.Interval.minValue(BigDecimal.class);
            case "Quantity": return org.cqframework.cql.runtime.Interval.minValue(org.cqframework.cql.runtime.Quantity.class);
            case "DateTime": return org.cqframework.cql.runtime.Interval.minValue(Partial.class);
            //case "Time": return org.cqframework.cql.runtime.Interval.minValue(PartialTime.class);
            default: throw new NotImplementedException(String.format("minValue not implemented for type %s", valueType.getLocalPart()));
        }
    }
}
