package org.opencds.cqf.cql.engine.execution;

import java.util.ArrayList;
import java.util.List;

public class ExpressionResult {
    Object result;
    List<Object> evaluatedResource;

    public ExpressionResult() {
        evaluatedResource = new ArrayList<>();
    }

    public static ExpressionResult newInstance() {
        return new ExpressionResult();
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
        this.evaluatedResource.addAll(evaluatedResource);
        return this;
    }
}
