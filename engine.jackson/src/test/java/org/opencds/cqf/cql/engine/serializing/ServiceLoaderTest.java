package org.opencds.cqf.cql.engine.serializing;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.IntStream.range;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.cqframework.cql.cql2elm.LibraryContentType;
import org.testng.annotations.Test;


public class ServiceLoaderTest {
    @Test
    void loaderIsAvailable() {
        assertNotNull(CqlLibraryReaderFactory.getReader(LibraryContentType.JSON.mimeType()));
        assertNotNull(CqlLibraryReaderFactory.getReader(LibraryContentType.XML.mimeType()));
    }

    @Test
    void multiThreadedServiceLoader() {
        var futures = range(0,10)
            .mapToObj(x -> runAsync(this::loadReader));

        allOf(futures.toArray(CompletableFuture[]::new)).join();
    }

    void loadReader() {
        var reader = CqlLibraryReaderFactory.getReader(LibraryContentType.JSON.mimeType());
        try {
            reader.read(JsonCqlLibraryReaderTest.class.getResourceAsStream("EXM108.json"));
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
