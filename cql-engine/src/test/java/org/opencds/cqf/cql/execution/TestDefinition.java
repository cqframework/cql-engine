package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.execution.tests.Test;

/**
 * Created by Darren on 2018 Mar 8.
 */
public class TestDefinition
{
    public enum MainFormat { EXPRESSION_PAIR, LIBRARY }

    private Test testNode;
    private MainFormat mainFormat;
    private String testNameWithHash;
    private String normalizedLibCql;

    public TestDefinition(Test testNode)
    {
        String testName = testNode.getName();
        if (testName == null || testName.equals(""))
        {
            throw new IllegalArgumentException(
                "A test definition must have a defined and nonempty name attribute.");
        }
        if (!testName.matches("[A-Za-z_][A-Za-z0-9_]*"))
        {
            throw new IllegalArgumentException(
                "A test definition name attribute must match the pattern"
                + " \"[A-Za-z_][A-Za-z_0-9]*\": " + testName);
        }
        this.testNode = testNode;

        testNameWithHash = testName + "_" + Math.abs(testName.hashCode());

        if (!hasExpressionText())
        {
            return;
        }

        mainFormat = getExpressionText().matches("(?s).*?\\bdefine\\s+[a-zA-Z_\"].+")
            ? MainFormat.LIBRARY : MainFormat.EXPRESSION_PAIR;

        if (mainFormat.equals(MainFormat.EXPRESSION_PAIR))
        {
            normalizedLibCql = "define public " + testNameWithHash + "Q: " + getExpressionText() + "\n";
            if (hasSingularOutputText())
            {
                normalizedLibCql += "define public " + testNameWithHash + "A: " + getSingularOutputText() + "\n";
            }
        }
        else
        {
            normalizedLibCql = getExpressionText();
        }
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

    public Boolean expectsCqlTranslationFail()
    {
        return testNode.getExpression() != null
            && testNode.getExpression().isInvalid() != null
            && testNode.getExpression().isInvalid();
    }

    public MainFormat getMainFormat()
    {
        return mainFormat;
    }

    public String getNameWithHash()
    {
        return testNameWithHash;
    }

    public String getNormalizedLibCql()
    {
        return normalizedLibCql;
    }
}
