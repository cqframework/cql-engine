package org.opencds.cqf.cql.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;

import java.util.Calendar;

import org.hl7.fhir.dstu2.model.*;

public class HL7FhirModelResolver extends FhirModelResolver<Base, BaseDateTimeType, TimeType, SimpleQuantity, IdType, Resource, Enumeration<?>, EnumFactory<?>> {

	public HL7FhirModelResolver() {
		this(FhirContext.forDstu2Hl7Org());
    }
    
    private  HL7FhirModelResolver(FhirContext fhirContext) {
        super(fhirContext);

        this.setPackageName("org.hl7.fhir.instance.model");
        
        if (fhirContext.getVersion().getVersion() != FhirVersionEnum.DSTU2_HL7ORG) {
            throw new IllegalArgumentException("The supplied context is not configured for DSTU2_HL7ORG");
        }
    }

    // TODO: Figure out the correct types to initialize for the HL7 Resolver
    // It uses a subset of Dstu2
    protected void initialize() {
        // The context loads Resources on demand which can cause resolution to fail in certain cases
        // This forces all Resource types to be loaded.
        // for (Enumerations.ResourceType type :Enumerations.ResourceType.values()) {
        //     // These are abstract types that should never be resolved directly.
        //     switch (type) {
        //         case DOMAINRESOURCE:
        //         case RESOURCE:
        //         case NULL:
        //             continue;
        //         default:
        //     }

        //     this.fhirContext.getResourceDefinition(type.toCode());
        // }
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
        String enumSimpleName = enumeration.getValue().getClass().getSimpleName();
        return this.resolveType(enumSimpleName + "EnumFactory");
    }
}