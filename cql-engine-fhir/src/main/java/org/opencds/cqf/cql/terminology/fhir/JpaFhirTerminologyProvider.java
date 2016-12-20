package org.opencds.cqf.cql.terminology.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.dstu3.model.*;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.terminology.TerminologyProvider;
import org.opencds.cqf.cql.terminology.ValueSetInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 12/20/2016.
 */
public class JpaFhirTerminologyProvider implements TerminologyProvider {

    private String endpoint;
    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        fhirContext = FhirContext.forDstu3();
        // TODO: remove this disabling of validation once mFHIR DTS server is active -- 10/4/2016
        fhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        fhirClient = fhirContext.newRestfulGenericClient(endpoint);

        if (userName != null && password != null) {
            BasicAuthInterceptor basicAuth = new BasicAuthInterceptor(userName, password);
            fhirClient.registerInterceptor(basicAuth);
        }
    }
    public JpaFhirTerminologyProvider withEndpoint(String endpoint) {
        setEndpoint(endpoint);
        return this;
    }

    private FhirContext fhirContext;
    private IGenericClient fhirClient;

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

    public JpaFhirTerminologyProvider withBasicAuth(String userName, String password) {
        this.userName = userName;
        this.password = password;
        return this;
    }

    @Override
    public boolean in(Code code, ValueSetInfo valueSet) throws ResourceNotFoundException {
        // Implement as ValueSet/$validate-code
        // http://hl7.org/fhir/2016Sep/valueset-operations.html#validate-code

        // Potential problems:
        // ValueSetInfo void of id --> want .ontype() instead
        Parameters respParam = fhirClient
                .operation()
                .onInstance(new IdType("ValueSet", valueSet.getId()))
                // .onType(ValueSet.class)
                .named("validate-code")
                .withParameter(Parameters.class, "code", new StringType(code.getCode()))
                .andParameter("system", new StringType(code.getSystem()))
                .useHttpGet()
                .execute();
        return ((BooleanType)respParam.getParameter().get(0).getValue()).booleanValue();
    }

    @Override
    public Iterable<Code> expand(ValueSetInfo valueSet) throws ResourceNotFoundException {
        // TODO: Implement as ValueSet/$expand
        // http://hl7.org/fhir/2016Sep/valueset-operations.html#expand
        Parameters respParam = fhirClient
                .operation()
                .onInstance(new IdType("ValueSet", valueSet.getId()))
                .named("expand")
                .withNoParameters(Parameters.class)
                .execute();

        ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
        List<Code> codes = new ArrayList<>();
        for (ValueSet.ConceptReferenceComponent codeInfo : expanded.getCompose().getInclude().get(0).getConcept()) {
            Code nextCode = new Code()
                    .withCode(codeInfo.getCode())
                    .withSystem(expanded.getCompose().getInclude().get(0).getSystem())
                    .withVersion(expanded.getCompose().getInclude().get(0).getVersion())
                    .withDisplay(codeInfo.getDisplay());
            codes.add(nextCode);
        }
        return codes;
    }

    @Override
    public Code lookup(Code code, CodeSystemInfo codeSystem) throws ResourceNotFoundException {
        // TODO: Implement as CodeSystem/$lookup
        // http://hl7.org/fhir/2016Sep/codesystem-operations.html#lookup
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
}
