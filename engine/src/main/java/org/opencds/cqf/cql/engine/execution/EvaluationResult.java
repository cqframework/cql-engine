package org.opencds.cqf.cql.engine.execution;

import java.util.HashMap;
import java.util.Map;

public class EvaluationResult {
    public Map<String, Object> expressionResults;

    public EvaluationResult() {
        this.expressionResults = new HashMap<>();
    }

    public Object forExpression(String expressionName) {
        return this.expressionResults.get(expressionName);
    }
    
}