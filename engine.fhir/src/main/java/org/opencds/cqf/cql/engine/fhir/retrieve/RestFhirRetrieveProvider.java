package org.opencds.cqf.cql.engine.fhir.retrieve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterMap;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;

import ca.uhn.fhir.rest.client.api.IGenericClient;

public class RestFhirRetrieveProvider extends SearchParamFhirRetrieveProvider {

	protected IGenericClient fhirClient;


	public RestFhirRetrieveProvider(SearchParameterResolver searchParameterResolver, IGenericClient fhirClient) {
		super(searchParameterResolver);
		this.fhirClient = fhirClient;
		// TODO: Figure out how to validate that the searchParameterResolver and the context are on the same version of FHIR.
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

		// TODO: evaluate this lazily in case the engine only needs the first element
		List<Object> objects = new ArrayList<>();
		for (IBaseBundle b : bundles) {
			FhirBundleCursor cursor = new FhirBundleCursor(fhirClient, b, dataType);
			cursor.forEach(objects::add);
		}

		return objects;
	}
}
