package org.opencds.cqf.cql.type;

import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.*;
import org.hl7.fhir.instance.model.api.*;
// import org.hl7.fhir.instance.model.Base;
// import org.hl7.fhir.instance.model.BaseDateTimeType;
// import org.hl7.fhir.instance.model.DateTimeType;
// import org.hl7.fhir.instance.model.DateType;
// import org.hl7.fhir.instance.model.IdType;
// import org.hl7.fhir.instance.model.InstantType;
// import org.hl7.fhir.instance.model.Quantity;
// import org.hl7.fhir.instance.model.TemporalPrecisionEnum;
// import org.hl7.fhir.instance.model.TimeType;
// import org.hl7.fhir.instance.model.api.IAnyResource;
// import org.hl7.fhir.instance.model.api.IBase;
// import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
// import org.hl7.fhir.instance.model.api.IBaseElement;
// import org.hl7.fhir.instance.model.api.ICompositeType;
// import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.opencds.cqf.cql.data.ModelResolver;
import org.opencds.cqf.cql.exception.DataProviderException;
import org.opencds.cqf.cql.exception.InvalidCast;
import org.opencds.cqf.cql.exception.InvalidPrecision;
import org.opencds.cqf.cql.exception.UnknownPath;
import org.opencds.cqf.cql.exception.UnknownType;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Precision;
import org.opencds.cqf.cql.runtime.TemporalHelper;
import org.opencds.cqf.cql.runtime.Time;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;

public abstract class FhirModelResolver implements ModelResolver {

    public FhirModelResolver(FhirContext fhirContext) {
		this.fhirContext = fhirContext;
		this.setPackageName();
	}
	
	protected abstract void setPackageName();

    // Data members
    protected FhirContext fhirContext;
    protected String packageName;

    // getters & setters
    public FhirContext getFhirContext() {
        return fhirContext;
    }
    public void setFhirContext(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
        fhirContext.getRestfulClientFactory().setSocketTimeout(1200 * 10000);
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
            return (BaseRuntimeElementCompositeDefinition<ICompositeType>) getFhirContext().getElementDefinition(base.getClass());
        }

        throw new UnknownType(String.format("Unable to resolve the runtime definition for %s", base.getClass().getName()));
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

