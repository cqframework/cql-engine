package org.opencds.cqf.cql.engine.serializing;

import java.util.Iterator;
import java.util.ServiceLoader;

public class CqlLibraryReaderFactory {

    static ServiceLoader<CqlLibraryReaderProvider> loader = ServiceLoader
            .load(CqlLibraryReaderProvider.class);

    public static Iterator<CqlLibraryReaderProvider> providers(boolean refresh) {
        if (refresh) {
            loader.reload();
        }
        return loader.iterator();
    }

    public static CqlLibraryReader getReader(String contentType) {
        if (providers(false).hasNext()) {
            return providers(false).next().create(contentType);
        }
        throw new RuntimeException("No ElmLibraryReaderProviders found");
    }
}
