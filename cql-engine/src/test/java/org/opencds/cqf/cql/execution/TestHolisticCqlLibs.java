package org.opencds.cqf.cql.execution;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.xml.bind.JAXB;

import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.Library;

import org.opencds.cqf.cql.elm.execution.ExpressionDefEvaluator;
import org.opencds.cqf.cql.execution.tests.Expression;
import org.opencds.cqf.cql.execution.tests.Group;
import org.opencds.cqf.cql.execution.tests.Tests;

import org.testng.annotations.Test;

/**
 * Created by Darren on 2018 Mar 3.
 */
public class TestHolisticCqlLibs {

    private Tests loadTestsFile(String testsFilePath) {
        try {
            InputStream testsFileRaw = TestHolisticCqlLibs.class.getResourceAsStream(testsFilePath);
            return JAXB.unmarshal(testsFileRaw, Tests.class);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Couldn't load tests file ["+testsFilePath+"]: " + e.toString());
        }
    }

    private Object[] loadResourceDirFileNameList(String resourceDirPath) {
        /* TODO: Should return String[] but how to do the cast that doesn't die at runtime. */
        ByteArrayInputStream fileNamesRaw
            = (ByteArrayInputStream)TestHolisticCqlLibs.class.getResourceAsStream(resourceDirPath);
        if (fileNamesRaw == null) {
            // The directory is empty / contains no files.
            return new Object[] {};
        }
        Stream<String> fileNames = new BufferedReader(
            new InputStreamReader(fileNamesRaw, StandardCharsets.UTF_8)).lines();
        return fileNames.toArray();
    }

    private void runHolisticCqlLibTest(org.opencds.cqf.cql.execution.tests.Test test) {
        Expression testQ = test.getExpression();
        if (testQ == null) {
            throw new RuntimeException("Test has no library definition (expression).");
        }
        String libCql = testQ.getValue();
        if (libCql == null || libCql.equals("")) {
            throw new RuntimeException("Test has no library definition (expression).");
        }

        Boolean expectInvalid = testQ.isInvalid() != null && testQ.isInvalid();

        // Note that we are not using "test" child node "output" yet for anything.

        // If the test expression is invalid, expect an error during
        // translation or evaluation and fail if we don't get one;
        // otherwise fail if we do get one.
        ArrayList<String> errors = new ArrayList<>();
        String libElm = CqlToElmLib.maybe_cql_to_elm_xml(libCql, errors);
        if (libElm == null) {
            if (expectInvalid) {
                return;
            }
            else {
                throw new RuntimeException("Test library CQL failed to translate to ELM in Translator: " + errors.toString());
            }
        }
        Library library;
        try {
            library = CqlLibraryReader.read(new ByteArrayInputStream(
                libElm.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e) {
            if (expectInvalid) {
                return;
            }
            else {
                throw new RuntimeException("Test library translated but ELM failed to parse in Engine: " + e.toString());
            }
        }

        for (ExpressionDef statement : library.getStatements().getDef())
        {
            if (!(statement instanceof ExpressionDefEvaluator))
            {
                // This skips over any FunctionDef statements for starters.
                continue;
            }
            if (!statement.getAccessLevel().value().equals("Public"))
            {
                // Note: It appears that Java interns the string "Public"
                // since using != here also seems to work.
                continue;
            }

            String stmtName = statement.getName();

            Object result;
            try
            {
                Context context = new Context(library);
                result = statement.evaluate(context);
            }
            catch (Exception e) {
                if (expectInvalid) {
                    continue;
                }
                else {
                    throw new RuntimeException("Test library parsed but evaluation of statement named ["
                        + stmtName + "] failed in Engine: " + e.toString());
                }
            }
            if (expectInvalid) {
                throw new RuntimeException("Test library statement named ["
                    + stmtName + "] didn't fail to translate/parse/evaluate as expected.");
            }

            if (!(result instanceof Boolean && (Boolean)result == true)) {
                throw new RuntimeException("Test library statement named ["
                    + stmtName + "] evaluation resulted in something that is not the expected Boolean true value.");
            }
        }
    }

    @Test
    public void testHolisticCqlLibs() {
        // Load Test cases from org/opencds/cqf/cql/execution/TestHolisticCqlLibs/tests/*.xml
        String testsDirPath = "TestHolisticCqlLibs/tests";
        Object[] testsFileNames = loadResourceDirFileNameList(testsDirPath);
        Integer padWidth = Arrays.stream(testsFileNames)
            .map(f -> ((String)f).length()).reduce(0, (x,y) -> x > y ? x : y);
        ArrayList<String> fileResults = new ArrayList<>();
        ArrayList<String> failedTests = new ArrayList<>();
        for (Object testsFileName : testsFileNames) {
            String testsFilePath = testsDirPath + "/" + testsFileName;
            System.out.println(String.format("Running test file %s...", testsFilePath));
            Tests tests = loadTestsFile(testsFilePath);
            int testCounter = 0;
            int passCounter = 0;
            for (Group group : tests.getGroup()) {
                System.out.println(String.format("Running test group %s...", group.getName()));
                for (org.opencds.cqf.cql.execution.tests.Test test : group.getTest()) {
                    testCounter += 1;
                    try {
                        //System.out.println(String.format("Running test %s...", test.getName()));
                        runHolisticCqlLibTest(test);
                        passCounter += 1;
                        System.out.println(String.format("Test %s passed.", test.getName()));
                    }
                    catch (Exception e) {
                        failedTests.add(testsFileName + " -> " + group.getName() + " -> " + test.getName());
                        System.out.println(String.format("Test %s failed with exception: %s", test.getName(), e.toString()));
                    }
                }
                //System.out.println(String.format("Finished test group %s.", group.getName()));
            }
            fileResults.add(String.format("%-"+padWidth.toString()+"s %3d/%3d", testsFileName, passCounter, testCounter));
            System.out.println(String.format("Tests file %s passed %s of %s tests.", testsFilePath, passCounter, testCounter));
        }
        System.out.println("==================================================");
        System.out.println("TestHolisticCqlLibs Results Summary:");
        System.out.println(" * Each file's passed/total test count:");
        for (String fileResult : fileResults) {
            System.out.println("   * " + fileResult);
        }
        System.out.println(" * List of failed tests:");
        for (String failedTest : failedTests) {
            System.out.println("   * " + failedTest);
        }
        System.out.println("==================================================");
    }
}
