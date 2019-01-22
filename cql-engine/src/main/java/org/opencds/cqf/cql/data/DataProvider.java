package org.opencds.cqf.cql.data;

import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Interval;

/**
 * Created by Bryn on 4/15/2016.
 */
public interface DataProvider {
    Iterable<Object> retrieve(String context, Object contextValue, String dataType, String templateId, String codePath,
              Iterable<Code> codes, String valueSet, String datePath, String dateLowPath, String dateHighPath,
              Interval dateRange);

    String getPackageName();

    void setPackageName(String packageName);

    Object resolvePath(Object target, String path);

    Class resolveType(String typeName);

    Class resolveType(Object value);

    Object createInstance(String typeName);

    void setValue(Object target, String path, Object value);
}
