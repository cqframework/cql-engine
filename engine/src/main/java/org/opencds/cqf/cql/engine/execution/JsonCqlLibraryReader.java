package org.opencds.cqf.cql.engine.execution;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;

import org.cqframework.cql.elm.execution.CodeSystemRef;
import org.cqframework.cql.elm.execution.CqlToElmBase;
import org.cqframework.cql.elm.execution.Element;
import org.cqframework.cql.elm.execution.Expression;
import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.TypeSpecifier;
import org.opencds.cqf.cql.engine.elm.serialization.CodeSystemRefMixin;
import org.opencds.cqf.cql.engine.elm.serialization.CqlToElmBaseMixIn;
import org.opencds.cqf.cql.engine.elm.serialization.ElementMixin;
import org.opencds.cqf.cql.engine.elm.serialization.ExpressionDefMixin;
import org.opencds.cqf.cql.engine.elm.serialization.ExpressionMixin;
import org.opencds.cqf.cql.engine.elm.serialization.LibraryMixin;
import org.opencds.cqf.cql.engine.elm.serialization.TypeSpecifierMixin;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class JsonCqlLibraryReader {
    private static final JsonMapper mapper = JsonMapper.builder()
        .defaultMergeable(true)
        .enable(SerializationFeature.INDENT_OUTPUT)
        .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        .enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
        .addModule(new JaxbAnnotationModule())
        .addMixIn(Library.class, LibraryMixin.class)
        // The ordering here of the mix ins for
        // ExpressionDef -> CodeSystemRef ->  Expression -> Element matters,
        // so the mix-ins match most specific to least
        .addMixIn(ExpressionDef.class, ExpressionDefMixin.class)
        .addMixIn(CodeSystemRef.class, CodeSystemRefMixin.class)
        .addMixIn(Expression.class, ExpressionMixin.class)
        .addMixIn(TypeSpecifier.class, TypeSpecifierMixin.class)
        .addMixIn(CqlToElmBase.class, CqlToElmBaseMixIn.class)
        .addMixIn(Element.class, ElementMixin.class)
        .build();

    private JsonCqlLibraryReader() {
    }

    public static Library read(File file) throws IOException {
        return mapper.readValue(file, Library.class);
    }

    public static Library read(URL url) throws IOException {
        return mapper.readValue(url, Library.class);
    }

    public static Library read(URI uri) throws IOException {
        return mapper.readValue(uri.toURL(), Library.class);
    }

    public static Library read(String string) throws IOException {
        return mapper.readValue(string, Library.class);
    }

    public static Library read(InputStream inputStream) throws IOException {
        return mapper.readValue(inputStream, Library.class);
    }

    public static Library read(Reader reader) throws IOException {
        return mapper.readValue(reader, Library.class);
    }
}
