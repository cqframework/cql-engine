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
    public static String maybeCqlToElm(String maybeCql)
    {
        return maybeCqlToElm(maybeCql, null);
    }

    public static String maybeCqlToElm(String maybeCql, ArrayList<String> errors)
    {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);

        ArrayList<CqlTranslator.Options> options = new ArrayList<>();
        options.add(CqlTranslator.Options.EnableDateRangeOptimization);

        CqlTranslator translator = CqlTranslator.fromText(
            maybeCql, modelManager, libraryManager,
            options.toArray(new CqlTranslator.Options[options.size()]));

        if (translator.getErrors().size() > 0)
        {
            if (errors != null)
            {
                collectErrors(errors, translator.getErrors());
            }
            return null;
        }

        String elm = translator.toXml();

        if (translator.getErrors().size() > 0)
        {
            if (errors != null)
            {
                collectErrors(errors, translator.getErrors());
            }
            return null;
        }

        return elm;
    }

    private static void collectErrors(ArrayList<String> errors,
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
