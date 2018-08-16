package org.opencds.cqf.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import javax.xml.namespace.QName;
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

    public static Object maxValue(String type) {
        if (type == null) {
            return null;
        }

        if (type.endsWith("Integer")) {
            return Value.MAX_INT;
        }
        if (type.endsWith("Decimal")) {
            return Value.MAX_DECIMAL;
        }
        // TODO - the temporal types are slightly limited here ... using system defaults for timezone instead of what's specified for evaluation
        if (type.endsWith("DateTime")) {
            return new DateTime(TemporalHelper.getDefaultOffset(), 9999, 12, 31, 23, 59, 59, 999).withEvaluationOffset(TemporalHelper.getDefaultZoneOffset());
        }
        if (type.endsWith("Time")) {
            return new Time(TemporalHelper.getDefaultOffset(), 23, 59, 59, 999).withEvaluationOffset(TemporalHelper.getDefaultZoneOffset());
        }
        // NOTE: Quantity max is not standard
        if (type.endsWith("Quantity")) {
            return new Quantity().withValue(Value.MAX_DECIMAL).withUnit("1");
        }

        throw new NotImplementedException(String.format("The Maximum operator is not implemented for type %s", type));
    }

    @Override
    public Object evaluate(Context context) {
        String type = getValueType().getLocalPart();
        if (type == null) {
            return null;
        }

        if (type.endsWith("Integer")) {
            return Value.MAX_INT;
        }
        if (type.endsWith("Decimal")) {
            return Value.MAX_DECIMAL;
        }
        if (type.endsWith("DateTime")) {
            return new DateTime(TemporalHelper.zoneToOffset(context.getEvaluationDateTime().getDateTime().getOffset()), 9999, 12, 31, 23, 59, 59, 999).withEvaluationOffset(context.getEvaluationDateTime().getDateTime().getOffset());
        }
        if (type.endsWith("Time")) {
            return new Time(TemporalHelper.zoneToOffset(context.getEvaluationDateTime().getDateTime().getOffset()), 23, 59, 59, 999).withEvaluationOffset(context.getEvaluationDateTime().getDateTime().getOffset());
        }
        throw new NotImplementedException(String.format("The Maximum operator is not implemented for type %s", type));
    }
}
