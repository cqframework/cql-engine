package org.opencds.cqf.cql.execution;

import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.FunctionDef;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.UsingDef;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.opencds.cqf.cql.data.DataProvider;
import org.opencds.cqf.cql.terminology.TerminologyProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * NOTE: Several possible approaches to traversing the ELM tree for execution:
 *
 * 1. "Executable" Node Hierarchy: Create nodes for each ELM type and deserialize into these nodes
 * This option works well, but is problematic for maintenance because Java doesn't have partial classes.
 * There also doesn't seem to be a way to tell JAXB which hierarchy to use if you have two different hierarchies
 * for the same schema (Trackable-based ELM used by the CQL-to-ELM translator, and Executable-based ELM used by the engine).
 * This could potentially be a bonus though, as it forces the engine to not take a dependency on the translator, forcing
 * a clean separation between the translator and the engine.
 *
 * 2. Visitor Pattern: This option is potentially simpler to implement, however:
 *  a. The visitor pattern doesn't lend itself well to aggregation of results, which is the real work of each node anyway
 *  b. Extensibility is compromised, difficult to introduce new nodes (unlikely to be a practical issue though)
 *  c. Without lambdas, the cost of traversal is quite high due to the expensive if-then-else chains in the visitor nodes
 *
 *  So, opting for the Executable Node Hierarchy for now, knowing that it creates a potential maintenance issue, but
 *  this is acceptable because the ELM Hierarchy is settling down, and so long as all the non-generated code is at the
 *  end, this should be easy to maintain. In addition, it will be much more performant, and lend itself much better to
 *  the aggregation of values from child nodes.
 */

public class CqlEngine {

    public static enum Options {
        EnableExpressionCaching
    }

    private LibraryLoader libraryLoader;
    private Map<String, DataProvider> dataProviders;
    private TerminologyProvider terminologyProvider;
    private EnumSet<Options> options;

    public CqlEngine(LibraryLoader libraryLoader) {
        this(libraryLoader, null, null);
    }

    public CqlEngine(LibraryLoader libraryLoader, Map<String, DataProvider> dataProviders, TerminologyProvider terminologyProvider) {
        this(libraryLoader, dataProviders, terminologyProvider, EnumSet.of(Options.EnableExpressionCaching));
    }

    // TODO: External function provider
    public CqlEngine(LibraryLoader libraryLoader, Map<String, DataProvider> dataProviders, TerminologyProvider terminologyProvider, EnumSet<Options> options) {
        if (libraryLoader == null) {
            throw new IllegalArgumentException("libraryLoader can not be null.");
        }

        this.libraryLoader = libraryLoader;
        this.dataProviders = dataProviders;
        this.terminologyProvider = terminologyProvider;
        this.options = options;
    }

    public EvaluationResult evaluate(Map<VersionedIdentifier, Set<String>> expressions)
    {
        return this.evaluate(null, null, expressions);
    }

    public EvaluationResult evaluate(VersionedIdentifier libraryIdentifier)
    {
        return this.evaluate(null, null, libraryIdentifier);
    }

    public EvaluationResult evaluate(Map<String, Object> contextParameters, Map<VersionedIdentifier, Map<String, Object>> parameters, VersionedIdentifier libraryIdentifier)
    {
        Library library = this.loadLibrary(libraryIdentifier);
        Map<VersionedIdentifier, Set<String>> expressions = this.getExpressionMap(library);
        return this.evaluate(contextParameters, parameters, expressions);
    }


    public EvaluationResult evaluate(Map<String, Object> contextParameters, Map<VersionedIdentifier, Map<String, Object>> parameters, Map<VersionedIdentifier, Set<String>> expressions)
    {
        Map<VersionedIdentifier, Library> libraries = this.loadLibraries(expressions.keySet());
        return this.evaluate(contextParameters, parameters, expressions, libraries);

    }

    private EvaluationResult evaluate(Map<String, Object> contextParameters, Map<VersionedIdentifier, Map<String, Object>> parameters, Map<VersionedIdentifier, Set<String>> expressions, Map<VersionedIdentifier, Library> libraries) {
        EvaluationResult evaluationResult = new EvaluationResult();

        for (Map.Entry<VersionedIdentifier, Set<String>> entry : expressions.entrySet()) {

            if (!libraries.containsKey(entry.getKey())) {
                throw new IllegalArgumentException(String.format("Library %s required to evaluate expressions and was not found.",
                 this.getLibraryDescription(entry.getKey())));
            }

            Library library = libraries.get(entry.getKey());

            Context context = this.setupContext(contextParameters, parameters, library);

            LibraryResult result = this.evaluateLibrary(context, library, entry.getValue());

            evaluationResult.libraryResults.put(entry.getKey(), result);
        }

        return evaluationResult;
    }

    private LibraryResult evaluateLibrary(Context context, Library library, Set<String> expressions) {
        LibraryResult result = new LibraryResult();

        for (String expression : expressions) {
            ExpressionDef def = context.resolveExpressionRef(expression);

            // TODO: We should probably move this validation further up the chain.
            // For example, we should tell the user that they've tried to evaluate a function def through incorrect
            // CQL or input parameters. And the code that gather the list of expressions to evaluate together should
            // not include function refs.
            if (def instanceof FunctionDef) {
                continue;
            }
            
            context.enterContext(def.getContext());
            Object object = def.evaluate(context);
            result.expressionResults.put(expression, object);
        }

        return result;
    }

