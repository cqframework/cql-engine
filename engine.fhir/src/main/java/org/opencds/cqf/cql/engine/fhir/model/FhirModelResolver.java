package org.opencds.cqf.cql.engine.fhir.model;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.opencds.cqf.cql.engine.exception.InvalidCast;
import org.opencds.cqf.cql.engine.exception.InvalidPrecision;
import org.opencds.cqf.cql.engine.fhir.exception.DataProviderException;
import org.opencds.cqf.cql.engine.fhir.exception.UnknownType;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Precision;
import org.opencds.cqf.cql.engine.runtime.TemporalHelper;
import org.opencds.cqf.cql.engine.runtime.Time;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeChildResourceBlockDefinition;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;

// TODO: Probably quite a bit of redundancy here. Probably only really need the BaseType and the PrimitiveType

// TODO: type-to-class and contextPath resolutions are potentially expensive. Should add caching for those.
@SuppressWarnings({"unchecked", "rawtypes" })
public abstract class FhirModelResolver<BaseType, BaseDateTimeType, TimeType, SimpleQuantityType, IdType, ResourceType, EnumerationType, EnumFactoryType>
        implements ModelResolver {
    public FhirModelResolver(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
        this.initialize();
    }

    protected abstract void initialize();
    protected abstract Boolean equalsDeep(BaseType left, BaseType right);
    protected abstract SimpleQuantityType castToSimpleQuantity(BaseType base);
    protected abstract Calendar getCalendar(BaseDateTimeType dateTime);
    protected abstract Integer getCalendarConstant(BaseDateTimeType dateTime);
    protected abstract String idToString(IdType id);
    protected abstract String timeToString(TimeType time);
    protected abstract String getResourceType(ResourceType resource);
    protected abstract EnumerationType enumConstructor(EnumFactoryType factory);
    protected abstract Boolean enumChecker(Object object);
    protected abstract Class<?> enumFactoryTypeGetter(EnumerationType enumeration);

    protected String packageName;

    // Data members
    protected FhirContext fhirContext;


    public Object getContextPath(String contextType, String targetType) {
        if (targetType == null || contextType == null ) {
            return null;
        }

        if (contextType != null && !(contextType.equals("Unspecified") || contextType.equals("Population"))) {
            if (targetType != null && contextType.equals(targetType)) {
                return "id";
            }

            RuntimeResourceDefinition resourceDefinition = this.fhirContext.getResourceDefinition(targetType);
            Object theValue = this.createInstance(contextType);
            Class<? extends IBase> type = (Class<? extends IBase>)theValue.getClass();

            List<BaseRuntimeChildDefinition> children = resourceDefinition.getChildren();
            for (BaseRuntimeChildDefinition child : children) {

                String path = this.innerGetContextPath(child, type);
                if (path != null) {
                    return path;
                }
            }
        }

        return null;
    }

    protected String innerGetContextPath(BaseRuntimeChildDefinition child, Class<? extends IBase> type) {
        if (child instanceof RuntimeChildResourceDefinition) {
            RuntimeChildResourceDefinition resourceChild = (RuntimeChildResourceDefinition)child;

            for (Class<?> resourceClass : resourceChild.getResourceTypes()) {
                if (resourceClass.equals(type)) {
                    return resourceChild.getElementName();
                }
            }

            return null;
        }

        if (child instanceof RuntimeChildResourceBlockDefinition) {
            RuntimeChildResourceBlockDefinition resourceChild = (RuntimeChildResourceBlockDefinition)child;
            String currentName = resourceChild.getElementName();
            BaseRuntimeElementCompositeDefinition<?> element = resourceChild.getChildByName(currentName);

            
            for (BaseRuntimeChildDefinition nextChild : element.getChildren()) {
                String path = this.innerGetContextPath(nextChild, type);
                if (path != null) {
                    return String.join(".", currentName, path);
                }
            }
        }

        return null;
    }

    @Override
    public Boolean objectEqual(Object left, Object right) {
        if (left == null) {
            return null;
        }

        if (right == null) {
            return null;
        }

        return this.equalsDeep((BaseType) left, (BaseType) right);
    }

    @Override
    public Boolean objectEquivalent(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }

        if (left == null) {
            return false;
        }

        return this.equalsDeep((BaseType) left, (BaseType) right);
    }

    @Override
    public Object createInstance(String typeName) {
        return createInstance(resolveType(typeName));
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
                target = ((ArrayList<?>) target).get(index);
            } else {
                target = resolveProperty(target, identifier);
            }
        }

        return target;
    }

    @Override
    public Class<?> resolveType(String typeName) {
            // dataTypes
            BaseRuntimeElementDefinition<?> definition = this.fhirContext.getElementDefinition(typeName);
            if (definition != null) {
                return  definition.getImplementingClass();
            }
    
            try {
                // Resources
                return this.fhirContext.getResourceDefinition(typeName).getImplementingClass();
            }
            catch (Exception e) {}
            try {
                // Special case for enumerations. They are often in the "Enumerations" class.
                return Class.forName(String.format("%s.Enumerations$%s", packageName, typeName));
            }
            catch (ClassNotFoundException e) {}
            try {
                // Other Types in package.
                return Class.forName(String.format("%s.%s", packageName, typeName));
            }
            catch (ClassNotFoundException e) {}
    
            // Scan all resources.
            // Really, HAPI ought to register inner classes, right?
            Class<?> clazz = deepSearch(typeName);
            if (clazz != null) {
                return clazz;
            }
            
            try {
                // Just give me SOMETHING.
                return Class.forName(typeName);
            }
            catch (ClassNotFoundException e) {
                throw new UnknownType(String.format("Could not resolve type %s. Primary package for this resolver is %s", typeName, packageName));
            }
    }

    @Override
    public Class<?> resolveType(Object value) {
        if (value == null) {
            return Object.class;
        }

        // For FHIR enumerations, return the type of the backing Enum
        if (this.enumChecker(value)) {
            String factoryName = this.enumFactoryTypeGetter((EnumerationType) value).getSimpleName();
            return this.resolveType(factoryName.substring(0, factoryName.indexOf("EnumFactory")));
        }

        return value.getClass();
    }

    @Override
    public void setValue(Object target, String path, Object value) {
        if (target == null) {
            return;
        }

        if (target instanceof IBaseEnumeration && path.equals("value")) {
            ((IBaseEnumeration<?>)target).setValueAsString((String)value);
            return;
        }

        IBase base = (IBase) target;
        BaseRuntimeElementCompositeDefinition<?> definition;
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

        if (child == null) {
            throw new DataProviderException(String.format("Unable to resolve path %s.", path));
        }

        try {
            if (value instanceof Iterable) {
                for (Object val : (Iterable<?>) value) {
                    child.getMutator().addValue(base, (IBase) fromJavaPrimitive(val, base));
                }
            }
            else {
                child.getMutator().setValue(base, (IBase) fromJavaPrimitive(value, base));
            }
        } catch (IllegalArgumentException le) {
            if (value.getClass().getSimpleName().equals("Quantity")) {
                try {
                    value = this.castToSimpleQuantity((BaseType) value);
                } catch (FHIRException e) {
                    throw new InvalidCast("Unable to cast Quantity to SimpleQuantity");
                }
                child.getMutator().setValue(base, (IBase) fromJavaPrimitive(value, base));
            }
            else {
                throw new DataProviderException(String.format("Configuration error encountered: %s", le.getMessage()));
            }
        }
    }

    // getters & setters
    public FhirContext getFhirContext() {
        return this.fhirContext;
    }

    // Resolutions
    protected Object resolveProperty(Object target, String path) {
        if (target == null) {
            return null;
        }

        if (target instanceof IBaseEnumeration && path.equals("value")) {
            return ((IBaseEnumeration<?>) target).getValueAsString();
        }


        // TODO: Consider using getResourceType everywhere?
        if (target instanceof IAnyResource && this.getResourceType((ResourceType) target).equals(path)) {
            return target;
        }

        IBase base = (IBase) target;
        BaseRuntimeElementCompositeDefinition<?> definition;
        if (base instanceof IPrimitiveType) {
            return toJavaPrimitive(path.equals("value") ? ((IPrimitiveType<?>) target).getValue() : target, base);
        }
        else {
            definition = resolveRuntimeDefinition(base);
        }

        BaseRuntimeChildDefinition child = definition.getChildByName(path);
        if (child == null) {
            child = resolveChoiceProperty(definition, path);
        }

        if (child == null) {
            return null;
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

    protected BaseRuntimeElementCompositeDefinition<?> resolveRuntimeDefinition(IBase base) {
        if (base instanceof IAnyResource) {
            return getFhirContext().getResourceDefinition((IAnyResource) base);
        }

        else if (base instanceof IBaseBackboneElement || base instanceof IBaseElement) {
            return (BaseRuntimeElementCompositeDefinition<?>) getFhirContext().getElementDefinition(base.getClass());
        }

        else if (base instanceof ICompositeType) {
            return (BaseRuntimeElementCompositeDefinition<ICompositeType>) getFhirContext().getElementDefinition(base.getClass());
        }

        throw new UnknownType(String.format("Unable to resolve the runtime definition for %s", base.getClass().getName()));
    }

    protected BaseRuntimeChildDefinition resolveChoiceProperty(BaseRuntimeElementCompositeDefinition<?> definition, String path) {
        for (Object child :  definition.getChildren()) {
            if (child instanceof RuntimeChildChoiceDefinition) {
                RuntimeChildChoiceDefinition choiceDefinition = (RuntimeChildChoiceDefinition) child;

                if (choiceDefinition.getElementName().startsWith(path)) {
                    return choiceDefinition;
                }
            }
        }

        return null;
    }

    private Class<?> deepSearch(String typeName) {
        // Special case for "Codes". This suffix is often removed from the HAPI type.
        String codelessName = typeName.replace("Codes", "").toLowerCase();
        String lowerName = typeName.toLowerCase();


        Collection<BaseRuntimeElementDefinition<?>> elements = this.fhirContext.getElementDefinitions();
        for  (BaseRuntimeElementDefinition<?> element : elements) {
            Class<?>[] innerClasses = element.getImplementingClass().getDeclaredClasses();
            for (Class<?> clazz : innerClasses) {
                String clazzLowerName = clazz.getSimpleName().toLowerCase();
                if (clazzLowerName.equals(lowerName) || clazzLowerName.equals(codelessName)) {
                    return clazz;
                }
            }
        }

        return null;
    }

    // Creators
    protected Object createInstance(Class<?> clazz) {
        try {
            if (clazz.isEnum()) {
                Class<?> factoryClass = this.resolveType(clazz.getName() + "EnumFactory");
                EnumFactoryType factory = (EnumFactoryType)this.createInstance(factoryClass);
                return this.enumConstructor(factory);
            }

            return clazz.getDeclaredConstructor().newInstance();
        }
        catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new UnknownType(String.format("Could not create an instance of class %s.\nRoot cause: %s", clazz.getName(), e.getMessage()));
        }
    }

    // Transformations
    protected DateTime toDateTime(java.util.Date result) {
        return DateTime.fromJavaDate(result);
    }

    protected DateTime toDateTime(BaseDateTimeType value) {
        return toDateTime(value, this.getCalendarConstant(value));
    }

    protected org.opencds.cqf.cql.engine.runtime.Date toDate(BaseDateTimeType value) {
        return toDate(value, this.getCalendarConstant(value));
    }

    protected Time toTime(TimeType value) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ISO_TIME;
        return new Time(OffsetTime.from(formatter.parse(this.timeToString(value))), Precision.MILLISECOND);
    }

    protected DateTime toDateTime(BaseDateTimeType value, Integer calendarConstant) {
        Calendar calendar = this.getCalendar(value);

        TimeZone tz = calendar.getTimeZone() == null ? TimeZone.getDefault() : calendar.getTimeZone();
        ZoneOffset zoneOffset = tz.toZoneId().getRules().getStandardOffset(calendar.toInstant());
        switch (calendarConstant) {
            case Calendar.YEAR: return new DateTime(
                    TemporalHelper.zoneToOffset(zoneOffset),
                    calendar.get(Calendar.YEAR)
            );
            case Calendar.MONTH: return new DateTime(
                    TemporalHelper.zoneToOffset(zoneOffset),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1
            );
            case Calendar.DAY_OF_MONTH: return new DateTime(
                    TemporalHelper.zoneToOffset(zoneOffset),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)
            );
            case Calendar.HOUR_OF_DAY: return new DateTime(
                TemporalHelper.zoneToOffset(zoneOffset),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY)
            );
            case Calendar.MINUTE: return new DateTime(
                    TemporalHelper.zoneToOffset(zoneOffset),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)
            );
            case Calendar.SECOND: return new DateTime(
                    TemporalHelper.zoneToOffset(zoneOffset),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)
            );
            case Calendar.MILLISECOND: return new DateTime(
                    TemporalHelper.zoneToOffset(zoneOffset),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND)
            );
            default: throw new InvalidPrecision(String.format("Invalid temporal precision %s", calendarConstant));
        }
    }

    protected org.opencds.cqf.cql.engine.runtime.Date toDate(BaseDateTimeType value, Integer calendarConstant) {
        Calendar calendar = this.getCalendar(value);
        //TimeZone tz = calendar.getTimeZone() == null ? TimeZone.getDefault() : calendar.getTimeZone();
        //ZoneOffset zoneOffset = tz.toZoneId().getRules().getStandardOffset(calendar.toInstant());
        switch (calendarConstant) {
            case Calendar.YEAR: return new org.opencds.cqf.cql.engine.runtime.Date(calendar.get(Calendar.YEAR));
            case Calendar.MONTH: return new org.opencds.cqf.cql.engine.runtime.Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
            case Calendar.DAY_OF_MONTH: return new org.opencds.cqf.cql.engine.runtime.Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            default: throw new InvalidPrecision(String.format("Invalid temporal precision %s", calendarConstant));
        }
    }

    // TODO: Find HAPI registry of Primitive Type conversions
    protected Object fromJavaPrimitive(Object value, Object target) {
        String simpleName = target.getClass().getSimpleName();
        switch(simpleName) {
            case "DateTimeType":
            case "DateType":
                DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
                return java.util.Date.from(Instant.from(dtf.parse(((DateTime) value).getDateTime().toString())));
            case "TimeType":
                return ((Time) value).getTime().toString();
        }

        if (value instanceof Time) {
            return ((Time) value).getTime().toString();
        }
        else {
            return value;
        }
    }
    
    protected Object toJavaPrimitive(Object result, Object source) {
        String simpleName = source.getClass().getSimpleName();
        switch (simpleName) {
            case "DateTimeType": return toDateTime((BaseDateTimeType)source);
            case "DateType": return toDate((BaseDateTimeType)source);
            case "TimeType": return toTime((TimeType)source);
            case "InstantType": return toDateTime((BaseDateTimeType)source);
            case "IdType": return this.idToString((IdType)source);
            default:
                return result;
        }
    }
}