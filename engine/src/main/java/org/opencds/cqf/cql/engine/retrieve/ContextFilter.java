package org.opencds.cqf.cql.engine.retrieve;

public class ContextFilter {
    public ContextFilter(String name, String path, Object value) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("name required");
        }
        if (path == null || path.equals("")) {
            throw new IllegalArgumentException("path required");
        }
        this.name = name;
        this.path = path;
        this.value = value;
    }

    private String name;
    public String getName() {
        return this.name;
    }

    private String path;
    public String getPath() {
        return this.path;
    }

    private Object value;
    public Object getValue() {
        return this.getValue();
    }
}
