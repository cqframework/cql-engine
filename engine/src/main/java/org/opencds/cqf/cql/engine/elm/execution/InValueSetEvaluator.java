package org.opencds.cqf.cql.engine.elm.execution;

import org.cqframework.cql.elm.execution.CodeSystemDef;
import org.cqframework.cql.elm.execution.CodeSystemRef;
import org.cqframework.cql.elm.execution.ValueSetDef;
import org.cqframework.cql.elm.execution.ValueSetRef;
import org.opencds.cqf.cql.engine.exception.InvalidOperatorArgument;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Concept;
import org.opencds.cqf.cql.engine.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;

/*
in(code String, valueset ValueSetRef) Boolean
in(code Code, valueset ValueSetRef) Boolean
in(concept Concept, valueset ValueSetRef) Boolean

The in (Valueset) operators determine whether or not a given code is in a particular valueset.
  Note that these operators can only be invoked by referencing a defined valueset.
For the String overload, if the given valueset contains a code with an equivalent code element, the result is true.
For the Code overload, if the given valueset contains an equivalent code, the result is true.
For the Concept overload, if the given valueset contains a code equivalent to any code in the given concept, the result is true.
If the code argument is null, the result is null.
*/

public class InValueSetEvaluator extends org.cqframework.cql.elm.execution.InValueSet {

    public static Object inValueSet(Context context, Object code, Object valueset) {

        if (code == null) {
            return null;
        }

        ValueSetDef vsd = resolveVSR(context, (ValueSetRef)valueset);
        ValueSetInfo vsi = new ValueSetInfo().withId(vsd.getId()).withVersion(vsd.getVersion());
        for (CodeSystemRef csr : vsd.getCodeSystem())
        {
            CodeSystemDef csd = resolveCSR(context, csr);
            CodeSystemInfo csi = new CodeSystemInfo().withId(csd.getId()).withVersion(csd.getVersion());
            vsi.getCodeSystems().add(csi);
        }

        TerminologyProvider provider = context.resolveTerminologyProvider();

        // perform operation
        if (code instanceof String) {
            if (provider.in(new Code().withCode((String)code), vsi)) {
                return true;
            }
            return false;
        }
        else if (code instanceof Code) {
            if (provider.in((Code)code, vsi)) {
                return true;
            }
            return false;
        }
        else if (code instanceof Concept) {
            for (Code codes : ((Concept)code).getCodes()) {
                if (codes == null) return null;
                if (provider.in(codes, vsi)) return true;
            }
            return false;
        }

        throw new InvalidOperatorArgument(
                "In(String, ValueSetRef), In(Code, ValueSetRef) or In(Concept, ValueSetRef)",
                String.format("In(%s, %s)", code.getClass().getName(), valueset.getClass().getName())
        );
    }

    public static ValueSetDef resolveVSR(Context context, ValueSetRef valueset) {
        return context.resolveValueSetRef(valueset.getLibraryName(), valueset.getName());
    }

    public static CodeSystemDef resolveCSR(Context context, CodeSystemRef codesystem) {
        return context.resolveCodeSystemRef(codesystem.getLibraryName(), codesystem.getName());
    }

    @Override
    protected Object internalEvaluate(Context context) {
        Object code = getCode().evaluate(context);
        Object valueset = getValueset();

        return inValueSet(context, code, valueset);
    }
}
