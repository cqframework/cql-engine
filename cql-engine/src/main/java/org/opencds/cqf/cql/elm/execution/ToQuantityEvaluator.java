package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import org.opencds.cqf.cql.runtime.Ratio;
import org.opencds.cqf.cql.runtime.Value;

import java.math.BigDecimal;

/*
ToQuantity(argument Decimal) Quantity
ToQuantity(argument Integer) Quantity
ToQuantity(argument Ratio) Quantity
ToQuantity(argument String) Quantity

Description:
The ToQuantity operator converts the value of its argument to a Quantity value.

For the String overload, the operator accepts strings using the following format:
(+|-)?#0(.0#)?('<unit>')?

Meaning an optional polarity indicator, followed by any number of digits (including none) followed by at least one digit,
  optionally followed by a decimal point, at least one digit, and any number of additional digits, all optionally
  followed by a unit designator as a string literal specifying a valid, case-sensitive UCUM unit of measure. Spaces are
  allowed between the quantity value and the unit designator.

Note that the decimal value of the quantity returned by this operator must be a valid value in the range representable
  for Decimal values in CQL.

If the input string is not formatted correctly, or cannot be interpreted as a valid Quantity value, the result is null.

For the Integer and Decimal overloads, the operator returns a quantity with the value of the argument and a unit of '1'
  (the default unit).

For the Ratio overload, the operator is equivalent to dividing the numerator of the ratio by the denominator.

If the argument is null, the result is null.

The following examples illustrate the behavior of the ToQuantity operator:

define DecimalOverload: ToQuantity(0.1) // 0.1 '1'
define IntegerOverload: ToQuantity(13) // 13 '1'
define StringOverload: ToQuantity('-0.1 \'mg\'') // -0.1 'mg'
define IsNull: ToQuantity('444 \'cm')

*/

public class ToQuantityEvaluator extends org.cqframework.cql.elm.execution.ToQuantity {

    public static Quantity toQuantity(Object operand) {
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
                } else if (c == ' ' || c == '\'') {
                    // continue
                } else {
                    throw new IllegalArgumentException(String.format("%c is not allowed in ToQuantity format", c));
                }
            }
            try {
                BigDecimal ret = new BigDecimal(number.toString());
                if (Value.validateDecimal(ret) == null) {
                    return null;
                }
                return new Quantity().withValue(ret).withUnit(unit.toString());
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        else if (operand instanceof Integer) {
            BigDecimal ret = new BigDecimal((Integer) operand);
            if (Value.validateDecimal(ret) == null) {
                return null;
            }
            return new Quantity().withValue(ret).withDefaultUnit();
        }
        else if (operand instanceof BigDecimal) {
            if (Value.validateDecimal((BigDecimal) operand) == null) {
                return null;
            }
            return new Quantity().withValue((BigDecimal) operand).withDefaultUnit();
        }
        else if (operand instanceof Ratio) {
            return (Quantity) DivideEvaluator.divide(((Ratio) operand).getNumerator(), ((Ratio) operand).getDenominator());
        }

        throw new IllegalArgumentException(String.format("Cannot cast a value of type %s as Quantity - use String, Integer, Decimal, or Ratio values.", operand.getClass().getName()));
    }

    @Override
    protected Object internalEvaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return toQuantity(operand);
    }
}