package org.opencds.cqf.cql.engine.serializing.jackson;

import org.cqframework.cql.elm.execution.Library;
import org.opencds.cqf.cql.engine.serializing.CqlLibraryReader;
import org.opencds.cqf.cql.engine.serializing.LibraryWrapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;

public class JsonCqlLibraryReader implements CqlLibraryReader {

    public JsonCqlLibraryReader() {
    }

    public Library read(File file) throws IOException {
        return JsonCqlMapper.getMapper().readValue(file, LibraryWrapper.class).getLibrary();
    }

    public Library read(URL url) throws IOException {
        return JsonCqlMapper.getMapper().readValue(url, LibraryWrapper.class).getLibrary();
    }

    public Library read(URI uri) throws IOException {
        return JsonCqlMapper.getMapper().readValue(uri.toURL(), LibraryWrapper.class).getLibrary();
    }

    public Library read(String string) throws IOException {
        return JsonCqlMapper.getMapper().readValue(string, LibraryWrapper.class).getLibrary();
    }

    public Library read(InputStream inputStream) throws IOException {
        return JsonCqlMapper.getMapper().readValue(inputStream, LibraryWrapper.class).getLibrary();
    }

    public Library read(Reader reader) throws IOException {
        return JsonCqlMapper.getMapper().readValue(reader, LibraryWrapper.class).getLibrary();
    }
}
