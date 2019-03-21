package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.util.ArrayList;
import java.util.List;

public class ToListEvaluator extends org.cqframework.cql.elm.execution.ToList {

    public static Object toList(Object operand) {
        List<Object> ret = new ArrayList<>();

        if (operand == null) {
            return ret;
        }

        ret.add(operand);
        return ret;
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return toList(operand);
    }
}
