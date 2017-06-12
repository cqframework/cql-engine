package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
is<T>(argument Any) Boolean

The is operator allows the type of a result to be tested.
If the run-time type of the argument is of the type being tested, the result of the operator is true;
  otherwise, the result is false.
*/

/**
* Created by Chris Schuler on 6/14/2016
*/
public class IsEvaluator extends org.cqframework.cql.elm.execution.Is {

    public static Object is(Class type, Object operand) {
        if (operand == null) {
            return null;
        }

        return type.isAssignableFrom(operand.getClass());
    }

  private Class resolveType(Context context) {
      if (this.getIsTypeSpecifier() != null) {
          return context.resolveType(this.getIsTypeSpecifier());
      }

      return context.resolveType(this.getIsType());
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);
    Class type = resolveType(context);

    return context.logTrace(this.getClass(), is(type, operand), operand);
  }
}
