package org.opencds.cqf.cql.retrieve;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.opencds.cqf.cql.exception.DataProviderException;
import org.opencds.cqf.cql.exception.UnknownPath;
import org.opencds.cqf.cql.model.ModelResolver;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.terminology.TerminologyProvider;
import org.opencds.cqf.cql.terminology.ValueSetInfo;
import org.opencds.cqf.cql.util.CodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

/*
What the heck does this thing do?
  This class is intended to provide the user with the alternative of using a local repository
  for clinical data retrieval instead of an external service.
  NOTE: This class still uses a Terminology service for value set retrieval and evaluating code membership

How do I use it?
  Point the provider to the directory of patients:
    FileBasedFhirProvider provider = new FileBasedFhirProvider(path to root data dir, terminology service endpoint(optional));
    Each subfolder name in the patients directory should be an id for a specific patients
      In each patient folder there should be subfolders containing clinical information
        (e.g. Condition, Procedure, etc...) for that patient in JSON format (XML support pending)
    Here is a mock directory structure:
    - patients
      - 123
        - Conditions
          - JSON representation of Fhir resources
        - Procedures
        - Encounters
        - etc...
      - 154
        - Observations
        - etc...
      - 209
      - etc...
*/

public class FileBasedFhirRetrieveProvider implements RetrieveProvider {

    private static final Logger logger = LoggerFactory.getLogger(FileBasedFhirRetrieveProvider.class);

    private Path path;
    protected FhirContext fhirContext;
    protected ModelResolver modelResolver;
    protected TerminologyProvider terminologyProvider;
    protected IParser parser;

    public FileBasedFhirRetrieveProvider(String path, TerminologyProvider terminologyProvider, FhirContext fhirContext, ModelResolver modelResolver) {
        this.fhirContext = fhirContext;
        this.modelResolver = modelResolver;
        if (path.isEmpty()) {
            throw new UnknownPath("Cannot resolve empty path");
        }
        this.path = Paths.get(path);
        this.terminologyProvider = terminologyProvider;
        this.parser = this.fhirContext.newJsonParser();
    }

