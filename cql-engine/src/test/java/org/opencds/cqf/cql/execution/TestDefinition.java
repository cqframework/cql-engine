package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.execution.tests.Test;

/**
 * Created by Darren on 2018 Mar 8.
 */
public class TestDefinition
{
    private Test testNode;

    public TestDefinition(Test testNode)
    {
        String name = testNode.getName();
        if (name == null || name.equals(""))
        {
            throw new IllegalArgumentException(
                "A test definition must have a defined and nonempty name attribute.");
        }
        if (!name.matches("[A-Za-z_][A-Za-z0-9_]*"))
        {
            throw new IllegalArgumentException(
                "A test definition name attribute must match the pattern"
                + " \"[A-Za-z_][A-Za-z_0-9]*\": " + name);
        }
        this.testNode = testNode;
    }

    public Test getDefinition()
    {
        return testNode;
    }

    public String getName()
    {
        return testNode.getName();
    }

    public Boolean hasExpressionText()
    {
        return testNode.getExpression() != null
            && testNode.getExpression().getValue() != null
            && !testNode.getExpression().getValue().equals("");
    }

    public String getExpressionText()
    {
        return hasExpressionText() ? testNode.getExpression().getValue() : null;
    }

    public Boolean isInvalid()
    {
        return testNode.getExpression() != null
            && testNode.getExpression().isInvalid() != null
            && testNode.getExpression().isInvalid();
    }

    public Boolean hasSingularOutputText()
    {
        return testNode.getOutput() != null
            && testNode.getOutput().size() == 1
            && testNode.getOutput().get(0).getValue() != null
            && !testNode.getOutput().get(0).getValue().equals("");
    }

    public String getSingularOutputText()
    {
        return hasSingularOutputText() ? testNode.getOutput().get(0).getValue() : null;
    }
}
