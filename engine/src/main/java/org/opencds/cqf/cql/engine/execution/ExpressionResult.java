package org.opencds.cqf.cql.engine.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExpressionResult {
    Object result;
    List<Object> evaluatedResource;
    UUID id;

    public ExpressionResult() {
        id = UUID.randomUUID();
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

    public UUID getId() {
        return id;
    }
}
