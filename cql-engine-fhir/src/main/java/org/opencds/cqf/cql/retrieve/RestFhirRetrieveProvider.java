package org.opencds.cqf.cql.retrieve;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.Bundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class RestFhirRetrieveProvider extends FhirRetrieveProvider {

	protected String endpoint;
	protected IGenericClient fhirClient;
	protected FhirContext fhirContext;

	public RestFhirRetrieveProvider(FhirContext fhirContext, String endpoint) {
		this.endpoint = endpoint;
		this.fhirContext = fhirContext;
        this.fhirClient = fhirContext.newRestfulGenericClient(endpoint);
	}

	@Override
	protected Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries) {
		// No filters for the the dataType.
		List<Bundle> bundles = new ArrayList<Bundle>();
		if (queries.size() == 0) {
			bundles.add(
				this.fhirClient.search().forResource(dataType)
					.returnBundle(org.hl7.fhir.instance.model.Bundle.class).execute());
		}
		else {
			for (SearchParameterMap map : queries) {
				Bundle bundle = this.fhirClient.search().byUrl(dataType + map.toNormalizedQueryString(this.fhirContext))
					.returnBundle(org.hl7.fhir.instance.model.Bundle.class).execute();
				bundles.add(bundle);
			}
		}

		return new FhirBundlesCursor(fhirClient, bundles, dataType);
	}
}
