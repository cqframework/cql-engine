package org.opencds.cqf.cql.engine.elm.execution;

import org.opencds.cqf.cql.engine.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.Date;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Precision;
import org.opencds.cqf.cql.engine.runtime.Quantity;
import org.opencds.cqf.cql.engine.runtime.TemporalHelper;
import org.opencds.cqf.cql.engine.runtime.Time;
import org.opencds.cqf.cql.engine.runtime.Value;

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
        if (type.endsWith("Date")) {
            return new Date(9999, 12, 31).setPrecision(Precision.DAY);
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

        throw new InvalidOperatorArgument(String.format("The Maximum operator is not implemented for type %s", type));
    }

    @Override
    protected Object internalEvaluate(Context context) {
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
        if (type.endsWith("Date")) {
            return new Date(9999, 12, 31).setPrecision(Precision.DAY);
        }
        if (type.endsWith("DateTime")) {
            return new DateTime(TemporalHelper.zoneToOffset(context.getEvaluationDateTime().getDateTime().getOffset()), 9999, 12, 31, 23, 59, 59, 999).withEvaluationOffset(context.getEvaluationDateTime().getDateTime().getOffset());
        }
        if (type.endsWith("Time")) {
            return new Time(TemporalHelper.zoneToOffset(context.getEvaluationDateTime().getDateTime().getOffset()), 23, 59, 59, 999).withEvaluationOffset(context.getEvaluationDateTime().getDateTime().getOffset());
        }
        throw new InvalidOperatorArgument(String.format("The Maximum operator is not implemented for type %s", type));
    }
}
