package org.opencds.cqf.cql.engine.fhir.terminology;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class Dstu3FhirTerminologyProvider implements TerminologyProvider {

    private IGenericClient fhirClient;

    public Dstu3FhirTerminologyProvider() {
    }

    /**
     *
     * @param fhirClient - an IGenericClient that has endpoint and authentication already defined and set.
     */
    public Dstu3FhirTerminologyProvider(IGenericClient fhirClient) {
        this.fhirClient = fhirClient;
    }

    public void setFhirClient(IGenericClient fhirClient) {
        this.fhirClient = fhirClient;
    }

    public IGenericClient getFhirClient() {
        return fhirClient;
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

        return code.withSystem(codeSystem.getId())
                .withDisplay(((StringType)respParam.getParameter().get(1).getValue()).getValue());
    }

    public Boolean resolveByUrl(ValueSetInfo valueSet) {
        try {
            URL url = new URL(valueSet.getId());
            Bundle searchResults = fhirClient.search().forResource(ValueSet.class).where(ValueSet.URL.matches().value(url.toString())).returnBundle(Bundle.class).execute();
            if (searchResults.hasEntry()) {
                if (searchResults.getEntryFirstRep().hasResource()) {
                    valueSet.setId(searchResults.getEntryFirstRep().getResource().getIdElement().getIdPart());
                }
            }
            else {
                return null;
            }
        } catch (MalformedURLException e) {
            // continue
        }

        return true;
    }
}
