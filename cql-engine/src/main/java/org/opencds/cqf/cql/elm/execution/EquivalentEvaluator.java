package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Value;

/**
 * Created by Bryn on 5/25/2016.
 */
public class EquivalentEvaluator extends org.cqframework.cql.elm.execution.Equivalent {

    public static Boolean equivalent(Object left, Object right) {
        return Value.similar(left, right, Value.SimilarityMode.EQUIVALENT);
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), equivalent(left, right), left, right);
    }
}
