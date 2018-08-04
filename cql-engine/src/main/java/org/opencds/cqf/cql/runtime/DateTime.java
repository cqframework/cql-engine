package org.opencds.cqf.cql.runtime;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

public class DateTime extends BaseTemporal {

    private OffsetDateTime dateTime;
    public OffsetDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(OffsetDateTime dateTime) {
        if (dateTime.getYear() < 1) {
            throw new IllegalArgumentException(String.format("The year: %d falls below the accepted bounds of 0001-9999.", dateTime.getYear()));
        }

        if (dateTime.getYear() > 9999) {
            throw new IllegalArgumentException(String.format("The year: %d falls above the accepted bounds of 0001-9999.", dateTime.getYear()));
        }
        this.dateTime = dateTime;
    }
    public DateTime withDateTime(OffsetDateTime dateTime) {
        setDateTime(dateTime);
        return this;
    }

    public OffsetDateTime getDateTimeWithEvaluationOffset() {
        return this.dateTime.withOffsetSameInstant(evaluationOffset);
    }

    public DateTime withEvaluationOffset(ZoneOffset evaluationOffset) {
        this.evaluationOffset = evaluationOffset;
        return this;
    }

    public DateTime withPrecision(Precision precision) {
        this.precision = precision;
        return this;
    }

    public DateTime(OffsetDateTime dateTime, Precision precision) {
        setDateTime(dateTime);
        this.precision = precision;
    }

    public DateTime(String dateString, ZoneOffset offset) {
        int size = 0;
        if (dateString.contains("T")) {
            String[] datetimeSplit = dateString.split("T");
            size += datetimeSplit[0].split("-").length;
            String[] tzSplit = dateString.contains("Z") ? dateString.split("Z") : datetimeSplit[1].split("[+-]");
            size += tzSplit[0].split(":").length;
            if (tzSplit[0].contains(".")) {
                ++size;
            }
            precision = Precision.fromDateTimeIndex(size - 1);
            if (tzSplit.length == 1 && !dateString.contains("Z")) {
                dateString = TemporalHelper.autoCompleteDateTimeString(dateString, precision);
                dateString += offset.getId();
            }
        }
        else {
            size += dateString.split("-").length;
            precision = Precision.fromDateTimeIndex(size - 1);
            dateString = TemporalHelper.autoCompleteDateTimeString(dateString, precision);
            dateString += ZoneOffset.systemDefault().getRules().getStandardOffset(Instant.now()).getId();
        }

        setDateTime(OffsetDateTime.parse(dateString));
    }

    public DateTime(BigDecimal offset, int ... dateElements) {
        if (dateElements.length == 0) {
            throw new IllegalArgumentException("DateTime must include a year");
        }

        StringBuilder dateString = new StringBuilder();
        String[] stringElements = TemporalHelper.normalizeDateTimeElements(dateElements);

        for (int i = 0; i < stringElements.length; ++i) {
            if (i == 0) {
                dateString.append(stringElements[i]);
                continue;
            }
            else if (i < 3) {
                dateString.append("-");
            }
            else if (i == 3) {
                dateString.append("T");
            }
            else if (i < 6) {
                dateString.append(":");
            }
            else if (i == 6) {
                dateString.append(".");
            }
            dateString.append(stringElements[i]);
        }

        precision = Precision.fromDateTimeIndex(stringElements.length - 1);
        dateString = new StringBuilder().append(TemporalHelper.autoCompleteDateTimeString(dateString.toString(), precision));

        if (offset == null) {
            dateString.append(ZoneOffset.systemDefault().getRules().getStandardOffset(Instant.now()).getId());
        }
        else {
            dateString.append(ZoneOffset.ofHoursMinutes(offset.intValue(), new BigDecimal("60").multiply(offset.remainder(BigDecimal.ONE)).intValue()).getId());
        }

        setDateTime(OffsetDateTime.parse(dateString.toString()));
    }

    public DateTime expandPartialMinFromPrecision(Precision thePrecision) {
        OffsetDateTime odt = this.getDateTime().plusYears(0);
        for (int i = thePrecision.toDateTimeIndex() + 1; i < 7; ++i) {
            odt = odt.with(
                    Precision.fromDateTimeIndex(i).toChronoField(),
                    odt.range(Precision.fromDateTimeIndex(i).toChronoField()).getMinimum()
            );
        }
        return new DateTime(odt, this.precision).withEvaluationOffset(this.evaluationOffset);
    }

