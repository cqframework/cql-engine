package org.opencds.cqf.cql.type;

import ca.uhn.fhir.context.FhirContext;

public class Dstu2FhirModelResolver extends FhirModelResolver {

	public Dstu2FhirModelResolver() {
		super(FhirContext.forDstu2());
	}

	public Dstu2FhirModelResolver(FhirContext fhirContext) {
		super(fhirContext);
	}

	@Override
	protected void setPackageName() {
		this.packageName = "org.hl7.fhir.instance.model";
	}

	@Override
	public Object resolveContextPath(String contextType, String targetType) {
        switch (targetType) {
            case "Patient":
                return "_id";
            default: return "patient";
        }
	}

}