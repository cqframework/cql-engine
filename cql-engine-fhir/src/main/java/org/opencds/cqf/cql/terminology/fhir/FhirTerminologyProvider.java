package org.opencds.cqf.cql.terminology.fhir;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.terminology.TerminologyProvider;
import org.opencds.cqf.cql.terminology.ValueSetInfo;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.IdType;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Bryn on 8/15/2016.
 */
public class FhirTerminologyProvider implements TerminologyProvider {

    private String endpoint;
    private FhirContext fhirContext;
    private IGenericClient fhirClient;
    public String getEndpoint() {
        return endpoint;
    }
    public FhirTerminologyProvider setEndpoint(String endpoint, boolean validation) {
        this.endpoint = endpoint;
        fhirContext = FhirContext.forDstu3();
        if (!validation) {
            fhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        }
        fhirClient = fhirContext.newRestfulGenericClient(endpoint);

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
}
