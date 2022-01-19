package org.opencds.cqf.cql.engine.fhir.terminology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class R4FhirTerminologyProvider implements TerminologyProvider {

    private static final String URN_UUID = "urn:uuid:";
    private static final String URN_OID = "urn:oid:";

    private IGenericClient fhirClient;

    public R4FhirTerminologyProvider() { }

    /**
     *
     * @param fhirClient - an IGenericClient that has endpoint and authentication already defined and set.
     */
    public R4FhirTerminologyProvider(IGenericClient fhirClient) {
        this.fhirClient = fhirClient;
    }

    public IGenericClient getFhirClient() {
        return this.fhirClient;
    }

    @Override
    public boolean in(Code code, ValueSetInfo valueSet) throws ResourceNotFoundException {
        // Potential problems:
        // ValueSetInfo void of id --> want .ontype() instead
        if (resolveByUrl(valueSet) == null) {
            return false;
        }

        Parameters respParam;
        if (code.getSystem() != null) {
            respParam = fhirClient
                    .operation()
                    .onInstance(new IdType("ValueSet", valueSet.getId()))
                    // .onType(ValueSet.class)
                    .named("validate-code")
                    .withParameter(Parameters.class, "code", new StringType(code.getCode()))
                    .andParameter("system", new StringType(code.getSystem()))
                    .useHttpGet()
                    .execute();
        }
        else {
            respParam = fhirClient
                    .operation()
                    .onInstance(new IdType("ValueSet", valueSet.getId()))
                    // .onType(ValueSet.class)
                    .named("validate-code")
                    .withParameter(Parameters.class, "code", new StringType(code.getCode()))
                    .useHttpGet()
                    .execute();
        }
        return ((BooleanType)respParam.getParameter().get(0).getValue()).booleanValue();
    }

    @Override
    public Iterable<Code> expand(ValueSetInfo valueSet) throws ResourceNotFoundException {
        if (resolveByUrl(valueSet) == null) {
            return Collections.emptyList();
        }
        Parameters respParam = fhirClient
                .operation()
                .onInstance(new IdType("ValueSet", valueSet.getId()))
                .named("expand")
                .withNoParameters(Parameters.class)
                .useHttpGet()
                .execute();

        ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
        List<Code> codes = new ArrayList<>();
        for (ValueSet.ValueSetExpansionContainsComponent codeInfo : expanded.getExpansion().getContains()) {
            Code nextCode = new Code()
                    .withCode(codeInfo.getCode())
                    .withSystem(codeInfo.getSystem())
                    .withVersion(codeInfo.getVersion())
                    .withDisplay(codeInfo.getDisplay());
            codes.add(nextCode);
        }
        return codes;
    }

    @Override
    public Code lookup(Code code, CodeSystemInfo codeSystem) throws ResourceNotFoundException {
        Parameters respParam = fhirClient
                .operation()
                .onType(CodeSystem.class)
                .named("lookup")
                .withParameter(Parameters.class, "code", new CodeType(code.getCode()))
                .andParameter("system", new UriType(codeSystem.getId()))
                .execute();

        StringType display = (StringType) respParam.getParameter("display");
        if( display != null ) {
        	code.withDisplay( display.getValue() );
        }

        return code.withSystem(codeSystem.getId());
    }

    public Boolean resolveByUrl(ValueSetInfo valueSet) {
        if (valueSet.getVersion() != null
                || (valueSet.getCodeSystems() != null && valueSet.getCodeSystems().size() > 0)) {
            throw new UnsupportedOperationException(String.format(
                    "Could not expand value set %s; version and code system bindings are not supported at this time.",
                    valueSet.getId()));
        }

        // https://github.com/DBCG/cql_engine/pull/462 - Use a search path of URL, identifier, and then resource id
        Bundle searchResults = fhirClient.search().forResource(ValueSet.class)
                .where(ValueSet.URL.matches().value(valueSet.getId())).returnBundle(Bundle.class).execute();
        if( ! searchResults.hasEntry() ) {
            searchResults = fhirClient.search().forResource(ValueSet.class)
                .where(ValueSet.IDENTIFIER.exactly().code(valueSet.getId())).returnBundle(Bundle.class).execute();
            if( ! searchResults.hasEntry() ) {
                String id = valueSet.getId();
                if( id.startsWith(URN_OID) ) {
                    id = id.replace(URN_OID, "");
                } else if( id.startsWith(URN_UUID)) {
                    id = id.replace(URN_UUID, "");
                }

                searchResults = new Bundle();
                // If we reached this point and it looks like it might
                // be a FHIR resource ID, we will try to read it.
                // See https://www.hl7.org/fhir/datatypes.html#id
                if( id.matches("[A-Za-z0-9\\-\\.]{1,64}") ) {
                    try {
                        ValueSet vs = fhirClient.read().resource(ValueSet.class).withId(id).execute();
                        searchResults.addEntry().setResource(vs);
                    } catch( ResourceNotFoundException rnfe ) {
                        // intentionally empty
                    }
                }
            }
        }

        if (!searchResults.hasEntry()) {
            throw new IllegalArgumentException(String.format("Could not resolve value set %s.", valueSet.getId()));
        } else if (searchResults.getEntry().size() == 1) {
            valueSet.setId(searchResults.getEntryFirstRep().getResource().getIdElement().getIdPart());
        } else {
            throw new IllegalArgumentException("Found more than 1 ValueSet with url: " + valueSet.getId());
        }

        return true;
    }
}
