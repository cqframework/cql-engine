package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.TemporalHelper;
import org.opencds.cqf.cql.runtime.Time;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

public class TemporalTypeTests {

    @Test
    public void DateTimeTests() {
        DateTime ndt = new DateTime(new BigDecimal("-8.5"), 1, 1, 1, 1, 1, 1, 1);
        int i = ndt.getDateTime().get(ChronoField.MILLI_OF_SECOND);
        DateTime anotherNdt = new DateTime(new BigDecimal("-8.5"), 1);
        i = ndt.getDateTime().get(ChronoField.MILLI_OF_SECOND);
        BaseTemporal nbt1 = ndt;
        BaseTemporal nbt2 = anotherNdt;
        nbt1.setEvaluationOffset(ZoneOffset.ofHours(-7));
        nbt2.setEvaluationOffset(ZoneOffset.ofHours(-7));
        Integer comparison = nbt1.compare(nbt2, false);

        Time nt = new Time(null, 1, 1, 1, 1);
        i = nt.getTime().get(ChronoField.MILLI_OF_SECOND);
        Time anotherNt = new Time(new BigDecimal("-5.0"), 1);
        BaseTemporal nbt3 = ndt;
        BaseTemporal nbt4 = anotherNdt;
        nbt3.setEvaluationOffset(ZoneOffset.ofHours(-7));
        nbt4.setEvaluationOffset(ZoneOffset.ofHours(-7));
        comparison = nbt3.compare(nbt4, false);

        BigDecimal offset = TemporalHelper.zoneToOffset(anotherNt.getTime().getOffset());

        String s = "s";
    }
 }