        throw new UnknownPath(String.format("Unable to resolve path %s for %s", path, definition.getName()));
    }

    protected Class resolveClass(String className) {
        try {
            return Class.forName(String.format("%s.%s", packageName, className));
        }
        catch (ClassNotFoundException e) {
            throw new UnknownType(String.format("Could not resolve type %s.%s.", packageName, className));
        }
    }

    // Creators
    protected Object createInstance(Class clazz) {
        try {
            return clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {
            throw new UnknownType(String.format("Could not create an instance of class %s.\nRoot cause: %s", clazz.getName(), e.getMessage()));
        }
    }

    @Override
    public Object createInstance(String typeName) {
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
                    throw new InvalidCast("Unable to cast Quantity to SimpleQuantity");
                }
                child.getMutator().setValue(base, (IBase) fromJavaPrimitive(value, base));
            }
            else {
                throw new DataProviderException(String.format("Configuration error encountered: %s", ce.getMessage()));
            }
        }
    }

    @Override
    public Boolean objectEqual(Object left, Object right) {
        if (left == null) {
            return null;
        }

        if (right == null) {
            return null;
        }

        Base base = (Base)left;
        return base.equalsDeep((Base)right);
    }

    @Override
    public Boolean objectEquivalent(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }

        if (left == null) {
            return false;
        }

        Base base = (Base)left;
        return base.equalsDeep((Base)right);
    }

    // Transformations
    protected DateTime toDateTime(java.util.Date result) {
        return DateTime.fromJavaDate(result);
    }
    

    // @Override
    // public String resolveContextPath(String contextType, String dataType) {
	// 	switch (contextType) {
	// 		case "Patient":
	// 			switch (dataType) {
	// 				case "Patient":
	// 					return "id";
	// 				default: return "patient";
	// 			}
		
	// 		default:
	// 			break;
	// 	}
		
    //     return null;
    // }

    // protected String convertPathToSearchParam(String type, String path) {
    //     return null;
    // }

	@Override
    public String resolveClassName(String typeName) {
        return null;
    }

    protected DateTime toDateTime(DateTimeType value) {
        return toDateTime(value, value.getPrecision());
    }

    protected org.opencds.cqf.cql.runtime.Date toDate(DateType value) {
        return toDate(value, value.getPrecision());
    }

    protected Time toTime(TimeType value) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ISO_TIME;
        return new Time(OffsetTime.from(formatter.parse(value.getValue())), Precision.MILLISECOND);
    }

    protected DateTime toDateTime(BaseDateTimeType value, TemporalPrecisionEnum precision) {
        Calendar calendar = value.toCalendar();
        TimeZone tz = calendar.getTimeZone() == null ? TimeZone.getDefault() : calendar.getTimeZone();
        ZoneOffset zoneOffset = tz.toZoneId().getRules().getStandardOffset(calendar.toInstant());
        switch (precision) {
            case YEAR: return new DateTime(
                    TemporalHelper.zoneToOffset(zoneOffset),
                    calendar.get(Calendar.YEAR)
            );
            case MONTH: return new DateTime(
                    TemporalHelper.zoneToOffset(zoneOffset),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1
            );
            case DAY: return new DateTime(
                    TemporalHelper.zoneToOffset(zoneOffset),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)
            );
            case MINUTE: return new DateTime(
                    TemporalHelper.zoneToOffset(zoneOffset),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)
            );
            case SECOND: return new DateTime(
                    TemporalHelper.zoneToOffset(zoneOffset),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)
            );
            case MILLI: return new DateTime(
                    TemporalHelper.zoneToOffset(zoneOffset),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND)
            );
            default: throw new InvalidPrecision(String.format("Invalid temporal precision %s", value.getPrecision().toString()));
        }
    }

    protected org.opencds.cqf.cql.runtime.Date toDate(BaseDateTimeType value, TemporalPrecisionEnum precision) {
        Calendar calendar = value.toCalendar();
        TimeZone tz = calendar.getTimeZone() == null ? TimeZone.getDefault() : calendar.getTimeZone();
        ZoneOffset zoneOffset = tz.toZoneId().getRules().getStandardOffset(calendar.toInstant());
        switch (precision) {
            case YEAR: return new org.opencds.cqf.cql.runtime.Date(calendar.get(Calendar.YEAR));
            case MONTH: return new org.opencds.cqf.cql.runtime.Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
            case DAY: return new org.opencds.cqf.cql.runtime.Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            default: throw new InvalidPrecision(String.format("Invalid temporal precision %s", value.getPrecision().toString()));
        }
    }

    protected DateTime toDateTime(InstantType value) {
        return toDateTime(value, value.getPrecision());
    }

    protected Object fromJavaPrimitive(Object value, Object target) {
        if (target instanceof DateTimeType || target instanceof DateType) {
            DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
            return java.util.Date.from(Instant.from(dtf.parse(((DateTime) value).getDateTime().toString())));
        }
        else if (target instanceof TimeType && value instanceof Time) {
            return ((Time) value).getTime().toString();
        }
        else {
            return value;
        }
    }
    
    protected Object toJavaPrimitive(Object result, Object source) {
        if (source instanceof DateTimeType) {
            return toDateTime((DateTimeType)source);
        }
        else if (source instanceof DateType) {
            return toDate((DateType)source);
        }
        else if (source instanceof TimeType) {
            return toTime((TimeType)source);
        }
        else if (source instanceof InstantType) {
            return toDateTime((InstantType)source);
        }
        else if (source instanceof IdType) {
            return ((IdType) source).getIdPart();
        }
        else {
            return result;
        }
    }
}