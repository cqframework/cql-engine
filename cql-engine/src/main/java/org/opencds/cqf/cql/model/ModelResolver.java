package org.opencds.cqf.cql.model;


public interface ModelResolver {
    String getPackageName();

    void setPackageName(String packageName);

    Object resolvePath(Object target, String path);

    Object getContextPath(String contextType, String targetType);

    Class resolveType(String typeName);

    Class resolveType(Object value);

    String resolveClassName(String typeName);

	Object createInstance(String typeName);

    void setValue(Object target, String path, Object value);

    Boolean objectEqual(Object left, Object right);

    Boolean objectEquivalent(Object left, Object right);
}