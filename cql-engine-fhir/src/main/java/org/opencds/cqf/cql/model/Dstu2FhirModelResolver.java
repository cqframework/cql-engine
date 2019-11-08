package org.opencds.cqf.cql.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;

import org.hl7.fhir.instance.model.*;

public class Dstu2FhirModelResolver extends  FhirModelResolver<Base, BaseDateTimeType, TimeType, SimpleQuantity, IdType> {

	public Dstu2FhirModelResolver() {
		this(FhirContext.forDstu2());
	}

	public Dstu2FhirModelResolver(FhirContext fhirContext) {
        super(
            fhirContext, 
            (x, y) -> x.equalsDeep(y),
            x -> x.castToSimpleQuantity(x),
            x -> x.toCalendar(),
            x -> x.getPrecision().getCalendarConstant(),
            x -> x.getValue(),
            x -> x.getIdPart()
        );

        this.setPackageName("org.hl7.fhir.instance.model");
        
        if (fhirContext.getVersion().getVersion() != FhirVersionEnum.DSTU2) {
            throw new IllegalArgumentException("The supplied context is not configured for DSTU2");
        }
	}
}