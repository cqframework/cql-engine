package org.cqframework.cql.elm.execution;

import org.cqframework.cql.execution.Context;

/**
 * Created by Bryn on 5/25/2016.
 */
public class CodeEvaluator extends Code {
    @Override
    public Object evaluate(Context context) {
        org.cqframework.cql.runtime.Code code = new org.cqframework.cql.runtime.Code().withCode(this.getCode()).withDisplay(this.getDisplay());
        CodeSystemRef codeSystemRef = this.getSystem();
        if (codeSystemRef != null) {
            CodeSystemDef codeSystemDef = context.resolveCodeSystemRef(codeSystemRef.getLibraryName(), codeSystemRef.getName());
            code.setSystem(codeSystemDef.getId());
            code.setVersion(codeSystemDef.getVersion());
        }

        return code;
    }
}
