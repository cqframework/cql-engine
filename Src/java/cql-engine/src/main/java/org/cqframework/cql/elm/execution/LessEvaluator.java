package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;
import org.cqframework.cql.runtime.Value;

/**
 * Created by Bryn on 5/25/2016 (v1), edited by Chris Schuler on 6/28/2016 (v2)
 */
public class LessEvaluator extends Less {

  public static Object less(Object left, Object right) {
    if (left == null || right == null) {
        return null;
    }

    return Value.compareTo(left, right, "<");
  }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return less(left, right);
    }
}
