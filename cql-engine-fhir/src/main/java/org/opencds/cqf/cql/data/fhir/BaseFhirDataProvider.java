package org.opencds.cqf.cql.data.fhir;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
import org.opencds.cqf.cql.data.DataProvider;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Precision;
import org.opencds.cqf.cql.terminology.TerminologyProvider;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Christopher Schuler on 6/19/2017.
 */
public abstract class BaseFhirDataProvider implements DataProvider {

    // Data members
    protected FhirContext fhirContext;
    protected String packageName;
    protected String endpoint;
    protected TerminologyProvider terminologyProvider;
    protected boolean expandValueSets;
    protected boolean searchUsingPOST;
    protected IGenericClient fhirClient;

    // Abstract methods
    protected abstract String resolveClassName(String typeName);
    protected abstract Object fromJavaPrimitive(Object value, Object target);
    protected abstract Object toJavaPrimitive(Object result, Object source);
    protected abstract String convertPathToSearchParam(String type, String path);

    // getters & setters
    public FhirContext getFhirContext() {
        return fhirContext;
    }
    public void setFhirContext(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
        fhirContext.getRestfulClientFactory().setSocketTimeout(1200 * 10000);
    }

    public String getEndpoint() {
        return endpoint;
    }
    public BaseFhirDataProvider setEndpoint(String endpoint){
        this.endpoint = endpoint;
        this.fhirClient = getFhirContext().newRestfulGenericClient(endpoint);
        return this;
    }

    public TerminologyProvider getTerminologyProvider() {
        return terminologyProvider;
    }
    public BaseFhirDataProvider setTerminologyProvider(TerminologyProvider terminologyProvider) {
        this.terminologyProvider = terminologyProvider;
        return this;
    }

    public boolean isExpandValueSets() {
        return expandValueSets;
    }
    public BaseFhirDataProvider setExpandValueSets(boolean expandValueSets) {
        this.expandValueSets = expandValueSets;
        return this;
    }

    public boolean isSearchUsingPOST() {
        return searchUsingPOST;
    }
    public BaseFhirDataProvider setSearchUsingPOST (boolean searchUsingPOST) {
        this.searchUsingPOST = searchUsingPOST;
        expandValueSets = true;
        return this;
    }

    public IGenericClient getFhirClient() {
        return fhirClient;
    }

    // Transformations
    protected DateTime toDateTime(Date result) {
        return DateTime.fromJavaDate(result);
    }

    // Retrieval helpers
    protected String getPatientSearchParam(String dataType) {
        switch (dataType) {
            case "Coverage":
                return "beneficiary";
            case "Patient":
                return "_id";
            case "Observation":
            case "RiskAssessment":
                return "subject";
            default: return "patient";
        }
    }

    // Resolutions
    protected Object resolveProperty(Object target, String path) {
        if (target == null) {
            return null;
        }

        IBase base = (IBase) target;
        BaseRuntimeElementCompositeDefinition definition;
        if (base instanceof IPrimitiveType) {
            return toJavaPrimitive(path.equals("value") ? ((IPrimitiveType) target).getValue() : target, base);
        }
        else {
            definition = resolveRuntimeDefinition(base);
        }

        BaseRuntimeChildDefinition child = definition.getChildByName(path);
        if (child == null) {
            child = resolveChoiceProperty(definition, path);
        }

        List<IBase> values = child.getAccessor().getValues(base);

        if (values == null || values.isEmpty()) {
            return null;
        }

        if (child instanceof RuntimeChildChoiceDefinition && !child.getElementName().equalsIgnoreCase(path)) {
            if (!values.get(0).getClass().getSimpleName().equalsIgnoreCase(child.getChildByName(path).getImplementingClass().getSimpleName()))
            {
                return null;
            }
        }

        return toJavaPrimitive(child.getMax() < 1 ? values : values.get(0), base);
    }

