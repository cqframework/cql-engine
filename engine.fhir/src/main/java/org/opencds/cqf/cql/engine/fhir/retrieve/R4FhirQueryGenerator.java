package org.opencds.cqf.cql.engine.fhir.retrieve;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.*;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.r4.model.*;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterMap;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.retrieve.TerminologyAwareRetrieveProvider;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class R4FhirQueryGenerator {
    private static final int DEFAULT_MAX_CODES_PER_QUERY = 64;
//    private final int DEFAULT_MAX_URI_LENGTH = 8000;

    private Integer pageSize;
//    private int maxUriLength;
    private int maxCodesPerQuery;

    private FhirContext context;
    private TerminologyProvider terminologyProvider;
    private SearchParameterResolver searchParameterResolver;

    // TODO: Think about how to best handle the decision to expand value sets... Should it be part of the
    // terminology provider if it detects support for "code:in"? How does that feed back to the retriever?
    protected boolean expandValueSets;
    public boolean isExpandValueSets() {
        return expandValueSets;
    }
    public void setExpandValueSets(boolean expandValueSets) {
        this.expandValueSets = expandValueSets;
    }

    public R4FhirQueryGenerator(SearchParameterResolver searchParameterResolver, TerminologyProvider terminologyProvider) {
        this.searchParameterResolver = searchParameterResolver;
        this.terminologyProvider = terminologyProvider;
        this.maxCodesPerQuery = DEFAULT_MAX_CODES_PER_QUERY;
//        this.maxUriLength = DEFAULT_MAX_URI_LENGTH;

        this.context = FhirContext.forR4();
    }

    public void setPageSize(Integer value) {
        if( value == null || value < 1 ) {
            throw new IllegalArgumentException("value must be a non-null integer > 0");
        }
        this.pageSize = value;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setMaxCodesPerQuery(int value) {
        if (value < 1) {
            throw new IllegalArgumentException("value must be > 0");
        }
        this.maxCodesPerQuery = value;
    }

    public int getMaxCodesPerQuery() {
        return this.maxCodesPerQuery;
    }

    public List<String> generateFhirQueries(org.hl7.fhir.r4.model.DataRequirement dataRequirement, org.hl7.fhir.r4.model.CapabilityStatement capabilityStatement) {
        List<String> queries = new ArrayList<>();

        String codePath = null;
        List<Code> codes = null;
        String valueSet = null;

        if (dataRequirement.hasCodeFilter()) {
            for (org.hl7.fhir.r4.model.DataRequirement.DataRequirementCodeFilterComponent codeFilterComponent : dataRequirement.getCodeFilter()) {
                if (!codeFilterComponent.hasPath()) continue;
                codePath = codeFilterComponent.getPath();

                if (codeFilterComponent.hasValueSetElement()) {
                    valueSet = codeFilterComponent.getValueSet();
                }

                if (codeFilterComponent.hasCode()) {
                    codes = new ArrayList<Code>();

                    List<org.hl7.fhir.r4.model.Coding> codeFilterValueCodings = codeFilterComponent.getCode();
                    for (Coding coding : codeFilterValueCodings) {
                        if (coding.hasCode()) {
                            Code code = new Code();
                            code.setSystem(coding.getSystem());
                            code.setCode(coding.getCode());
                            codes.add(code);
                        }
                    }
                }
            }
        }

        String datePath = null;
        String dateLowPath = null;
        String dateHighPath = null;
        Interval dateRange = null;
//        if (dataRequirement.hasDateFilter()) {
//            for (org.hl7.fhir.r4.model.DataRequirement.DataRequirementDateFilterComponent dateFilterComponent : dataRequirement.getDateFilter()) {
//                if (dateFilterComponent.hasPath() && dateFilterComponent.hasSearchParam()) {
//                    throw new UnsupportedOperationException(String.format("Either a path or a searchParam must be provided, but not both"));
//                }
//
//                if (dateFilterComponent.hasPath()) {
//                    datePath = dateFilterComponent.getPath();
//                } else if (dateFilterComponent.hasSearchParam()) {
//                    datePath = dateFilterComponent.getSearchParam();
//                }
//
//                Type dateFilterValue = dateFilterComponent.getValue();
//                if (dateFilterValue instanceof DateTimeType) {
//
//                } else if (dateFilterValue instanceof Duration) {
//
//                } else if (dateFilterValue instanceof Period) {
//
//                }
//            }
//        }

        List<SearchParameterMap> maps = new ArrayList<SearchParameterMap>();
        maps = setupQueries(null, null, null, dataRequirement.getType(), null,
            codePath, codes, valueSet, datePath, null, null, null);

        for (SearchParameterMap map : maps) {
            String query = null;
            try {
                query = URLDecoder.decode(map.toNormalizedQueryString(context), "UTF-8");
            } catch (Exception ex) {
                query = map.toNormalizedQueryString(context);
            }
            queries.add(dataRequirement.getType() + query);
        }

        return queries;
    }

    public boolean isPatientCompartmentResource(String dataType) {
        RuntimeResourceDefinition resourceDef = context.getResourceDefinition(dataType);
        return searchParameterResolver.getPatientSearchParams(resourceDef).size() > 0;
    }

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
        } else if (isPatientCompartmentResource(dataType)) {
            return this.searchParameterResolver.getPreferredPatientSearchParam(dataType, contextPath, (String)contextValue);
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
            if (isExpandValueSets()) {
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
            if (codeCount % this.maxCodesPerQuery == 0) {
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
                                                    String valueSet, String datePath,
                                                    String dateLowPath, String dateHighPath, Interval dateRange) {

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

    protected SearchParameterMap getBaseMap(Pair<String, IQueryParameterType> templateParam,
                                            Pair<String, IQueryParameterType> contextParam, Pair<String, DateRangeParam> dateRangeParam) {

        SearchParameterMap baseMap = new SearchParameterMap();
        baseMap.setLastUpdated(new DateRangeParam());

        if (this.pageSize != null) {
            baseMap.setCount(this.pageSize);
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