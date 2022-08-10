package org.opencds.cqf.cql.engine.elm.serialization;

import org.opencds.cqf.cql.engine.elm.execution.ExpressionDefEvaluator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ExpressionDefEvaluator.class, name = "ExpressionDef"),
})
public class ElementMixin {
}
