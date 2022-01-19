package org.opencds.cqf.cql.engine.fhir.retrieve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterMap;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;

public abstract class BaseFhirQueryGenerator {
    protected static final int DEFAULT_MAX_CODES_PER_QUERY = 64;
    protected static final boolean DEFAULT_SHOULD_EXPAND_VALUESETS = false;
//    private final int DEFAULT_MAX_URI_LENGTH = 8000;

    protected FhirContext fhirContext;

    protected TerminologyProvider terminologyProvider;
    protected SearchParameterResolver searchParameterResolver;
    protected ModelResolver modelResolver;
//    protected int maxUriLength;
//    public int getMaxUriLength() { return this.maxUriLength; }
//    public void setMaxUriLength(int maxUriLength) {
//        throw new NotImplementedException("MaxUriLength is not yet leveraged in the Dstu3FhirQueryGenerator");
////        if (maxUriLength <= 0) {
////            throw new IllegalArgumentException("maxUriLength must be > 0");
////        }
////
////        this.maxUriLength = maxUriLength;
//    }

    private Integer pageSize;
    public Integer getPageSize() {
        return this.pageSize;
    }
    public void setPageSize(Integer value) {
        if( value == null || value < 1 ) {
            throw new IllegalArgumentException("value must be a non-null integer > 0");
        }
        this.pageSize = value;
    }

    private int maxCodesPerQuery;
    public int getMaxCodesPerQuery() {
        return this.maxCodesPerQuery;
    }
    public void setMaxCodesPerQuery(int value) {
        if (value < 1) {
            throw new IllegalArgumentException("value must be > 0");
        }
        this.maxCodesPerQuery = value;
    }

    // TODO: Think about how to best handle the decision to expand value sets... Should it be part of the
    // terminology provider if it detects support for "code:in"? How does that feed back to the retriever?
    private boolean expandValueSets;
    public boolean isExpandValueSets() {
        return this.expandValueSets;
    }
    public void setExpandValueSets(boolean expandValueSets) {
        this.expandValueSets = expandValueSets;
    }

    protected BaseFhirQueryGenerator(SearchParameterResolver searchParameterResolver, TerminologyProvider terminologyProvider,
                                  ModelResolver modelResolver, FhirContext fhirContext) {
        this.searchParameterResolver = searchParameterResolver;
        this.terminologyProvider = terminologyProvider;
        this.modelResolver = modelResolver;
//        this.maxUriLength = DEFAULT_MAX_URI_LENGTH;
        this.maxCodesPerQuery = DEFAULT_MAX_CODES_PER_QUERY;
        this.expandValueSets = DEFAULT_SHOULD_EXPAND_VALUESETS;

        this.fhirContext = fhirContext;
    }

    public abstract List<String> generateFhirQueries(ICompositeType dataRequirement, Context engineContext, IBaseConformance capabilityStatement);

    protected Pair<String, IQueryParameterType> getTemplateParam(String dataType, String templateId) {
        if (templateId == null || templateId.equals("")) {
            return null;
        }

        // Do something?
        return null;
    }

    protected Pair<String, DateRangeParam> getDateRangeParam(String dataType, String datePath, String dateLowPath,
                                                             String dateHighPath, Interval dateRange) {
        if (dateRange == null) {
            return null;
        }

        DateParam low = null;
        DateParam high = null;
        if (dateRange.getLow() != null) {
            low = new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS,
                Date.from(((DateTime) dateRange.getLow()).getDateTime().toInstant()));
        }

