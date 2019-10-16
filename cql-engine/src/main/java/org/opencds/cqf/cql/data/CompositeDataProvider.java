package org.opencds.cqf.cql.data;

import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Interval;

public class CompositeDataProvider implements DataProvider {

    private TypeProvider typeProvider;
    private RetrieveProvider retrieveProvider;

    public CompositeDataProvider(TypeProvider typeProvider, RetrieveProvider retrieveProvider) {
        this.typeProvider = typeProvider;
        this.retrieveProvider = retrieveProvider;
    }

    @Override
    public String getPackageName() {
        return this.typeProvider.getPackageName();
    }

    @Override
    public void setPackageName(String packageName) {
        this.typeProvider.setPackageName(packageName);

    }

    @Override
    public Object resolvePath(Object target, String path) {
        return this.typeProvider.resolvePath(target, path);
    }

    @Override
    public Object resolveContextPath(String contextType, String targetType) {
        return this.typeProvider.resolveContextPath(contextType, targetType);
    }

    @Override
    public Class resolveType(String typeName) {
        return this.typeProvider.resolveType(typeName);
    }

    @Override
    public Class resolveType(Object value) {
        return this.typeProvider.resolveType(value);
    }

    @Override
    public Object createInstance(String typeName) {
        return this.typeProvider.createInstance(typeName);
    }

    @Override
    public void setValue(Object target, String path, Object value) {
        this.typeProvider.setValue(target, path, value);
    }

    @Override
    public Boolean objectEqual(Object left, Object right) {
        return this.typeProvider.objectEqual(left, right);
    }

    @Override
    public Boolean objectEquivalent(Object left, Object right) {
        return this.typeProvider.objectEquivalent(left, right);
    }

    @Override
    public Iterable<Object> retrieve(String context, Object contextValue, String contextPath, String dataType,
            String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath,
            String dateLowPath, String dateHighPath, Interval dateRange) {
        return this.retrieveProvider.retrieve(context, contextValue, contextPath, dataType, templateId, codePath, codes, valueSet, datePath, dateLowPath, dateHighPath, dateRange);
    }


}