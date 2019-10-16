package org.opencds.cqf.cql.data;


public interface TypeProvider {
    String getPackageName();

    void setPackageName(String packageName);

    Object resolvePath(Object target, String path);

    Object resolveContextPath(String contextType, String targetType);

    Class resolveType(String typeName);

    Class resolveType(Object value);

    Object createInstance(String typeName);

    void setValue(Object target, String path, Object value);

    Boolean objectEqual(Object left, Object right);

    Boolean objectEquivalent(Object left, Object right);
}