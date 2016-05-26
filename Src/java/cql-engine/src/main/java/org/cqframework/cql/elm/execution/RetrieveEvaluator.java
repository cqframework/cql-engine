package org.cqframework.cql.elm.execution;

import org.cqframework.cql.data.DataProvider;
import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.*;

import java.util.ArrayList;

/**
 * Created by Bryn on 5/25/2016.
 */
public class RetrieveEvaluator extends Retrieve {

    public Object evaluate(Context context) {
        DataProvider dataProvider = context.resolveDataProvider(this.dataType);
        Iterable<org.cqframework.cql.runtime.Code> codes = null;
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
                if (codesResult instanceof org.cqframework.cql.runtime.Code) {
                    ArrayList<org.cqframework.cql.runtime.Code> codesList = new ArrayList<org.cqframework.cql.runtime.Code>();
                    codesList.add((org.cqframework.cql.runtime.Code)codesResult);
                    codes = codesList;
                }
                else if (codesResult instanceof org.cqframework.cql.runtime.Concept) {
                    ArrayList<org.cqframework.cql.runtime.Code> codesList = new ArrayList<org.cqframework.cql.runtime.Code>();
                    for (org.cqframework.cql.runtime.Code conceptCode : ((org.cqframework.cql.runtime.Concept)codesResult).getCodes()) {
                        codesList.add(conceptCode);
                    }
                    codes = codesList;
                }
                else {
                    codes = (Iterable<org.cqframework.cql.runtime.Code>) this.getCodes().evaluate(context);
                }
            }
        }
        org.cqframework.cql.runtime.Interval dateRange = null;
        if (this.getDateRange() != null) {
            dateRange = (org.cqframework.cql.runtime.Interval)this.getDateRange().evaluate(context);
        }

        return dataProvider.retrieve(context.getCurrentContext(), context.getCurrentContextValue(), getDataType().getLocalPart(), getTemplateId(),
                getCodeProperty(), codes, valueSet, getDateProperty(), getDateLowProperty(), getDateHighProperty(), dateRange);
    }
}
