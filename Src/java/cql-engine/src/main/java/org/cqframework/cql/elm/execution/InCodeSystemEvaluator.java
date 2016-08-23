package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

import org.cqframework.cql.elm.execution.CodeSystemRef;
import org.cqframework.cql.elm.execution.CodeSystemDef;

import org.cqframework.cql.runtime.Code;
import org.cqframework.cql.runtime.Concept;

import org.cqframework.cql.terminology.CodeSystemInfo;
import org.cqframework.cql.terminology.TerminologyProvider;
import org.cqframework.cql.terminology.SystemTerminologyProvider;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/*
in(code String, codesystem CodeSystemRef) Boolean
in(code Code, codesystem CodeSystemRef) Boolean
in(concept Concept, codesystem CodeSystemRef) Boolean

The in (Codesystem) operators determine whether or not a given code is in a particular codesystem.
  Note that these operators can only be invoked by referencing a defined codesystem.
For the String overload, if the given code system contains a code with an equivalent code element, the result is true.
For the Code overload, if the given code system contains an equivalent code, the result is true.
For the Concept overload, if the given code system contains a code equivalent to any code in the given concept, the result is true.
If the code argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 7/13/2016
*/
public class InCodeSystemEvaluator extends InCodeSystem {

  public Object inCodeSystem(Context context, Object code, Object codeSystem) {
    if (code == null) { return null; }

    CodeSystemDef csd = resolveCSR(context, (CodeSystemRef)codeSystem);
    CodeSystemInfo csi = new CodeSystemInfo().withId(csd.getId()).withVersion(csd.getVersion());

    context.registerTerminologyProvider(new SystemTerminologyProvider());
    TerminologyProvider provider = context.resolveTerminologyProvider();

    try {
      if (code instanceof String) {
        // TODO: is null the right matcher or will there be an error message?
        return provider.lookup(new Code().withCode((String)code), csi) != null ? true : false;
      }
      else if (code instanceof Code) {
        return provider.lookup((Code)code, csi) != null ? true : false;
      }
      else if (code instanceof Concept) {
        for (Code codes : ((Concept)code).getCodes()) {
          if (provider.lookup(codes, csi) != null) { return true; }
        }
        return false;
      }
    } catch (ResourceNotFoundException e) {
        return false;
      }
    throw new IllegalArgumentException(String.format("Cannot InCodeSystem Code arguments of type '%s'.", code.getClass().getName()));
  }

  @Override
  public Object evaluate(Context context) {
    Object code = getCode().evaluate(context);
    Object codeSystem = getCodesystem();

    return inCodeSystem(context, code, codeSystem);
  }

  public CodeSystemDef resolveCSR(Context context, CodeSystemRef codesystem) {
    // TODO: replace null with codesystem.getLibraryName() when library resolution is implemented
    return context.resolveCodeSystemRef((String)null, codesystem.getName());
  }
}
