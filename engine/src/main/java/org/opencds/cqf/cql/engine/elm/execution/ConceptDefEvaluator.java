package org.opencds.cqf.cql.engine.elm.execution;

import org.cqframework.cql.elm.execution.CodeRef;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Concept;

import java.util.List;
import java.util.stream.Collectors;

public class ConceptDefEvaluator extends org.cqframework.cql.elm.execution.ConceptDef {

    @Override
    protected Object internalEvaluate(Context context) {
        List<Code> codeList = this.getCode().stream()
                .map(CodeRef::getName)
                .map(context::resolveCodeRef)
                .map(codeDef -> codeDef.evaluate(context))
                .map(object -> (Code)object)
                .collect(Collectors.toList());

        return new Concept().withDisplay(this.getDisplay()).withCodes(codeList);
    }
}
