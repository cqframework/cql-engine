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
    public static void perform(PrintStream out, ArrayList<String> errors,
        String source_code_text)
    {
        String elm_xml = CqlToElmLib.maybe_cql_to_elm_xml(errors, source_code_text);
        if (elm_xml == null)
        {
            return;
        }

        Library library = null;
        try
        {
            library = CqlLibraryReader.read(new ByteArrayInputStream(
                elm_xml.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e)
        {
            errors.add("Translation of CQL to ELM failed due to errors:");
            errors.add(e.toString());
        }

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