    // TODO: Handle global parameters?
    private Context setupContext(Map<String, Object> contextParameters, Map<VersionedIdentifier, Map<String, Object>> parameters, Library library) {
        
        // Context requires an initial library to init properly.
        // TODO: Allow context to be initialized with multiple libraries
        Context context = new Context(library);

        // TODO: Does the context actually need a library loaded if all the libraries are prefetched?
        // We'd have to make sure we include the dependencies too.
        context.registerLibraryLoader(this.libraryLoader);

        if (this.options.contains(Options.EnableExpressionCaching)) {
            context.setExpressionCaching(true);
        }

        if (this.terminologyProvider != null) {
            context.registerTerminologyProvider(this.terminologyProvider);
        }
        
        if (this.dataProviders != null) {
            for (Map.Entry<String, DataProvider> pair : this.dataProviders.entrySet()) {
                context.registerDataProvider(pair.getKey(), pair.getValue());
            }
        }

        if (contextParameters != null) {
            for (Map.Entry<String, Object> pair : contextParameters.entrySet()) {
                context.setContextValue(pair.getKey(), pair.getValue());
            }
        }

        if (parameters != null) {
            for (Map.Entry<VersionedIdentifier, Map<String,Object>> libraryParameters : parameters.entrySet()) {
                for (Map.Entry<String, Object> parameterValue : libraryParameters.getValue().entrySet()) {
                    context.setParameter(libraryParameters.getKey().getId(), parameterValue.getKey(), parameterValue.getValue());
                }
            }
        }

        return context;
    }


    private Map<VersionedIdentifier, Library> loadLibraries(Set<VersionedIdentifier> libraryIdentifiers) {
        
        Map<VersionedIdentifier, Library> libraries = new HashMap<>();

        for (VersionedIdentifier libraryIdentifier : libraryIdentifiers) {   
            Library library = this.loadLibrary(libraryIdentifier);
            libraries.put(libraryIdentifier, library);
        }

        return libraries;
    }

    private Library loadLibrary(VersionedIdentifier libraryIdentifier) {
        Library library = this.libraryLoader.load(libraryIdentifier);

        if (library == null) {
            throw new IllegalArgumentException(String.format("Unable to load library %s", 
                libraryIdentifier.getId() + libraryIdentifier.getVersion() != null ? "-" + libraryIdentifier.getVersion() : ""));
        }

        // TODO: Removed this validation pending more intelligent handling at the service layer
        // For example, providing a mock or dummy data provider in the event there's no data store
        //this.validateDataRequirements(library);
        this.validateTerminologyRequirements(library);

        // TODO: Optimization ?
        // TODO: Validate Expressions as well?

        return library;
    }

    private void validateDataRequirements(Library library) {
        if (library.getUsings() != null && library.getUsings().getDef() != null && !library.getUsings().getDef().isEmpty())
        {
            for (UsingDef using : library.getUsings().getDef()) {
                // Skip system using since the context automatically registers that.
                if (using.getUri().equals("urn:hl7-org:elm-types:r1"))
                {
                    continue;
                }

                if (this.dataProviders == null || !this.dataProviders.containsKey(using.getUri())) {
                    throw new IllegalArgumentException(String.format("Library %1$s is using %2$s and no data provider is registered for uri %2$s.",
                    this.getLibraryDescription(library.getIdentifier()),
                    using.getUri()));
                }
            }
        }
    }

    private void validateTerminologyRequirements(Library library) {
        if ((library.getCodeSystems() != null && library.getCodeSystems().getDef() != null && !library.getCodeSystems().getDef().isEmpty()) || 
            (library.getCodes() != null  && library.getCodes().getDef() != null && !library.getCodes().getDef().isEmpty()) || 
            (library.getValueSets() != null  && library.getValueSets().getDef() != null && !library.getValueSets().getDef().isEmpty())) {
            if (this.terminologyProvider == null) {
                throw new IllegalArgumentException(String.format("Library %s has terminology requirements and no terminology provider is registered.",
                    this.getLibraryDescription(library.getIdentifier())));
            }
        }
    }

    private String getLibraryDescription(VersionedIdentifier libraryIdentifier) {
        return libraryIdentifier.getId() + (libraryIdentifier.getVersion() != null ? ("-" + libraryIdentifier.getVersion()) : "");
    }

    private Map<VersionedIdentifier, Set<String>> getExpressionMap(Library library) {
        Map<VersionedIdentifier, Set<String>> map = new HashMap<>();
        Set<String> expressionNames = new HashSet<>();
        if (library.getStatements() != null && library.getStatements().getDef() != null) {
            for (ExpressionDef ed : library.getStatements().getDef()) {
                expressionNames.add(ed.getName());
            }
        }

        map.put(library.getIdentifier(), expressionNames);

        return map;
    }
}