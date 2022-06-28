package org.opencds.cqf.cql.engine.execution;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import org.cqframework.cql.elm.execution.*;
import org.opencds.cqf.cql.engine.elm.execution.*;

public class JsonCqlLibraryReader {
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

    private JsonCqlLibraryReader() {
    }

    public static Library read(File file) throws IOException {
        return mapper.readValue(file, LibraryWrapper.class).getLibrary();
    }

    public static Library read(URL url) throws IOException {
        return mapper.readValue(url, LibraryWrapper.class).getLibrary();
    }

    public static Library read(URI uri) throws IOException {
        return mapper.readValue(uri.toURL(), LibraryWrapper.class).getLibrary();
    }

    public static Library read(String string) throws IOException {
        return mapper.readValue(string, LibraryWrapper.class).getLibrary();
    }

    public static Library read(InputStream inputStream) throws IOException {
        return mapper.readValue(inputStream, LibraryWrapper.class).getLibrary();
    }

    public static Library read(Reader reader) throws IOException {
        return mapper.readValue(reader, LibraryWrapper.class).getLibrary();
    }
}
