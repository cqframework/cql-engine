package org.opencds.cqf.cql.file.fhir;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.joda.time.Partial;
import org.opencds.cqf.cql.data.fhir.BaseFhirDataProvider;
import org.opencds.cqf.cql.elm.execution.InEvaluator;
import org.opencds.cqf.cql.elm.execution.IncludesEvaluator;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.terminology.ValueSetInfo;
import org.opencds.cqf.cql.terminology.fhir.FhirTerminologyProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
What the heck does this thing do?
  This class is intended to provide the user with the alternative of using a local repository
  for clinical data retrieval instead of an external service.
  NOTE: This class still uses a Terminology service for value set retrieval and evaluating code membership

How do I use it?
  Point the provider to the directory of patients:
    FileBasedFhirProvider provider = new FileBasedFhirProvider().withPath("...");
    Each subfolder name in the patients directory should be an id for a specific patients
      In each patient folder there should be subfolders contatining clinical information
        (e.g. Condition, Procedure, etc...) for that patient
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

public class FileBasedFhirProvider extends BaseFhirDataProvider {

  private Path path;
  public Path getPath() {
    return path;
  }

  public void setPath(String path) {
    if (path.isEmpty() || path == null) {
      throw new InvalidPathException(path, "Invalid path!");
    }
    this.path = Paths.get(path);
  }

  public FileBasedFhirProvider withPath(String path) {
    setPath(path);
    return this;
  }

