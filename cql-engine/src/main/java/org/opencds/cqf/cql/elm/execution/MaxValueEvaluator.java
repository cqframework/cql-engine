package org.opencds.cqf.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Quantity;
import org.opencds.cqf.cql.runtime.Time;
import org.opencds.cqf.cql.runtime.Value;

import java.math.BigDecimal;

/*
maximum<T>() T

The maximum operator returns the maximum representable value for the given type.
The maximum operator is defined for the Integer, Decimal, DateTime, and Time types.
For Integer, maximum returns the maximum signed 32-bit integer, 231 - 1.
For Decimal, maximum returns the maximum representable decimal value, (1037 – 1) / 108 (9999999999999999999999999999.99999999).
For DateTime, maximum returns the maximum representable date/time value, DateTime(9999, 12, 31, 23, 59, 59, 999).
For Time, maximum returns the maximum representable time value, Time(23, 59, 59, 999).
For any other type, attempting to invoke maximum results in an error.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class MaxValueEvaluator extends org.cqframework.cql.elm.execution.MaxValue {

    public static Object maximum(String type) {
        switch (type) {
            case "Integer": return Value.maxValue(Integer.class);
            case "Decimal": return Value.maxValue(BigDecimal.class);
            case "Quantity": return Value.maxValue(Quantity.class);
            case "DateTime": return Value.maxValue(DateTime.class);
            case "Time": return Value.maxValue(Time.class);
            default: throw new NotImplementedException(String.format("The Maximum operator is not implemented for type %s", type));
        }
    }

    @Override
    public Object evaluate(Context context) {
        String type = getValueType().getLocalPart();

        return context.logTrace(this.getClass(), maximum(type), type);
    }
}
