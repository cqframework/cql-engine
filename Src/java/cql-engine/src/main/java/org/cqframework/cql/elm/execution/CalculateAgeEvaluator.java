package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.DateTime;
import java.util.ArrayList;
// for Uncertainty
import org.cqframework.cql.runtime.Interval;
import org.cqframework.cql.runtime.Uncertainty;
/*
CalculateAgeInYears(birthDate DateTime) Integer
CalculateAgeInMonths(birthDate DateTime) Integer
CalculateAgeInDays(birthDate DateTime) Integer
CalculateAgeInHours(birthDate DateTime) Integer
CalculateAgeInMinutes(birthDate DateTime) Integer
CalculateAgeInSeconds(birthDate DateTime) Integer

The CalculateAge operators calculate the age of a person born on the given birthdate as of now in the precision named in the operator.
If the birthdate is null, the result is null.
The CalculateAge operators are defined in terms of a DateTime duration calculation.
  This means that if the given birthDate is not specified to the level of precision corresponding to the operator being invoked,
    the result will be an uncertainty over the range of possible values, potentially causing some comparisons to return null.
*/

public class CalculateAgeEvaluator extends CalculateAge {

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);
    String precision = getPrecision().value();

    if (operand == null) { return null; }

    return CalculateAgeAtEvaluator.calculateAgeAt((DateTime)operand, DateTime.getToday(), precision);
  }
}
