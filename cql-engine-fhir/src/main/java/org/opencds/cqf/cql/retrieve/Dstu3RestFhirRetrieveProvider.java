package org.opencds.cqf.cql.retrieve;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;

public class Dstu3RestFhirRetrieveProvider extends Dstu3FhirRetrieveProvider {

	protected String endpoint;
	protected IGenericClient fhirClient;

	public Dstu3RestFhirRetrieveProvider(FhirContext fhirContext, String endpoint) {
        this.endpoint = endpoint;
        this.fhirClient = fhirContext.newRestfulGenericClient(endpoint);
	}

	@Override
	protected Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries) {
		IQuery<IBaseBundle> search = fhirClient.search().forResource(dataType);
		if (queries.size() > 0) {
			search = fhirClient.search().byUrl(String.format("%s?%s", dataType, queries.toString()));
		}
		else {
			search = fhirClient.search().byUrl(String.format("%s", dataType));
		}

		org.hl7.fhir.instance.model.Bundle results = cleanEntry(search.returnBundle(org.hl7.fhir.instance.model.Bundle.class).execute(), dataType);

		return new FhirBundleCursor(fhirClient, results);
	}

	private org.hl7.fhir.instance.model.Bundle cleanEntry(org.hl7.fhir.instance.model.Bundle bundle, String dataType) {
		org.hl7.fhir.instance.model.Bundle cleanBundle = new org.hl7.fhir.instance.model.Bundle();
		for (org.hl7.fhir.instance.model.Bundle.BundleEntryComponent comp : bundle.getEntry()){
			if (comp.getResource().getResourceType().name().equals(dataType)) {
				cleanBundle.addEntry(comp);
			}
		}

		return cleanBundle;
	}
}