  // TODO: This method is getting exceedingly long -- decompose
  public Iterable<Object> retrieve(String context, Object contextValue, String dataType, String templateId,
                                   String codePath, Iterable<Code> codes, String valueSet, String datePath,
                                   String dateLowPath, String dateHighPath, Interval dateRange) {

    List<Object> results = new ArrayList<>();
    List<String> patientFiles = new ArrayList<>();
    Path toResults = path;
    Path toValueset = null;
    Path toCode = null;

    if (templateId != null && !templateId.equals("")) {
      // do something?
    }

    if (codePath == null && (codes != null || valueSet != null)) {
        throw new IllegalArgumentException("A code path must be provided when filtering on codes or a valueset.");
    }

    if (context != null && context.equals("Patient") && contextValue != null) {
      toResults = toResults.resolve((String)contextValue);
    }

    if (dataType != null) {
      toResults = toResults.resolve(dataType.toLowerCase());
    }
    else { // Just in case -- probably redundant error checking...
      throw new IllegalArgumentException("A data type (i.e. Procedure, Valueset, etc...) must be specified for clinical data retrieval");
    }

    // list of json files as String
    // TODO: xml files as well?
    if (dateRange == null && codePath == null) {
      patientFiles = getPatientFiles(toResults, context);
      results.addAll(patientFiles);
      return results;
    }

    if (context == null)
      context = "Population";
    patientFiles = getPatientFiles(toResults, context);

    // filtering
    // NOTE: retrieves can include both date and code filtering,
    // so even though I may include a record if it is within the date range,
    // that record may be excluded later during the code filtering stage
    for (String resource : patientFiles) {
      Object outcome = null;
      Object res = fhirContext.newJsonParser().parseResource(resource);
      // since retrieves can include both date and code filtering, I need this flag
      // to determine inclusion of codes -- if date is no good -- don't test code
      boolean includeRes = true;
      // dateRange element optionally allows a date range to be provided.
      // The clinical statements returned would be only those clinical statements whose date
      // fell within the range specified.
      if (dateRange != null) {
        // Expand Interval DateTimes to avoid InEvalutor returning null
        // TODO: account for possible null for high or low?
        Interval expanded = new Interval(
                                  DateTime.expandPartialMin((DateTime)dateRange.getLow(), 7), true,
                                  DateTime.expandPartialMin((DateTime)dateRange.getHigh(), 7), true
                                  );
        if (datePath != null) {
          if (dateHighPath != null || dateLowPath != null) {
            throw new IllegalArgumentException("If the datePath is specified, the dateLowPath and dateHighPath attributes must not be present.");
          }

          DateTime date = (DateTime)resolvePath(res, datePath);
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
          // What we want here is to build a new Interval using corresponding high and low dates
          // Then use the IncludesEvaluator to check membership
          DateTime lowDt = null;
          DateTime highDt = null;

          // get the high and low dates if present
          // if not present, set to corresponding value in the expanded Interval
          if (dateHighPath != null) {
              highDt = (DateTime)resolvePath(res, dateHighPath);
          }
          else {
              highDt = (DateTime)expanded.getHigh();
          }

          if (dateLowPath != null) {
              lowDt = (DateTime)resolvePath(res, dateLowPath);
          }
          else {
              lowDt = (DateTime)expanded.getLow();
          }

          // the low and high dates are resolved -- create the Interval
          Interval highLowDtInterval = new Interval(lowDt, true, highDt, true);

          // Now the Includes operation
          if ((Boolean)IncludesEvaluator.includes(expanded, highLowDtInterval)) {
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
    } // end of filtering for each loop

    return results;
  }

  // evalPath examples -- NOTE: this occurs before filtering
  // ..../data/procedure -- all procedures for all patients (Population context)
  // ..../data/123/procedure -- all procedures for patient 123
  public List<String> getPatientFiles(Path evalPath, String context) {
    List<String> fileContents = new ArrayList<>();
    if (context.equals("Patient") || context.equals("") || context == null) {
      File file = new File(evalPath.toString());

      if (!file.exists()) {
        throw new IllegalArgumentException("Invalid path: " + evalPath.toString());
      }

      try {
        for (File f : file.listFiles()) {
          if (f.getName().indexOf(".xml") != -1 || f.getName().indexOf(".json") != -1)
            fileContents.add(readFile(f));
        }
      }
      catch(NullPointerException npe) {
        throw new IllegalArgumentException("The target directory is empty!");
      }
    }
    else { // Population
      File rootDir = new File(path.toString());
      for (File patientFolder : rootDir.listFiles()) { // all the patients in data set
        for (File patientSubFolder : patientFolder.listFiles()) { // all the folders in the patient directory
          if (!patientSubFolder.isDirectory()) { continue; }
          // find the data type directory (condition, encounter, etc...)
          if (patientSubFolder.getName().equals(evalPath.getName(evalPath.getNameCount() - 1).toString())) {
            for (File dataTypeFile : patientSubFolder.listFiles()) {
              if (dataTypeFile.getName().indexOf(".xml") != -1 || dataTypeFile.getName().indexOf(".json") != -1)
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
    }
    catch (IOException e) {
      throw new IllegalArgumentException("File not found at path " + f.getPath());
    }
    return fileContent.toString();
  }

  public boolean checkCodeMembership(Object codeObj, String vsId) {
    // TODO: endpoint hardcoded for now -- may want to enable user to choose...
    // This endpoint is causing error:
    // ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException: Failed to retrieve the server metadata statement during client initialization. URL used was https://ontoserver.csiro.au/stu3/metadata
    // FhirTerminologyProvider provider = new FhirTerminologyProvider().withEndpoint("https://ontoserver.csiro.au/stu3");
    FhirTerminologyProvider provider = new FhirTerminologyProvider().withEndpoint("http://fhirtest.uhn.ca/baseDstu3");
    Iterable<Coding> conceptCodes = ((CodeableConcept)codeObj).getCoding();
    for (Coding code : conceptCodes) {
      if (provider.in(new Code()
                  .withCode(code.getCodeElement().getValue())
                  .withSystem(code.getSystem()),
                  new ValueSetInfo().withId(vsId)))
      {
        return true;
      }
    }
    return false;
  }

  public DateTime toDateTime(DateTimeType hapiDt) {
    // TODO: do we want 0 to be the default value if null?
    int year = hapiDt.getYear() == null ? 0 : hapiDt.getYear();
    // months in HAPI are zero-indexed -- don't want that
    int month = hapiDt.getMonth() == null ? 0 : hapiDt.getMonth() + 1;
    int day = hapiDt.getDay() == null ? 0 : hapiDt.getDay();
    int hour = hapiDt.getHour() == null ? 0 : hapiDt.getHour();
    int minute = hapiDt.getMinute() == null ? 0 : hapiDt.getMinute();
    int sec = hapiDt.getSecond() == null ? 0 : hapiDt.getSecond();
    int millis = hapiDt.getMillis() == null ? 0 : hapiDt.getMillis();
    return new DateTime().withPartial(new Partial(DateTime.getFields(7), new int[] {year, month, day, hour, minute, sec, millis}));
  }
}
