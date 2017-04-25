package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.terminology.CodeSystemInfo;

/**
 * Created by Christopher on 4/25/2017.
 */
public class CodeDefEvaluator extends org.cqframework.cql.elm.execution.CodeDef {

    @Override
    public Object evaluate(Context context) {
        CodeSystemInfo info = (CodeSystemInfo) getCodeSystem().evaluate(context);
        return context.resolveTerminologyProvider().lookup(new Code().withCode(this.getId()), info);
    }
}
