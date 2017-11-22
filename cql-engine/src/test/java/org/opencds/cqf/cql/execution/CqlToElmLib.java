package org.opencds.cqf.cql.execution;

import java.util.ArrayList;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.tracking.TrackBack;

/**
 * Created by Darren on 2017-11-15.
 */
public class CqlToElmLib
{
    public static String maybe_cql_to_elm_xml(String maybe_cql_source_code)
    {
        return maybe_cql_to_elm_xml(maybe_cql_source_code, null);
    }

    public static String maybe_cql_to_elm_xml(String maybe_cql_source_code,
        ArrayList<String> errors)
    {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);

        ArrayList<CqlTranslator.Options> options = new ArrayList<>();
        options.add(CqlTranslator.Options.EnableDateRangeOptimization);

        CqlTranslator translator = CqlTranslator.fromText(
            maybe_cql_source_code, modelManager, libraryManager,
            options.toArray(new CqlTranslator.Options[options.size()]));

        if (translator.getErrors().size() > 0)
        {
            if (errors != null)
            {
                collect_errors(errors, translator.getErrors());
            }
            return null;
        }

        String elm_xml = translator.toXml();

        if (translator.getErrors().size() > 0)
        {
            if (errors != null)
            {
                collect_errors(errors, translator.getErrors());
            }
            return null;
        }

        return elm_xml;
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
