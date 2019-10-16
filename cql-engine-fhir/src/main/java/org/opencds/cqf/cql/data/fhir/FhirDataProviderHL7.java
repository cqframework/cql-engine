package org.opencds.cqf.cql.data.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.gclient.IQuery;
import org.hl7.fhir.instance.model.*;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.opencds.cqf.cql.exception.DataProviderException;
import org.opencds.cqf.cql.exception.InvalidPrecision;
import org.opencds.cqf.cql.exception.UnknownType;
import org.opencds.cqf.cql.runtime.*;
import org.opencds.cqf.cql.terminology.ValueSetInfo;

import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class FhirDataProviderHL7 extends FhirDataProviderStu3 {

    // Although this is Dstu2, it is closer in package structure to the STU3 provider...

    public FhirDataProviderHL7() {
        setFhirContext(FhirContext.forDstu2Hl7Org());
    }

    public Iterable<Object> retrieve(String context, Object contextValue, String contextPath, String dataType, String templateId,
                                     String codePath, Iterable<Code> codes, String valueSet, String datePath, String dateLowPath,
                                     String dateHighPath, Interval dateRange) {

        // Apply filtering based on
        //  profile (templateId)
        //  codes
        //  dateRange
        IQuery<IBaseBundle> search = null; //fhirClient.search().forResource(dataType);

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

            params.append(String.format("%s=%s", contextPath, URLEncode((String)contextValue)));
        }

        if (codePath != null && !codePath.equals("")) {
            if (params.length() > 0) {
                params.append("&");
            }
            if (valueSet != null && !valueSet.equals("")) {
                if (terminologyProvider != null && expandValueSets) {
                    ValueSetInfo valueSetInfo = new ValueSetInfo().withId(valueSet);
                    codes = terminologyProvider.expand(valueSetInfo);
                }
                else {
                    params.append(String.format("%s:in=%s", convertPathToSearchParam(dataType, codePath), URLEncode(valueSet)));
                }
            }

            if (codes != null) {
                StringBuilder codeList = new StringBuilder();
                for (Code code : codes) {
                    if (codeList.length() > 0) {
                        codeList.append(",");
                    }

                    if (code.getSystem() != null) {
                        codeList.append(URLEncode(code.getSystem()));
                        codeList.append("|");
                    }

                    codeList.append(URLEncode(code.getCode()));
                }
                params.append(String.format("%s=%s", convertPathToSearchParam(dataType, codePath), codeList.toString()));
            }
        }

        if (dateRange != null) {
            if (dateRange.getLow() != null) {
                String lowDatePath = convertPathToSearchParam(dataType, dateLowPath != null ? dateLowPath : datePath);
                if (lowDatePath == null || lowDatePath.equals("")) {
                    throw new DataProviderException("A date path or low date path must be provided when filtering on a date range.");
                }

                params.append(String.format("&%s=%s%s",
                        lowDatePath,
                        dateRange.getLowClosed() ? "ge" : "gt",
                        dateRange.getLow().toString()));
            }

            if (dateRange.getHigh() != null) {
                String highDatePath = convertPathToSearchParam(dataType, dateHighPath != null ? dateHighPath : datePath);
                if (highDatePath == null || highDatePath.equals("")) {
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
            search = fhirClient.search().byUrl(String.format("%s?%s", dataType, params.toString()));
        }
        else {
            search = fhirClient.search().byUrl(String.format("%s", dataType));
        }

        org.hl7.fhir.instance.model.Bundle results = cleanEntry(search.returnBundle(org.hl7.fhir.instance.model.Bundle.class).execute(), dataType);

        return new FhirBundleCursorHL7(fhirClient, results);
    }

    public org.hl7.fhir.instance.model.Bundle cleanEntry(org.hl7.fhir.instance.model.Bundle bundle, String dataType) {
        org.hl7.fhir.instance.model.Bundle cleanBundle = new org.hl7.fhir.instance.model.Bundle();
        for (org.hl7.fhir.instance.model.Bundle.BundleEntryComponent comp : bundle.getEntry()){
            if (comp.getResource().getResourceType().name().equals(dataType)) {
                cleanBundle.addEntry(comp);
            }
        }

        return cleanBundle;
    }
}
