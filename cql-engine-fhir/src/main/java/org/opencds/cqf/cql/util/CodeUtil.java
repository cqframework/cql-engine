package org.opencds.cqf.cql.util;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.opencds.cqf.cql.runtime.Code;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition.IAccessor;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeCompositeDatatypeDefinition;

public class CodeUtil {

    public static List<Code> getElmCodesFromObject(Object object, FhirContext fhirContext) {
        return tryIterableThenConcept(fhirContext, object);
    }

    private static List<Code> tryIterableThenConcept(FhirContext fhirContext, Object object) {
        List<Code> codes = new ArrayList<Code>();
        if(object instanceof Iterable) {
            for (Object concept : (Iterable)object) {
                codes.addAll(tryConceptThenCoding(fhirContext, (IBase)concept));
            }
        }
        else {
            codes.addAll(tryConceptThenCoding(fhirContext, (IBase)object));
        }
        return codes;
    }

    private static List<Code> tryConceptThenCoding(FhirContext fhirContext, IBase object) {
        RuntimeCompositeDatatypeDefinition conceptDefinition = (RuntimeCompositeDatatypeDefinition)getElementDefinition(fhirContext, "CodeableConcept");
        List<IBase> codingObjects = getCodingObjectsFromDefinition(conceptDefinition, object);
        if(codingObjects == null) {
            return getCodesInCoding(fhirContext, object);
        }
        //would like to get the coding element definition from the codingObject rather than hardcoding it here
        RuntimeCompositeDatatypeDefinition codingDefinition = (RuntimeCompositeDatatypeDefinition)getElementDefinition(fhirContext, "Coding");
        return getCodeChildren(codingDefinition, codingObjects);
    }

    private static List<Code> getCodesInCoding(FhirContext fhirContext, IBase object) {
        //would like to get the coding element definition from the codingObject rather than hardcoding it here
        RuntimeCompositeDatatypeDefinition codingDefinition = (RuntimeCompositeDatatypeDefinition)getElementDefinition(fhirContext, "Coding");
        List<IBase> codingObjects = getCodingObjectsFromDefinition(codingDefinition, object);
        if (codingObjects == null) {
            return null;
        }
        return getCodeChildren(codingDefinition, codingObjects);
    }

    private static List<Code> getCodeChildren(RuntimeCompositeDatatypeDefinition codingDefinition, List<IBase> codingObjects) {
        BaseRuntimeChildDefinition versionDefinition = (BaseRuntimeChildDefinition)codingDefinition.getChildByName("version");
        BaseRuntimeChildDefinition codeDefinition = (BaseRuntimeChildDefinition)codingDefinition.getChildByName("code");
        BaseRuntimeChildDefinition systemDefinition = (BaseRuntimeChildDefinition)codingDefinition.getChildByName("system");
        BaseRuntimeChildDefinition displayDefinition = (BaseRuntimeChildDefinition)codingDefinition.getChildByName("display");

        return generateCodes(codingObjects, versionDefinition, codeDefinition, systemDefinition, displayDefinition);
    }

    private static List<Code> generateCodes(List<IBase> codingObjects, BaseRuntimeChildDefinition versionDefinition,
                                            BaseRuntimeChildDefinition codeDefinition, BaseRuntimeChildDefinition systemDefinition,
                                            BaseRuntimeChildDefinition displayDefinition) {

        List<Code> codes = new ArrayList<>();
        for (IBase coding : codingObjects) {
            String code = getStringValueFromPrimitiveDefinition(codeDefinition, coding);
            String display = getStringValueFromPrimitiveDefinition(displayDefinition, coding);
            String system = getStringValueFromPrimitiveDefinition(systemDefinition, coding);
			String version = getStringValueFromPrimitiveDefinition(versionDefinition, coding);
				codes.add(new Code()
					.withSystem(system)
					.withCode(code)
					.withDisplay(display)
					.withVersion(version));
        }
        return codes;
    }

    private static BaseRuntimeElementDefinition getElementDefinition(FhirContext fhirContext, String ElementName) {
        BaseRuntimeElementDefinition<?> def = fhirContext.getElementDefinition(ElementName);
        return def;
    }

    private static List<IBase> getCodingObjectsFromDefinition(RuntimeCompositeDatatypeDefinition definition, IBase object) {
        BaseRuntimeChildDefinition coding = (BaseRuntimeChildDefinition)definition.getChildByName("coding");
        List<IBase> codingObject = null;
        try {
            codingObject = coding.getAccessor().getValues(object);
        } catch (Exception e) {
            //TODO: handle exception
        }
        return codingObject;
    }

    private static String getStringValueFromPrimitiveDefinition(BaseRuntimeChildDefinition definition, IBase value) {
        IAccessor accessor = definition.getAccessor();
		if (value == null || accessor == null) {
			return null;
		}

		List<IBase> values = accessor.getValues(value);
		if (values == null || values.isEmpty()) {
			return null;
		}

		if (values.size() > 1) {
			throw new IllegalArgumentException("More than one value returned while attempting to access primitive value.");
		}

		IBase baseValue = values.get(0);

		if (!(baseValue instanceof IPrimitiveType)) {
			throw new IllegalArgumentException("Non-primitive value encountered while trying to access primitive value.");
		}
		else {
			return ((IPrimitiveType)baseValue).getValueAsString();
		}
	}

        
}