package org.opencds.cqf.cql.file.fhir;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Christopher on 10/5/2016.
 */
public class JsonFileBasedFhirProvider extends BaseFhirDataProvider {

    private Path path;
    private FhirTerminologyProvider terminologyProvider;

    public JsonFileBasedFhirProvider (String path, String endpoint) {
        if (path.isEmpty()) {
            throw new InvalidPathException(path, "Invalid path!");
        }
        this.path = Paths.get(path);
        this.terminologyProvider = endpoint == null ? new FhirTerminologyProvider().withEndpoint("http://fhirtest.uhn.ca/baseDstu3")
                : new FhirTerminologyProvider().withEndpoint(endpoint);
    }

    private URL pathToModelJar;
    public URL getpathToModelJar() {
        return pathToModelJar;
    }

    public void setPathToModelJar(URL pathToModelJar) {
        this.pathToModelJar = pathToModelJar;
    }

    public JsonFileBasedFhirProvider withPathToModelJar(URL pathToModelJar) {
        setPathToModelJar(pathToModelJar);
        return this;
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
            toResults = toResults.resolve(getDefaultPatient(toResults));
        }

        if (dataType == null) {
            throw new IllegalArgumentException("A data type (i.e. Procedure, Valueset, etc...) must be specified for clinical data retrieval");
        }

        // No filtering
        if (dateRange == null && codePath == null) {

            patientResources = getPatientResources(toResults, context, dataType);
            for (JSONArray patientResource : patientResources) {
                Object res;
                if (getPackageName().equals("com.motivemi.cds2.model") || getPackageName().equals("com.ge.ns.fhir.model")) {
                    res = deserialize(patientResource.toString());
                }
                else {
                    res = fhirContext.newJsonParser().parseResource(patientResource.toString());
                }
                results.add(res);
            }
            return results;
        }

        patientResources = getPatientResources(toResults, context, dataType);

        // filtering
        // NOTE: retrieves can include both date and code filtering,
        // so even though I may include a record if it is within the date range,
        // that record may be excluded later during the code filtering stage
        for (JSONArray resource : patientResources) {
            Object res;
            if (getPackageName().equals("com.motivemi.cds2.model") || getPackageName().equals("com.ge.ns.fhir.model")) {
                res = deserialize(resource.toString());
            }
            else {
                res = fhirContext.newJsonParser().parseResource(resource.toString());
            }

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

                    DateTime date = null;
                    Object temp = resolvePath(res, datePath);

                    // May not need this - currently useful for testing
                    // TODO: alter tests to operate on java.util.Date dates
                    if (temp instanceof DateTime) {
                        date = (DateTime)temp;
                    }
                    else if (temp instanceof Date) {
                        date = DateTime.fromJavaDate((Date)temp);
                    }

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
                            else if (!inValSet)
                                results.remove(res);
                        }
                    }
                    else if (resCodes instanceof CodeableConcept) {
                        if (checkCodeMembership(resCodes, valueSet) && results.indexOf(res) == -1)
                            results.add(res);
                    }
                }
                else if (codes != null) {
                    for (Code code : codes) {
                        Object resCodes = resolvePath(res, codePath);
                        if (resCodes instanceof Iterable) {
                            for (Object codeObj : (Iterable)resCodes) {
                                Iterable<Coding> conceptCodes = ((CodeableConcept)codeObj).getCoding();
                                for (Coding c : conceptCodes) {
                                    if (c.getCodeElement().getValue().equals(code.getCode()) && c.getSystem().equals(code.getSystem()))
                                    {
                                        if (results.indexOf(res) == -1)
                                            results.add(res);
                                    }
                                    else if (results.indexOf(res) != -1)
                                        results.remove(res);
                                }
                            }
                        }
                        else if (resCodes instanceof CodeableConcept) {
                            for (Coding c : ((CodeableConcept)resCodes).getCoding()) {
                                if (c.getCodeElement().getValue().equals(code.getCode()) && c.getSystem().equals(code.getSystem()))
                                {
                                    if (results.indexOf(res) == -1)
                                        results.add(res);
                                }
                                else if (results.indexOf(res) != -1)
                                    results.remove(res);
                            }
                        }
                    }
                }
            }
        }

        return results;
    }

    private Object deserialize(String resource) {
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
        URLClassLoader loader = new URLClassLoader(new URL[] {pathToModelJar});
        Class<?> clazz;
        try {
            clazz = loader.loadClass(getPackageName() + "." + resourceType);
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

    private List<JSONArray> getPatientResources(Path evalPath, String context, String dataType) {
        List<JSONArray> resources = new ArrayList<>();
        // fetch patient directory
        File file = new File(evalPath.toString());

        if (file.exists()) {
            if (context.equals("Patient")) {
                // fetch the json file (assuming single file) within directory
                File temp = file.listFiles()[0];
                JSONArray arr = getRelevantResources(temp, evalPath, dataType);
                if (!arr.isEmpty())
                  resources.add(arr);
            }
            else { // Population
                try {
                    for (File temp : file.listFiles()) {
                      JSONArray arr = getRelevantResources(temp, evalPath, dataType);
                      if (!arr.isEmpty())
                        resources.add(arr);
                    }
                } catch (NullPointerException e) {
                    throw new IllegalArgumentException("Patient directory empty...");
                }
            }
        }
        return resources;
    }

    private JSONArray getRelevantResources(File temp, Path evalPath, String dataType) {
        JSONArray resources = new JSONArray();
        try {
            // Account for possible .js json
            if (temp.getName().contains(".json") || temp.getName().contains(".js")) {
                FileReader reader = new FileReader(evalPath.resolve(temp.getName()).toString());
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
                JSONArray jsonArray =  (JSONArray) jsonObject.get("entry");
                for (int i = 0; i < jsonArray.size(); ++i) {
                    if (((JSONObject)((JSONObject)jsonArray.get(i)).get("resource")).get("resourceType").equals(dataType)) {
                        resources.add(jsonArray.get(i));
                    }
                }
            } else {
                throw new RuntimeException("The patient files must contain .json or .js extensions");
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("The target directory is empty!");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error reading file path...");
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Error reading json file...");
        }
        return resources;
    }

    // If Patient context without patient id, get the first patient
    public String getDefaultPatient(Path evalPath) {
        File file = new File(evalPath.toString());
        if (!file.exists()) {
            throw new IllegalArgumentException("Invalid path: " + evalPath.toString());
        }
        try {
            return file.listFiles()[0].getName();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("The target directory is empty.");
        }
    }

    public boolean checkCodeMembership(Object codeObj, String vsId) {
        Iterable<Coding> conceptCodes = ((CodeableConcept)codeObj).getCoding();
        for (Coding code : conceptCodes) {
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
