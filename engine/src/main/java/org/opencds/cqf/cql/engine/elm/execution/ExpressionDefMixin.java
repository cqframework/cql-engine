package org.opencds.cqf.cql.engine.elm.execution;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
//import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import org.cqframework.cql.elm.execution.*;

@JsonTypeInfo(use = Id.NAME, defaultImpl = ExpressionDefEvaluator.class)
public class ExpressionDefMixin extends ExpressionDef {
}
