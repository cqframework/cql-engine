package org.opencds.cqf.cql.terminology.fhir;

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import org.hl7.fhir.dstu3.model.*;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.terminology.TerminologyProvider;
import org.opencds.cqf.cql.terminology.ValueSetInfo;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Bryn on 8/15/2016.
 */
public class FhirTerminologyProvider implements TerminologyProvider {

    private IClientInterceptor headerInjectionInterceptor;

    public FhirTerminologyProvider withInjectedHeader(String headerKey, String headerValue) {
        this.headerInjectionInterceptor = new HeaderInjectionInterceptor(headerKey, headerValue);
        return this;
    }

    public FhirTerminologyProvider withInjectedHeaders(HashMap<String, String> headers) {
        this.headerInjectionInterceptor = new HeaderInjectionInterceptor(headers);
        return this;
    }

    private IGenericClient fhirClient;
    public IGenericClient getFhirClient() {
        return fhirClient;
    }

    private String endpoint;
    public String getEndpoint() {
        return endpoint;
    }
    public FhirTerminologyProvider setEndpoint(String endpoint, boolean validation) {
        this.endpoint = endpoint;
        FhirContext fhirContext = FhirContext.forDstu3();
        if (!validation) {
            fhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        }
        fhirContext.getRestfulClientFactory().setSocketTimeout(1200 * 10000);
        fhirClient = fhirContext.newRestfulGenericClient(endpoint);

        if (this.headerInjectionInterceptor != null) {
            fhirClient.registerInterceptor(headerInjectionInterceptor);
        }

        if (userName != null && password != null) {
            BasicAuthInterceptor basicAuth = new BasicAuthInterceptor(userName, password);
            fhirClient.registerInterceptor(basicAuth);
        }
        return this;
    }

    // TODO: Obviously don't want to do this, just a quick-fix for now
    private String userName;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public FhirTerminologyProvider withBasicAuth(String userName, String password) {
        this.userName = userName;
        this.password = password;
        return this;
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
