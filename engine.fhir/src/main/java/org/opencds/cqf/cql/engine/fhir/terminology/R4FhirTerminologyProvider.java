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

        return code.withSystem(codeSystem.getId())
                .withDisplay(display != null ? display.getValue() : null );
    }

    public Boolean resolveByUrl(ValueSetInfo valueSet) {
        if (valueSet.getVersion() != null
                || (valueSet.getCodeSystems() != null && valueSet.getCodeSystems().size() > 0)) {
            if (!(valueSet.getCodeSystems().size() == 1 && valueSet.getCodeSystems().get(0).getVersion() == null)) {
                throw new UnsupportedOperationException(String.format(
                        "Could not expand value set %s; version and code system bindings are not supported at this time.",
                        valueSet.getId()));
            }
        }
        
        if (valueSet.getId().startsWith("urn:oid:")) {
            valueSet.setId(valueSet.getId().replace("urn:oid:", ""));
        } else if (valueSet.getId().startsWith("http:") || valueSet.getId().startsWith("https:")) {
            Bundle searchResults = fhirClient.search().forResource(ValueSet.class)
                    .where(ValueSet.URL.matches().value(valueSet.getId())).returnBundle(Bundle.class).execute();
            if (searchResults.isEmpty()) {
                throw new IllegalArgumentException(String.format("Could not resolve value set %s.", valueSet.getId()));
            } else if (searchResults.getEntry().size() == 1) {
                valueSet.setId(searchResults.getEntryFirstRep().getResource().getId());
            } else {
                throw new IllegalArgumentException("Found more than 1 ValueSet with url: " + valueSet.getId());
            }
        }

        return true;
    }
}
