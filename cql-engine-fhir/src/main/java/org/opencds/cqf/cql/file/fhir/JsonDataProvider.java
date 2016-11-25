package org.opencds.cqf.cql.file.fhir;

import org.opencds.cqf.cql.terminology.TerminologyProvider;
import java.nio.file.Path;

/**
 * Created by Christopher Schuler on 11/8/2016.
 */
public interface JsonDataProvider {

    Path path = null; // path to root patient data directory
    TerminologyProvider terminologyProvider = null;
    Object deserialize(String resource);
    boolean checkCodeMembership(Object codeObj, String vsId);

}
