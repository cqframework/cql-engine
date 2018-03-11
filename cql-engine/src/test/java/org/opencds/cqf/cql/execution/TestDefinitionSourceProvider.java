package org.opencds.cqf.cql.execution;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXB;

import org.opencds.cqf.cql.execution.tests.Group;
import org.opencds.cqf.cql.execution.tests.Tests;
import org.opencds.cqf.cql.execution.tests.Test;

/**
 * Created by Darren on 2018 Mar 9.
 */
public class TestDefinitionSourceProvider
{
    // Note: gatherTestsFromJavaResourceXmlFiles() is not atomic;
    // it will add new tests to testCollection as it finds them, and those
    // will persist even if it halts with a thrown exception part way.

    public static void gatherTestsFromJavaResourceXmlFiles(
        TestCollection testCollection, Class hostClass, String dirPath)
    {
        // Get names of Java resource files under hostClass/dirPath.
        ByteArrayInputStream fileNamesRaw
            = (ByteArrayInputStream)hostClass.getResourceAsStream(dirPath);
        String[] fileNames = fileNamesRaw == null ? new String[] {}
            : new BufferedReader(
                    new InputStreamReader(fileNamesRaw, StandardCharsets.UTF_8)
                ).lines().sorted().toArray(String[]::new);

        // Read in contents of found Java resource files.
        for (String fileName : fileNames)
        {
            InputStream testsFileRaw = hostClass.getResourceAsStream(dirPath + "/" + fileName);
            gatherTestsFromOneXmlFile(testCollection, hostClass, dirPath, fileName, JAXB.unmarshal(testsFileRaw, Tests.class));
        }
    }

    private static void gatherTestsFromOneXmlFile(TestCollection testCollection,
        Class hostClass, String dirPath, String fileName, Tests tests)
    {
        for (Group group : tests.getGroup())
        {
            TestDefinitionSourceMeta source = new TestDefinitionSourceMeta(
                TestDefinitionSourceMeta.SourceKind.JAVA_RESOURCE, hostClass, dirPath, fileName, group.getName());
            for (Test test : group.getTest())
            {
                testCollection.addSourcedTestDefinition(new TestDefinition(test), source);
            }
        }
        testCollection.addTestFileHierarchy(dirPath + "/" + fileName, tests);
    }
}
