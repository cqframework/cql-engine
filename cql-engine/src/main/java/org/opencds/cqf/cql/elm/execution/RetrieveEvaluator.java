package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.data.DataProvider;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Concept;
import org.opencds.cqf.cql.runtime.Interval;
import org.cqframework.cql.elm.execution.ValueSetRef;
import org.cqframework.cql.elm.execution.ValueSetDef;


import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

public class RetrieveEvaluator extends org.cqframework.cql.elm.execution.Retrieve {

    protected Object internalEvaluate(Context context) {
        QName dataType = context.fixupQName(this.dataType);
        DataProvider dataProvider = context.resolveDataProvider(dataType);
        Iterable<Code> codes = null;
        String valueSet = null;
        if (this.getCodes() != null) {
            if (this.getCodes() instanceof ValueSetRef) {
                ValueSetRef valueSetRef = (ValueSetRef)this.getCodes();
                ValueSetDef valueSetDef = context.resolveValueSetRef(valueSetRef.getLibraryName(), valueSetRef.getName());
                // TODO: Handle value set versions.....
                valueSet = valueSetDef.getId();
            }
            else {
                Object codesResult = this.getCodes().evaluate(context);
                if (codesResult instanceof String) {
                    List<Code> codesList = new ArrayList<>();
                    codesList.add(new Code().withCode((String)codesResult));
                    codes = codesList;
                }
                else if (codesResult instanceof Code) {
                    List<Code> codesList = new ArrayList<>();
                    codesList.add((Code)codesResult);
                    codes = codesList;
                }
                else if (codesResult instanceof Concept) {
                    List<Code> codesList = new ArrayList<>();
                    for (Code conceptCode : ((Concept)codesResult).getCodes()) {
                        codesList.add(conceptCode);
                    }
                    codes = codesList;
                }
                else {
                    codes = (Iterable<Code>) codesResult;
                }
            }
        }
        Interval dateRange = null;
        if (this.getDateRange() != null) {
            dateRange = (Interval)this.getDateRange().evaluate(context);
        }

		Object result = dataProvider.retrieve(context.getCurrentContext(), (String)dataProvider.getContextPath(context.getCurrentContext(), dataType.getLocalPart()),
				context.getCurrentContextValue(), dataType.getLocalPart(), getTemplateId(),
                getCodeProperty(), codes, valueSet, getDateProperty(), getDateLowProperty(), getDateHighProperty(), dateRange);

        //append list results to evaluatedResources list
        if (result instanceof List) {
            for (Object element : (List)result) {
                context.getEvaluatedResources().add(element);
            }
        }
        else {
            context.getEvaluatedResources().add(result);
        }

        return result;
    }
}
