package org.opencds.cqf.cql.engine.execution;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.model.serialization.LibraryWrapper;

import org.cqframework.cql.elm.execution.*;
import org.hl7.cql_annotations.r1.CqlToElmBase;
import org.opencds.cqf.cql.engine.elm.execution.*;

public class TranslatingTestBase {

    public LibraryManager toLibraryManager(Map<org.hl7.elm.r1.VersionedIdentifier, String> libraryText) throws IOException {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);
        libraryManager.getLibrarySourceLoader().registerProvider(new InMemoryLibrarySourceProvider(libraryText));
        return libraryManager;
    }

    public Library toLibrary(String text) throws IOException  {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);

        return this.toLibrary(text, modelManager, libraryManager);
    }

    public Library toLibrary(String text, ModelManager modelManager, LibraryManager libraryManager) throws IOException {
        CqlTranslator translator = CqlTranslator.fromText(text, modelManager, libraryManager);
        return this.readJson(translator.toJson());
    }

    public Library readJson(String json) throws IOException {
        return JsonCqlLibraryReader.read(new StringReader(json));
    }

    public org.hl7.elm.r1.VersionedIdentifier toElmIdentifier(String name, String version) {
        return new org.hl7.elm.r1.VersionedIdentifier().withId(name).withVersion(version);
    }

    public String convertToJson(org.hl7.elm.r1.Library library) throws JsonProcessingException {
        LibraryWrapper wrapper = new LibraryWrapper();
        wrapper.setLibrary(library);

        ObjectMapper mapper = JsonMapper.builder()
            .defaultMergeable(true)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
            .defaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
            .addModule(new JaxbAnnotationModule())
            .addMixIn(CqlToElmBase.class, CqlToElmBaseMixIn.class)
            .build();

        return mapper.writeValueAsString(wrapper);
    }
}
