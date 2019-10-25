package org.opencds.cqf.cql.data;

import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.terminology.TerminologyProvider;

public interface RetrieveProvider {
    Iterable<Object> retrieve(String context, String contextPath, Object contextValue, String dataType, String templateId, String codePath,
              Iterable<Code> codes, String valueSet, String datePath, String dateLowPath, String dateHighPath,
			  Interval dateRange);
	
	// boolean isPatientCompartment(String dataType);
}
