package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/*
in(code String, valueset ValueSetRef) Boolean
in(code Code, valueset ValueSetRef) Boolean
in(concept Concept, valueset ValueSetRef) Boolean

The in (Valueset) operators determine whether or not a given code is in a particular valueset.
  Note that these operators can only be invoked by referencing a defined valueset.
For the String overload, if the given valueset contains a code with an equivalent code element, the result is true.
For the Code overload, if the given valueset contains an equivalent code, the result is true.
For the Concept overload, if the given valueset contains a code equivalent to any code in the given concept, the result is true.
If the code argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 7/13/2016
*/
public class InValueSetEvaluator extends InValueSet {

  @Override
  public Object evaluate(Context context) {
    Object code = getCode().evaluate(context);
    if (code == null) { return null; }
    Object valueset = getValueset();

    throw new IllegalArgumentException(code.toString() + " " + valueset.toString());
  }
}
