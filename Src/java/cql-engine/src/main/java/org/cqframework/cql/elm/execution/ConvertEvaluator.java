package org.cqframework.cql.elm.execution;

import org.apache.commons.lang3.NotImplementedException;
import org.cqframework.cql.execution.Context;

/*
* The convert operator converts a value to a specific type.
* The result of the operator is the value of the argument converted to the target type, if possible.
* Note that use of this operator may result in a run-time exception being thrown if there is no valid conversion from the actual value to the target type.
*/

/**
 * Created by Bryn on 5/25/2016.
 * Edited by Chris Schuler in 6/15/2016
 */
public class ConvertEvaluator extends Convert {

  private Class resolveType(Context context) {
    if (this.getToTypeSpecifier() != null) {
      return context.resolveType(this.getToTypeSpecifier());
    }

    return context.resolveType(this.getToType());
  }

  @Override
  public Object evaluate(Context context) {

    Object operand = getOperand().evaluate(context);
    if (operand == null) { return null; }

    Class type = resolveType(context);

    try {
      if (type.isInstance(operand)) {
        Class cls = operand.getClass();
        return cls.newInstance();
      }
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    throw new IllegalArgumentException(String.format("Cannot Convert a value of type %s as %s.", operand.getClass().getName(), type.getName()));

    // TODO: Fix this
    //  String packageName = Convert.class.getPackage().getName();
    //  try {
    //      Class toClass = Class.forName(String.format("%s.To%s", packageName, getToType().getLocalPart()));
    //      Expression expresion = (Expression)toClass.newInstance();
    //      Method setOperand = expresion.getClass().getMethod("setOperand",Expression.class);
    //      setOperand.invoke(expresion, getOperand());
    //
    //      return expresion.evaluate(context);
    //  } catch (ClassNotFoundException e) {
    //      e.printStackTrace();
    //  } catch (InstantiationException e) {
    //      e.printStackTrace();
    //  } catch (IllegalAccessException e) {
    //      e.printStackTrace();
    //  } catch (NoSuchMethodException e) {
    //      e.printStackTrace();
    //  } catch (InvocationTargetException e) {
    //      e.printStackTrace();
    //  }
  }
}
