package org.opencds.cqf.cql.execution;

import java.util.HashMap;
import java.util.Map;

public class LibraryResult {
    public Map<String, Object> expressionResults;

    public LibraryResult() {
        this.expressionResults = new HashMap<>();
    }

    public Object forExpression(String expressionName) {
        return this.expressionResults.get(expressionName);
    }
    
}