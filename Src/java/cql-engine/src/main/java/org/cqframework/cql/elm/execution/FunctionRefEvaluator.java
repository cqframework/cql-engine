package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.execution.Variable;

import java.util.ArrayList;

/**
 * Created by Bryn on 5/25/2016.
 */
public class FunctionRefEvaluator extends FunctionRef {

    @Override
    public Object evaluate(Context context) {
        ArrayList<Object> arguments = new ArrayList<Object>();
        for (Expression operand : this.getOperand()) {
            arguments.add(operand.evaluate(context));
        }

        FunctionDef functionDef = context.resolveFunctionRef(this.getLibraryName(), this.getName(), arguments);
        context.pushWindow();
        try {
            for (int i = 0; i < arguments.size(); i++) {
                context.push(new Variable().withName(functionDef.getOperand().get(i).getName()).withValue(arguments.get(i)));
            }

            return functionDef.getExpression().evaluate(context);
        }
        finally {
            context.popWindow();
        }
    }
}
