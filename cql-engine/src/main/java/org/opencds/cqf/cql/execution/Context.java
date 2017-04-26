package org.opencds.cqf.cql.execution;

import org.cqframework.cql.elm.execution.*;
import org.opencds.cqf.cql.data.DataProvider;
import org.opencds.cqf.cql.data.SystemDataProvider;
import org.opencds.cqf.cql.terminology.TerminologyProvider;

import javax.xml.namespace.QName;
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

    private Map<String, Object> parameters = new HashMap<>();
    private Stack<String> currentContext = new Stack<>();
    private Map<String, Object> contextValues = new HashMap<>();
    private Stack<Stack<Variable> > windows = new Stack<>();
    private Map<String, Library> libraries = new HashMap<>();
    private Stack<Library> currentLibrary = new Stack<>();
    private LibraryLoader libraryLoader;

    private Library library;

    public Context(Library library) {
        this.library = library;
        pushWindow();
        registerDataProvider("urn:hl7-org:elm-types:r1", new SystemDataProvider());
        libraryLoader = new DefaultLibraryLoader();
		if (library.getIdentifier() != null)
			libraries.put(library.getIdentifier().getId(), library);
        currentLibrary.push(library);
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

    public ExpressionDef resolveExpressionRef(String name) {
        for (ExpressionDef expressionDef : getCurrentLibrary().getStatements().getDef()) {
            if (expressionDef.getName().equals(name)) {
                return expressionDef;
            }
        }

        throw new IllegalArgumentException(String.format("Could not resolve expression reference '%s' in library '%s'.",
                name, getCurrentLibrary().getIdentifier().getId()));
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
        else {
            // TODO: This doesn't allow for tuple-distinguished overloads....
            return Tuple.class;
        }
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
        return operandType.isAssignableFrom(argumentType);
    }

    // TODO: Could use some caching here, and potentially some better type resolution structures
    public FunctionDef resolveFunctionRef(String name, Iterable<Object> arguments) {
      String str = "";
      String str2 = "";
        for (ExpressionDef expressionDef : getCurrentLibrary().getStatements().getDef()) {
            //str += expressionDef.getName() + " ";
            if (expressionDef instanceof FunctionDef) {
                FunctionDef functionDef = (FunctionDef)expressionDef;
                //str2 += functionDef.getName() + " ";
                if (functionDef.getName().equals(name)) {
                    java.util.Iterator<OperandDef> operandIterator = functionDef.getOperand().iterator();
                    java.util.Iterator<Object> argumentIterator = arguments.iterator();
                    boolean isMatch = true;
                    while (operandIterator.hasNext()) {
                        if (argumentIterator.hasNext()) {
                            OperandDef operandDef = operandIterator.next();
                            Object argument = argumentIterator.next();
                            // TODO: This is actually wrong, but to fix this would require preserving type information in the ELM....
                            isMatch = isType(argument == null ? Object.class : argument.getClass(), resolveOperandType(operandDef));
                        }
                        else {
                            isMatch = false;
                        }
                        if (!isMatch) {
                            break;
                        }
                    }
                    if (isMatch) {
                        return functionDef;
                    }
                }
            }
        }
        throw new IllegalArgumentException(String.format("Could not resolve call to operator '%s' in library '%s'.",
                name, getCurrentLibrary().getIdentifier().getId()));
        //throw new IllegalArgumentException(String.format("Name: %s,\nExpDef: %s,\nFunDef: %s.", name, str, str2));
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
        for (Stack<Variable> stack : windows) {
            for (Variable v : stack) {
                if (v.getName().equals(name)) {
                    return v;
                }
            }
        }

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
        Class<? extends Object> clazz = target.getClass();

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
