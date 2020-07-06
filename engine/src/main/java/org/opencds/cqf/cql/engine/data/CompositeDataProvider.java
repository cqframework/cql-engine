package org.opencds.cqf.cql.engine.data;

import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.cql.engine.retrieve.RetrieveProvider;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Interval;

public class CompositeDataProvider implements DataProvider {

    protected ModelResolver modelResolver;
    protected RetrieveProvider retrieveProvider;

    public CompositeDataProvider(ModelResolver modelResolver, RetrieveProvider retrieveProvider) {
        this.modelResolver = modelResolver;
        this.retrieveProvider = retrieveProvider;
    }

    @Override
    public String getPackageName() {
        return this.modelResolver.getPackageName();
    }

    @Override
    public void setPackageName(String packageName) {
        this.modelResolver.setPackageName(packageName);

    }

    @Override
    public Object resolvePath(Object target, String path) {
        return this.modelResolver.resolvePath(target, path);
    }

    @Override
    public Object getContextPath(String contextType, String targetType) {
        return this.modelResolver.getContextPath(contextType, targetType);
    }

    @Override
    public Class<?> resolveType(String typeName) {
        return this.modelResolver.resolveType(typeName);
    }

    @Override
    public Class<?> resolveType(Object value) {
        return this.modelResolver.resolveType(value);
	}

    @Override
    public Object createInstance(String typeName) {
        return this.modelResolver.createInstance(typeName);
    }

    @Override
    public void setValue(Object target, String path, Object value) {
        this.modelResolver.setValue(target, path, value);
    }

    @Override
    public Boolean objectEqual(Object left, Object right) {
        return this.modelResolver.objectEqual(left, right);
    }

    @Override
    public Boolean objectEquivalent(Object left, Object right) {
        return this.modelResolver.objectEquivalent(left, right);
    }

    @Override
    public Iterable<Object> retrieve(String context, String contextPath, Object contextValue, String dataType,
            String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath,
            String dateLowPath, String dateHighPath, Interval dateRange) {
        return this.retrieveProvider.retrieve(context, contextPath, contextValue, dataType, templateId, codePath, codes, valueSet, datePath, dateLowPath, dateHighPath, dateRange);
    }
}