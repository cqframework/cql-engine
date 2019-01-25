package org.opencds.cqf.cql.runtime;

import java.time.LocalDate;

public class Date extends BaseTemporal {

    private LocalDate date;
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        if (date.getYear() < 1) {
            throw new IllegalArgumentException(String.format("The year: %d falls below the accepted bounds of 0001-9999.", date.getYear()));
        }
        if (date.getYear() > 9999) {
            throw new IllegalArgumentException(String.format("The year: %d falls above the accepted bounds of 0001-9999.", date.getYear()));
        }
        this.date = date;
    }

    public Date(int year, int month, int day) {
        setDate(LocalDate.of(year, month, day));
    }

    @Override
    public Integer compare(BaseTemporal other, boolean forSort) {
        return null;
    }

    @Override
    public Integer compareToPrecision(BaseTemporal other, Precision p) {
        return null;
    }

    @Override
    public boolean isUncertain(Precision p) {
        return false;
    }

    @Override
    public Interval getUncertaintyInterval(Precision p) {
        return null;
    }

    @Override
    public int compareTo(BaseTemporal o) {
        return 0;
    }

    @Override
    public Boolean equivalent(Object other) {
        return null;
    }

    @Override
    public Boolean equal(Object other) {
        return null;
    }
}
