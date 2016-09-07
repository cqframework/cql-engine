package org.opencds.cqf.cql.file.fhir;

import org.joda.time.Partial;
import org.opencds.cqf.cql.data.DataProvider;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.elm.execution.InEvaluator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.net.URI;
import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.EnumFactory;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.BaseDateTimeType;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.context.FhirContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ArrayList;

/*
What the heck does this thing do?
  This class is intended to provide the user with the alternative of using a local repository
  for clinical data retrival instead of an external service.

How do I use it?
  Point the provider to the directory of patients:
    FileBasedFhirProvider provider = new FileBasedFhirProvider().withPath("...");
    -- NOTE: the path parameter must be an absolute path
    Each subfolder name in the patients directory should be an id for a specific patients
      In each patient folder there should be subfolders contatining clinical information
        (e.g. Condition, Procedure, etc...) for that patient
    Here is a mock directory:
    - patients
      - 123
        - Conditions
          - filtering
        - Procedures
        - Encounters
        - etc...
      - 154
        - Observations
        - etc...
      - 209

*/

public class FileBasedFhirProvider implements DataProvider {

  FhirContext fhirContext;
  public FileBasedFhirProvider() {
    fhirContext = FhirContext.forDstu3();
    this.packageName = "org.hl7.fhir.dstu3.model";
  }

  private String packageName;
  public String getPackageName() {
    return packageName;
  }

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

  public Object resolvePath(Object target, String path) {
    if (target == null) {
      return null;
    }

    if (target instanceof Enumeration && path.equals("value")) {
      return ((Enumeration)target).getValueAsString();
    }

    Class<? extends Object> clazz = target.getClass();
    try {
      String accessorMethodName = String.format("%s%s%s", "get", path.substring(0, 1).toUpperCase(), path.substring(1));
      String elementAccessorMethodName = String.format("%sElement", accessorMethodName);
      Method accessor = null;
      try {
        accessor = clazz.getMethod(elementAccessorMethodName);
      }
      catch (NoSuchMethodException e) {
        accessor = clazz.getMethod(accessorMethodName);
      }
      return accessor.invoke(target);
    }
    catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(String.format("Could not determine accessor function for property %s of type %s", path, clazz.getSimpleName()));
    }
    catch (InvocationTargetException e) {
      throw new IllegalArgumentException(String.format("Errors occurred attempting to invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
    }
    catch (IllegalAccessException e) {
      throw new IllegalArgumentException(String.format("Could not invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
    }
  }

  public void setValue(Object target, String path, Object value) {
    if (target == null) {
      return;
    }

    Class<? extends Object> clazz = target.getClass();
    try {
      String accessorMethodName = String.format("%s%s%s", "set", path.substring(0, 1).toUpperCase(), path.substring(1));
      Method accessor = clazz.getMethod(accessorMethodName);
      accessor.invoke(target, value);
    }
    catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(String.format("Could not determine accessor function for property %s of type %s", path, clazz.getSimpleName()));
    }
    catch (InvocationTargetException e) {
      throw new IllegalArgumentException(String.format("Errors occurred attempting to invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
    }
    catch (IllegalAccessException e) {
      throw new IllegalArgumentException(String.format("Could not invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
    }
  }

  public Class resolveType(String typeName) {
    try {
      return Class.forName(String.format("%s.%s", packageName, typeName));
    }
    catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(String.format("Could not resolve type %s.%s.", packageName, typeName));
    }
  }

  private Field getProperty(Class clazz, String path) {
    try {
      Field field = clazz.getDeclaredField(path);
      return field;
    }
    catch (NoSuchFieldException e) {
      throw new IllegalArgumentException(String.format("Could not determine field for path %s of type %s", path, clazz.getSimpleName()));
    }
  }

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

    if (context == "Patient" && contextValue != null) {
      toResults = toResults.resolve((String)contextValue);
    }

    if (dataType != null) {
      toResults = toResults.resolve(dataType.toLowerCase());
    }
    else {
      throw new IllegalArgumentException("A data type (i.e. Procedure, Valueset, etc...) must be specified for clinical data retrieval");
    }

    // list of json files as String
    if (dateRange == null && codePath == null) {
      patientFiles = getPatientFiles(toResults, context);
      results.addAll(patientFiles);
      return results;
    }

    patientFiles = getPatientFiles(toResults, context);

    // filtering
    for (String resource : patientFiles) {
      Object outcome = null;
      Object res = fhirContext.newJsonParser().parseResource(resource);

      // dateRange element optionally allows a date range to be provided.
      // The clinical statements returned would be only those clinical statements whose date
      // fell within the range specified.
      if (dateRange != null) {
        if (datePath != null) {
          if (dateHighPath != null || dateLowPath != null)
            throw new IllegalArgumentException("If the datePath is specified, the dateLowPath and dateHighPath attributes must not be present.");

          // Here's what I am trying to do here:
          // Consider the path period.end.value
          // I need to chain the following calls together:
          // getPeriod().getEnd() for the Type (Encounter in this case)
          if (datePath.indexOf(".") != -1) {
            Object predecessor = null;
            for (String s : parsePath(datePath)) {
              if (s.equals("value")) break;
              if (predecessor == null)
                predecessor = resolvePath(res, s);
              else {
                predecessor = resolvePath(predecessor, s);
              }
            }
            outcome = predecessor;
          }
          else {
            outcome = resolvePath(res, datePath);
          }
          // now we need to convert the outcome into DateTime
          if (outcome instanceof DateTimeType) {
            // TODO: more precise than this?
            // TODO: These casts are not working...
            // int year = ((DateTimeType)outcome).getYear();
            // // months are zero-indexed -- don't want that
            // int month = ((DateTimeType)outcome).getMonth() + 1;
            // int day = ((DateTimeType)outcome).getDay();
            // DateTime date = new DateTime().withPartial(new Partial(DateTime.getFields(3), new int[] {year, month, day}));
            // if (date != null && InEvaluator.in(date, dateRange))
            //   results.add(res);
          }
        }
        else {
          Object low = null;
          Object high = null;
          if (dateHighPath != null)
            high = resolvePath(res, dateHighPath);
          if (dateLowPath != null)
            low = resolvePath(res, dateLowPath);
          Interval newDateRange = new Interval(low, true, high, true);
          if (low == null) {
            if (InEvaluator.in(high, dateRange))
              results.add(res);
          }
          else if (high == null) {
            if (InEvaluator.in(low, dateRange))
              results.add(res);
          }
          else if (high == null && low == null) {
            throw new IllegalArgumentException("If the datePath is not given, either the lowDatePath or highDatePath must be provided.");
          }
          else {
            if (InEvaluator.in(low, dateRange) && InEvaluator.in(high, dateRange))
              results.add(res);
          }
        }
      }

      // codePath specifies which property/path of the model contains the Code or Codes for the clinical statement
      if (codePath != null && !codePath.equals("")) {
        if (valueSet != null && !valueSet.equals("")) {

        }
        else if (codes != null) {
          for (Code code : codes) {

          }
        }
      }
    }

    return results;
  }

  // evalPath examples -- NOTE: this occurs before filtering
  // ..../data/procedure -- all procedures for all patients (Population context)
  // ..../data/123/procedure -- all procedures for patient 123
  // TODO: implement better error reporting
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

  public String[] parsePath(String path) {
    return path.split("\\.");
  }
}
