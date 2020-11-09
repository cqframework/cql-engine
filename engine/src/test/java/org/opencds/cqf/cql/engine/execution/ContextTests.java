package org.opencds.cqf.cql.engine.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.cqframework.cql.elm.execution.Library;
import org.junit.Test;

public class ContextTests extends TranslatingTestBase {
    
    @Test
    public void TestNamespaceParsing() throws IOException, JAXBException {

        // This is not relevant to the test. It's just used for setting up the context.
        Library library = this.toLibrary("library Test version '1.0.0'\ndefine X:\n5+5");
        Context context = new Context(library);

        String actual = context.getUriPart("http://test.org/Library/TestLibrary");
        assertEquals("http://test.org/Library", actual);

        actual = context.getUriPart("TestLibrary");
        assertNull(actual);

        actual = context.getNamePart("http://test.org/Library/TestLibrary");
        assertEquals("TestLibrary", actual);

        actual = context.getNamePart("TestLibrary");
        assertEquals("TestLibrary", actual);
    }
}
