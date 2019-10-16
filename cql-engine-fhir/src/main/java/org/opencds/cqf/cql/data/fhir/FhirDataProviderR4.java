package org.opencds.cqf.cql.data.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.gclient.*;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.terminology.ValueSetInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FhirDataProviderR4 extends BaseFhirDataProvider {

    public FhirDataProviderR4() {
        setFhirContext(FhirContext.forR4());
    }

    protected String URLEncode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Iterable<Object> retrieve(String context, Object contextValue, String contextPath, String dataType, String templateId,
                                     String codePath, Iterable<Code> codes, String valueSet, String datePath, String dateLowPath,
                                     String dateHighPath, Interval dateRange) {

        // Apply filtering based on
        //  profile (templateId)
        //  codes
        //  dateRange
        IQuery<IBaseBundle> search = searchUsingPOST ? fhirClient.search().forResource(dataType) : null; //fhirClient.search().forResource(dataType);
        boolean doAnd = false;

        // TODO: Would like to be able to use the criteria builders, but it looks like they don't have one for :in with a valueset?
        // So..... I'll just construct a search URL
        //        if (templateId != null && !templateId.equals("")) {
        //            search = search.withProfile(templateId);
        //        }
        //
        //        if (codePath != null && !codePath.equals("")) {
        //            search.where(Patient.ACTIVE.)
        //        }

        // TODO: It's unclear from the FHIR documentation whether we need to use a URLEncoder.encode call on the embedded system and valueset uris here...
        StringBuilder params = new StringBuilder();

        if (templateId != null && !templateId.equals("")) {
            if (searchUsingPOST && search != null) {
                search = search.withProfile(templateId);
            }
            else {
                params.append(String.format("_profile=%s", templateId));
            }
        }

        if (valueSet != null && valueSet.startsWith("urn:oid:")) {
            valueSet = valueSet.replace("urn:oid:", "");
        }

        if (codePath == null && (codes != null || valueSet != null)) {
            throw new IllegalArgumentException("A code path must be provided when filtering on codes or a valueset.");
        }

        if (context != null && context.equals("Patient") && contextValue != null) {
            if (searchUsingPOST && search != null) {
                search = search.where(new TokenClientParam(contextPath).exactly().identifier(contextValue.toString()));
                doAnd = true;
            }
            else {
                if (params.length() > 0) {
                    params.append("&");
                }

                params.append(String.format("%s=%s", contextPath, URLEncode((String) contextValue)));
            }
        }

        if (codePath != null && !codePath.equals("")) {
            if (params.length() > 0 && !searchUsingPOST) {
                params.append("&");
            }
            if (valueSet != null && !valueSet.equals("")) {
                if (terminologyProvider != null && expandValueSets) {
                    ValueSetInfo valueSetInfo = new ValueSetInfo().withId(valueSet);
                    codes = terminologyProvider.expand(valueSetInfo);
                }
                else {
                    if (searchUsingPOST) {
                        throw new IllegalArgumentException("A terminology provider must be provided and the expandValueSets flag must be set to true when searching using POST method");
                    }
                    else {
                        params.append(String.format("%s:in=%s", convertPathToSearchParam(dataType, codePath), URLEncode(valueSet)));
                    }
                }
            }

            if (codes != null) {
                StringBuilder codeList = new StringBuilder();
                List<String> codeValues = new ArrayList<>();
                for (Code code : codes) {
                    if (searchUsingPOST && search != null) {
                        codeValues.add(code.getCode());
                    }
                    else {
                        if (codeList.length() > 0) {
                            codeList.append(",");
                        }

                        if (code.getSystem() != null) {
                            codeList.append(URLEncode(code.getSystem()));
                            codeList.append("|");
                        }

                        codeList.append(URLEncode(code.getCode()));
                    }
                }
                if (searchUsingPOST && search != null) {
                    search = doAnd
                            ? search.and(new TokenClientParam(convertPathToSearchParam(dataType, codePath))
                            .exactly().codes(codeValues.toArray(new String[codeValues.size()])))
                            : search.where(new TokenClientParam(convertPathToSearchParam(dataType, codePath))
                            .exactly().codes(codeValues.toArray(new String[codeValues.size()])));
                }
                else {
                    params.append(String.format("%s=%s", convertPathToSearchParam(dataType, codePath), codeList.toString()));
                }
            }
        }

        if (dateRange != null) {
            if (dateRange.getLow() != null) {
                String lowDatePath = convertPathToSearchParam(dataType, dateLowPath != null ? dateLowPath : datePath);
                if (lowDatePath == null || lowDatePath.equals("")) {
                    throw new IllegalArgumentException("A date path or low date path must be provided when filtering on a date range.");
                }

                if (searchUsingPOST && search != null) {
                    DateClientParam.IDateCriterion dateCriterion = dateRange.getLowClosed()
                            ? new DateClientParam(lowDatePath).afterOrEquals().day(dateRange.getLow().toString())
                            : new DateClientParam(lowDatePath).after().day(dateRange.getLow().toString());
                    search = doAnd ? search.and(dateCriterion) : search.where(dateCriterion);
                }
                else {
                    params.append(String.format("&%s=%s%s",
                            lowDatePath,
                            dateRange.getLowClosed() ? "ge" : "gt",
                            dateRange.getLow().toString()));
                }
            }

            if (dateRange.getHigh() != null) {
                String highDatePath = convertPathToSearchParam(dataType, dateHighPath != null ? dateHighPath : datePath);
                if (highDatePath == null || highDatePath.equals("")) {
                    throw new IllegalArgumentException("A date path or high date path must be provided when filtering on a date range.");
                }

                if (searchUsingPOST && search != null) {
                    DateClientParam.IDateCriterion dateCriterion = dateRange.getHighClosed()
                            ? new DateClientParam(highDatePath).beforeOrEquals().day(dateRange.getHigh().toString())
                            : new DateClientParam(highDatePath).before().day(dateRange.getHigh().toString());
                    search = doAnd ? search.and(dateCriterion) : search.where(dateCriterion);
                }
                else {
                    params.append(String.format("&%s=%s%s",
                            highDatePath,
                            dateRange.getHighClosed() ? "le" : "lt",
                            dateRange.getHigh().toString()));
                }
            }
        }

        if (search == null) {
            // TODO: Use compartment search for patient context?
            if (params.length() > 0) {
                search = fhirClient.search().byUrl(String.format("%s?%s", dataType, params.toString()));
            }
            else {
                search = fhirClient.search().byUrl(String.format("%s", dataType));
            }
        }

        org.hl7.fhir.r4.model.Bundle results = cleanEntry(search.returnBundle(org.hl7.fhir.r4.model.Bundle.class).execute(), dataType);

        return new FhirBundleCursorR4(fhirClient, results);
    }

    private org.hl7.fhir.r4.model.Bundle cleanEntry(org.hl7.fhir.r4.model.Bundle bundle, String dataType) {
        List<org.hl7.fhir.r4.model.Bundle.BundleEntryComponent> entry = new ArrayList<>();
        for (org.hl7.fhir.r4.model.Bundle.BundleEntryComponent comp : bundle.getEntry()){
            if (comp.getResource().getResourceType().name().equals(dataType)) {
                entry.add(comp);
            }
        }
        bundle.setEntry(entry);
        return bundle;
    }
}
