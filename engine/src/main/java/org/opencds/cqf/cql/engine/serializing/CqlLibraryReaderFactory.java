package org.opencds.cqf.cql.engine.serializing;

import java.util.Iterator;
import java.util.ServiceLoader;

public class CqlLibraryReaderFactory {

    static ServiceLoader<CqlLibraryReaderProvider> loader = ServiceLoader
            .load(CqlLibraryReaderProvider.class);

    public static synchronized Iterator<CqlLibraryReaderProvider> providers(boolean refresh) {
        if (refresh) {
            loader.reload();
        }

        return loader.iterator();
    }

    public static CqlLibraryReader getReader(String contentType) {
        var providers = providers(false);
        if (providers.hasNext()) {
            return providers.next().create(contentType);
        }

        throw new RuntimeException("No ElmLibraryReaderProviders found for " + contentType);
    }
}
