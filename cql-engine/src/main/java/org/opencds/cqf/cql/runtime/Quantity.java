package org.opencds.cqf.cql.runtime;

import java.math.BigDecimal;

/**
 * Created by Bryn on 4/15/2016.
 */
public class Quantity {

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

    public Integer compareTo(Quantity other) {
        return this.getValue().compareTo(other.getValue());
    }

    public Boolean equal(Quantity other) {
        return value.equals(other.getValue()) && ((unit == null && other.getUnit() == null) || unit.equals(other.getUnit()));
    }

    @Override
    public String toString() {
        return String.format("%s %s", getValue().toString(), getUnit());
    }
}
