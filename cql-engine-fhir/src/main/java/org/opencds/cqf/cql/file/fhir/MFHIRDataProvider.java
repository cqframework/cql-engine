package org.opencds.cqf.cql.file.fhir;

import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.opencds.cqf.cql.terminology.TerminologyProvider;
import org.opencds.cqf.cql.terminology.ValueSetInfo;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Christopher Schuler on 10/25/2016.
 */
public class MFHIRDataProvider extends BaseFhirDataProvider {

    private Path path;
    private URL urlToModelJar;
    private String typePackageName = "com.motivemi.cds2.types";
    protected TerminologyProvider terminologyProvider;

    public MFHIRDataProvider (String path, TerminologyProvider terminologyProvider, URL urlToModelJar) {
        if (path.isEmpty()) {
            throw new InvalidPathException(path, "Invalid path!");
        }
        this.path = Paths.get(path);
        this.terminologyProvider = terminologyProvider;
        this.urlToModelJar = urlToModelJar;
    }

    @Override
    protected Object resolveProperty(Object target, String path) {
        /*
        * This method is complex for mFHIR
        * May be accessing 2 different packages: com.motivemi.cds2.model or com.motivemi.cds2.types
        * Neither of these is on the class path
        * */
        if (target == null) {
            return null;
        }

        URLClassLoader loader = new URLClassLoader(new URL[] {urlToModelJar});
        Class<?> clazz;
        try {
            String classPath = target.getClass().getCanonicalName();
            clazz = loader.loadClass(classPath);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("The resource is not a valid model type...");
        }
        try {
            String accessorMethodName = String.format("%s%s%s", "get", path.substring(0, 1).toUpperCase(), path.substring(1));
            Method accessor;
            accessor = clazz.getMethod(accessorMethodName);

            Object result = accessor.invoke(target);
            result = mapPrimitive(result);
            return result;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("Could not determine accessor function for property %s of type %s", path, clazz.getSimpleName()));
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("Errors occurred attempting to invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Could not invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
        }
    }

    @Override
    public Object resolvePath(Object target, String path) {
        String[] identifiers = path.split("\\.");
        for (int i = 0; i < identifiers.length; i++) {
            target = resolveProperty(target, identifiers[i]);
        }

        return target;
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
            toResults = toResults.resolve((String) contextValue);
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
                    DateTime date = null;
                    if (temp instanceof String) {
                        date = DateTime.fromJodaDateTime(new org.joda.time.DateTime(temp));
                    }
                    else if (temp instanceof Date) {
                      date = DateTime.fromJavaDate((Date)temp);
                    }

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
                    // NOTE: making an assumption here that the date paths will be either null or java.util.Date dates
                    DateTime highDt = dateHighPath != null ? (DateTime)resolvePath(res, dateHighPath) : DateTime.fromJavaDate((Date)expanded.getHigh());
                    DateTime lowDt = dateLowPath != null ? (DateTime)resolvePath(res, dateLowPath) : DateTime.fromJavaDate((Date)expanded.getLow());

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
                                Object conceptCodes = resolveProperty(codeObj, "coding");
                                for (Object o : (Iterable)conceptCodes) {
                                    if (resolveProperty(o, "code").equals(code.getCode())
                                            && resolveProperty(o, "system").equals(code.getSystem()))
                                    {
                                        if (results.indexOf(res) == -1)
                                            results.add(res);
                                    }
                                }
                            }
                        }
                        else {
                            Object conceptCodes = resolveProperty(resCodes, "coding");
                            for (Object o : (Iterable)conceptCodes) {
                                if (resolveProperty(o, "code").equals(code.getCode())
                                        && resolveProperty(o, "system").equals(code.getSystem()))
                                {
                                    if (results.indexOf(res) == -1)
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
        // get the resource type
        String resourceType;
        try {
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(resource);
            resource = ((JSONObject)jsonArray.get(0)).get("resource").toString();
            resourceType = ((JSONObject)((JSONObject)jsonArray.get(0)).get("resource")).get("resourceType").toString();
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse resource...");
        }

        // load the model class from the given url
        URLClassLoader loader = new URLClassLoader(new URL[] {urlToModelJar});
        Class<?> clazz;
        try {
            String classPath = getPackageName() + "." + resourceType;
            clazz = loader.loadClass(classPath);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("The resource is not a valid model type...");
        }

        // de-serialize into mFHIR resource
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(resource, clazz);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to parse resource with specified model class...");
        }
    }

    public boolean checkCodeMembership(Object codeObj, String vsId) {
        Object conceptCodes = resolveProperty(codeObj, "coding");
        for (Object code : (Iterable)conceptCodes) {
            try {
                if (terminologyProvider.in(new Code()
                                .withCode((String) resolveProperty(code, "code"))
                                .withSystem((String) resolveProperty(code, "system")),
                        new ValueSetInfo().withId(vsId))) {
                    return true;
                }
            } catch (InvalidRequestException e) {
                throw new IllegalArgumentException("Some value sets (with id: " + vsId + ") are not available in the terminology service. Please fix these errors to proceed with evaluation.");
            }
            catch (FhirClientConnectionException e) {
                throw new IllegalArgumentException("ERROR: The terminology service is down.");
            }
        }
        return false;
    }
}
