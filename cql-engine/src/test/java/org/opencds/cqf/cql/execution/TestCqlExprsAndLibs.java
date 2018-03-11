package org.opencds.cqf.cql.execution;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.Library;

import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.elm.execution.ExpressionDefEvaluator;
import org.opencds.cqf.cql.execution.tests.Group;
import org.opencds.cqf.cql.execution.tests.Tests;

import org.testng.annotations.Test;

/**
 * Created by Darren on 2018 Jan 16.
 */
public class TestCqlExprsAndLibs {

    private static void runTestNode(TestDefinition test) {
        if (!test.hasExpressionText()) {
            throw new RuntimeException("Test has no question or library definition (expression).");
        }
        String cqlExprQOrLibCql = test.getExpressionText();

        Boolean expectInvalid = test.isInvalid();

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

        if (!test.hasSingularOutputText()) {
            throw new RuntimeException("Test has not exactly one answer (output).");
        }
        String cqlExprA = test.getSingularOutputText();

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
        TestCollection testCollection = new TestCollection();
        // Gather Test cases from org/opencds/cqf/cql/execution/TestCqlExprsAndLibs/tests/*.xml
        String testsDirPath = "TestCqlExprsAndLibs/tests";
        System.out.println("Gathering all tests.");
        TestDefinitionSourceProvider.gatherTestsFromJavaResourceXmlFiles(
            testCollection, TestCqlExprsAndLibs.class, testsDirPath);
        System.out.println("Evaluating all tests.");
        HashMap<String, Tests> testHierarchy = testCollection.getTestHierarchy();
        String[] testsFilePaths = testHierarchy.keySet().stream().sorted().toArray(String[]::new);
        Integer padWidth = Arrays.stream(testsFilePaths)
            .map(f -> f.length()).reduce(0, (x,y) -> x > y ? x : y);
        int testCounterAllFiles = 0;
        int passCounterAllFiles = 0;
        ArrayList<String> fileResults = new ArrayList<>();
        ArrayList<String> failedTests = new ArrayList<>();
        for (String testsFilePath : testsFilePaths) {
            System.out.println(String.format("Running test file %s...", testsFilePath));
            Tests tests = testHierarchy.get(testsFilePath);
            int testCounter = 0;
            int passCounter = 0;
            for (Group group : tests.getGroup()) {
                System.out.println(String.format("Running test group %s...", group.getName()));
                for (org.opencds.cqf.cql.execution.tests.Test test : group.getTest()) {
                    testCounter += 1;
                    testCounterAllFiles += 1;
                    try {
                        //System.out.println(String.format("Running test %s...", test.getName()));
                        runTestNode(testCollection.getTestDefinition(test.getName()));
                        passCounter += 1;
                        passCounterAllFiles += 1;
                        System.out.println(String.format("Test %s passed.", test.getName()));
                    }
                    catch (Exception e) {
                        failedTests.add(testsFilePath + " -> " + group.getName() + " -> " + test.getName());
                        System.out.println(String.format("Test %s failed with exception: %s", test.getName(), e.toString()));
                    }
                }
                //System.out.println(String.format("Finished test group %s.", group.getName()));
            }
            fileResults.add(String.format("%-"+padWidth.toString()+"s %3d/%3d", testsFilePath, passCounter, testCounter));
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
