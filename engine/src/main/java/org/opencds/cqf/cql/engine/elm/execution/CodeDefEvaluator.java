package org.opencds.cqf.cql.engine.elm.execution;

import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.terminology.CodeSystemInfo;

public class CodeDefEvaluator extends org.cqframework.cql.elm.execution.CodeDef {

    @Override
    protected Object internalEvaluate(Context context) {
        CodeSystemInfo info = (CodeSystemInfo) getCodeSystem().evaluate(context);
        return new Code().withCode(this.getId()).withSystem(info.getId()).withDisplay(this.getDisplay());
    }
}
