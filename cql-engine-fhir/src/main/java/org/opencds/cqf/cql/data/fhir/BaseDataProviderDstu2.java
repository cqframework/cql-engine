package org.opencds.cqf.cql.data.fhir;

import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Interval;

/**
 * Created by Christopher Schuler on 6/15/2017.
 */
public abstract class BaseDataProviderDstu2 extends BaseFhirDataProvider {

    @Override
    public Iterable<Object> retrieve(String context, Object contextValue, String dataType,
                                     String templateId, String codePath, Iterable<Code> codes,
                                     String valueSet, String datePath, String dateLowPath,
                                     String dateHighPath, Interval dateRange)
    {
        return null;
    }

    @Override
    protected String resolveClassName(String typeName) {
        return null;
    }

    @Override
    protected Object fromJavaPrimitive(Object value, Object target) {
        return null;
    }

    @Override
    protected Object toJavaPrimitive(Object result, Object source) {
        return null;
    }
}
