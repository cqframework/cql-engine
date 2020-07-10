package org.opencds.cqf.cql.engine.runtime;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.opencds.cqf.cql.engine.elm.execution.MaxValueEvaluator;
import org.opencds.cqf.cql.engine.elm.execution.MinValueEvaluator;

public class Value {

    public static final Integer MAX_INT = Integer.MAX_VALUE;
    public static final BigDecimal MAX_DECIMAL = new BigDecimal("9999999999999999999999999999.99999999");
    public static final Integer MIN_INT = Integer.MIN_VALUE;
    public static final BigDecimal MIN_DECIMAL = new BigDecimal("-9999999999999999999999999999.99999999");

    public static BigDecimal verifyPrecision(BigDecimal value) {
        // NOTE: The CQL specification does not mandate a maximum precision, it specifies a minimum precision,
        // implementations are free to provide more precise values. However, for simplicity and to provide
        // a consistent reference implementation, this engine applies the minimum precision as the maximum precision.
        // NOTE: precision is often used loosely to mean "number of decimal places", which is not what BigDecimal.precision() means
        // BigDecimal.scale() (when positive) is the number of digits to the right of the decimal
        // at most 8 decimal places
        if (value.scale() > 8) {
            return value.setScale(8, RoundingMode.FLOOR);
        }

        return value;
    }

    public static BigDecimal validateDecimal(BigDecimal ret) {
        if (ret.compareTo((BigDecimal) MaxValueEvaluator.maxValue("Decimal")) > 0) {
            return null;
        }
        else if (ret.compareTo((BigDecimal) MinValueEvaluator.minValue("Decimal")) < 0) {
            return null;
        }
        else if (ret.precision() > 8) {
            return ret.setScale(8, RoundingMode.DOWN);
        }
        return ret;
    }

    public static Integer validateInteger(Integer ret) {
        if (ret > MAX_INT || ret < MIN_INT) {
            return null;
        }
        return ret;
    }

    public static Integer validateInteger(Double ret) {
        if (ret > MAX_INT || ret < MIN_INT) {
            return null;
        }
        return ret.intValue();
    }
}
