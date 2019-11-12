package org.opencds.cqf.cql.retrieve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class RestFhirRetrieveProvider extends SearchParamFhirRetrieveProvider {

	protected String endpoint;
	IGenericClient client;


	public RestFhirRetrieveProvider(FhirContext fhirContext, ISearchParamRegistry searchParamRegistry, String endpoint) {
		super(fhirContext, searchParamRegistry);
		this.endpoint = endpoint;
		this.client = this.fhirContext.newRestfulGenericClient(this.endpoint);
	}

	@Override
	protected Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries) {
		if (queries == null || queries.isEmpty()) {
            return Collections.emptyList();
		}
		
		// No filters for the the dataType.
		List<IBaseBundle> bundles = new ArrayList<IBaseBundle>();
		for (SearchParameterMap map : queries) {
			IBaseBundle bundle = this.client.search().byUrl(dataType + map.toNormalizedQueryString(this.fhirContext)).execute();
			bundles.add(bundle);
		}

		return new FhirBundlesCursor(client, bundles, dataType);
	}
}
