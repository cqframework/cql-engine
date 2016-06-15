package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Quantity;

import java.math.BigDecimal;

/*
* The is operator allows the type of a result to be tested.
* If the run-time type of the argument is of the type being tested, the result of the operator is true; otherwise, the result is false.
*/

/**
* Created by Chris Schuler on 6/14/2016
*/
public class IsEvaluator extends Is {

  private Class resolveType(Context context) {
      if (this.getIsTypeSpecifier() != null) {
          return context.resolveType(this.getIsTypeSpecifier());
      }

      return context.resolveType(this.getIsType());
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);

    if (operand == null) { return null; }

    Class type = resolveType(context);

    return type.isAssignableFrom(operand.getClass());
  }
}
