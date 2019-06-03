package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Date;
import org.opencds.cqf.cql.runtime.DateTime;

import java.lang.reflect.InvocationTargetException;

// for Uncertainty
/*
CalculateAgeInYears(birthDate Date) Integer
CalculateAgeInYears(birthDate DateTime) Integer
CalculateAgeInMonths(birthDate Date) Integer
CalculateAgeInMonths(birthDate DateTime) Integer
CalculateAgeInWeeks(birthDate Date) Integer
CalculateAgeInWeeks(birthDate DateTime) Integer
CalculateAgeInDays(birthDate Date) Integer
CalculateAgeInDays(birthDate DateTime) Integer
CalculateAgeInHours(birthDate DateTime) Integer
CalculateAgeInMinutes(birthDate DateTime) Integer
CalculateAgeInSeconds(birthDate DateTime) Integer

The CalculateAge operators calculate the age of a person born on the given birth date/time
    as of today/now in the precision named in the operator.

If the birthdate is null, the result is null.

The CalculateAge operators are defined in terms of a date/time duration calculation.
    This means that if the given birthDate is not specified to the level of precision corresponding
        to the operator being invoked, the result will be an uncertainty over the range of possible values,
        potentially causing some comparisons to return null.
*/

public class CalculateAgeEvaluator extends org.cqframework.cql.elm.execution.CalculateAge {

    public static Object calculateAge(Object operand, String precision, Object today) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (operand == null) {
            return null;
        }

        return CalculateAgeAtEvaluator.calculateAgeAt(operand, today, precision);
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        String precision = getPrecision().value();

        Object today =
                operand instanceof Date
                        ? DateFromEvaluator.dateFrom(context.getEvaluationDateTime())
                        : context.getEvaluationDateTime();

        try
        {
            return calculateAge(operand, precision, today);
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}