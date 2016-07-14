package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

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
  @Override
  public Object evaluate(Context context) {
    Object code = getCode().evaluate(context);
    if (code == null) { return null; }
    Object codeSystem = getCodesystem();

    throw new IllegalArgumentException(code.toString() + " " + codeSystem.toString());
  }
}
