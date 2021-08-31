package org.opencds.cqf.cql.engine.retrieve;

import org.opencds.cqf.cql.engine.runtime.Interval;

public class DateRangeFilter {
    public DateRangeFilter(String datePath, String dateLowPath, String dateHighPath, Interval dateRange) {
        this.datePath = datePath;
        this.dateLowPath = dateLowPath;
        this.dateHighPath = dateHighPath;
        this.dateRange = dateRange;
    }
    private String datePath;
    public String getDatePath() {
        return datePath;
    }

    private String dateLowPath;
    public String getDateLowPath() {
        return dateLowPath;
    }

    private String dateHighPath;
    public String getDateHighPath() {
        return dateHighPath;
    }

    private Interval dateRange;
    public Interval getDateRange() {
        return dateRange;
    }
}