    public DateTime expandPartialMin(Precision thePrecision) {
        OffsetDateTime odt = this.getDateTime().plusYears(0);
        return new DateTime(odt, thePrecision == null ? Precision.MILLISECOND : thePrecision).withEvaluationOffset(this.evaluationOffset);
    }

    public DateTime expandPartialMax(Precision thePrecision) {
        OffsetDateTime odt = this.getDateTime().plusYears(0);
        for (int i = this.getPrecision().toDateTimeIndex() + 1; i < 7; ++i) {
            if (i <= thePrecision.toDateTimeIndex()) {
                odt = odt.with(
                        Precision.fromDateTimeIndex(i).toChronoField(),
                        odt.range(Precision.fromDateTimeIndex(i).toChronoField()).getMaximum()
                );
            }
            else {
                odt = odt.with(
                        Precision.fromDateTimeIndex(i).toChronoField(),
                        odt.range(Precision.fromDateTimeIndex(i).toChronoField()).getMinimum()
                );
            }
        }
        return new DateTime(odt, thePrecision == null ? Precision.MILLISECOND : thePrecision).withEvaluationOffset(this.evaluationOffset);
    }

    @Override
    public boolean isUncertain(Precision thePrecision) {
        if (thePrecision == Precision.WEEK) {
            thePrecision = Precision.DAY;
        }

        return this.precision.toDateTimeIndex() < thePrecision.toDateTimeIndex();
    }

    @Override
    public Interval getUncertaintyInterval(Precision thePrecision) {
        DateTime start = expandPartialMin(thePrecision);
        DateTime end = expandPartialMax(thePrecision).expandPartialMinFromPrecision(thePrecision);
        return new Interval(start, true, end, true);
    }

    @Override
    public Integer compare(BaseTemporal other, boolean forSort) {
        boolean differentPrecisions = this.getPrecision() != other.getPrecision();

        if (differentPrecisions) {
            Integer result = this.compareToPrecision(other, Precision.getHighestDateTimePrecision(this.precision, other.precision));
            if (result == null && forSort) {
                return this.precision.toDateTimeIndex() > other.precision.toDateTimeIndex() ? 1 : -1;
            }
            return result;
        }
        else {
            return compareToPrecision(other, this.precision);
        }
    }

    @Override
    public Integer compareToPrecision(BaseTemporal other, Precision thePrecision) {
        boolean leftMeetsPrecisionRequirements = this.precision.toDateTimeIndex() >= thePrecision.toDateTimeIndex();
        boolean rightMeetsPrecisionRequirements = other.precision.toDateTimeIndex() >= thePrecision.toDateTimeIndex();

        // adjust dates to evaluation offset
        OffsetDateTime leftDateTime = this.getDateTimeWithEvaluationOffset();
        OffsetDateTime rightDateTime = ((DateTime) other).getDateTimeWithEvaluationOffset();

        if (!leftMeetsPrecisionRequirements || !rightMeetsPrecisionRequirements) {
            thePrecision = Precision.getLowestDateTimePrecision(this.precision, other.precision);
        }

        for (int i = 0; i < thePrecision.toDateTimeIndex() + 1; ++i) {
            int leftComp = leftDateTime.get(Precision.getDateTimeChronoFieldFromIndex(i));
            int rightComp = rightDateTime.get(Precision.getDateTimeChronoFieldFromIndex(i));
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
            case YEAR: return String.format("%04d", dateTime.getYear());
            case MONTH: return String.format("%04d-%02d", dateTime.getYear(), dateTime.getMonthValue());
            case DAY: return String.format("%04d-%02d-%02d", dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
            case HOUR: return String.format("%04d-%02d-%02dT%02d", dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getHour());
            case MINUTE: return String.format("%04d-%02d-%02dT%02d:%02d", dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute());
            case SECOND: return String.format("%04d-%02d-%02dT%02d:%02d:%02d", dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
            default: return String.format("%04d-%02d-%02dT%02d:%02d:%02d.%03d", dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond(), dateTime.get(precision.toChronoField()));
        }
    }

    // conversion functions

    public static DateTime fromJavaDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new DateTime(OffsetDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()), Precision.MILLISECOND);
    }
}
