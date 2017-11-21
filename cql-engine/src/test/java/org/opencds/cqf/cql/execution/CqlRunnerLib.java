package org.opencds.cqf.cql.execution;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;

/**
 * Created by Darren on 2017-11-15.
 */
public class CqlRunnerLib
{
    public static void perform(PrintStream out, ArrayList<String> errors,
        String source_code_text)
    {
        Library library = cql_to_library(errors, source_code_text);
        if (library == null)
        {
            return;
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

    public static Library cql_to_library(ArrayList<String> errors, String source_code_text)
    {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);

        ArrayList<CqlTranslator.Options> options = new ArrayList<>();
        options.add(CqlTranslator.Options.EnableDateRangeOptimization);

        CqlTranslator translator = CqlTranslator.fromText(
            source_code_text, modelManager, libraryManager,
            options.toArray(new CqlTranslator.Options[options.size()]));

        if (translator.getErrors().size() > 0)
        {
            collect_errors(errors, translator.getErrors());
            return null;
        }

        String xml = translator.toXml();

        if (translator.getErrors().size() > 0)
        {
            collect_errors(errors, translator.getErrors());
            return null;
        }

        Library library = null;
        try
        {
            library = CqlLibraryReader.read(new ByteArrayInputStream(
                xml.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e)
        {
            errors.add("Translation of CQL to ELM failed due to errors:");
            errors.add(e.toString());
        }

        return library;
    }

    public static void collect_errors(ArrayList<String> errors,
        Iterable<CqlTranslatorException> exceptions)
    {
        errors.add("Translation of CQL to ELM failed due to errors:");
        for (CqlTranslatorException error : exceptions)
        {
            TrackBack tb = error.getLocator();
            String lines = tb == null ? "[n/a]" : String.format(
                "%s[%d:%d, %d:%d]",
                (tb.getLibrary() == null ? ""
                    : tb.getLibrary().getId()
                        + (tb.getLibrary().getVersion() == null ? ""
                            : "-" + tb.getLibrary().getVersion()
                        )
                ),
                tb.getStartLine(), tb.getStartChar(),
                tb.getEndLine(), tb.getEndChar()
            );
            errors.add(lines + error.getMessage());
        }
    }
}
