package org.opencds.cqf.cql.engine.elm.serialization;

import org.opencds.cqf.cql.engine.elm.execution.ExpressionDefEvaluator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, defaultImpl = ExpressionDefEvaluator.class)
public interface ExpressionDefMixin {
}
