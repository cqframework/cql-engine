package org.opencds.cqf.cql.engine.elm.serialization;

import org.cqframework.cql.elm.execution.Annotation;
import org.cqframework.cql.elm.execution.CqlToElmError;
import org.cqframework.cql.elm.execution.CqlToElmInfo;
import org.cqframework.cql.elm.execution.Locator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = Annotation.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CqlToElmInfo.class, name = "a:CqlToElmInfo"),
        @JsonSubTypes.Type(value = CqlToElmError.class, name = "a:CqlToElmError"),
        @JsonSubTypes.Type(value = Annotation.class, name = "a:Annotation"),
        @JsonSubTypes.Type(value = Locator.class, name = "locator")
})
public interface CqlToElmBaseMixIn {}