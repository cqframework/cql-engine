package org.opencds.cqf.cql.runtime;

import org.opencds.cqf.cql.elm.execution.MaxValueEvaluator;
import org.opencds.cqf.cql.elm.execution.MinValueEvaluator;

import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Bryn on 5/2/2016.
 */
public class Value {

    public static final Integer MAX_INT = Integer.MAX_VALUE;
    public static final BigDecimal MAX_DECIMAL = new BigDecimal("9999999999999999999999999999.99999999");
    public static final Integer MIN_INT = Integer.MIN_VALUE;
    public static final BigDecimal MIN_DECIMAL = new BigDecimal("-9999999999999999999999999999.99999999");

    public static BigDecimal verifyPrecision(BigDecimal value) {
        // at most 8 decimal places
        if (value.precision() > 8) {
            return value.setScale(8, RoundingMode.FLOOR);
        }

        else if (value.precision() < 2) {
            return value.setScale(1, RoundingMode.FLOOR);
        }

        return value;
    }

    public static String getValueType(Class clazz) {
        if (clazz == Integer.class) {
            return "Integer";
        }
        if (clazz == BigDecimal.class) {
            return "Decimal";
        }
        if (clazz == DateTime.class) {
            return "DateTime";
        }
        if (clazz == Time.class) {
            return "Time";
        }

        throw new IllegalArgumentException("Invalid type for Successor/Predecessor operator " + clazz.getName());
    }

    public static BigDecimal validateDecimal(BigDecimal ret) {
        if (ret.compareTo((BigDecimal) MaxValueEvaluator.maxValue("Decimal")) > 0) {
            throw new IllegalArgumentException("Decimal exceeds the maximum value allowed");
        }
        else if (ret.compareTo((BigDecimal) MinValueEvaluator.minValue("Decimal")) < 0) {
            throw new IllegalArgumentException("Decimal precedes the minimum value allowed");
        }
        else if (ret.precision() > 8) {
            throw new IllegalArgumentException("Decimal exceeds the level of precision allowed");
        }
        return ret;
    }
}
