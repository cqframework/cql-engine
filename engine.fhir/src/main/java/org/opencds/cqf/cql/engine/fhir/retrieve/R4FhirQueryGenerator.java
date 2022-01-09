package org.opencds.cqf.cql.engine.fhir.retrieve;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.r4.model.*;
import org.opencds.cqf.cql.engine.elm.execution.SubtractEvaluator;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.fhir.model.R4FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterMap;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;

import java.net.URLDecoder;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class R4FhirQueryGenerator extends BaseFhirQueryGenerator {
    public R4FhirQueryGenerator(SearchParameterResolver searchParameterResolver, TerminologyProvider terminologyProvider, R4FhirModelResolver modelResolver) {
        super(searchParameterResolver, terminologyProvider, modelResolver, FhirContext.forR4());
    }

    @Override
    public List<String> generateFhirQueries(ICompositeType dreq, Context engineContext, IBaseConformance capStatement) {
        if (!(dreq instanceof DataRequirement)) {
            throw new IllegalArgumentException("dataRequirement argument must be a DataRequirement");
        }
        if (capStatement != null && !(capStatement instanceof CapabilityStatement)) {
            throw new IllegalArgumentException("capabilityStatement argument must be a CapabilityStatement");
        }

        DataRequirement dataRequirement = (DataRequirement)dreq;
        CapabilityStatement capabilityStatement = (CapabilityStatement)capStatement;

        List<String> queries = new ArrayList<>();

        String codePath = null;
        List<Code> codes = null;
        String valueSet = null;

        if (dataRequirement.hasCodeFilter()) {
            for (DataRequirement.DataRequirementCodeFilterComponent codeFilterComponent : dataRequirement.getCodeFilter()) {
                if (!codeFilterComponent.hasPath()) continue;
                codePath = codeFilterComponent.getPath();

                if (codeFilterComponent.hasValueSetElement()) {
                    valueSet = codeFilterComponent.getValueSet();
                }

                if (codeFilterComponent.hasCode()) {
                    codes = new ArrayList<Code>();

                    List<Coding> codeFilterValueCodings = codeFilterComponent.getCode();
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
        if (dataRequirement.hasDateFilter()) {
            for (DataRequirement.DataRequirementDateFilterComponent dateFilterComponent : dataRequirement.getDateFilter()) {
                if (dateFilterComponent.hasPath() && dateFilterComponent.hasSearchParam()) {
                    throw new UnsupportedOperationException(String.format("Either a path or a searchParam must be provided, but not both"));
                }

                if (dateFilterComponent.hasPath()) {
                    datePath = dateFilterComponent.getPath();
                } else if (dateFilterComponent.hasSearchParam()) {
                    datePath = dateFilterComponent.getSearchParam();
                }

                // TODO: Deal with the case that the value is expressed as an expression extension
                if (dateFilterComponent.hasValue()) {
                    Type dateFilterValue = dateFilterComponent.getValue();
                    if (dateFilterValue instanceof DateTimeType && dateFilterValue.hasPrimitiveValue()) {
                        dateLowPath = "valueDateTime";
                        dateHighPath = "valueDateTime";
                        String offsetDateTimeString = ((DateTimeType)dateFilterValue).getValueAsString();
                        DateTime dateTime = new DateTime(OffsetDateTime.parse(offsetDateTimeString));

                        dateRange = new Interval(dateTime, true, dateTime, true);
                    } else if (dateFilterValue instanceof Duration && ((Duration)dateFilterValue).hasValue()) {
                        // If a Duration is specified, the filter will return only those data items that fall within Duration before now.
                        Duration dateFilterAsDuration = (Duration)dateFilterValue;

                        org.opencds.cqf.cql.engine.runtime.Quantity dateFilterDurationAsCQLQuantity =
                            new org.opencds.cqf.cql.engine.runtime.Quantity().withValue(dateFilterAsDuration.getValue()).withUnit(dateFilterAsDuration.getUnit());

                        DateTime evaluationDateTime = engineContext.getEvaluationDateTime();
                        DateTime diff = ((DateTime)SubtractEvaluator.subtract(evaluationDateTime, dateFilterDurationAsCQLQuantity));

                        dateRange = new Interval(diff, true, evaluationDateTime, true);
                    } else if (dateFilterValue instanceof Period && ((Period)dateFilterValue).hasStart() && ((Period)dateFilterValue).hasEnd()) {
                        dateLowPath = "valueDateTime";
                        dateHighPath = "valueDateTime";
                        dateRange = new Interval(((Period)dateFilterValue).getStart(), true, ((Period)dateFilterValue).getEnd(), true);
                    }
                }
            }
        }

        List<SearchParameterMap> maps = new ArrayList<SearchParameterMap>();

        Object contextPath = modelResolver.getContextPath(engineContext.getCurrentContext(), dataRequirement.getType());
        Object contextValue = engineContext.getCurrentContextValue();
        String templateId = dataRequirement.getProfile() != null && dataRequirement.getProfile().size() > 0
            ? dataRequirement.getProfile().get(0).getValue()
            : null;

        maps = setupQueries(engineContext.getCurrentContext(), (String)contextPath, contextValue, dataRequirement.getType(), templateId,
            codePath, codes, valueSet, datePath, dateLowPath, dateHighPath, dateRange);

        for (SearchParameterMap map : maps) {
            String query = null;
            try {
                query = URLDecoder.decode(map.toNormalizedQueryString(fhirContext), "UTF-8");
            } catch (Exception ex) {
                query = map.toNormalizedQueryString(fhirContext);
            }
            queries.add(dataRequirement.getType() + query);
        }

        return queries;
    }
}