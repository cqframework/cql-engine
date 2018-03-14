package org.opencds.cqf.cql.runtime;

import javax.annotation.Nonnull;
import org.opencds.cqf.cql.elm.execution.EqualEvaluator;
import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;

import java.math.BigDecimal;

/**
 * Created by Bryn on 4/15/2016.
 */
public class Quantity implements CqlType, Comparable<Quantity> {

    public Quantity() {
        this.value = new BigDecimal("0.0");
        this.unit = "";
    }

    private BigDecimal value;
    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    public Quantity withValue(BigDecimal value) {
        setValue(value);
        return this;
    }

    private String unit;
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Quantity withUnit(String unit) {
        setUnit(unit);
        return this;
    }

    @Override
    public int compareTo(@Nonnull Quantity other) {
        return this.getValue().compareTo(other.getValue());
    }

    @Override
    public Boolean equivalent(Object other) {
        return EquivalentEvaluator.equivalent(this.getValue(), ((Quantity) other).getValue())
                && EquivalentEvaluator.equivalent(this.getUnit(), ((Quantity) other).getUnit());
    }

    @Override
    public Boolean equal(Object other) {
        Boolean valueEqual = EqualEvaluator.equal(this.getValue(), ((Quantity) other).getValue());
        Boolean unitEqual = EqualEvaluator.equal(this.getUnit(), ((Quantity) other).getUnit());
        return valueEqual == null || unitEqual == null ? null : valueEqual && unitEqual;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getValue().toString(), getUnit());
    }
}
