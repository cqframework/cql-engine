package org.opencds.cqf.cql.execution;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.Library;

/**
 * Created by Darren on 2017-11-15.
 */
public class CqlRunnerLib
{
    public static void perform(String maybe_cql_or_elm_sc, PrintStream out,
        ArrayList<String> errors)
    {
        Library library = null;
        try
        {
            // First try and parse the input as ELM.
            library = CqlLibraryReader.read(new ByteArrayInputStream(
                maybe_cql_or_elm_sc.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e1)
        {
            // Parsing the input as ELM failed; try and parse it as CQL.
            String elm_xml = CqlToElmLib.maybe_cql_to_elm_xml(
                maybe_cql_or_elm_sc, errors);
            if (elm_xml == null)
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
                    elm_xml.getBytes(StandardCharsets.UTF_8)));
            }
            catch (Exception e2)
            {
                errors.add("Translation of CQL to ELM succeeded; however"
                    + " parsing that ELM failed due to errors:");
                errors.add(e2.toString());
            }
        }

        // Whether ELM or CQL, we succeeded in parsing it.

        Context context = new Context(library);

        for (ExpressionDef expressionDef : library.getStatements().getDef())
        {
            // TODO: Only evaluate the statement directly if it is a Message() call,
            // and handle the output differently.
            Object result = expressionDef.evaluate(context);
            out.println(expressionDef.getName() + ": " + (result == null ? "" : result.toString()));
        }
    }
}
