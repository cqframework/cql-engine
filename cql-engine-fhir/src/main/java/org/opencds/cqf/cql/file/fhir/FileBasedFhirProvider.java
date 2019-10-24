package org.opencds.cqf.cql.file.fhir;

import org.opencds.cqf.cql.data.CompositeDataProvider;
import org.opencds.cqf.cql.type.Dstu3FhirModelResolver;

import ca.uhn.fhir.context.FhirContext;

import java.net.URL;

public class FileBasedFhirProvider {

	private CompositeDataProvider dataProvider;

    public FileBasedFhirProvider (String path, String endpoint) {
		FhirContext fhirContext = FhirContext.forDstu3();
		Dstu3FhirModelResolver modelResolver = new Dstu3FhirModelResolver(fhirContext);
		FileBasedFhirRetrieveProvider retrieveProvider = new FileBasedFhirRetrieveProvider(path, endpoint, fhirContext, modelResolver);
		this.dataProvider = new CompositeDataProvider(modelResolver, retrieveProvider);
    }

    private URL pathToModelJar;
    public void setPathToModelJar(URL pathToModelJar) {
        this.pathToModelJar = pathToModelJar;
    }

    public FileBasedFhirProvider withPathToModelJar(URL pathToModelJar) {
        setPathToModelJar(pathToModelJar);
        return this;
    }
}
