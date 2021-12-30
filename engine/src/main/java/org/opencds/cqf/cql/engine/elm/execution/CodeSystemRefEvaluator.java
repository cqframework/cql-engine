package org.opencds.cqf.cql.engine.elm.execution;

import org.cqframework.cql.elm.execution.CodeSystemDef;
import org.cqframework.cql.elm.execution.CodeSystemRef;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.CodeSystem;

// References a code system by its previously defined name

public class CodeSystemRefEvaluator extends org.cqframework.cql.elm.execution.CodeSystemRef {

    public static CodeSystem toCodeSystem(Context context, CodeSystemRef csr) {
        CodeSystemDef csd = context.resolveCodeSystemRef(csr.getLibraryName(), csr.getName());
        return new CodeSystem().withId(csd.getId()).withVersion(csd.getVersion()).withName(csd.getName());
    }

    @Override
    protected Object internalEvaluate(Context context) {
        return toCodeSystem(context, this);
    }
}
