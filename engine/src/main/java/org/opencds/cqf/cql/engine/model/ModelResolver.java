package org.opencds.cqf.cql.engine.model;


public interface ModelResolver {
    String getPackageName();

    void setPackageName(String packageName);

    // Expected to return null whenever a path doesn't exist on the target.
    Object resolvePath(Object target, String path);

    Object getContextPath(String contextType, String targetType);

    Class<?> resolveType(String typeName);

    Class<?> resolveType(Object value);

	Object createInstance(String typeName);

    void setValue(Object target, String path, Object value);

    Boolean objectEqual(Object left, Object right);

    Boolean objectEquivalent(Object left, Object right);
}