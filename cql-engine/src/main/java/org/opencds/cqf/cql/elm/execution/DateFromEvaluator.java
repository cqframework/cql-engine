package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Precision;

/*
date from(argument DateTime) DateTime

NOTE: this is within the purview of DateTimeComponentFrom
  Description available in that class
*/

/**
 * Created by Chris Schuler on 6/22/2016
 */
public class DateFromEvaluator extends org.cqframework.cql.elm.execution.DateFrom {

    public static DateTime dateFrom(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof DateTime) {
            if (((DateTime) operand).getPrecision().toDateTimeIndex() < 1) {
                return new DateTime(((DateTime) operand).getDateTime().plusYears(0), Precision.YEAR).expandPartialMinFromPrecision(Precision.YEAR);
            }
            else if (((DateTime) operand).getPrecision().toDateTimeIndex() < 2) {
                return new DateTime(((DateTime) operand).getDateTime().plusYears(0), Precision.MONTH).expandPartialMinFromPrecision(Precision.MONTH);
            }
            else {
                return new DateTime(((DateTime) operand).getDateTime().plusYears(0), Precision.DAY).expandPartialMinFromPrecision(Precision.DAY);
            }
        }

        throw new IllegalArgumentException(String.format("Cannot perform DateFrom with argument of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        return dateFrom(operand);
    }
}