    protected BaseRuntimeElementCompositeDefinition resolveRuntimeDefinition(IBase base) {
        if (base instanceof IAnyResource) {
            return getFhirContext().getResourceDefinition((IAnyResource) base);
        }

        else if (base instanceof IBaseBackboneElement || base instanceof IBaseElement) {
            return (BaseRuntimeElementCompositeDefinition) getFhirContext().getElementDefinition(base.getClass());
        }

        else if (base instanceof ICompositeType) {
            return (RuntimeCompositeDatatypeDefinition) getFhirContext().getElementDefinition(base.getClass());
        }

        throw new IllegalArgumentException(String.format("Unable to resolve the runtime definition for %s", base.getClass().getName()));
    }

    protected BaseRuntimeChildDefinition resolveChoiceProperty(BaseRuntimeElementCompositeDefinition definition, String path) {
        for (Object child :  definition.getChildren()) {
            if (child instanceof RuntimeChildChoiceDefinition) {
                RuntimeChildChoiceDefinition choiceDefinition = (RuntimeChildChoiceDefinition) child;

                if (choiceDefinition.getElementName().startsWith(path)) {
                    return choiceDefinition;
                }
            }
        }

        throw new IllegalArgumentException(String.format("Unable to resolve path %s for %s", path, definition.getName()));
    }

    protected Class resolveClass(String className) {
        try {
            return Class.forName(String.format("%s.%s", packageName, className));
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("Could not resolve type %s.%s.", packageName, className));
        }
    }

    // Creators
    protected Object createInstance(Class clazz) {
        try {
            return clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Could not create an instance of class %s.", clazz.getName()));
        }
    }

    // DataProvider methods
    @Override
    public Iterable<Object> retrieve(String context, Object contextValue, String dataType, String templateId, String codePath, Iterable<Code> codes, String valueSet, String datePath, String dateLowPath, String dateHighPath, Interval dateRange) {
        return null;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public Object resolvePath(Object target, String path) {
        String[] identifiers = path.split("\\.");
        for (String identifier : identifiers) {
            // handling indexes: i.e. item[0].code
            if (identifier.contains("[")) {
                int index = Character.getNumericValue(identifier.charAt(identifier.indexOf("[") + 1));
                target = resolveProperty(target, identifier.replaceAll("\\[\\d\\]", ""));
                target = ((ArrayList) target).get(index);
            }
            else {
                target = resolveProperty(target, identifier);
            }
        }

        return target;
    }

    @Override
    public Class resolveType(String typeName) {
        return resolveClass(resolveClassName(typeName));
    }

    @Override
    public Class resolveType(Object value) {
        return null;
    }

    @Override
    public Object createInstance(String typeName) {
        return null;
    }

    @Override
    public void setValue(Object target, String path, Object value) {
        if (target == null) {
            return;
        }

        IBase base = (IBase) target;
        BaseRuntimeElementCompositeDefinition definition;
        if (base instanceof IPrimitiveType) {
            ((IPrimitiveType) target).setValue(fromJavaPrimitive(value, base));
            return;
        }
        else {
            definition = resolveRuntimeDefinition(base);
        }

        BaseRuntimeChildDefinition child = definition.getChildByName(path);
        if (child == null) {
            child = resolveChoiceProperty(definition, path);
        }

        try {
            if (value instanceof Iterable) {
                for (Object val : (Iterable) value) {
                    child.getMutator().addValue(base, (IBase) fromJavaPrimitive(val, base));
                }
            }
            else {
                child.getMutator().setValue(base, (IBase) fromJavaPrimitive(value, base));
            }
        } catch (ConfigurationException ce) {
            if (value instanceof Quantity) {
                try {
                    value = ((Quantity) value).castToSimpleQuantity((Base) value);
                } catch (FHIRException e) {
                    throw new IllegalArgumentException("Unable to cast Quantity to SimpleQuantity");
                }
                child.getMutator().setValue(base, (IBase) fromJavaPrimitive(value, base));
            }
            else {
                throw new ConfigurationException(ce.getMessage());
            }
        }
    }
}
