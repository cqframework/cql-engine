package org.opencds.cqf.cql.file.fhir;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.opencds.cqf.cql.data.fhir.BaseFhirDataProvider;
import org.opencds.cqf.cql.elm.execution.InEvaluator;
import org.opencds.cqf.cql.elm.execution.IncludesEvaluator;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.terminology.ValueSetInfo;
import org.opencds.cqf.cql.terminology.fhir.FhirTerminologyProvider;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Christopher Schuler on 10/25/2016.
 */
public class GFHIRDataProvider extends BaseFhirDataProvider {

    private Path path;
    protected FhirTerminologyProvider terminologyProvider;

    public GFHIRDataProvider (String path, String endpoint) {
        if (path.isEmpty()) {
            throw new InvalidPathException(path, "Invalid path!");
        }
        this.path = Paths.get(path);
        this.terminologyProvider = endpoint == null ? new FhirTerminologyProvider().withEndpoint("http://fhirtest.uhn.ca/baseDstu3")
                : new FhirTerminologyProvider().withEndpoint(endpoint);
    }

    public Iterable<Object> retrieve(String context, Object contextValue, String dataType, String templateId,
                                     String codePath, Iterable<Code> codes, String valueSet, String datePath,
                                     String dateLowPath, String dateHighPath, Interval dateRange) {
        List<Object> results = new ArrayList<>();
        List<JSONArray> patientResources;
        Path toResults = path;

        // default context is Patient
        if (context == null) {
            context = "Patient";
        }

        if (templateId != null && !templateId.equals("")) {
            // TODO: do something?
        }

        if (codePath == null && (codes != null || valueSet != null)) {
            throw new IllegalArgumentException("A code path must be provided when filtering on codes or a valueset.");
        }

        if (context.equals("Patient") && contextValue != null) {
            toResults = toResults.resolve((String)contextValue);
        }

        // Need the context value (patient id) to resolve the toResults path correctly
        else if (context.equals("Patient") && contextValue == null) {
            toResults = toResults.resolve(JsonFileProcessing.getDefaultPatient(toResults));
        }

        if (dataType == null) {
            throw new IllegalArgumentException("A data type (i.e. Procedure, Valueset, etc...) must be specified for clinical data retrieval");
        }

        // No filtering
        if (dateRange == null && codePath == null) {

            patientResources = JsonFileProcessing.getPatientResources(toResults, context, dataType);
            for (JSONArray patientResource : patientResources) {
                Object res = deserialize(patientResource.toString());
                if (res != null)
                    results.add(res);
            }
            return results;
        }

        patientResources = JsonFileProcessing.getPatientResources(toResults, context, dataType);

        // filtering
        // NOTE: retrieves can include both date and code filtering,
        // so even though I may include a record if it is within the date range,
        // that record may be excluded later during the code filtering stage
        for (JSONArray resource : patientResources) {
            Object res = deserialize(resource.toString());
            if (res == null) { continue; }

            // since retrieves can include both date and code filtering, I need this flag
            // to determine inclusion of codes -- if date is no good -- don't test code
            boolean includeRes = true;

            // dateRange element optionally allows a date range to be provided.
            // The clinical statements returned would be only those clinical statements whose date
            // fell within the range specified.
            if (dateRange != null) {
                // Expand Interval DateTimes to avoid InEvalutor returning null
                // TODO: account for possible null for high or low? - No issues with this yet...
                Interval expanded = new Interval(
                        DateTime.expandPartialMin((DateTime)dateRange.getLow(), 7), true,
                        DateTime.expandPartialMin((DateTime)dateRange.getHigh(), 7), true
                );

                if (datePath != null) {
                    if (dateHighPath != null || dateLowPath != null) {
                        throw new IllegalArgumentException("If the datePath is specified, the dateLowPath and dateHighPath attributes must not be present.");
                    }

                    Object temp = resolvePath(res, datePath);
                    // TODO: verify that gfhir always returns a DateTime -- should be mapped in super
                    DateTime date = (DateTime) temp;

                    // TODO: what if the datePath returns a range instead of a point?
                    if (date != null && InEvaluator.in(date, expanded)) {
                        results.add(res);
                    }
                    else {
                        includeRes = false;
                    }
                }

                else {
                    if (dateHighPath == null && dateLowPath == null) {
                        throw new IllegalArgumentException("If the datePath is not given, either the lowDatePath or highDatePath must be provided.");
                    }

                    // get the high and low dates if present
                    // if not present, set to corresponding value in the expanded Interval
                    DateTime highDt = dateHighPath != null ? DateTime.fromJavaDate(((DateTimeDt)resolvePath(res, dateHighPath)).getValue())
                                                           : DateTime.fromJavaDate((Date)expanded.getHigh());
                    DateTime lowDt = dateLowPath != null ? DateTime.fromJavaDate(((DateTimeDt)resolvePath(res, dateLowPath)).getValue())
                                                         : DateTime.fromJavaDate((Date)expanded.getLow());

                    // the low and high dates are resolved -- create the Interval
                    Interval highLowDtInterval = new Interval(lowDt, true, highDt, true);

                    // Now the Includes operation
                    if ((Boolean) IncludesEvaluator.includes(expanded, highLowDtInterval)) {
                        results.add(res);
                    }
                    else {
                        includeRes = false;
                    }
                }
            }

            // codePath specifies which property/path of the model contains the Code or Codes for the clinical statement
            if (codePath != null && !codePath.equals("") && includeRes) {
                if (valueSet != null && !valueSet.equals("")) {
                    // now we need to get the codes in the resource and check for membership in the valueset
                    Object resCodes = resolvePath(res, codePath);
                    if (resCodes instanceof Iterable) {
                        for (Object codeObj : (Iterable)resCodes) {
                            boolean inValSet = checkCodeMembership(codeObj, valueSet);
                            if (inValSet && results.indexOf(res) == -1)
                                results.add(res);
                        }
                    }
                    else {
                        if (checkCodeMembership(resCodes, valueSet) && results.indexOf(res) == -1)
                            results.add(res);
                    }
                }
                else if (codes != null) {
                    for (Code code : codes) {
                        Object resCodes = resolvePath(res, codePath);
                        if (resCodes instanceof Iterable) {
                            for (Object codeObj : (Iterable)resCodes) {
                                List<CodingDt> conceptCodes = ((CodeableConceptDt)codeObj).getCoding();
                                for (CodingDt c : conceptCodes) {
                                    if (c.getCodeElement().getValue().equals(code.getCode())
                                            && c.getSystem().equals(code.getSystem())
                                            && results.indexOf(res) == -1)
                                    {
                                        results.add(res);
                                    }
                                }
                            }
                        }
                        else {
                            for (CodingDt c : ((CodeableConceptDt)resCodes).getCoding()) {
                                if (c.getCodeElement().getValue().equals(code.getCode())
                                        && c.getSystem().equals(code.getSystem())
                                        && results.indexOf(res) == -1)
                                {
                                    results.add(res);
                                }
                            }
                        }
                    }
                }
            }
        }

        return results;
    }

    public Object deserialize(String resource) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(resource);
            resource = ((JSONObject) jsonArray.get(0)).get("resource").toString();
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse resource...");
        }
        return fhirContext.newJsonParser().parseResource(resource);
    }

    public boolean checkCodeMembership(Object codeObj, String vsId) {
        Iterable<CodingDt> conceptCodes = ((CodeableConceptDt)codeObj).getCoding();
        for (CodingDt code : conceptCodes) {
            if (terminologyProvider.in(new Code()
                            .withCode(code.getCodeElement().getValue())
                            .withSystem(code.getSystem()),
                            new ValueSetInfo().withId(vsId)))
            {
                return true;
            }
        }
        return false;
    }
}
