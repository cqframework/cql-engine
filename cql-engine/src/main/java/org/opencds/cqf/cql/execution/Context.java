package org.opencds.cqf.cql.execution;

import org.cqframework.cql.elm.execution.*;
import org.opencds.cqf.cql.data.DataProvider;
import org.opencds.cqf.cql.data.ExternalFunctionProvider;
import org.opencds.cqf.cql.data.SystemDataProvider;
import org.opencds.cqf.cql.runtime.Precision;
import org.opencds.cqf.cql.runtime.TemporalHelper;
import org.opencds.cqf.cql.terminology.TerminologyProvider;

import javax.xml.namespace.QName;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.List;

/**
 * Created by Bryn on 4/12/2016.
 */
public class Context {

    private boolean enableExpressionCache = false;
    private LinkedHashMap expressions = new LinkedHashMap(15, 0.9f, true) {
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > 10;
        }
    };

    private List<Object> evaluatedResources = new ArrayList<Object>();
    public List<Object> getEvaluatedResources() {
        return evaluatedResources;
    }

    public void clearEvaluatedResources() {
        this.evaluatedResources.clear();
    }

    private Map<String, Object> parameters = new HashMap<>();
    private Stack<String> currentContext = new Stack<>();
    private Map<String, Object> contextValues = new HashMap<>();
    private Stack<Stack<Variable> > windows = new Stack<>();
    private Map<String, Library> libraries = new HashMap<>();
    private Stack<Library> currentLibrary = new Stack<>();
    private org.opencds.cqf.cql.runtime.Tuple letExpressions = new org.opencds.cqf.cql.runtime.Tuple();
    private LibraryLoader libraryLoader;

    private Library library;

    private TraceExecution trace = new TraceExecution();
    private boolean enableTraceLogging = false;

    private org.opencds.cqf.cql.runtime.DateTime evaluationDateTime = 
            new org.opencds.cqf.cql.runtime.DateTime(OffsetDateTime.now().withOffsetSameInstant(TemporalHelper.getDefaultZoneOffset()), Precision.MILLISECOND);

    public Context(Library library) {
        init(library);
    }

    public Context(Library library, org.opencds.cqf.cql.runtime.DateTime evaluationDateTime) {
        this.evaluationDateTime = evaluationDateTime;
        init(library);
    }

    private void init(Library library) {
        this.library = library;
        pushWindow();
        registerDataProvider("urn:hl7-org:elm-types:r1", new SystemDataProvider());
        libraryLoader = new DefaultLibraryLoader();
        if (library.getIdentifier() != null)
            libraries.put(library.getIdentifier().getId(), library);
        currentLibrary.push(library);
    }

    public void logEntry(Class clazz, Object ... operands) {
        if (!enableTraceLogging) {
            return;
        }

        trace.logEntry(clazz, operands);
    }

    public Object logExit(Class clazz, Object result) {
        if (!enableTraceLogging) {
            return result;
        }

        return trace.logExit(clazz, result);
    }

    public Object logTrace(Class clazz, Object ... criteria) {
        if (!enableTraceLogging) {
            return criteria[0];
        }

        return trace.logTrace(clazz, criteria);
    }

    public void logError(Class clazz, String message) {
        if (!enableTraceLogging) {
            return;
        }

        trace.logError(clazz, message);
    }

    public String getTrace() {
        String header = getCurrentLibrary().getLocalId() + "\n";
        return trace.getTraceString();
    }

    public boolean isTraceLoggingEnabled() {
        return enableTraceLogging;
    }

    public Context setEnableTraceLogging(boolean enableTraceLogging) {
        this.enableTraceLogging = enableTraceLogging;
        return this;
    }

    public org.opencds.cqf.cql.runtime.DateTime getEvaluationDateTime() {
        return this.evaluationDateTime;
    }

    public void setExpressionCaching(boolean yayOrNay) {
        this.enableExpressionCache = yayOrNay;
    }

    public boolean isExpressionInCache(String name) {
        return this.expressions.containsKey(name);
    }

    public boolean isExpressionCachingEnabled() {
        return this.enableExpressionCache;
    }

    public void addExpressionToCache(String name, Object result) {
        this.expressions.put(name, result);
    }

    public Object getExpressionResultFromCache(String name) {
        return this.expressions.get(name);
    }

    public void addLetExpression(String name, Expression result) {
        if (letExpressions.getElements().containsKey(name)) {
            return;
        }

        letExpressions.getElements().put(name, result);
    }

    public void clearLetExpressions() {
        letExpressions = new org.opencds.cqf.cql.runtime.Tuple();
    }

    public void registerLibraryLoader(LibraryLoader libraryLoader) {
        if (libraryLoader == null) {
            throw new IllegalArgumentException("Library loader implementation must not be null.");
        }

        this.libraryLoader = libraryLoader;
    }

    private Library getCurrentLibrary() {
        return currentLibrary.peek();
    }

    private Library resolveIncludeDef(IncludeDef includeDef) {
        VersionedIdentifier libraryIdentifier = new VersionedIdentifier().withId(includeDef.getPath()).withVersion(includeDef.getVersion());
        Library library = libraries.get(libraryIdentifier.getId());
        if (library == null) {
            library = libraryLoader.load(libraryIdentifier);
            libraries.put(libraryIdentifier.getId(), library);
        }

        if (libraryIdentifier.getVersion() != null && !libraryIdentifier.getVersion().equals(library.getIdentifier().getVersion())) {
            throw new IllegalArgumentException(String.format("Could not load library '%s' version '%s' because version '%s' is already loaded.",
                    libraryIdentifier.getId(), libraryIdentifier.getVersion(), library.getIdentifier().getVersion()));
        }

        return library;
    }

    public boolean enterLibrary(String libraryName) {
        if (libraryName != null) {
            IncludeDef includeDef = resolveLibraryRef(libraryName);
            Library library = resolveIncludeDef(includeDef);
            currentLibrary.push(library);
            return true;
        }

        return false;
    }

    public void exitLibrary(boolean enteredLibrary) {
        if (enteredLibrary) {
            currentLibrary.pop();
        }
    }

    public CodeDef resolveCodeRef(String name) {
        for (CodeDef codeDef : getCurrentLibrary().getCodes().getDef()) {
            if (codeDef.getName().equals(name)) {
                return codeDef;
            }
        }

        throw new IllegalArgumentException(String.format("Could not resolve code reference '%s'.", name));
    }

    private IncludeDef resolveLibraryRef(String libraryName) {
        for (IncludeDef includeDef : getCurrentLibrary().getIncludes().getDef()) {
            if (includeDef.getLocalIdentifier().equals(libraryName)) {
                return includeDef;
            }
        }

        throw new IllegalArgumentException(String.format("Could not resolve library reference '%s'.", libraryName));
    }

    public Expression resolveLetExpressionRef(String name) {
        for (String key : letExpressions.getElements().keySet()) {
            if (key.equals(name)) {
                return (Expression) letExpressions.getElements().get(key);
            }
        }

        throw new IllegalArgumentException(String.format("Could not resolve let expression reference '%s' in library '%s'.",
                name, getCurrentLibrary().getIdentifier().getId()));
    }

    public ExpressionDef resolveExpressionRef(String name) {

        for (ExpressionDef expressionDef : getCurrentLibrary().getStatements().getDef()) {
            if (expressionDef.getName().equals(name)) {
                return expressionDef;
            }
        }

        throw new IllegalArgumentException(String.format("Could not resolve expression reference '%s' in library '%s'.",
                name, getCurrentLibrary().getIdentifier().getId()));
    }

    public Object resolveIdentifierRef(String name) {
        for (int i = windows.size() - 1; i >= 0; i--) {
            for (int j = windows.get(i).size() - 1; j >= 0; j--) {
                Object value = windows.get(i).get(j).getValue();
                if (value instanceof org.opencds.cqf.cql.runtime.Tuple) {
                    for (String key : ((org.opencds.cqf.cql.runtime.Tuple) value).getElements().keySet()) {
                        if (key.equals(name)) {
                            return ((org.opencds.cqf.cql.runtime.Tuple) value).getElements().get(key);
                        }
                    }
                }
                try {
                    return resolvePath(value, name);
                } catch (Exception ignored) {

                }
            }
        }
//        for (Stack<Variable> stack : windows) {
//            for (Variable var : stack) {
//                Object value = var.getValue();
//                if (value instanceof org.opencds.cqf.cql.runtime.Tuple) {
//                    for (String key : ((org.opencds.cqf.cql.runtime.Tuple) value).getElements().keySet()) {
//                        if (key.equals(name)) {
//                            return ((org.opencds.cqf.cql.runtime.Tuple) value).getElements().get(key);
//                        }
//                    }
//                }
//                try {
//                    return resolvePath(value, name);
//                } catch (Exception ignored) {
//
//                }
//            }
//        }

        throw new IllegalArgumentException("Cannot resolve identifier " + name);
    }

    public Object createInstance(QName typeName) {
        DataProvider dataProvider = resolveDataProvider(typeName);
        return dataProvider.createInstance(typeName.getLocalPart());
    }

    public Class resolveType(QName typeName) {
        DataProvider dataProvider = resolveDataProvider(typeName);
        return dataProvider.resolveType(typeName.getLocalPart());
    }

    public Class resolveType(TypeSpecifier typeSpecifier) {
        if (typeSpecifier instanceof NamedTypeSpecifier) {
            return resolveType(((NamedTypeSpecifier)typeSpecifier).getName());
        }
        else if (typeSpecifier instanceof ListTypeSpecifier) {
            // TODO: This doesn't allow for list-distinguished overloads...
            return List.class;
            //return resolveType(((ListTypeSpecifier)typeSpecifier).getElementType());
        }
        else if (typeSpecifier instanceof IntervalTypeSpecifier) {
            return org.opencds.cqf.cql.runtime.Interval.class;
        }
        else if (typeSpecifier instanceof ChoiceTypeSpecifier) {
            // TODO: This doesn't allow for choice-distinguished overloads...
            return Object.class;
        }
        else {
            // TODO: This doesn't allow for tuple-distinguished overloads....
            return org.opencds.cqf.cql.runtime.Tuple.class;
        }
    }

    public Class resolveType(Object value) {
        if (value == null) {
//            return Object.class;
            return null;
        }

        String packageName = value.getClass().getPackage().getName();

        // May not be necessary, idea is to sync with the use of List.class for ListTypeSpecifiers in the resolveType above
        if (value instanceof Iterable) {
            return List.class;
        }

        if (value instanceof Tuple) {
            return org.opencds.cqf.cql.runtime.Tuple.class;
        }

        // Primitives should just use the type
        if (packageName.startsWith("java")) {
            return value.getClass();
        }

        DataProvider dataProvider = resolveDataProvider(value.getClass().getPackage().getName());
        return dataProvider.resolveType(value);
    }

    private Class resolveOperandType(OperandDef operandDef) {
        if (operandDef.getOperandTypeSpecifier() != null) {
            return resolveType(operandDef.getOperandTypeSpecifier());
        }
        else {
            return resolveType(operandDef.getOperandType());
        }
    }

    private boolean isType(Class argumentType, Class operandType) {
        return argumentType == null || operandType.isAssignableFrom(argumentType);
    }

    private FunctionDef resolveFunctionRef(FunctionDef functionDef, String name, Iterable<Object> arguments) {
        java.util.Iterator<OperandDef> operandIterator = functionDef.getOperand().iterator();
        java.util.Iterator<Object> argumentIterator = arguments.iterator();
        boolean isMatch = true;
        while (operandIterator.hasNext()) {
            if (argumentIterator.hasNext()) {
                OperandDef operandDef = operandIterator.next();
                Object argument = argumentIterator.next();
                // TODO: This is actually wrong, but to fix this would require preserving type information in the ELM....
                isMatch = isType(resolveType(argument), resolveOperandType(operandDef));
            }
            else {
                isMatch = false;
            }
            if (!isMatch) {
                break;
            }
        }
        if (isMatch && !argumentIterator.hasNext()) {
            return functionDef;
        }

        return null;
    }

    private Map<String, List<FunctionDef>> functionCache = new HashMap<>();
    // TODO: Could use some caching here, and potentially some better type resolution structures
    public FunctionDef resolveFunctionRef(String name, Iterable<Object> arguments) {
        FunctionDef ret = null;
        if (functionCache.containsKey(name)) {
            for (FunctionDef functionDef : functionCache.get(name)) {
                if ((ret = resolveFunctionRef(functionDef, name, arguments)) != null) {
                    break;
                }
            }
        }
        else {
            // this logic adds all function defs with the specified name to the cache
            for (ExpressionDef expressionDef : getCurrentLibrary().getStatements().getDef()) {
                if (expressionDef.getName().equals(name)) {
                    if (expressionDef instanceof FunctionDef) {
                        FunctionDef candidate = resolveFunctionRef((FunctionDef) expressionDef, name, arguments);
                        if (candidate != null) {
                            ret = candidate;
                        }
                        if (functionCache.containsKey(name)) {
                            functionCache.get(name).add((FunctionDef) expressionDef);
                        }
                        else {
                            List<FunctionDef> functionDefs = new ArrayList<>();
                            functionDefs.add((FunctionDef) expressionDef);
                            functionCache.put(name, functionDefs);
                        }
                    }
                }
            }
        }
        if (ret != null) {
            return ret;
        }
        throw new IllegalArgumentException(String.format("Could not resolve call to operator '%s' in library '%s'.",
                name, getCurrentLibrary().getIdentifier().getId()));
    }

    private ParameterDef resolveParameterRef(String name) {
        for (ParameterDef parameterDef : getCurrentLibrary().getParameters().getDef()) {
            if (parameterDef.getName().equals(name)) {
                return parameterDef;
            }
        }

        throw new IllegalArgumentException(String.format("Could not resolve parameter reference '%s' in library '%s'.",
                name, getCurrentLibrary().getIdentifier().getId()));
    }

    public void setParameter(String libraryName, String name, Object value) {
        boolean enteredLibrary = enterLibrary(libraryName);
        try {
            String fullName = libraryName != null ? String.format("%s.%s", getCurrentLibrary().getIdentifier().getId(), name) : name;
            parameters.put(fullName, value);
        }
        finally {
            exitLibrary(enteredLibrary);
        }
    }

    public Object resolveParameterRef(String libraryName, String name) {
        boolean enteredLibrary = enterLibrary(libraryName);
        try {
            String fullName = libraryName != null ? String.format("%s.%s", getCurrentLibrary().getIdentifier().getId(), name) : name;
            if (parameters.containsKey(fullName)) {
                return parameters.get(fullName);
            }

            ParameterDef parameterDef = resolveParameterRef(name);
            Object result = parameterDef.getDefault() != null ? parameterDef.getDefault().evaluate(this) : null;
            parameters.put(fullName, result);
            return result;
        }
        finally {
            exitLibrary(enteredLibrary);
        }
    }

    public ValueSetDef resolveValueSetRef(String name) {
        for (ValueSetDef valueSetDef : getCurrentLibrary().getValueSets().getDef()) {
            if (valueSetDef.getName().equals(name)) {
                return valueSetDef;
            }
        }

        throw new IllegalArgumentException(String.format("Could not resolve value set reference '%s' in library '%s'.",
                name, getCurrentLibrary().getIdentifier().getId()));
    }

    public ValueSetDef resolveValueSetRef(String libraryName, String name) {
        boolean enteredLibrary = enterLibrary(libraryName);
        try {
            return resolveValueSetRef(name);
        }
        finally {
            exitLibrary(enteredLibrary);
        }
    }

    public CodeSystemDef resolveCodeSystemRef(String name) {
        for (CodeSystemDef codeSystemDef : getCurrentLibrary().getCodeSystems().getDef()) {
            if (codeSystemDef.getName().equals(name)) {
                return codeSystemDef;
            }
        }

        throw new IllegalArgumentException(String.format("Could not resolve code system reference '%s' in library '%s'.",
                name, getCurrentLibrary().getIdentifier().getId()));
    }

    public CodeSystemDef resolveCodeSystemRef(String libraryName, String name) {
        boolean enteredLibrary = enterLibrary(libraryName);
        try {
            return resolveCodeSystemRef(name);
        }
        finally {
            exitLibrary(enteredLibrary);
        }
    }

    private Map<String, DataProvider> dataProviders = new HashMap<>();
    private Map<String, DataProvider> packageMap = new HashMap<>();

    public void registerDataProvider(String modelUri, DataProvider dataProvider) {
        dataProviders.put(modelUri, dataProvider);
        packageMap.put(dataProvider.getPackageName(), dataProvider);
    }

    public DataProvider resolveDataProvider(QName dataType) {
        DataProvider dataProvider = dataProviders.get(dataType.getNamespaceURI());
        if (dataProvider == null) {
            throw new IllegalArgumentException(String.format("Could not resolve data provider for model '%s'.", dataType.getNamespaceURI()));
        }

        return dataProvider;
    }

    public DataProvider resolveDataProvider(String packageName) {
        DataProvider dataProvider = packageMap.get(packageName);
        if (dataProvider == null) {
            if (packageName.startsWith("ca.uhn.fhir.model.dstu2") || packageName.equals("ca.uhn.fhir.model.primitive"))
            {
                for (DataProvider provider : dataProviders.values()) {
                    if (provider.getPackageName().startsWith("ca.uhn.fhir.model.dstu2")
                            || provider.getPackageName().equals("ca.uhn.fhir.model.primitive"))
                    {
                        provider.setPackageName(packageName);
                        return provider;
                    }
                }
            }
            throw new IllegalArgumentException(String.format("Could not resolve data provider for package '%s'.", packageName));
        }

        return dataProvider;
    }

    private TerminologyProvider terminologyProvider;
    public void registerTerminologyProvider(TerminologyProvider tp) {
      terminologyProvider = tp;
    }

    public TerminologyProvider resolveTerminologyProvider() {
      return terminologyProvider;
    }

    private Map<VersionedIdentifier, ExternalFunctionProvider> externalFunctionProviders = new HashMap<>();

    public void registerExternalFunctionProvider(VersionedIdentifier identifier, ExternalFunctionProvider provider) {
        externalFunctionProviders.put(identifier, provider);
    }

    public ExternalFunctionProvider getExternalFunctionProvider() {
        Library currentLibrary = getCurrentLibrary();
        VersionedIdentifier identifier = currentLibrary.getIdentifier();
        ExternalFunctionProvider provider = externalFunctionProviders.get(identifier);
        if (provider == null) {
            throw new IllegalArgumentException(String.format(
                "Could not resolve external function provider for library '%s'.", identifier));
        }
        return provider;
    }

    public void enterContext(String context) {
        currentContext.push(context);
    }

    public void exitContext() {
        currentContext.pop();
    }

    public String getCurrentContext() {
        if (currentContext.empty()) {
            return null;
        }

        return currentContext.peek();
    }

    public void setContextValue(String context, Object contextValue) {
        contextValues.put(context, contextValue);
    }

    public Object getCurrentContextValue() {
        String context = getCurrentContext();
        if (context != null && this.contextValues.containsKey(context)) {
            return this.contextValues.get(context);
        }

        return null;
    }

    public void push(Variable variable) {
        getStack().push(variable);
    }

    public Variable resolveVariable(String name) {
        for (int i = windows.size() - 1; i >= 0; i--) {
            for (int j = windows.get(i).size() - 1; j >= 0; j--) {
                if (windows.get(i).get(j).getName().equals(name)) {
                    return windows.get(i).get(j);
                }
            }
        }
//        for (Stack<Variable> stack : windows) {
//            for (Variable v : stack) {
//                if (v.getName().equals(name)) {
//                    return v;
//                }
//            }
//        }

        return null;
    }

    public Variable resolveVariable(String name, boolean mustResolve) {
        Variable result = resolveVariable(name);
        if (mustResolve && result == null) {
            throw new IllegalArgumentException(String.format("Could not resolve variable reference %s", name));
        }

        return result;
    }

    public Object resolveAlias(String name) {
        // This method needs to account for multiple variables on the stack with the same name
        ArrayList<Object> ret = new ArrayList<>();
        boolean isList = false;
        for (Variable v : getStack()) {
            if (v.getName().equals(name)) {
                if (v.isList())
                    isList = true;
                ret.add(v.getValue());
            }
        }
        return isList ? ret : ret.get(0);
    }

    public void pop() {
        if (!windows.peek().empty())
            getStack().pop();
    }

    public void pushWindow() {
        windows.push(new Stack<>());
    }

    public void popWindow() {
        windows.pop();
    }

    private Stack<Variable> getStack() {
        return windows.peek();
    }

    public Object resolvePath(Object target, String path) {

        if (target == null) {
            return null;
        }

        // TODO: Path may include .'s and []'s.
        // For now, assume no qualifiers or indexers...
        Class<?> clazz = target.getClass();

        if (clazz.getPackage().getName().startsWith("java.lang")) {
            throw new IllegalArgumentException(String.format("Invalid path: %s for type: %s - this is likely an issue with the data model.", path, clazz.getName()));
        }

        DataProvider dataProvider = resolveDataProvider(clazz.getPackage().getName());
        return dataProvider.resolvePath(target, path);
    }

    public void setValue(Object target, String path, Object value) {
        if (target == null) {
            return;
        }

        Class<? extends Object> clazz = target.getClass();

        DataProvider dataProvider = resolveDataProvider(clazz.getPackage().getName());
        dataProvider.setValue(target, path, value);
    }
}
