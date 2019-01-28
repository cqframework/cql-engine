package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

/*

same _precision_ as(left Date, right Date) Boolean
same _precision_ as(left DateTime, right DateTime) Boolean
same _precision_ as(left Time, right Time) Boolean

The same-precision-as operator compares two date/time values to the specified precision for equality.
    The comparison is performed by considering each precision in order, beginning with years (or hours for time values).
    If the values are the same, comparison proceeds to the next precision;
    if the values are different, the comparison stops and the result is false;
    if either input has no value for the precision, the comparison stops and the result is null; if the specified precision has been reached,
        the comparison stops and the result is true.

If no precision is specified, the comparison is performed beginning with years (or hours for time values)
    and proceeding to the finest precision specified in either input.

For Date values, precision must be one of: year, month, or day.
For DateTime values, precision must be one of: year, month, day, hour, minute, second, or millisecond.
For Time values, precision must be one of: hour, minute, second, or millisecond.

Note specifically that due to variability in the way week numbers are determined, comparisons involving weeks are not supported.

When this operator is called with both Date and DateTime inputs, the Date values will be implicitly converted to DateTime as defined by the ToDateTime operator.

As with all date/time calculations, comparisons are performed respecting the timezone offset.

If either or both arguments are null, the result is null.

*/

public class SameAsEvaluator extends org.cqframework.cql.elm.execution.SameAs {

    public static Boolean sameAs(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        if (precision == null) {
            precision = BaseTemporal.getHighestPrecision((BaseTemporal) left, (BaseTemporal) right);
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
