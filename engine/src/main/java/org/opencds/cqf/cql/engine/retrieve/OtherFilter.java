package org.opencds.cqf.cql.engine.retrieve;

public class OtherFilter {
    public OtherFilter(String path, String comparator, Object value) {
        this.path = path;
        this.comparator = comparator;
        this.value = value;
    }

    private String path;
    public String getPath() {
        return path;
    }

    private String comparator;
    public String getComparator() {
        return comparator;
    }

    private Object value;
    public Object getValue() {
        return value;
    }
}
