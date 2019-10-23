package org.opencds.cqf.cql.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.jpa.provider.dstu3.JpaResourceProviderDstu3;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;

public class Dstu3JpaFhirRetrieveProvider extends Dstu3FhirRetrieveProvider {
	
    // need these to access the dao
	private Collection<IResourceProvider> collectionProviders;
	
	public Dstu3JpaFhirRetrieveProvider(Collection<IResourceProvider> providers) {
		this.collectionProviders = providers;
	}

    public synchronized Collection<IResourceProvider> getCollectionProviders() {
        return this.collectionProviders;
    }

	@Override
	protected Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries) {
        if (queries == null || queries.isEmpty()) {
            return Collections.emptyList();
        }

        JpaResourceProviderDstu3<? extends IAnyResource> jpaResProvider = resolveResourceProvider(dataType);

        List<Object> objects = new ArrayList<>();
        for (SearchParameterMap map : queries) {
            objects.addAll(executeQuery(jpaResProvider, map));
        }

        return objects;
    }

    protected Collection<Object> executeQuery(JpaResourceProviderDstu3<? extends IAnyResource> resourceProvider, SearchParameterMap map) {
        IBundleProvider bundleProvider = resourceProvider.getDao().search(map);
        if (bundleProvider.size() == null)
        {
            return resolveResourceList(bundleProvider.getResources(0, 10000));
        }
        if (bundleProvider.size() == 0) {
            return new ArrayList<>();
        }
        List<IBaseResource> resourceList = bundleProvider.getResources(0, bundleProvider.size());
        return resolveResourceList(resourceList);
    }

    public synchronized Collection<Object> resolveResourceList(List<IBaseResource> resourceList) {
        List<Object> ret = new ArrayList<>();
        for (IBaseResource res : resourceList) {
            Class clazz = res.getClass();
            ret.add(clazz.cast(res));
        }
        // ret.addAll(resourceList);
        return ret;
    }

    public synchronized JpaResourceProviderDstu3<? extends IAnyResource> resolveResourceProvider(String datatype) {
        for (IResourceProvider resource : collectionProviders) {
            if (resource.getResourceType().getSimpleName().toLowerCase().equals(datatype.toLowerCase())) {
                return (JpaResourceProviderDstu3<? extends IAnyResource>) resource;
            }
        }
        throw new RuntimeException("Could not find resource provider for type: " + datatype);
	}
}