    @Override
    public Iterable<Object> retrieve(String context, String contextPath, Object contextValue, String dataType,
                                     String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath,
                                     String dateLowPath, String dateHighPath, Interval dateRange) {

        List<Object> results = new ArrayList<>();
        List<String> patientFiles;
        Path toResults = path;

        // default context is Patient
        if (context == null) {
            context = "Patient";
        }

        if (templateId != null && !templateId.equals("")) {
            // TODO: do something?
        }

        if (codePath == null && (codes != null || valueSet != null)) {
            throw new DataProviderException("A code path must be provided when filtering on codes or a valueset.");
        }

        if (context.equals("Patient") && contextValue != null) {
            toResults = toResults.resolve((String) contextValue);
        }

        // Need the context value (patient id) to resolve the toResults path correctly
        else if (context.equals("Patient") && contextValue == null) {
            toResults = toResults.resolve(getDefaultPatient(toResults));
        }

        if (dataType != null) {
            toResults = toResults.resolve(dataType);
        } else { // Just in case -- probably redundant error checking...
            throw new DataProviderException(
                    "A data type (i.e. Procedure, Valueset, etc...) must be specified for clinical data retrieval");
        }

        // No filtering
        if (dateRange == null && codePath == null) {
            patientFiles = getPatientFiles(toResults, context);
            for (String resource : patientFiles) {
                results.add(this.parser.parseResource(resource));
            }
            return results;
        }

        patientFiles = getPatientFiles(toResults, context);

        // filtering
        // NOTE: retrieves can include both date and code filtering,
        // so even though I may include a record if it is within the date range,
        // that record may be excluded later during the code filtering stage
        for (String resource : patientFiles) {
            Object res = this.parser.parseResource(resource);

            // since retrieves can include both date and code filtering, I need this flag
            // to determine inclusion of codes -- if date is no good -- don't test code
            boolean includeRes = true;

            // dateRange element optionally allows a date range to be provided.
            // The clinical statements returned would be only those clinical statements
            // whose date
            // fell within the range specified.
            // if (dateRange != null) {

            // // Expand Interval DateTimes to avoid InEvaluator returning null
            // // TODO: account for possible null for high or low? - No issues with this
            // yet...
            // Interval expanded = new Interval(
            // ((DateTime)dateRange.getLow()).expandPartialMin(Precision.MILLISECOND), true,
            // ((DateTime)dateRange.getHigh()).expandPartialMin(Precision.MILLISECOND), true
            // );
            // if (datePath != null) {
            // if (dateHighPath != null || dateLowPath != null) {
            // throw new DataProviderException("If the datePath is specified, the
            // dateLowPath and dateHighPath attributes must not be present.");
            // }

            // DateTime date = null;
            // Interval dateInterval = null;
            // Object path = resolvePath(res, datePath);

            // if (path instanceof DateTime) {
            // date = (DateTime) path;
            // }
            // else if (path instanceof DateTimeType) {
            // date = toDateTime((DateTimeType) path);
            // }

            // else if (path instanceof Interval) {
            // dateInterval = (Interval) path;
            // }
            // // Interval could be represented as a Period
            // else if (path instanceof Period) {
            // DateTime start = toDateTime(((Period) path).getStart());
            // DateTime end = toDateTime(((Period) path).getEnd());
            // dateInterval = new Interval(start, true, end, true);
            // }

            // if (date != null && InEvaluator.in(date, expanded, null)) {
            // results.add(res);
            // }
            // else if (dateInterval != null && (Boolean)
            // IncludesEvaluator.includes(expanded, dateInterval, "day")) {
            // results.add(res);
            // }
            // else {
            // includeRes = false;
            // }
            // }

            // else {
            // if (dateHighPath == null && dateLowPath == null) {
            // throw new DataProviderException("If the datePath is not given, either the
            // lowDatePath or highDatePath must be provided.");
            // }

            // // get the high and low dates if present
            // // if not present, set to corresponding value in the expanded Interval
            // DateTime highDt = dateHighPath != null ? (DateTime)resolvePath(res,
            // dateHighPath) : (DateTime)expanded.getHigh();
            // DateTime lowDt = dateLowPath != null ? (DateTime)resolvePath(res,
            // dateLowPath) : (DateTime)expanded.getLow();

            // // the low and high dates are resolved -- create the Interval
            // Interval highLowDtInterval = new Interval(lowDt, true, highDt, true);

            // // Now the Includes operation
            // if ((Boolean)IncludesEvaluator.includes(expanded, highLowDtInterval, "day"))
            // {
            // results.add(res);
            // }
            // else {
            // includeRes = false;
            // }
            // }
            // }


            // Check to make sure the resource matches the context if applicable.
            if (contextPath != null && contextValue != null && context != null) {
                try {
                    Object resContextValue = this.modelResolver.resolvePath(res, contextPath);
                    IPrimitiveType<?> referenceValue = (IPrimitiveType<?>)this.modelResolver.resolvePath(resContextValue, "reference");
                    if (referenceValue == null) {
                        logger.warn("Found {} resource for unrelated to context. Check the resource.", dataType);
                        continue;
                    }

                    String referenceString = referenceValue.getValueAsString();
                    if (referenceString.contains("/")) {
                        referenceString = referenceString.substring(referenceString.indexOf("/") + 1, referenceString.length());
                    }

                    if (!referenceString.equals((String)contextValue)) {
                        logger.warn("Found {} resource for context value: {} when expecting: {}. Check the resource.", dataType, referenceString, (String)contextValue);
                        continue;
                    }
                }
                catch (Exception e ) {
                    continue;
                }
            }

            // codePath specifies which property/path of the model contains the Code or
            // Codes for the clinical statement
            if (codePath != null && !codePath.equals("") && includeRes) {
                if (valueSet != null && !valueSet.equals("")) {
                    // now we need to get the codes in the resource and check for membership in the
                    // valueset
                    Object resCodes = this.modelResolver.resolvePath(res, codePath);
                    List<Code> resVersionIndependentCodes = CodeUtil.getElmCodesFromObject(resCodes, fhirContext);
                    if(resVersionIndependentCodes != null) {
                        for (Code code : resVersionIndependentCodes) {
                            boolean inValSet = checkCodeMembership(code, valueSet);
                            if (inValSet && results.indexOf(res) == -1)
                                results.add(res);
                        }
                    }
                } else if (codes != null && codes.iterator().hasNext()) {
                    boolean codeMatch = false;
                    for (Code code : codes) {
                        if (codeMatch)
                            break;

                        Object resCodes = this.modelResolver.resolvePath(res, codePath);
                        List<Code> resVersionIndependentCodes = CodeUtil.getElmCodesFromObject(resCodes, fhirContext);
                        if(resVersionIndependentCodes != null && isCodeMatch(code, resVersionIndependentCodes)) {
                            codeMatch = true;
                            break;
                        }

                    }
                    if (codeMatch && results.indexOf(res) == -1) {
                        results.add(res);
                    } else if (!codeMatch && results.indexOf(res) != -1) {
                        results.remove(res);
                    }
                }
            }
        } // end of filtering for each loop

        return results;
    }

