package org.opencds.cqf.cql.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.opencds.cqf.cql.data.RetrieveProvider;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.terminology.TerminologyProvider;
import org.opencds.cqf.cql.terminology.ValueSetInfo;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;

public abstract class FhirRetrieveProvider implements RetrieveProvider {

    private static final int MAX_CODES_PER_QUERY = 1024;
    protected TerminologyProvider terminologyProvider;
    protected boolean expandValueSets;

    public TerminologyProvider getTerminologyProvider() {
        return terminologyProvider;
    }
    public FhirRetrieveProvider setTerminologyProvider(TerminologyProvider terminologyProvider) {
        this.terminologyProvider = terminologyProvider;
        return this;
    }

    public boolean isExpandValueSets() {
        return expandValueSets;
    }
    public FhirRetrieveProvider setExpandValueSets(boolean expandValueSets) {
        this.expandValueSets = expandValueSets;
        return this;
    }

	protected abstract boolean isPatientCompartment(String dataType);
	protected abstract Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries);

	@Override
	public Iterable<Object> retrieve(String context, String contextPath, Object contextValue, String dataType,
			String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath,
			String dateLowPath, String dateHighPath, Interval dateRange) {
		
		List<SearchParameterMap> queries = this.setupQueries(context, contextPath, contextValue, dataType, 
			templateId, codePath, codes, valueSet, datePath, dateLowPath, dateHighPath, dateRange);

		return this.executeQueries(dataType, queries);
	}

    protected Pair<String, IQueryParameterType> getTemplateParam(String dataType, String templateId) {
        if (templateId == null || templateId.equals("")) {
            return null;
        }

        // Do something?
        return null;
    }

    protected Pair<String, DateRangeParam> getDateRangeParam(String dataType, String datePath, String dateLowPath, String dateHighPath, Interval dateRange) 
    {
        if (dateRange == null) {
            return null;
        }

        DateParam low = null;
        DateParam high = null;
        if (dateRange.getLow() != null) {
            low = new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, Date.from(((DateTime) dateRange.getLow()).getDateTime().toInstant()));
        }

        if (dateRange.getHigh() != null) {
            high = new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, Date.from(((DateTime) dateRange.getHigh()).getDateTime().toInstant()));
        }

        DateRangeParam rangeParam;
        if (low == null && high != null) {
            rangeParam = new DateRangeParam(high);
        }
        else if (high == null && low != null) {
            rangeParam = new DateRangeParam(low);
        }
        else {
            rangeParam = new DateRangeParam(low, high);
        }

        return Pair.of(datePath, rangeParam);
    }

    protected Pair<String, ReferenceParam> getContextParam(String dataType, String context, String contextPath, Object contextValue) {
        if (context != null && context.equals("Patient") && contextValue != null) {
            if (isPatientCompartment(dataType)) {
                ReferenceParam patientParam = new ReferenceParam(contextValue.toString());
                return Pair.of(contextPath, patientParam);
            }
        }

        return null;
    }

    protected Pair<String, List<TokenOrListParam>> getCodeParams(String dataType, String codePath, Iterable<Code> codes, String valueSet) {
        if (valueSet != null && valueSet.startsWith("urn:oid:")) {
            valueSet = valueSet.replace("urn:oid:", "");
        }

        if (codePath == null && (codes != null || valueSet != null)) {
            throw new IllegalArgumentException("A code path must be provided when filtering on codes or a valueset.");
        }

        if (codePath == null || codePath.isEmpty()) {
            return null;
        }

        List<TokenOrListParam> codeParamLists = this.getCodeParams(codePath, codes, valueSet);
        if (codeParamLists == null || codeParamLists.isEmpty()) {
            return null;
        }

        return Pair.of(codePath, codeParamLists);
    }

    protected List<TokenOrListParam> getCodeParams(String codePath, Iterable<Code> codes, String valueSet) {
        if (valueSet != null) {
            if (isExpandValueSets()) {
                if (getTerminologyProvider() == null) {
                    throw new IllegalArgumentException("Expand value sets cannot be used without a terminology provider and no terminology provider is set.");
                }
                ValueSetInfo valueSetInfo = new ValueSetInfo().withId(valueSet);
                codes = getTerminologyProvider().expand(valueSetInfo);
            }
            else {
                return Collections.singletonList(new TokenOrListParam().addOr(new TokenParam(null, valueSet).setModifier(TokenParamModifier.IN)));
            }
        }

        if (codes == null) {
            return Collections.emptyList();
        }

        List<TokenOrListParam> codeParamsList = new ArrayList<>();

        TokenOrListParam codeParams = null;
        int codeCount = 0;
        for (Code code : codes) {
            if (codeCount % MAX_CODES_PER_QUERY == 0) {
                if (codeParams != null) {
                    codeParamsList.add(codeParams);
                }

                codeParams = new TokenOrListParam();
            }

            codeCount++;
            codeParams.addOr(new TokenParam(code.getSystem(), code.getCode()));
        }

        if (codeParams != null) {
            codeParamsList.add(codeParams);
        }

        return codeParamsList;
    }

    protected List<SearchParameterMap> setupQueries(String context, String contextPath, Object contextValue, String dataType, String templateId,
			String codePath, Iterable<Code> codes, String valueSet, String datePath,
			String dateLowPath, String dateHighPath, Interval dateRange) {

        Pair<String, IQueryParameterType> templateParam = this.getTemplateParam(dataType, templateId);
        Pair<String, ReferenceParam> contextParam = this.getContextParam(dataType, context, contextPath, contextValue);
        Pair<String, DateRangeParam> dateRangeParam = this.getDateRangeParam(dataType, datePath, dateLowPath, dateHighPath, dateRange);
        Pair<String, List<TokenOrListParam>> codeParams = this.getCodeParams(dataType, codePath, codes, valueSet);

        // In the case we filtered to a valueSet without codes, there are no possible results.
        if (valueSet != null && (codeParams == null || codeParams.getValue().isEmpty())) {
            return Collections.emptyList();
        }

        return this.innerSetupQueries(templateParam, contextParam, dateRangeParam, codeParams);
    }

    protected List<SearchParameterMap> innerSetupQueries(
        Pair<String, IQueryParameterType> templateParam,
        Pair<String, ReferenceParam> contextParam,
        Pair<String, DateRangeParam> dateRangeParam,
        Pair<String, List<TokenOrListParam>> codeParams) {

        if (codeParams == null || codeParams.getValue() == null || codeParams.getValue().isEmpty()) {
            return Collections.singletonList(this.getBaseMap(templateParam, contextParam, dateRangeParam));
        }

        List<SearchParameterMap> maps = new ArrayList<>();
        for (TokenOrListParam tolp: codeParams.getValue())
        {
            SearchParameterMap base = this.getBaseMap(templateParam, contextParam, dateRangeParam);
            base.add(codeParams.getKey(), tolp);
            maps.add(base);
        }

        return maps;
    }

    protected SearchParameterMap getBaseMap(        
        Pair<String, IQueryParameterType> templateParam,
        Pair<String, ReferenceParam> contextParam,
        Pair<String, DateRangeParam> dateRangeParam
    ) {

        SearchParameterMap baseMap = new SearchParameterMap();
        baseMap.setLastUpdated(new DateRangeParam());

        if (templateParam != null) {
            baseMap.add(templateParam.getKey(), templateParam.getValue());
        }

        if (dateRangeParam != null) {
            baseMap.add(dateRangeParam.getKey(), dateRangeParam.getValue());
        }

        if (contextParam != null) {
            baseMap.add(contextParam.getKey(), contextParam.getValue());
        }

        return baseMap;
    }
}