package org.opencds.cqf.cql.engine.execution;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.cqframework.cql.elm.execution.Library;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ElmTests {

    private Library library;

    @BeforeMethod
    public void setup() {
        try {
            this.library = CqlLibraryReader.read(ElmTests.class.getResourceAsStream("ElmTests.xml"));
        } catch (IOException | JAXBException e) {
            throw new IllegalArgumentException("Error reading ELM: " + e.getMessage());
        }
    }

    /**
     * {@link org.opencds.cqf.cql.engine.elm.execution.FilterEvaluator#evaluate(Context)}
     */
    @Test
    public void FilterTest() {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("TestFilter").getExpression().evaluate(context);

        Assert.assertTrue(((List<?>) result).size() == 2);
    }

    @Test
    public void TestLibraryLoad() {
        try {
            CqlLibraryReader.read(ElmTests.class.getResourceAsStream("CMS53Draft/PrimaryPCIReceivedWithin90MinutesofHospitalArrival-7.0.001.xml"));
        } catch (IOException | JAXBException e) {
            throw new IllegalArgumentException("Error reading ELM: " + e.getMessage());
        }
    }
}
