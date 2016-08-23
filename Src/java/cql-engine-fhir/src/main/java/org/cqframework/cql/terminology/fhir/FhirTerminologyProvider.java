package org.cqframework.cql.terminology.fhir;

import org.cqframework.cql.runtime.Code;
import org.cqframework.cql.terminology.CodeSystemInfo;
import org.cqframework.cql.terminology.TerminologyProvider;
import org.cqframework.cql.terminology.ValueSetInfo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;

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
    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        fhirContext = FhirContext.forDstu3();
        fhirClient = fhirContext.newRestfulGenericClient(endpoint);
    }
    public FhirTerminologyProvider withEndpoint(String endpoint) {
        setEndpoint(endpoint);
        return this;
    }

    private FhirContext fhirContext;
    private IGenericClient fhirClient;

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
