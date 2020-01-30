package org.opencds.cqf.cql.data;

import org.opencds.cqf.cql.data.SystemDataProvider;
import org.opencds.cqf.cql.runtime.*;
import org.testng.annotations.Test;

import static org.junit.Assert.assertNull;

public class SystemDataProviderTest {

    @Test
    public void resolveMissingPropertyReturnsNull() {
        SystemDataProvider provider = new SystemDataProvider();
        
        Date date = new Date(2019, 01, 01);

        Object result = provider.resolvePath(date, "notapath");
        assertNull(result);
    }
}
