package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.terminology.CodeSystemInfo;

/**
 * Created by Christopher on 4/25/2017.
 */
public class CodeSystemDefEvaluator extends org.cqframework.cql.elm.execution.CodeSystemDef {

    @Override
    public Object evaluate(Context context) {
        return new CodeSystemInfo().withId(getId()).withVersion(getVersion());
    }
}
