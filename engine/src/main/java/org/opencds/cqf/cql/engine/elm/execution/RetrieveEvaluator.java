package org.opencds.cqf.cql.engine.elm.execution;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cqframework.cql.elm.execution.*;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.retrieve.*;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Concept;
import org.opencds.cqf.cql.engine.runtime.Interval;

public class RetrieveEvaluator extends org.cqframework.cql.elm.execution.Retrieve {

    @Override
    public void prepare(Context context) {
        context.indexRetrieve(this);
    }

    protected static String getValueSet(Context context, Expression codes) {
        if (codes != null) {
            if (codes instanceof ValueSetRef) {
                ValueSetRef valueSetRef = (ValueSetRef) codes;
                ValueSetDef valueSetDef = context.resolveValueSetRef(valueSetRef.getLibraryName(), valueSetRef.getName());
                // TODO: Handle value set versions.....
                return valueSetDef.getId();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected static Iterable<Code> getCodes(Context context, Expression codes) {
        if (codes != null) {
            if (!(codes instanceof ValueSetRef)) {
                Object codesResult = codes.evaluate(context);
                if (codesResult instanceof String) {
                    List<Code> codesList = new ArrayList<>();
                    codesList.add(new Code().withCode((String)codesResult));
                    return codesList;
                }
                else if (codesResult instanceof Code) {
                    List<Code> codesList = new ArrayList<>();
                    codesList.add((Code)codesResult);
                    return codesList;
                }
                else if (codesResult instanceof Concept) {
                    List<Code> codesList = new ArrayList<>();
                    for (Code conceptCode : ((Concept)codesResult).getCodes()) {
                        codesList.add(conceptCode);
                    }
                    return codesList;
                }
                else {
                    return (Iterable<Code>) codesResult;
                }
            }
        }

        return null;
    }

    protected static Interval getDateRange(Context context, Expression dateRange) {
        if (dateRange != null) {
            return (Interval)dateRange.evaluate(context);
        }

        return null;
    }

    protected static Object getValue(Context context, Expression value) {
        if (value != null) {
            return value.evaluate(context);
        }

        return null;
    }

    protected Object internalEvaluate(Context context) {
        QName dataType = context.fixupQName(this.dataType);
        DataProvider dataProvider = context.resolveDataProvider(dataType);

        if (dataProvider instanceof IncludeAwareRetrieveProvider) {
            // If this is an including retrieve, pass the information for all included data to the retrieve using Include elements in the retrieve call
            // If this is an included retrieve, call retrieveIncluded
            Request request = getRequest(this, dataType, context, (String)dataProvider.getContextPath(context.getCurrentContext(), dataType.getLocalPart()));
            return ((IncludeAwareRetrieveProvider)dataProvider).retrieve(request);
        }
        else {
            Iterable<Code> codes = getCodes(context, this.getCodes());
            String valueSet = getValueSet(context, this.getCodes());
            Interval dateRange = getDateRange(context, this.getDateRange());
            Object result = dataProvider.retrieve(context.getCurrentContext(), (String)dataProvider.getContextPath(context.getCurrentContext(), dataType.getLocalPart()),
                context.getCurrentContextValue(), dataType.getLocalPart(), getTemplateId(),
                getCodeProperty(), codes, valueSet, getDateProperty(), getDateLowProperty(), getDateHighProperty(), dateRange);

            // TODO: This kills pipelined evaluation...
            //append list results to evaluatedResources list
            if (result instanceof List) {
                for (Object element : (List<?>)result) {
                    context.getEvaluatedResources().add(element);
                }
            }
            else {
                context.getEvaluatedResources().add(result);
            }

            return result;
        }
    }

    protected static TerminologyFilter toTerminologyFilter(Context context, CodeFilterElement cfe) {
        TerminologyFilter tf = new TerminologyFilter(cfe.getProperty(), getCodes(context, cfe.getValue()), getValueSet(context, cfe.getValue()));
        return tf;
    }

    protected static DateRangeFilter toDateRangeFilter(Context context, DateFilterElement dfe) {
        DateRangeFilter drf = new DateRangeFilter(dfe.getProperty(), dfe.getLowProperty(), dfe.getHighProperty(), getDateRange(context, dfe.getValue()));
        return drf;
    }

    protected static OtherFilter toOtherFilter(Context context, OtherFilterElement ofe) {
        OtherFilter of = new OtherFilter(ofe.getProperty(), ofe.getComparator(), getValue(context, ofe.getValue()));
        return of;
    }

    public static Request getRequest(Retrieve retrieve, QName fixedUpDataType, Context context, String contextPath) {
        Request request = new Request();
        // DataType
        request.setDataType(fixedUpDataType.getLocalPart());
        // TemplateId
        request.setTemplateId(retrieve.getTemplateId());
        // Context
        // TODO: Support context evaluation....
        request.setContext(new ContextFilter(context.getCurrentContext(), contextPath, context.getCurrentContextValue()));
        // Codes
        for (CodeFilterElement cfe : retrieve.getCodeFilter()) {
            request.addCodes(toTerminologyFilter(context, cfe));
        }
        // DateRanges
        for (DateFilterElement dfe : retrieve.getDateFilter()) {
            request.addDateRange(toDateRangeFilter(context, dfe));
        }
        // Values
        for (OtherFilterElement ofe : retrieve.getOtherFilter()) {
            request.addValue(toOtherFilter(context, ofe));
        }
        // Includes
        for (IncludeElement ie : retrieve.getInclude()) {
            if (ie.getIncludeFrom() != null) {
                Retrieve relatedRetrieve = context.getRetrieveByLocalId(ie.getIncludeFrom());
                if (relatedRetrieve != null) {
                    QName relatedDataType = context.fixupQName(relatedRetrieve.getDataType());
                    Request relatedRequest = getRequest(relatedRetrieve, relatedDataType, context, contextPath);
                    // TODO: getRelatedSearch vs getRelatedProperty
                    relatedRequest.setIncludePath(ie.getRelatedSearch());
                    relatedRequest.setIncludeReverse(ie.isIsReverse());
                    request.addInclude(relatedRequest);
                }
            }
        }
        return request;
    }
}
