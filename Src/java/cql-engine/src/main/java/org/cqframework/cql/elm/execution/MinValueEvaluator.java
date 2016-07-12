package org.cqframework.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.DateTime;
import org.cqframework.cql.runtime.Time;

import java.math.BigDecimal;

/*
minimum<T>() T

The minimum operator returns the minimum representable value for the given type.
The minimum operator is defined for the Integer, Decimal, DateTime, and Time types.
For Integer, minimum returns the minimum signed 32-bit integer, -231.
For Decimal, minimum returns the minimum representable decimal value, (-1037 – 1) / 108 (-9999999999999999999999999999.99999999).
For DateTime, minimum returns the minimum representable date/time value, DateTime(1, 1, 1, 0, 0, 0, 0).
For Time, minimum returns the minimum representable time value, Time(0, 0, 0, 0).
For any other type, attempting to invoke minimum results in an error.
*/

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
            case "DateTime": return org.cqframework.cql.runtime.Interval.minValue(DateTime.class);
            case "Time": return org.cqframework.cql.runtime.Interval.minValue(Time.class);
            default: throw new NotImplementedException(String.format("minValue not implemented for type %s", valueType.getLocalPart()));
        }
    }
}
