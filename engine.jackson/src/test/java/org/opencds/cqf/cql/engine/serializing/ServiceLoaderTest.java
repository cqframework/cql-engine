package org.opencds.cqf.cql.engine.serializing;

import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
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
        var futures = new ArrayList<CompletableFuture<Void>>();
        for (int i = 0; i < 10; i++) {
            futures.add(CompletableFuture.runAsync(this::loadReader));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
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
