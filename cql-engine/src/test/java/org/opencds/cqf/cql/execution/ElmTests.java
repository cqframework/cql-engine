package org.opencds.cqf.cql.execution;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.cqframework.cql.elm.execution.Library;
import org.opencds.cqf.cql.elm.execution.ExpressionDefEvaluator;
import org.opencds.cqf.cql.elm.execution.RetrieveEvaluator;
import org.opencds.cqf.cql.elm.execution.SingletonFromEvaluator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

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
     * {@link org.opencds.cqf.cql.elm.execution.FilterEvaluator#evaluate(Context)}
     */
    @Test
    public void FilterTest() {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("TestFilter").getExpression().evaluate(context);

        Assert.assertTrue(((List) result).size() == 2);
    }

    @Test
    public void TestLibraryLoad() {
        try {
            Library library = CqlLibraryReader.read(ElmTests.class.getResourceAsStream("CMS53Draft/PrimaryPCIReceivedWithin90MinutesofHospitalArrival-7.0.001.xml"));
        } catch (IOException | JAXBException e) {
            throw new IllegalArgumentException("Error reading ELM: " + e.getMessage());
        }
    }

    @Test
    public void TestJsonLibraryLoad() {
        try {
            Library library = JsonCqlLibraryReader.read(new InputStreamReader(ElmTests.class.getResourceAsStream("ANCFHIRDummy.json")));
            Assert.assertTrue(library != null);
            Assert.assertTrue(library.getStatements() != null);
            Assert.assertTrue(library.getStatements().getDef() != null);
            Assert.assertTrue(library.getStatements().getDef().size() >= 2);
            Assert.assertTrue(library.getStatements().getDef().get(0) instanceof ExpressionDefEvaluator);
            Assert.assertTrue(library.getStatements().getDef().get(0).getExpression() instanceof SingletonFromEvaluator);
            Assert.assertTrue(((SingletonFromEvaluator)library.getStatements().getDef().get(0).getExpression()).getOperand() instanceof RetrieveEvaluator);
            Assert.assertTrue(library.getStatements().getDef().get(1) instanceof ExpressionDefEvaluator);
            Assert.assertTrue(library.getStatements().getDef().get(1).getExpression() instanceof RetrieveEvaluator);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Error reading ELM: " + e.getMessage());
        }
    }

    @Test
    public void TestJsonTerminologyLibraryLoad() {
        try {
            Library library = JsonCqlLibraryReader.read(new InputStreamReader(ElmTests.class.getResourceAsStream("ANCFHIRTerminologyDummy.json")));
            Assert.assertTrue(library != null);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading ELM: " + e.getMessage());
        }
    }
}
