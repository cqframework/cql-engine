package org.opencds.cqf.cql.type;

import org.hl7.fhir.dstu3.model.EnumFactory;
import org.opencds.cqf.cql.exception.UnknownType;

public class Dstu3FhirTypeProvider extends FhirTypeProvider {

    @Override
    public Object createInstance(String typeName) {
        String className = resolveClassName(typeName);
        if (className.indexOf('$') >= 0) {
            className += "EnumFactory";

            return new org.hl7.fhir.dstu3.model.Enumeration((EnumFactory)createInstance(resolveClass(className)));
        }

        return createInstance(resolveClass(className));
    }

    @Override
    public Class resolveType(Object value) {
        if (value == null) {
            return Object.class;
        }

        if (value instanceof org.hl7.fhir.dstu3.model.Enumeration) {
            String className = ((org.hl7.fhir.dstu3.model.Enumeration)value).getEnumFactory().getClass().getName();
            try {
                className = className.substring(0, className.indexOf("EnumFactory"));
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new UnknownType(String.format("Could not resolve type %s", className));
            }
        }

        return value.getClass();
    }
}