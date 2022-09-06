package org.opencds.cqf.cql.engine.execution;

import java.util.List;

public class ExpressionResult {
    Object result;
    List<Object> evaluatedResource;

    public static ExpressionResult newInstance(Object result, List<Object> er) {
        return new ExpressionResult().withResult(result).withEvaluatedResource(er);
    }
    public Object getResult() {
        return result;
    }

    public ExpressionResult withResult(Object result) {
        this.result = result;
        return this;
    }

    public List<Object> getEvaluatedResource() {
        return evaluatedResource;
    }

    public ExpressionResult withEvaluatedResource(List<Object> evaluatedResource) {
        this.evaluatedResource = evaluatedResource;
        return this;
    }
}
