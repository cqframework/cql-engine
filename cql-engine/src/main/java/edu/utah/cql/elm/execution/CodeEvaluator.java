package edu.utah.cql.elm.execution;

import edu.utah.cql.execution.Context;

/*
structured type Code
{
  code String,
  display String,
  system String,
  version String
}

The Code type represents single terminology codes within CQL.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class CodeEvaluator extends org.cqframework.cql.elm.execution.Code {
    @Override
    public Object evaluate(Context context) {
        edu.utah.cql.runtime.Code code = new edu.utah.cql.runtime.Code().withCode(this.getCode()).withDisplay(this.getDisplay());
        org.cqframework.cql.elm.execution.CodeSystemRef codeSystemRef = this.getSystem();
        if (codeSystemRef != null) {
            org.cqframework.cql.elm.execution.CodeSystemDef codeSystemDef = context.resolveCodeSystemRef(codeSystemRef.getLibraryName(), codeSystemRef.getName());
            code.setSystem(codeSystemDef.getId());
            code.setVersion(codeSystemDef.getVersion());
        }

        return code;
    }
}
