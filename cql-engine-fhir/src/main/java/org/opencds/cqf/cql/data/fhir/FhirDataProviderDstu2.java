package org.opencds.cqf.cql.data.fhir;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.opencds.cqf.cql.exception.DataProviderException;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.terminology.ValueSetInfo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.gclient.IQuery;

public class FhirDataProviderDstu2 extends BaseFhirDataProvider {

    public FhirDataProviderDstu2() {
        setFhirContext(FhirContext.forDstu2());
    }

    @Override
    public Iterable<Object> retrieve(String context, Object contextValue, String contextPath, String dataType,
                                     String templateId, String codePath, Iterable<Code> codes,
                                     String valueSet, String datePath, String dateLowPath,
                                     String dateHighPath, Interval dateRange)
    {
        IQuery<IBaseBundle> search;

        // TODO: It's unclear from the FHIR documentation whether we need to use a URLEncoder.encode call on the embedded system and valueset uris here...
        StringBuilder params = new StringBuilder();

        if (templateId != null && !templateId.equals("")) {
            params.append(String.format("_profile=%s", templateId));
        }

        if (valueSet != null && valueSet.startsWith("urn:oid:")) {
            valueSet = valueSet.replace("urn:oid:", "");
        }

        if (codePath == null && (codes != null || valueSet != null)) {
            throw new DataProviderException("A code path must be provided when filtering on codes or a valueset.");
        }

        if (context != null && context.equals("Patient") && contextValue != null) {
            if (params.length() > 0) {
                params.append("&");
            }

            params.append(String.format("%s=%s", contextPath, contextValue));
        }

        if (codePath != null && !codePath.equals("")) {
            if (params.length() > 0) {
                params.append("&");
            }
            if (valueSet != null && !valueSet.equals("")) {
                if (getTerminologyProvider() != null && isExpandValueSets()) {
                    ValueSetInfo valueSetInfo = new ValueSetInfo().withId(valueSet);
                    codes = getTerminologyProvider().expand(valueSetInfo);
                }
                else {
                    params.append(String.format("%s:in=%s", convertPathToSearchParam(dataType, codePath), valueSet));
                }
            }

            if (codes != null) {
                StringBuilder codeList = new StringBuilder();
                for (Code code : codes) {
                    if (codeList.length() > 0) {
                        codeList.append(",");
                    }

                    if (code.getSystem() != null) {
                        codeList.append(code.getSystem());
                        codeList.append("|");
                    }

                    codeList.append(code.getCode());
                }
                params.append(String.format("%s=%s", convertPathToSearchParam(dataType, codePath), codeList.toString()));
            }
        }

        if (dateRange != null) {
            if (dateRange.getLow() != null) {
                String lowDatePath = convertPathToSearchParam(dataType, dateLowPath != null ? dateLowPath : datePath);
                if (lowDatePath.equals("")) {
                    throw new DataProviderException("A date path or low date path must be provided when filtering on a date range.");
                }

                params.append(String.format("&%s=%s%s",
                        lowDatePath,
                        dateRange.getLowClosed() ? "ge" : "gt",
                        dateRange.getLow().toString()));
            }

            if (dateRange.getHigh() != null) {
                String highDatePath = convertPathToSearchParam(dataType, dateHighPath != null ? dateHighPath : datePath);
                if (highDatePath.equals("")) {
                    throw new DataProviderException("A date path or high date path must be provided when filtering on a date range.");
                }

                params.append(String.format("&%s=%s%s",
                        highDatePath,
                        dateRange.getHighClosed() ? "le" : "lt",
                        dateRange.getHigh().toString()));
            }
        }

        // TODO: Use compartment search for patient context?
        if (params.length() > 0) {
            search = getFhirClient().search().byUrl(String.format("%s?%s", dataType, params.toString()));
        }
        else {
            search = getFhirClient().search().byUrl(String.format("%s", dataType));
        }

        ca.uhn.fhir.model.dstu2.resource.Bundle results = cleanEntry(search.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class).execute(), dataType);

        return new FhirBundleCursorDstu2(getFhirClient(), results);
    }

    protected ca.uhn.fhir.model.dstu2.resource.Bundle cleanEntry(ca.uhn.fhir.model.dstu2.resource.Bundle bundle, String dataType) {
        List<ca.uhn.fhir.model.dstu2.resource.Bundle.Entry> entry = new ArrayList<>();
        for (ca.uhn.fhir.model.dstu2.resource.Bundle.Entry comp : bundle.getEntry()){
            if (comp.getResource().getResourceName().equals(dataType)) {
                entry.add(comp);
            }
        }
        bundle.setEntry(entry);
        return bundle;
    }
}
