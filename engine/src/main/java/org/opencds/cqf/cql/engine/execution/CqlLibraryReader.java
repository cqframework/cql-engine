package org.opencds.cqf.cql.engine.execution;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.ObjectFactory;
import org.opencds.cqf.cql.engine.elm.execution.ObjectFactoryEx;
import org.opencds.cqf.cql.engine.exception.CqlException;

public class CqlLibraryReader {

    private static JAXBContext context;
    private static Unmarshaller unmarshaller;

    // Performance enhancement additions ~ start
    public static Unmarshaller getUnmarshaller() throws JAXBException {
        // This is supposed to work based on this link:
        // https://jaxb.java.net/2.2.11/docs/ch03.html#compiling-xml-schema-adding-behaviors
        // Override the unmarshal to use the XXXEvaluator classes
        // This doesn't work exactly how it's described in the link above, but this is functional
        if (context == null)
        {
            context = JAXBContext.newInstance(ObjectFactory.class);
        }

        if (unmarshaller == null) {
            unmarshaller = context.createUnmarshaller();
            try {
                // https://bugs.eclipse.org/bugs/show_bug.cgi?id=406032
                //https://javaee.github.io/jaxb-v2/doc/user-guide/ch03.html#compiling-xml-schema-adding-behaviors
                // for jre environment
                unmarshaller.setProperty("com.sun.xml.bind.ObjectFactory", new ObjectFactoryEx());
            } catch (javax.xml.bind.PropertyException e) {
                // for jdk environment
                unmarshaller.setProperty("com.sun.xml.internal.bind.ObjectFactory", new ObjectFactoryEx());
            }
        }

        return unmarshaller;
    }

    public static Library read(Unmarshaller u, File file) throws IOException, JAXBException {
        return read(u, toSource(file));
    }

    public static Library read(Unmarshaller u, URL url) throws IOException, JAXBException {
        return read(u, toSource(url));
    }

    public static Library read(Unmarshaller u, URI uri) throws IOException, JAXBException {
        return read(u, toSource(uri));
    }

    public static Library read(Unmarshaller u, String string) throws IOException, JAXBException {
        return read(u, toSource(string));
    }

    public static Library read(Unmarshaller u, InputStream inputStream) throws IOException, JAXBException {
        return read(u, toSource(inputStream));
    }

    public static Library read(Unmarshaller u, Reader reader) throws IOException, JAXBException {
        return read(u, toSource(reader));
    }

    @SuppressWarnings("unchecked")
    public static Library read(Unmarshaller u, Source source) throws JAXBException {
        Object result = u.unmarshal(source);
        return ((JAXBElement<Library>)result).getValue();
    }
    // Performance enhancement additions ~ end

    public static Library read(File file) throws IOException, JAXBException {
        return read(toSource(file));
    }

    public static Library read(URL url) throws IOException, JAXBException {
        return read(toSource(url));
    }

    public static Library read(URI uri) throws IOException, JAXBException {
        return read(toSource(uri));
    }

    public static Library read(String string) throws IOException, JAXBException {
        return read(toSource(string));
    }

    public static Library read(InputStream inputStream) throws IOException, JAXBException {
        return read(toSource(inputStream));
    }

    public static Library read(Reader reader) throws IOException, JAXBException {
        return read(toSource(reader));
    }

    @SuppressWarnings("unchecked")
    public static Library read(Source source) throws JAXBException {
        Unmarshaller u = getUnmarshaller();
        Object result = u.unmarshal(source);
        return ((JAXBElement<Library>)result).getValue();
    }

    /**
     * Creates {@link Source} from various XML representation.
     */
    private static Source toSource(Object xml) throws IOException {
        if (xml == null)
            throw new CqlException("no XML is given");

        if (xml instanceof String) {
            try {
                xml = new URI((String)xml);
            } catch (URISyntaxException e) {
                xml = new File((String)xml);
            }
        }

        if (xml instanceof File) {
            return new StreamSource((File)xml);
        }

        if (xml instanceof URI) {
            xml = ((URI)xml).toURL();
        }

        if (xml instanceof URL) {
            return new StreamSource(((URL)xml).toExternalForm());
        }

        if (xml instanceof InputStream) {
            return new StreamSource((InputStream)xml);
        }

        if (xml instanceof Reader) {
            return new StreamSource((Reader)xml);
        }

        if (xml instanceof Source) {
            return (Source)xml;
        }

        throw new CqlException(String.format("Could not determine access path for input of type %s.", xml.getClass()));
    }
}
