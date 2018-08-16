package org.opencds.cqf.cql.runtime;

import java.time.ZoneOffset;

public abstract class BaseTemporal implements CqlType, Comparable<BaseTemporal> {

    Precision precision;
    public Precision getPrecision() {
        return precision;
    }

    ZoneOffset evaluationOffset = TemporalHelper.getDefaultZoneOffset();
    public ZoneOffset getEvaluationOffset() {
        return evaluationOffset;
    }
    public void setEvaluationOffset(ZoneOffset evaluationOffset) {
        this.evaluationOffset = evaluationOffset;
    }

    public static String getHighestPrecision(BaseTemporal ... values) {
        int max = -1;
        boolean isDateTime = true;
        for (BaseTemporal baseTemporal : values) {
            if (baseTemporal instanceof DateTime) {
                if (baseTemporal.precision.toDateTimeIndex() > max) {
                    max = ((DateTime) baseTemporal).precision.toDateTimeIndex();
                }
            }
            else if (baseTemporal instanceof Time) {
                isDateTime = false;
                if (baseTemporal.precision.toTimeIndex() > max) {
                    max = ((Time) baseTemporal).precision.toTimeIndex();
                }
            }
        }

        if (max == -1) {
            return Precision.MILLISECOND.toString();
        }

        return isDateTime ? Precision.fromDateTimeIndex(max).toString() : Precision.fromTimeIndex(max).toString();
    }

    public abstract Integer compare(BaseTemporal other, boolean forSort);
    public abstract Integer compareToPrecision(BaseTemporal other, Precision p);
    public abstract boolean isUncertain(Precision p);
    public abstract Interval getUncertaintyInterval(Precision p);
}
