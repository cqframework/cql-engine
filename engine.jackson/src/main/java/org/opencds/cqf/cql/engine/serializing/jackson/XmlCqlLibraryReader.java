package org.opencds.cqf.cql.engine.serializing.jackson;

import org.cqframework.cql.elm.execution.Library;
import org.opencds.cqf.cql.engine.serializing.CqlLibraryReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;

public class XmlCqlLibraryReader implements CqlLibraryReader {

    public XmlCqlLibraryReader() {
    }

    public Library read(File file) throws IOException {
        return XmlCqlMapper.getMapper().readValue(file, Library.class);
    }

    public Library read(URL url) throws IOException {
        return XmlCqlMapper.getMapper().readValue(url, Library.class);
    }

    public Library read(URI uri) throws IOException {
        return XmlCqlMapper.getMapper().readValue(uri.toURL(), Library.class);
    }

    public Library read(String string) throws IOException {
        return XmlCqlMapper.getMapper().readValue(string, Library.class);
    }

    public Library read(InputStream inputStream) throws IOException {
        return XmlCqlMapper.getMapper().readValue(inputStream, Library.class);
    }

    public Library read(Reader reader) throws IOException {
        return XmlCqlMapper.getMapper().readValue(reader, Library.class);
    }
}
