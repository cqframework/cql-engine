package org.opencds.cqf.cql.execution;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.Library;

import org.opencds.cqf.cql.elm.execution.ExpressionDefEvaluator;

/**
 * Created by Darren on 2017-11-15.
 */
public class CqlRunnerLib
{
    public static void perform(String maybeCqlOrElm, ArrayList<String> errors)
    {
        Library library = null;
        try
        {
            // First try and parse the input as ELM.
            library = CqlLibraryReader.read(new ByteArrayInputStream(
                maybeCqlOrElm.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e1)
        {
            // Parsing the input as ELM failed; try and parse it as CQL.
            String elm = CqlToElmLib.maybeCqlToElm(maybeCqlOrElm, errors);
            if (elm == null)
            {
                errors.add("The source code failed to parse either as ELM or as CQL.");
                errors.add("The prior errors were during the attempt to parse as CQL.");
                errors.add("The next errors were during the attempt to parse as ELM:");
                errors.add(e1.toString());
                return;
            }
            try
            {
                library = CqlLibraryReader.read(new ByteArrayInputStream(
                    elm.getBytes(StandardCharsets.UTF_8)));
            }
            catch (Exception e2)
            {
                errors.add("Translation of CQL to ELM succeeded; however"
                    + " parsing that ELM failed due to errors:");
                errors.add(e2.toString());
            }
        }

        // Whether ELM or CQL, we succeeded in parsing it.

        // Next execute all of the regular statements in the library that
        // are not marked "private".
        // Do this such that the only output the user sees from this is the
        // output from any Message() calls.

        Context context = new Context(library);

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
            try
            {
                statement.evaluate(context);
            }
            catch (Exception e3)
            {
                errors.add("Execution of CQL or ELM statement named ["
                    + statement.getName() + "] failed due to error:");
                errors.add(e3.toString());
            }
        }
    }
}
