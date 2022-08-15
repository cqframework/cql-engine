package org.opencds.cqf.cql.engine.serializing.jackson.mixins;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import org.cqframework.cql.elm.execution.ExpressionDef;
import org.opencds.cqf.cql.engine.elm.execution.ExpressionDefEvaluator;

@JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = ExpressionDefEvaluator.class)
@JsonSubTypes({
    @Type(value = ExpressionDefEvaluator.class, name = "ExpressionDef")
    // @Type(value = FunctionDefEvaluator.class, name = "FunctionDef"),
})
public class ExpressionDefMixin extends ExpressionDef {
}
