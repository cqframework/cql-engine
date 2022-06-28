package org.opencds.cqf.cql.engine.serializing.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.cqframework.cql.elm.execution.CqlToElmBase;
import org.cqframework.cql.elm.execution.Element;
import org.cqframework.cql.elm.execution.Expression;
import org.cqframework.cql.elm.execution.TypeSpecifier;
import org.opencds.cqf.cql.engine.elm.execution.*;
import org.opencds.cqf.cql.engine.serializing.jackson.mixins.*;

public class JsonCqlMapper {
    private static final JsonMapper mapper = JsonMapper.builder()
        .defaultMergeable(true)
        .enable(SerializationFeature.INDENT_OUTPUT)
        .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        .enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
        .addModule(new JaxbAnnotationModule())
        .addMixIn(Element.class, ElementMixin.class)
        .addMixIn(Expression.class, ExpressionMixin.class)
        .addMixIn(TypeSpecifier.class, TypeSpecifierMixin.class)
        .addMixIn(CqlToElmBase.class, CqlToElmBaseMixIn.class)
        .addMixIn(Executable.class, ExecutableMixin.class)
        .build();

    public static JsonMapper getMapper() {
        return mapper;
    }
}
