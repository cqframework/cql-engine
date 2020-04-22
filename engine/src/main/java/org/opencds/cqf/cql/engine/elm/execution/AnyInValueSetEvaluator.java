package org.opencds.cqf.cql.engine.elm.execution;

import org.opencds.cqf.cql.engine.execution.Context;

public class AnyInValueSetEvaluator extends org.cqframework.cql.elm.execution.AnyInValueSet
{
    @Override
    protected Object internalEvaluate(Context context)
    {
        Object codes = this.getCodes().evaluate(context);
        Object valueset = this.getValueset();

        if (codes == null || valueset == null) return null;

        if (codes instanceof Iterable)
        {
            Object result;
            for (Object code : (Iterable<?>) codes)
            {
                result = InValueSetEvaluator.inValueSet(context, code, valueset);
                if (result instanceof Boolean && (Boolean) result)
                {
                    return true;
                }
            }
        }

        return false;
    }
}
