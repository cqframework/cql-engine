package org.opencds.cqf.cql.runtime;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;

public class Time extends BaseTemporal {

    private OffsetTime time;
    public OffsetTime getTime() {
        return time;
    }
    public Time withTime(OffsetTime time) {
        this.time = time;
        return this;
    }

    public Time withEvaluationOffset(ZoneOffset evaluationOffset) {
        this.evaluationOffset = evaluationOffset;
        return this;
    }

    public Time withPrecision(Precision precision) {
        this.precision = precision;
        return this;
    }

    public Time(OffsetTime time, Precision precision) {
        this.time = time;
        this.precision = precision;
    }

    public Time(String dateString, ZoneOffset offset) {
        dateString = dateString.replace("T", "");
        String[] tzSplit = dateString.contains("Z") ? dateString.split("Z") : dateString.split("[+-]");
        int size = tzSplit[0].split(":").length;
        if (tzSplit[0].contains(".")) {
            ++size;
        }
        precision = Precision.fromTimeIndex(size - 1);
        if (tzSplit.length == 1 && !dateString.contains("Z")) {
            dateString = TemporalHelper.autoCompleteDateTimeString(dateString, precision);
            dateString += offset.getId();
        }

        time = OffsetTime.parse(dateString);
    }

    public Time(BigDecimal offset, int ... timeElements) {
        if (timeElements.length == 0) {
            throw new IllegalArgumentException("Time must include an hour");
        }

        StringBuilder timeString = new StringBuilder();
        String[] stringElements = TemporalHelper.normalizeTimeElements(timeElements);

        for (int i = 0; i < stringElements.length; ++i) {
            if (i == 0) {
                timeString.append(stringElements[i]);
                continue;
            }
            else if (i < 3) {
                timeString.append(":");
            }
            else if (i == 3) {
                timeString.append(".");
            }
            timeString.append(stringElements[i]);
        }

        precision = Precision.fromTimeIndex(stringElements.length - 1);
        timeString = new StringBuilder().append(TemporalHelper.autoCompleteDateTimeString(timeString.toString(), precision));

        if (offset == null) {
            timeString.append(ZoneOffset.systemDefault().getRules().getStandardOffset(Instant.now()).getId());
        }
        else {
            timeString.append(ZoneOffset.ofHoursMinutes(offset.intValue(), new BigDecimal("60").multiply(offset.remainder(BigDecimal.ONE)).intValue()).getId());
        }

        time = OffsetTime.parse(timeString.toString());
    }

    public Time expandPartialMinFromPrecision(Precision thePrecision) {
        OffsetTime ot = this.time.plusHours(0);
        for (int i = thePrecision.toTimeIndex() + 1; i < 4; ++i) {
            ot = ot.with(
                    Precision.fromTimeIndex(i).toChronoField(),
                    ot.range(Precision.fromTimeIndex(i).toChronoField()).getMinimum()
            );
        }
        return new Time(ot, this.precision).withEvaluationOffset(this.evaluationOffset);
    }

    public Time expandPartialMin(Precision thePrecision) {
        OffsetTime ot = this.getTime().plusHours(0);
        return new Time(ot, thePrecision == null ? Precision.MILLISECOND : thePrecision).withEvaluationOffset(this.evaluationOffset);
    }

    public Time expandPartialMax(Precision thePrecision) {
        OffsetTime ot = this.getTime().plusHours(0);
        for (int i = this.getPrecision().toTimeIndex() + 1; i < 4; ++i) {
            if (i <= thePrecision.toTimeIndex()) {
                ot = ot.with(
                        Precision.fromTimeIndex(i).toChronoField(),
                        ot.range(Precision.fromTimeIndex(i).toChronoField()).getMaximum()
                );
            }
            else {
                ot = ot.with(
                        Precision.fromTimeIndex(i).toChronoField(),
                        ot.range(Precision.fromTimeIndex(i).toChronoField()).getMinimum()
                );
            }
        }
        return new Time(ot, thePrecision == null ? Precision.MILLISECOND : thePrecision).withEvaluationOffset(this.evaluationOffset);
    }

    @Override
    public boolean isUncertain(Precision thePrecision) {
        return this.precision.toTimeIndex() < thePrecision.toTimeIndex();
    }

    @Override
    public Interval getUncertaintyInterval(Precision thePrecision) {
        Time start = expandPartialMin(thePrecision);
        Time end = expandPartialMax(thePrecision).expandPartialMinFromPrecision(thePrecision);
        return new Interval(start, true, end, true);
    }

    @Override
    public Integer compare(BaseTemporal other, boolean forSort) {
        boolean differentPrecisions = this.getPrecision() != other.getPrecision();

        Precision thePrecision;
        if (differentPrecisions) {
            Integer result = this.compareToPrecision(other, Precision.getHighestTimePrecision(this.precision, other.precision));
            if (result == null && forSort) {
                return this.precision.toTimeIndex() > other.precision.toTimeIndex() ? 1 : -1;
            }
            return result;
        }
        else {
            return compareToPrecision(other, this.precision);
        }
    }

    @Override
    public Integer compareToPrecision(BaseTemporal other, Precision thePrecision) {
        boolean leftMeetsPrecisionRequirements = this.precision.toTimeIndex() >= thePrecision.toTimeIndex();
        boolean rightMeetsPrecisionRequirements = other.precision.toTimeIndex() >= thePrecision.toTimeIndex();

        // adjust dates to evaluation offset
        OffsetTime leftTime = this.time.withOffsetSameInstant(evaluationOffset);
        OffsetTime rightTime = ((Time) other).time.withOffsetSameInstant(evaluationOffset);

        if (!leftMeetsPrecisionRequirements || !rightMeetsPrecisionRequirements) {
            thePrecision = Precision.getLowestTimePrecision(this.precision, other.precision);
        }

        for (int i = 0; i < thePrecision.toTimeIndex() + 1; ++i) {
            int leftComp = leftTime.get(Precision.getTimeChronoFieldFromIndex(i));
            int rightComp = rightTime.get(Precision.getTimeChronoFieldFromIndex(i));
            if (leftComp > rightComp) {
                return 1;
            }
            else if (leftComp < rightComp) {
                return -1;
            }
        }

        if (leftMeetsPrecisionRequirements && rightMeetsPrecisionRequirements) {
            return 0;
        }

        return null;
    }

    @Override
    public int compareTo(@Nonnull BaseTemporal other) {
        return this.compare(other, true);
    }

    @Override
    public Boolean equivalent(Object other) {
        Integer comparison = compare((BaseTemporal) other, false);
        return comparison != null && comparison == 0;
    }

    @Override
    public Boolean equal(Object other) {
        Integer comparison = compare((BaseTemporal) other, false);
        return comparison == null ? null : comparison == 0;
    }

    @Override
    public String toString() {
        switch (precision) {
            case HOUR: return String.format("%02d", time.getHour());
            case MINUTE: return String.format("%02d:%02d", time.getHour(), time.getMinute());
            case SECOND: return String.format("%02d:%02d:%02d", time.getHour(), time.getMinute(), time.getSecond());
            default: return String.format("%02d:%02d:%02d.%03d", time.getHour(), time.getMinute(), time.getSecond(), time.get(precision.toChronoField()));
        }
    }
}