    private boolean isCodeMatch(Code code, Iterable<Code> codes) {
        for (Code otherCode : codes) {
            if (code.getCode().equals(otherCode.getCode())
                    && code.getSystem().equals(otherCode.getSystem())) {
                return true;
            }
        }

        return false;
    }

    // If Patient context without patient id, get the first patient
    public String getDefaultPatient(Path evalPath) {
        File file = new File(evalPath.toString());
        if (!file.exists()) {
            throw new UnknownPath(String.format("Unknown path: %s", evalPath.toString()));
        } else if (file.listFiles().length == 0) {
            throw new DataProviderException("The target directory is empty!");
        }

        return file.listFiles()[0].getName();
    }

    // evalPath examples -- NOTE: this occurs before filtering
    // ..../data/procedure -- all procedures for all patients (Population context)
    // ..../data/123/procedure -- all procedures for patient 123
    public List<String> getPatientFiles(Path evalPath, String context) {
        List<String> fileContents = new ArrayList<>();
        if (context.equals("Patient") || context.equals("")) {
            File file = new File(evalPath.toString());

            if (!file.exists()) {
                return fileContents;
            }

            try {
                for (File f : file.listFiles()) {
                    if (f.getName().contains(".json"))
                        fileContents.add(readFile(f));
                }
            } catch (NullPointerException npe) {
                throw new DataProviderException("The target directory is empty!");
            }
        } else { // Population
            File rootDir = new File(path.toString());
            for (File patientFolder : rootDir.listFiles()) { // all the patients in data set
                for (File patientSubFolder : patientFolder.listFiles()) { // all the folders in the patient directory
                    if (!patientSubFolder.isDirectory()) {
                        continue;
                    }
                    // find the data type directory (condition, encounter, etc...)
                    if (patientSubFolder.getName().equals(evalPath.getName(evalPath.getNameCount() - 1).toString())) {
                        for (File dataTypeFile : patientSubFolder.listFiles()) {
                            if (dataTypeFile.getName().contains(".json"))
                                fileContents.add(readFile(dataTypeFile));
                        }
                    }
                }
            }
        }
        return fileContents;
    }

    public String readFile(File f) {
        StringBuilder fileContent = new StringBuilder();
        // try with resources -- automatically closes files once read -- cool =)
        try (BufferedReader data = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = data.readLine()) != null) {
                fileContent.append(line);
            }
        } catch (IOException e) {
            throw new UnknownPath("File not found at path " + f.getPath());
        }
        return fileContent.toString();
    }

    public boolean checkCodeMembership(Code code, String vsId) {
        ValueSetInfo valueSet = new ValueSetInfo().withId(vsId);
        boolean result = this.terminologyProvider.in(code, valueSet);
        return result;
    }
}