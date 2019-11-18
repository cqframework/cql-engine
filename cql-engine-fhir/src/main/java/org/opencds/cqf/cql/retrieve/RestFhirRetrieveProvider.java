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

	protected IGenericClient fhirClient;


	public RestFhirRetrieveProvider(ISearchParamRegistry searchParamRegistry, IGenericClient fhirClient) {
		super(searchParamRegistry);
		this.fhirClient = fhirClient;
		// TODO: Figure out how to validate that the searchParameter registry and the context are on the same version of FHIR.
	}

	@Override
	protected Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries) {
		if (queries == null || queries.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<IBaseBundle> bundles = new ArrayList<IBaseBundle>();
		for (SearchParameterMap map : queries) {
			IBaseBundle bundle = this.fhirClient.search().byUrl(dataType + map.toNormalizedQueryString(this.fhirClient.getFhirContext())).execute();
			bundles.add(bundle);
		}

		return new FhirBundlesCursor(this.fhirClient, bundles, dataType);
	}
}
