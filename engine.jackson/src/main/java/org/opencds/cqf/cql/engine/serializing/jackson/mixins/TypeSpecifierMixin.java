package org.opencds.cqf.cql.engine.serializing.jackson.mixins;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.cqframework.cql.elm.execution.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = TupleTypeSpecifier.class, name = "TupleTypeSpecifier"),
    @Type(value = NamedTypeSpecifier.class, name = "NamedTypeSpecifier"),
    @Type(value = ChoiceTypeSpecifier.class, name = "ChoiceTypeSpecifier"),
    @Type(value = ChoiceTypeSpecifier.class, name = "IntervalTypeSpecifier"),
    @Type(value = ListTypeSpecifier.class, name = "ListTypeSpecifier"),
    @Type(value = ParameterTypeSpecifier.class, name = "ParameterTypeSpecifier")
  })
public class TypeSpecifierMixin extends TypeSpecifier {
}
