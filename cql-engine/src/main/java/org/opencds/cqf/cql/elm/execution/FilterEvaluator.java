package org.opencds.cqf.cql.elm.execution;

import org.cqframework.cql.elm.execution.Filter;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 6/8/2017.
 */
public class FilterEvaluator extends Filter {

    @Override
    public Object evaluate(Context context) {
        Object source = this.getSource().evaluate(context);

        List<Object> ret = new ArrayList<>();

        if (source == null) {
            ret = null;
        }

        if (source instanceof Iterable) {

            for (Object obj : (List) source) {
                try {
                    // Hmmm... This is hard without the alias.
                    // TODO: verify this works for all cases -> will scope always be present?
                    if (this.scope != null) {
                        context.push(new Variable().withName(this.getScope()).withValue(obj));
                    }

                    Object condition = this.getCondition().evaluate(context);
                    if (condition instanceof Boolean && (Boolean) condition) {
                        ret.add(obj);
                    }
                }
                finally {
                    context.pop();
                }
            }
        }

        return context.logTrace(this.getClass(), ret, source);
    }
}
