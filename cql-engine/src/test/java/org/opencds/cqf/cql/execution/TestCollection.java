package org.opencds.cqf.cql.execution;

import java.util.HashMap;
import java.util.Set;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.opencds.cqf.cql.execution.tests.Tests;

/**
 * Created by Darren on 2018 Mar 8.
 */
public class TestCollection
{
    private HashMap<String, TestDefinition> definitions = new HashMap<>();
    private HashMap<String, TestDefinitionSourceMeta> sources = new HashMap<>();

    public void addSourcedTestDefinition(TestDefinition definition, TestDefinitionSourceMeta source)
    {
        String testName = definition.getName();
        if (definitions.containsKey(testName))
        {
            throw new KeyAlreadyExistsException(
                "Every test must have a distinct unqualified name: " + testName);
        }
        definitions.put(testName, definition);
        sources.put(testName, source);
    }

    public Integer getTestDefinitionsCount()
    {
        return definitions.size();
    }

    public Set<String> getTestDefinitionsNames()
    {
        return definitions.keySet();
    }

    public String[] getTestDefinitionsNamesSorted()
    {
        return definitions.keySet().stream().sorted().toArray(String[]::new);
    }

    public TestDefinition getTestDefinition(String testName)
    {
        return definitions.get(testName);
    }

    public TestDefinitionSourceMeta getTestDefinitionSource(String testName)
    {
        return sources.get(testName);
    }

    // Note: testHierarchy is only here temporarily until replaced.
    private HashMap<String, Tests> testHierarchy = new HashMap<>();
    public HashMap<String, Tests> getTestHierarchy()
    {
        return testHierarchy;
    }
    public void addTestFileHierarchy(String filePath, Tests testFileHierarchy)
    {
        testHierarchy.put(filePath, testFileHierarchy);
    }
}
