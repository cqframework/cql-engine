package org.opencds.cqf.cql.engine.execution;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;

import org.hl7.elm.r1.ObjectFactory;

public class TranslatingTestBase {

    public LibraryManager toLibraryManager(Map<org.hl7.elm.r1.VersionedIdentifier, String> libraryText) throws IOException, JAXBException {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);
        libraryManager.getLibrarySourceLoader().registerProvider(new InMemoryLibrarySourceProvider(libraryText));
        return libraryManager;
    }

    public Library toLibrary(String text) throws IOException, JAXBException  {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);

        return this.toLibrary(text, modelManager, libraryManager);
    }

    public Library toLibrary(String text, ModelManager modelManager, LibraryManager libraryManager) throws IOException, JAXBException {
        CqlTranslator translator = CqlTranslator.fromText(text, modelManager, libraryManager);
        return this.readXml(translator.toXml());
    }

    public Library readXml(String xml) throws IOException, JAXBException {
        return CqlLibraryReader.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    public org.hl7.elm.r1.VersionedIdentifier toElmIdentifier(String name, String version) {
        return new org.hl7.elm.r1.VersionedIdentifier().withId(name).withVersion(version);
    }

    public String convertToXml(org.hl7.elm.r1.Library library) throws JAXBException {
        Marshaller marshaller = getJaxbContext().createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(new ObjectFactory().createLibrary(library), writer);
        return writer.getBuffer().toString();
    }

    public static JAXBContext getJaxbContext() throws JAXBException {
        return JAXBContext.newInstance(org.hl7.elm.r1.Library.class, org.hl7.cql_annotations.r1.Annotation.class);
    }
}
