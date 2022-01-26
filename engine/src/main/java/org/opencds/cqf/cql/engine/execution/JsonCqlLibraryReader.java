package org.opencds.cqf.cql.engine.execution;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import org.cqframework.cql.elm.execution.Element;
import org.cqframework.cql.elm.execution.Expression;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.TypeSpecifier;
import org.opencds.cqf.cql.engine.elm.execution.LibraryWrapper;

public class JsonCqlLibraryReader {
    private JsonCqlLibraryReader() {
    }

    public static ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        mapper.registerModule(module);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.setMixInAnnotation(Element.class, org.opencds.cqf.cql.engine.elm.execution.ElementMixin.class);
        simpleModule.setMixInAnnotation(Expression.class, org.opencds.cqf.cql.engine.elm.execution.ExpressionMixin.class);
        simpleModule.setMixInAnnotation(TypeSpecifier.class, org.opencds.cqf.cql.engine.elm.execution.TypeSpecifierMixin.class);
        //simpleModule.setMixInAnnotation(ExpressionDef.class, org.opencds.cqf.cql.elm.execution.ExpressionDefMixin.class);
        mapper.registerModule(simpleModule);
        return mapper;
    }

    public static Library read(File reader) throws IOException {
        return mapper().readValue(reader, LibraryWrapper.class).getLibrary();
    }

    public static Library read(Reader reader) throws IOException {
        return mapper().readValue(reader, LibraryWrapper.class).getLibrary();
    }
}
