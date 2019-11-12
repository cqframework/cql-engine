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

	private FhirContext fhirContext;


	public RestFhirRetrieveProvider(FhirContext fhirContext, ISearchParamRegistry searchParamRegistry, String endpoint) {
		super(searchParamRegistry);
		this.endpoint = endpoint;
		this.fhirContext = fhirContext;

		// TODO: Figure out how to validate that the searchParameter register and the context are on the same version of FHIR.
	}

	@Override
	protected Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries) {
		if (queries == null || queries.isEmpty()) {
            return Collections.emptyList();
		}

		IGenericClient client = this.fhirContext.newRestfulGenericClient(this.endpoint);
		
		// No filters for the the dataType.
		List<IBaseBundle> bundles = new ArrayList<IBaseBundle>();
		for (SearchParameterMap map : queries) {
			IBaseBundle bundle = client.search().byUrl(dataType + map.toNormalizedQueryString(this.fhirContext)).execute();
			bundles.add(bundle);
		}

		return new FhirBundlesCursor(client, bundles, dataType);
	}
}
