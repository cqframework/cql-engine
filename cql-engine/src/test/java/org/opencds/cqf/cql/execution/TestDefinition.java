package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.execution.tests.Test;

/**
 * Created by Darren on 2018 Mar 9.
 */
public class TestDefinition
{
    private Test definition;

    public TestDefinition(Test definition)
    {
        String name = definition.getName();
        if (name == null || name.equals(""))
        {
            throw new IllegalArgumentException(
                "A test definition must have a defined and nonempty name attribute.");
        }
        this.definition = definition;
    }

    public Test getDefinition()
    {
        return definition;
    }

    public String getName()
    {
        return definition.getName();
    }
}
