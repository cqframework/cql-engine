package org.opencds.cqf.cql.engine.execution;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EvaluationResult {
    public Map<String, Object> expressionResults;

    public EvaluationResult() {
        this.expressionResults = new LinkedHashMap<>();
    }

    public Object forExpression(String expressionName) {
        return this.expressionResults.get(expressionName);
    }
    
}