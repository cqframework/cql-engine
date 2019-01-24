package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
ToQuantity(argument Decimal) Quantity
ToQuantity(argument Integer) Quantity
ToQuantity(argument String) Quantity
The ToQuantity operator converts the value of its argument to a Quantity value.
The operator accepts strings using the following format:
(+|-)?#0(.0#)?('<unit>')?
Meaning an optional polarity indicator, followed by any number of digits (including none) followed by at least one digit,
    optionally followed by a decimal point, at least one digit, and any number of additional digits, all optionally
    followed by a unit designator as a string literal specifying a valid, case-sensitive UCUM unit of measure.
    Spaces are allowed between the quantity value and the unit designator.
Note that the decimal value of the quantity returned by this operator must be a valid value in the range representable
    for Decimal values in CQL.
If the input string is not formatted correctly, or cannot be interpreted as a valid Quantity value, the result is null.
For the Integer and Decimal overloads, the operator returns a quantity with the value of the argument
    and a unit of '1' (the default unit).
If the argument is null, the result is null.
*/

public class ToQuantityEvaluator extends org.cqframework.cql.elm.execution.ToQuantity {

    public static Object toQuantity(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof String) {
            String str = (String) operand;
            StringBuilder number = new StringBuilder();
            StringBuilder unit = new StringBuilder();
            for (char c : str.toCharArray()) {
                if ((Character.isDigit(c) || c == '.' || c == '+' || c == '-') && unit.length() == 0) {
                    number.append(c);
                } else if (Character.isLetter(c) || c == '/') {
                    unit.append(c);
                } else if (c == ' ') {
                    // continue
                } else {
                    throw new IllegalArgumentException(String.format("%c is not allowed in ToQuantity format", c));
                }
            }
            return new Quantity().withValue(new BigDecimal(number.toString())).withUnit(unit.toString().toLowerCase());
        } else if (operand instanceof Integer) {
            return new Quantity().withValue(new BigDecimal((Integer) operand)).withDefaultUnit();
        } else if (operand instanceof BigDecimal) {
            return new Quantity().withValue((BigDecimal) operand).withDefaultUnit();
        }

        throw new IllegalArgumentException(String.format("Cannot cast a value of type %s as Quantity - use String values.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return toQuantity(operand);
    }
}