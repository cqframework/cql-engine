package org.opencds.cqf.cql.engine.fhir.model;

import java.lang.reflect.Field;
import java.util.Calendar;

import org.hl7.fhir.dstu2.model.AnnotatedUuidType;
import org.hl7.fhir.dstu2.model.Base;
import org.hl7.fhir.dstu2.model.BaseDateTimeType;
import org.hl7.fhir.dstu2.model.EnumFactory;
import org.hl7.fhir.dstu2.model.Enumeration;
import org.hl7.fhir.dstu2.model.Enumerations;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.dstu2.model.Resource;
import org.hl7.fhir.dstu2.model.SimpleQuantity;
import org.hl7.fhir.dstu2.model.TimeType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;

public class Dstu2FhirModelResolver extends  FhirModelResolver<Base, BaseDateTimeType, TimeType, SimpleQuantity, IdType, Resource, Enumeration<?>, EnumFactory<?>> {

	public Dstu2FhirModelResolver() {
		this(FhirContext.forDstu2());
	}

	private Dstu2FhirModelResolver(FhirContext fhirContext) {
        super(fhirContext);

        this.setPackageName("org.hl7.fhir.dstu2.model");
        
        if (fhirContext.getVersion().getVersion() != FhirVersionEnum.DSTU2) {
            throw new IllegalArgumentException("The supplied context is not configured for DSTU2");
        }
    }

    protected void initialize() {
        // HAPI has some bugs where it's missing annotations on certain types. This patches that.
        this.fhirContext.registerCustomType(AnnotatedUuidType.class);

        // The context loads Resources on demand which can cause resolution to fail in certain cases
        // This forces all Resource types to be loaded.
        for (Enumerations.ResourceType type :Enumerations.ResourceType.values()) {
            // These are abstract types that should never be resolved directly.
            switch (type) {
                case DOMAINRESOURCE:
                case RESOURCE:
                case NULL:
                    continue;
                default:
            }

            this.fhirContext.getResourceDefinition(type.toCode());
        }
    }
    
    protected Boolean equalsDeep(Base left, Base right) {
        return left.equalsDeep(right);
    }

    protected SimpleQuantity castToSimpleQuantity(Base base) {
        return base.castToSimpleQuantity(base);
    }

    protected Calendar getCalendar(BaseDateTimeType dateTime) {
        return dateTime.toCalendar();
    }

    protected Integer getCalendarConstant(BaseDateTimeType dateTime) {
        return dateTime.getPrecision().getCalendarConstant();
    }

    protected String timeToString(TimeType time) {
        return time.getValue();
    }

    protected String idToString(IdType id) {
        return id.getIdPart();
    }

    protected String getResourceType(Resource resource) {
        return resource.fhirType();
    }

    protected Enumeration<?> enumConstructor(EnumFactory<?> factory) {
        return new Enumeration<>(factory);
    }

    protected Boolean enumChecker(Object object) {
        return object instanceof Enumeration;
    }

    protected Class<?> enumFactoryTypeGetter(Enumeration<?> enumeration) {
        Enum<?> value = enumeration.getValue();
        if (value != null) {
            String enumSimpleName = value.getClass().getSimpleName();
            return this.resolveType(enumSimpleName + "EnumFactory");
        }
        else {
            try
            {
                Field myEnumFactoryField = enumeration.getClass().getDeclaredField("myEnumFactory");
                myEnumFactoryField.setAccessible(true);
                EnumFactory<?> factory = (EnumFactory<?>)myEnumFactoryField.get(enumeration);
                return factory.getClass();
            }
            catch (Exception e) {
                return null;
            }
        }
    }
}