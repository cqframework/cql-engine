package org.opencds.cqf.cql.execution;

import java.util.HashMap;

import org.opencds.cqf.cql.execution.tests.Group;
import org.opencds.cqf.cql.execution.tests.Test;
import org.opencds.cqf.cql.execution.tests.Tests;

/**
 * Created by Darren on 2018 Mar 9.
 */
public class TestCollection
{
    private HashMap<String, Tests> testHierarchy = new HashMap<>();
    private HashMap<String, TestDefinition> testDefinitions = new HashMap<>();

    public HashMap<String, Tests> getTestHierarchy()
    {
        return testHierarchy;
    }

    // Note: addTestFileHierarchy() is not atomic; while it does test its
    // inputs and throws exceptions on bad ones, it can still have made
    // partial changes to TestCollection attributes first that persist.

    public void addTestFileHierarchy(String filePath, Tests testFileHierarchy)
    {
        testHierarchy.put(filePath, testFileHierarchy);
        for (Group group : testFileHierarchy.getGroup())
        {
            for (Test test : group.getTest())
            {
                String testName = test.getName();
                if (testName == null || testName.equals(""))
                {
                    throw new IllegalArgumentException(
                        "A test definition must have a defined and nonempty name attribute.");
                }
                if (testDefinitions.containsKey(testName))
                {
                    throw new IllegalArgumentException(
                        "Every test must have a distinct unqualified name: " + testName);
                }
                testDefinitions.put(testName, new TestDefinition(test));
            }
        }
    }
}
