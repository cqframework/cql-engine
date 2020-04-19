package org.opencds.cqf.cql.engine.execution;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.cqframework.cql.elm.execution.Library;
import org.opencds.cqf.cql.elm.execution.ExpressionDefEvaluator;
import org.opencds.cqf.cql.elm.execution.RetrieveEvaluator;
import org.opencds.cqf.cql.elm.execution.SingletonFromEvaluator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

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

    private void testElmDeserialization(String path, String xmlFileName, String jsonFileName) throws IOException, JAXBException {
        Library xmlLibrary = null;
        try {
            xmlLibrary = CqlLibraryReader.read(new FileReader(path + "/" + xmlFileName));
        }
        catch (Exception e) {
            throw new IllegalArgumentException(String.format("Errors occurred reading ELM from xml %s: %s", xmlFileName, e.getMessage()));
        }

        Library jsonLibrary = null;
        try {
            jsonLibrary = JsonCqlLibraryReader.read(new FileReader(path + "/" + jsonFileName));
        }
        catch (Exception e) {
            throw new IllegalArgumentException(String.format("Errors occurred reading ELM from json %s: %s", jsonFileName, e.getMessage()));
        }

        if (xmlLibrary != null && jsonLibrary != null) {
            Assert.assertTrue(equivalent(xmlLibrary, jsonLibrary));
        }
    }

    private void testElmDeserialization(String directoryName) throws URISyntaxException, IOException, JAXBException {
        URL dirURL = ElmTests.class.getResource(String.format("ElmTests/Regression/%s/", directoryName));
        File file = new File(dirURL.toURI());
        for (String fileName : file.list()) {
            if (fileName.endsWith(".xml")) {
                try {
                    testElmDeserialization(file.getAbsolutePath(), fileName, fileName.substring(0, fileName.length() - 4) + ".json");
                }
                catch (Exception e) {
                    throw new IllegalArgumentException(String.format("Errors occurred testing: %s", fileName));
                }
            }
        }
    }

    @Test
    public void RegressionTestJsonSerializer() throws URISyntaxException, IOException, JAXBException {
        // This test validates that the ELM library deserialized from the Json matches the ELM library deserialized from Xml
        // Regression inputs are annual update measure Xml for QDM and FHIR
        testElmDeserialization("qdm");
        testElmDeserialization("fhir");
    }

    private static boolean equivalent(Library left, Library right) {
        if (left == null && right == null) {
            return true;
        }

        if (left != null) {
            return left.getIdentifier().equals(right.getIdentifier());
        }

        // TODO: validate ELM equivalence... big job...
        // Simplest would be to introduce on Executable...

        return false;
    }
}
