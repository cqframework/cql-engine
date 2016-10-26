package org.opencds.cqf.cql.file.fhir;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher Schuler on 10/25/2016.
 */
public class JsonFileProcessing {

    public static List<JSONArray> getPatientResources(Path evalPath, String context, String dataType) {
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

    public static JSONArray getRelevantResources(File temp, Path evalPath, String dataType) {
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
    public static String getDefaultPatient(Path evalPath) {
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
}
