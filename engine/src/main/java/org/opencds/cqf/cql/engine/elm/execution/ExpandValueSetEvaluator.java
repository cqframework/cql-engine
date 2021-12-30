package org.opencds.cqf.cql.engine.elm.execution;

import org.cqframework.cql.elm.execution.CodeSystemDef;
import org.cqframework.cql.elm.execution.CodeSystemRef;
import org.cqframework.cql.elm.execution.ValueSetDef;
import org.cqframework.cql.elm.execution.ValueSetRef;
import org.opencds.cqf.cql.engine.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Concept;
import org.opencds.cqf.cql.engine.runtime.ValueSet;
import org.opencds.cqf.cql.engine.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;

/*
expand(valueSet ValueSet) List<Code>

The ExpandValueSet function expands the given value set using the terminology provider.
*/

public class ExpandValueSetEvaluator extends org.cqframework.cql.elm.execution.ExpandValueSet {

    public static Object expand(Context context, Object valueset) {
        if (valueset == null) {
            return null;
        }

        if (valueset instanceof ValueSet) {
            TerminologyProvider tp = context.resolveTerminologyProvider();
            return tp.expand(ValueSetInfo.fromValueSet((ValueSet)valueset));
        }

        throw new InvalidOperatorArgument(
            "ExpandValueSet(ValueSet)",
            String.format("ExpandValueSet(%s, %s)", valueset.getClass().getName())
        );
    }

    @Override
    protected Object internalEvaluate(Context context) {
        Object valueset = getOperand().evaluate(context);
        return expand(context, valueset);
    }
}
