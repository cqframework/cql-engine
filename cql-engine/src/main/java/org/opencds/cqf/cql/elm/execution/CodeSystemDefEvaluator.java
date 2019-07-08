package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.terminology.CodeSystemInfo;

public class CodeSystemDefEvaluator extends org.cqframework.cql.elm.execution.CodeSystemDef {

    @Override
    protected Object internalEvaluate(Context context) {
        return new CodeSystemInfo().withId(getId()).withVersion(getVersion());
    }
}
