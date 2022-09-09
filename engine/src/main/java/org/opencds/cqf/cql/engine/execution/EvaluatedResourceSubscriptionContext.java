package org.opencds.cqf.cql.engine.execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EvaluatedResourceSubscriptionContext {
    private Map<UUID, ExpressionResult> subscribers;

    public EvaluatedResourceSubscriptionContext() {
        subscribers = new HashMap<>();
    }

    public void addSubscriber(UUID id, ExpressionResult expressionResult) {
        subscribers.put(id, expressionResult);
    }

    public void removeSubscriber(UUID id) {
        if (subscribers.containsKey(id)) {
            subscribers.remove(id);
        }
    }

    public void notifySubscribers(List<Object> resources) {
        System.out.println("notify subscriber:" + resources);
        subscribers.values().forEach(expressionResult -> {
            System.out.println("Adding to expressionResult: " + expressionResult.getId());
            expressionResult.getEvaluatedResource().addAll(resources);
        });
    }
}
