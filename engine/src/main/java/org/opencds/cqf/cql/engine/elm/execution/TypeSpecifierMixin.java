package org.opencds.cqf.cql.engine.elm.execution;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import org.cqframework.cql.elm.execution.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
    @Type(value = TupleTypeSpecifier.class, name = "TupleTypeSpecifier"), 
    @Type(value = NamedTypeSpecifier.class, name = "NamedTypeSpecifier"),
    @Type(value = ChoiceTypeSpecifier.class, name = "ChoiceTypeSpecifier"),
    @Type(value = ChoiceTypeSpecifier.class, name = "IntervalTypeSpecifier"),
    @Type(value = ListTypeSpecifier.class, name = "ListTypeSpecifier")
  })
public class TypeSpecifierMixin extends TypeSpecifier {
}
