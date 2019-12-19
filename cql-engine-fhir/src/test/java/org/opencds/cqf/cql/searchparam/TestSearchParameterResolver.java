package org.opencds.cqf.cql.searchparam;

import static org.junit.Assert.assertNull;

import org.testng.annotations.Test;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryDstu3;


public class TestSearchParameterResolver {
    @Test
    public void testReturnsNullForNullPath() {
        SearchParameterResolver resolver = new SearchParameterResolver(new SearchParamRegistryDstu3());

        RuntimeSearchParam param = resolver.getSearchParameterDefinition("Patient", null);
        assertNull(param);
    }

    @Test
    public void testReturnsNullForNullDataType() {
        SearchParameterResolver resolver = new SearchParameterResolver(new SearchParamRegistryDstu3());

        RuntimeSearchParam param = resolver.getSearchParameterDefinition(null, "code");
        assertNull(param);
    }

    // TODO: Create mock search parameter provider to populate examples of search parameters.
}