        if (dateRange.getHigh() != null) {
            high = new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS,
                Date.from(((DateTime) dateRange.getHigh()).getDateTime().toInstant()));
        }

        DateRangeParam rangeParam;
        if (low == null && high != null) {
            rangeParam = new DateRangeParam(high);
        } else if (high == null && low != null) {
            rangeParam = new DateRangeParam(low);
        } else {
            rangeParam = new DateRangeParam(low, high);
        }

        RuntimeSearchParam dateParam = this.searchParameterResolver.getSearchParameterDefinition(dataType, datePath, RestSearchParameterTypeEnum.DATE);

        if (dateParam == null) {
            throw new UnsupportedOperationException(String.format("Could not resolve a search parameter with date type for %s.%s ", dataType, datePath));
        }

        return Pair.of(dateParam.getName(), rangeParam);
    }

    protected Pair<String, IQueryParameterType> getContextParam(String dataType, String context, String contextPath,
                                                                Object contextValue) {
        if (context != null && context.equals("Patient") && contextValue != null && contextPath != null) {
            return this.searchParameterResolver.createSearchParameter(context, dataType, contextPath, (String)contextValue);
        }

        return null;
    }

    protected Pair<String, List<TokenOrListParam>> getCodeParams(String dataType, String codePath, Iterable<Code> codes,
                                                                 String valueSet) {
        if (valueSet != null && valueSet.startsWith("urn:oid:")) {
            valueSet = valueSet.replace("urn:oid:", "");
        }

        if (codePath == null && (codes != null || valueSet != null)) {
            throw new IllegalArgumentException("A code path must be provided when filtering on codes or a valueset.");
        }

        if (codePath == null || codePath.isEmpty()) {
            return null;
        }

        // TODO: This assumes the code path will always be a token param.
        List<TokenOrListParam> codeParamLists = this.getCodeParams(codes, valueSet);
        if (codeParamLists == null || codeParamLists.isEmpty()) {
            return null;
        }

        RuntimeSearchParam codeParam = this.searchParameterResolver.getSearchParameterDefinition(dataType, codePath, RestSearchParameterTypeEnum.TOKEN);

        if (codeParam == null) {
            return null;
        }

        return Pair.of(codeParam.getName(), codeParamLists);
    }

    // The code params will be either the literal set of codes in the event the data server doesn't have the referenced
    // ValueSet (or doesn't support pulling and caching a ValueSet). If the target server DOES support that then it's
    // "dataType.codePath in ValueSet"
    protected List<TokenOrListParam> getCodeParams(Iterable<Code> codes, String valueSet) {
        if (valueSet != null) {
            if (this.isExpandValueSets()) {
                if (this.terminologyProvider == null) {
                    throw new IllegalArgumentException(
                        "Expand value sets cannot be used without a terminology provider and no terminology provider is set.");
                }
                ValueSetInfo valueSetInfo = new ValueSetInfo().withId(valueSet);
                codes = this.terminologyProvider.expand(valueSetInfo);
            } else {
                return Collections.singletonList(new TokenOrListParam()
                    .addOr(new TokenParam(valueSet).setModifier(TokenParamModifier.IN)));
            }
        }

        if (codes == null) {
            return Collections.emptyList();
        }

        List<TokenOrListParam> codeParamsList = new ArrayList<>();

        TokenOrListParam codeParams = null;

        int codeCount = 0;
        for (Object code : codes) {
            if (codeCount % this.getMaxCodesPerQuery() == 0) {
                if (codeParams != null) {
                    codeParamsList.add(codeParams);
                }

                codeParams = new TokenOrListParam();
            }

            codeCount++;
            if (code instanceof Code) {
                Code c = (Code)code;
                codeParams.addOr(new TokenParam(c.getSystem(), c.getCode()));
            }
            else if (code instanceof String) {
                String s = (String)code;
                codeParams.addOr(new TokenParam(s));
            }

        }

        if (codeParams != null) {
            codeParamsList.add(codeParams);
        }

        return codeParamsList;
    }

    protected List<SearchParameterMap> setupQueries(String context, String contextPath, Object contextValue,
                                                    String dataType, String templateId, String codePath, Iterable<Code> codes,
                                                    String valueSet, String datePath, String dateLowPath, String dateHighPath,
                                                    Interval dateRange) {

        Pair<String, IQueryParameterType> templateParam = this.getTemplateParam(dataType, templateId);

        Pair<String, IQueryParameterType> contextParam = this.getContextParam(dataType, context, contextPath, contextValue);

        Pair<String, DateRangeParam> dateRangeParam = this.getDateRangeParam(dataType, datePath, dateLowPath,
            dateHighPath, dateRange);
        Pair<String, List<TokenOrListParam>> codeParams = this.getCodeParams(dataType, codePath, codes, valueSet);

        // In the case we filtered to a valueSet without codes, there are no possible results.
        if (valueSet != null && (codeParams == null || codeParams.getValue().isEmpty())) {
            return Collections.emptyList();
        }

        return this.innerSetupQueries(templateParam, contextParam, dateRangeParam, codeParams);
    }

    protected List<SearchParameterMap> innerSetupQueries(Pair<String, IQueryParameterType> templateParam,
                                                         Pair<String, IQueryParameterType> contextParam, Pair<String, DateRangeParam> dateRangeParam,
                                                         Pair<String, List<TokenOrListParam>> codeParams) {

        if (codeParams == null || codeParams.getValue() == null || codeParams.getValue().isEmpty()) {
            return Collections.singletonList(this.getBaseMap(templateParam, contextParam, dateRangeParam));
        }

        List<SearchParameterMap> maps = new ArrayList<>();
        for (TokenOrListParam tolp : codeParams.getValue()) {
            SearchParameterMap base = this.getBaseMap(templateParam, contextParam, dateRangeParam);
            base.add(codeParams.getKey(), tolp);
            maps.add(base);
        }

        return maps;
    }

    protected SearchParameterMap getBaseMap(Pair<String, IQueryParameterType> templateParam, Pair<String, IQueryParameterType> contextParam,
                                            Pair<String, DateRangeParam> dateRangeParam) {
        SearchParameterMap baseMap = new SearchParameterMap();
        baseMap.setLastUpdated(new DateRangeParam());

        if (this.getPageSize() != null) {
            baseMap.setCount(pageSize);
        }

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