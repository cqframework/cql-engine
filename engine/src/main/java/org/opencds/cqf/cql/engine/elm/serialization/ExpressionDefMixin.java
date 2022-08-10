package org.opencds.cqf.cql.engine.elm.serialization;

import org.opencds.cqf.cql.engine.elm.execution.ExpressionDefEvaluator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = ExpressionDefEvaluator.class)
@JsonSubTypes({
    @Type(value = ExpressionDefEvaluator.class, name = "ExpressionDef")
    // @Type(value = FunctionDefEvaluator.class, name = "FunctionDef"),
})
public interface ExpressionDefMixin {
}
