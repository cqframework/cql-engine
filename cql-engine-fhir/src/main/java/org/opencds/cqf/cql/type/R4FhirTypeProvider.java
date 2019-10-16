package org.opencds.cqf.cql.type;

import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.r4.model.EnumFactory;
import org.hl7.fhir.r4.model.Enumeration;

public class R4FhirTypeProvider extends FhirTypeProvider {

    @Override
    public Object createInstance(String typeName) {
        String className = resolveClassName(typeName);
        if (className.indexOf('$') >= 0) {
            className += "EnumFactory";
            return new org.hl7.fhir.r4.model.Enumeration((EnumFactory)createInstance(resolveClass(className)));
        }

        return createInstance(resolveClass(className));
    }

    @Override
    public Class resolveType(Object value) {
        if (value == null) {
            return Object.class;
        }

        if (value instanceof org.hl7.fhir.r4.model.Enumeration) {
            String className = ((org.hl7.fhir.r4.model.Enumeration)value).getEnumFactory().getClass().getName();
            try {
                className = className.substring(0, className.indexOf("EnumFactory"));
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(String.format("Could not resolve type %s", className));
            }
        }

        return value.getClass();
    }


    @Override
    public void setValue(Object target, String path, Object value) {
        if (target instanceof Enumeration && path.equals("value")) {
            ((Enumeration)target).setValueAsString((String)value);
            return;
        }

        super.setValue(target, path, value);
    }

    @Override
    protected Object resolveProperty(Object target, String path) {
        if (target instanceof Enumeration && path.equals("value")) {
            return ((Enumeration)target).getValueAsString();
        }

        // This is kind of a hack to get around contained resources - HAPI doesn't have ResourceContainer type for STU3
        if (target instanceof Resource && ((Resource) target).fhirType().equals(path))
        {
            return target;
        }

        return super.resolveProperty(target, path);
    }
}