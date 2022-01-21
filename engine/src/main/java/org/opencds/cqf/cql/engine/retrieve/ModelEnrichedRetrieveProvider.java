package org.opencds.cqf.cql.engine.retrieve;

import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Interval;

public interface ModelEnrichedRetrieveProvider extends RetrieveProvider {
    Iterable<Object> retrieve(ModelResolver modelResolver, String context, String contextPath, Object contextValue, String dataType, String templateId, String codePath,
                              Iterable<Code> codes, String valueSet, String datePath, String dateLowPath, String dateHighPath,
                              Interval dateRange);

    default Iterable<Object> retrieve(String context, String contextPath, Object contextValue, String dataType, String templateId, String codePath,
                                      Iterable<Code> codes, String valueSet, String datePath, String dateLowPath, String dateHighPath,
                                      Interval dateRange) {
        return retrieve(null, context, contextPath, contextValue, dataType, templateId, codePath,
            codes, valueSet, datePath, dateLowPath, dateHighPath,
            dateRange);
    }
}
