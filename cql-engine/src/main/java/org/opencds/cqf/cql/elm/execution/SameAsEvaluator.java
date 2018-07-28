package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Precision;

/*
same precision as(left DateTime, right DateTime) Boolean
same precision as(left Time, right Time) Boolean

The same-precision-as operator compares two date/time values to the specified precision for equality.
  Individual component values are compared starting from the year component down to the specified precision.
    If all values are specified and have the same value for each component, then the result is true.
      If a compared component is specified in both dates, but the values are not the same, then the result is false.
        Otherwise the result is null, as there is not enough information to make a determination.
For DateTime values, precision must be one of: year, month, day, hour, minute, second, or millisecond.
For Time values, precision must be one of: hour, minute, second, or millisecond.
For comparisons involving date/time or time values with imprecision, note that the result of the comparison may be null,
  depending on whether the values involved are specified to the level of precision used for the comparison.
If either or both arguments are null, the result is null.
*/

/**
 * Created by Chris Schuler on 6/23/2016
 */
public class SameAsEvaluator extends org.cqframework.cql.elm.execution.SameAs {

    public static Boolean sameAs(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        if (precision == null) {
            precision = "millisecond";
        }

        if (left instanceof BaseTemporal && right instanceof BaseTemporal) {
            Integer result = ((BaseTemporal) left).compareToPrecision((BaseTemporal) right, Precision.fromString(precision));
            return result == null ? null : result == 0;
        }

        throw new IllegalArgumentException(String.format("Cannot perform SameAs operation with arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return sameAs(left, right, precision);
    }
}
