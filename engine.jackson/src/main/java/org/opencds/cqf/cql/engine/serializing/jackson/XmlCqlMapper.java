package org.opencds.cqf.cql.engine.serializing.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.cqframework.cql.elm.execution.CqlToElmBase;
import org.cqframework.cql.elm.execution.Element;
import org.cqframework.cql.elm.execution.Expression;
import org.cqframework.cql.elm.execution.TypeSpecifier;
import org.opencds.cqf.cql.engine.elm.execution.Executable;
import org.opencds.cqf.cql.engine.serializing.jackson.mixins.*;

public class XmlCqlMapper {
    private static final XmlMapper mapper = XmlMapper.builder()
            .defaultUseWrapper(true)
            .defaultMergeable(true)
            .enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
            .enable(ToXmlGenerator.Feature.WRITE_XML_1_1)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
            .defaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
            .addModule(new JaxbAnnotationModule())
            .addMixIn(Element.class, ElementMixin.class)
            .addMixIn(Expression.class, ExpressionMixin.class)
            .addMixIn(TypeSpecifier.class, TypeSpecifierMixin.class)
            .addMixIn(CqlToElmBase.class, CqlToElmBaseMixIn.class)
            .addMixIn(Executable.class, ExecutableMixin.class)
            .build();

    public static XmlMapper getMapper() {
        return mapper;
    }
}
