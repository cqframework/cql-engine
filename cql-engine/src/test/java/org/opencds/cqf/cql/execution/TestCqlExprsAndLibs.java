package org.opencds.cqf.cql.execution;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.bind.JAXB;

import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.Library;

import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.elm.execution.ExpressionDefEvaluator;
import org.opencds.cqf.cql.execution.tests.Expression;
import org.opencds.cqf.cql.execution.tests.Group;
import org.opencds.cqf.cql.execution.tests.Output;
import org.opencds.cqf.cql.execution.tests.Tests;

import org.testng.annotations.Test;

/**
 * Created by Darren on 2018 Jan 16.
 */
public class TestCqlExprsAndLibs {

    private Tests loadTestsFile(String testsFilePath) {
        try {
            InputStream testsFileRaw = TestCqlExprsAndLibs.class.getResourceAsStream(testsFilePath);
            return JAXB.unmarshal(testsFileRaw, Tests.class);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Couldn't load tests file ["+testsFilePath+"]: " + e.toString());
        }
    }

    private Object[] loadResourceDirFileNameList(String resourceDirPath) {
        /* TODO: Should return String[] but how to do the cast that doesn't die at runtime. */
        ByteArrayInputStream fileNamesRaw
            = (ByteArrayInputStream)TestCqlExprsAndLibs.class.getResourceAsStream(resourceDirPath);
        if (fileNamesRaw == null) {
            // The directory is empty / contains no files.
            return new Object[] {};
        }
        Stream<String> fileNames = new BufferedReader(
            new InputStreamReader(fileNamesRaw, StandardCharsets.UTF_8)).lines();
        return fileNames.toArray();
    }

    private void runTestFile(org.opencds.cqf.cql.execution.tests.Test test) {
        Expression testQ = test.getExpression();
        if (testQ == null) {
            throw new RuntimeException("Test has no question or library definition (expression).");
        }
        String cqlExprQOrLibCql = testQ.getValue();
        if (cqlExprQOrLibCql == null || cqlExprQOrLibCql.equals("")) {
            throw new RuntimeException("Test has no question or library definition (expression).");
        }

        Boolean expectInvalid = testQ.isInvalid() != null && testQ.isInvalid();

        if (cqlExprQOrLibCql.matches("(?s).*?\\bdefine\\s+[a-zA-Z_\"].+")) {
            // If we get here, assume this "test" node defines a CQL library.
            String libCql = cqlExprQOrLibCql;

            // Note that we are not using "test" child node "output" yet for anything.

            // If the test expression is invalid, expect an error during
            // translation or evaluation and fail if we don't get one;
            // otherwise fail if we do get one.
            ArrayList<String> errors = new ArrayList<>();
            String libElm = CqlToElmLib.maybeCqlToElm(libCql, errors);
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

            Library.Statements statements = library.getStatements();
            if (statements == null) {
                throw new RuntimeException("Test library parsed but didn't declare any statements.");
            }

            for (ExpressionDef statement : statements.getDef())
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

            return;
        }

        // If we get here, assume this "test" node defines a CQL expression question/answer pair.
        String cqlExprQ = cqlExprQOrLibCql;

        // If the test expression is invalid, expect an error during
        // translation or evaluation and fail if we don't get one;
        // otherwise fail if we do get one.
        String cqlLibQ = "library TestQ define Q: " + cqlExprQ;
        ArrayList<String> errorsQ = new ArrayList<>();
        String elmLibQ = CqlToElmLib.maybeCqlToElm(cqlLibQ, errorsQ);
        if (elmLibQ == null) {
            if (expectInvalid) {
                return;
            }
            else {
                throw new RuntimeException("Test question CQL failed to translate to ELM in Translator: " + errorsQ.toString());
            }
        }
        Library libraryQ;
        try {
            libraryQ = CqlLibraryReader.read(new ByteArrayInputStream(
                elmLibQ.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e) {
            if (expectInvalid) {
                return;
            }
            else {
                throw new RuntimeException("Test question translated but ELM failed to parse in Engine: " + e.toString());
            }
        }
        Object resultQ;
        try {
            Context contextQ = new Context(libraryQ);
            resultQ = contextQ.resolveExpressionRef("Q").getExpression().evaluate(contextQ);
        }
        catch (Exception e) {
            if (expectInvalid) {
                return;
            }
            else {
                throw new RuntimeException("Test question parsed but evaluation failed in Engine: " + e.toString());
            }
        }
        if (expectInvalid) {
            throw new RuntimeException("Test question didn't fail to translate/parse/evaluate as expected.");
        }

        List<Output> testA = test.getOutput();
        if (testA.size() != 1) {
            throw new RuntimeException("Test has not exactly one answer (output).");
        }
        String cqlExprA = testA.get(0).getValue();
        if (cqlExprA == null || cqlExprA.equals("")) {
            throw new RuntimeException("Test has not exactly one answer (output).");
        }

        String cqlLibA = "library TestA define A: " + cqlExprA;
        ArrayList<String> errorsA = new ArrayList<>();
        String elmLibA = CqlToElmLib.maybeCqlToElm(cqlLibA, errorsA);
        if (elmLibA == null) {
            throw new RuntimeException("Test answer CQL failed to translate to ELM in Translator: " + errorsA.toString());
        }
        Library libraryA;
        try {
            libraryA = CqlLibraryReader.read(new ByteArrayInputStream(
                elmLibA.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e) {
            throw new RuntimeException("Test answer translated but ELM failed to parse in Engine: " + e.toString());
        }
        Object resultA;
        try {
            Context contextA = new Context(libraryA);
            resultA = contextA.resolveExpressionRef("A").getExpression().evaluate(contextA);
        }
        catch (Exception e) {
            throw new RuntimeException("Test answer parsed but evaluation failed in Engine: " + e.toString());
        }

        Object resultC;
        try {
            resultC = EquivalentEvaluator.equivalent(resultQ, resultA);
        }
        catch (Exception e) {
            throw new RuntimeException("Test comparison evaluation failed in Engine: " + e.toString());
        }

        if (resultC == null) {
            throw new RuntimeException("Test comparison of actual and expected answers resulted in an uncertainty/null value in Engine.");
        }
        if (!(resultC instanceof Boolean)) {
            throw new RuntimeException("Equivalent() had an internal error resulting in a value that is neither Boolean nor null.");
        }
        if ((Boolean)resultC != true) {
            throw new RuntimeException("Actual test answer is not equivalent to expected test answer.");
        }
    }

    @Test
    public void testCqlExprsAndLibs() {
        // Load Test cases from org/opencds/cqf/cql/execution/TestCqlExprsAndLibs/tests/*.xml
        String testsDirPath = "TestCqlExprsAndLibs/tests";
        Object[] testsFileNames = loadResourceDirFileNameList(testsDirPath);
        Integer padWidth = Arrays.stream(testsFileNames)
            .map(f -> ((String)f).length()).reduce(0, (x,y) -> x > y ? x : y);
        int testCounterAllFiles = 0;
        int passCounterAllFiles = 0;
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
                    testCounterAllFiles += 1;
                    try {
                        //System.out.println(String.format("Running test %s...", test.getName()));
                        runTestFile(test);
                        passCounter += 1;
                        passCounterAllFiles += 1;
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
        System.out.println("TestCqlExprsAndLibs Results Summary:");
        System.out.println(" * Summary passed/total test count: "+passCounterAllFiles+"/"+testCounterAllFiles);
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
