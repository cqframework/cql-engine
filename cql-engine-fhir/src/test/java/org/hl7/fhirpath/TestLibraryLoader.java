package org.hl7.fhirpath;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.opencds.cqf.cql.execution.CqlLibraryReader;
import org.opencds.cqf.cql.execution.LibraryLoader;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bryn on 12/29/2016.
 */
public class TestLibraryLoader implements LibraryLoader {

    public TestLibraryLoader(LibraryManager libraryManager) {
        if (libraryManager == null) {
            throw new IllegalArgumentException("libraryManager is null");
        }

        this.libraryManager = libraryManager;
    }

    private LibraryManager libraryManager;

    private Map<String, Library> libraries = new HashMap();

    private Library resolveLibrary(VersionedIdentifier libraryIdentifier) {
        if (libraryIdentifier == null) {
            throw new IllegalArgumentException("Library identifier is null.");
        }

        if (libraryIdentifier.getId() == null) {
            throw new IllegalArgumentException("Library identifier id is null.");
        }

        Library library = libraries.get(libraryIdentifier.getId());
        if (library != null && libraryIdentifier.getVersion() != null && !libraryIdentifier.getVersion().equals(library.getIdentifier().getVersion())) {
            throw new IllegalArgumentException(String.format("Could not load library %s, version %s because version %s is already loaded.",
                    libraryIdentifier.getId(), libraryIdentifier.getVersion(), library.getIdentifier().getVersion()));
        }
        else {
            library = loadLibrary(libraryIdentifier);
            libraries.put(libraryIdentifier.getId(), library);
        }

        return library;
    }

    private Library loadLibrary(VersionedIdentifier libraryIdentifier) {
        List<CqlTranslatorException> errors = new ArrayList();
        org.hl7.elm.r1.VersionedIdentifier identifier = new org.hl7.elm.r1.VersionedIdentifier()
                .withId(libraryIdentifier.getId())
                .withSystem(libraryIdentifier.getSystem())
                .withVersion(libraryIdentifier.getVersion());

        org.cqframework.cql.cql2elm.model.TranslatedLibrary translatedLibrary = libraryManager.resolveLibrary(identifier, errors);

        String xml = null;
        try {
            xml = CqlTranslator.convertToXML(translatedLibrary.getLibrary());
        } catch (JAXBException e) {
            throw new RuntimeException(String.format("Errors encountered while loading library %s: %s", libraryIdentifier.getId(), e.getMessage()));
        }

        Library library = null;
        try {
            library = CqlLibraryReader.read(new StringReader(xml));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Errors encountered while loading library %s: %s", libraryIdentifier.getId(), e.getMessage()));
        } catch (JAXBException e) {
            throw new RuntimeException(String.format("Errors encountered while loading library %s: %s", libraryIdentifier.getId(), e.getMessage()));
        }

        return library;
    }

    @Override
    public Library load(VersionedIdentifier libraryIdentifier) {
        return resolveLibrary(libraryIdentifier);
    }
}
