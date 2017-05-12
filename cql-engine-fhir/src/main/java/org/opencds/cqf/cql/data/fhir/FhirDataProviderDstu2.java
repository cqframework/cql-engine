package org.opencds.cqf.cql.data.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IValueSetEnumBinder;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import org.apache.commons.lang3.EnumUtils;
import org.opencds.cqf.cql.data.DataProvider;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Time;
import org.opencds.cqf.cql.terminology.TerminologyProvider;
import org.opencds.cqf.cql.terminology.ValueSetInfo;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Christopher on 5/2/2017.
 */
public class FhirDataProviderDstu2 implements DataProvider {

    private FhirContext fhirContext;
    private String packageName;
    private String endpoint;
    private IGenericClient fhirClient;
    private boolean expandValueSets;

    private TerminologyProvider terminologyProvider;

    public FhirDataProviderDstu2() {
        this.fhirContext = FhirContext.forDstu2();
    }

    public TerminologyProvider getTerminologyProvider() {
        return terminologyProvider;
    }
    public void setTerminologyProvider(TerminologyProvider terminologyProvider) {
        this.terminologyProvider = terminologyProvider;
    }

    @Override
    public String getPackageName() { return this.packageName; }

    public void setPackageName(String packageName) { this.packageName = packageName; }
    public FhirDataProviderDstu2 withPackageName(String packageName) {
        setPackageName(packageName);
        return this;
    }

    public boolean getExpandValueSets() { return expandValueSets; }
    public void setExpandValueSets(boolean expandValueSets) { this.expandValueSets = expandValueSets; }

    public IGenericClient getFhirClient() { return fhirClient; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        fhirClient = fhirContext.newRestfulGenericClient(endpoint);
    }
    public FhirDataProviderDstu2 withEndpoint(String endpoint) {
        setEndpoint(endpoint);
        return this;
    }

    protected Object fromJavaPrimitive(Object value, Object target) {
        if (target instanceof DateTimeDt) {
            return new Date(); // TODO: Why is this so hard?
        }
        else if (target instanceof DateDt) {
            return new Date();
        }
        else if (target instanceof TimeDt) {
            if (value instanceof Time) {
                return ((Time) value).getPartial().toString();
            }
            return new Date();
        }
        else {
            return value;
        }
    }

    protected Class resolveReturnType(Class clazz, Object target, String path) {
        if (target instanceof PeriodDt) {
            if (path.equals("start") || path.equals("end")) {
                return DateTimeDt.class;
            }
        }
        else if (target instanceof QuantityDt) {
            if (path.equals("value")) {
                return DecimalDt.class;
            }
            else if (path.equals("unit")) {
                return StringDt.class;
            }
        }
        else if (target instanceof CodingDt) {
            if (path.equals("code")) {
                return CodeDt.class;
            }
            else if (path.equals("system")) {
                return UriDt.class;
            }
            else if (path.equals("version") || path.equals("display")) {
                return StringDt.class;
            }
        }
        else if (target instanceof CodeableConceptDt) {
            if (path.equals("text")) {
                return StringDt.class;
            }
        }
        return clazz;
    }

    @Override
    public Object resolvePath(Object target, String path) {
        String[] identifiers = path.split("\\.");
        for (String identifier : identifiers) {
            // handling indexes: item[0].code
            if (identifier.contains("[")) {
                int j = Character.getNumericValue(identifier.charAt(identifier.indexOf("[") + 1));
                target = resolveProperty(target, identifier.replaceAll("\\[\\d\\]", ""));
                target = ((ArrayList) target).get(j);
            } else
                target = resolveProperty(target, identifier);
        }

        return target;
    }

    @Override
    public Iterable<Object> retrieve(String context, Object contextValue, String dataType,
                                     String templateId, String codePath, Iterable<Code> codes,
                                     String valueSet, String datePath, String dateLowPath,
                                     String dateHighPath, Interval dateRange)
    {
        IQuery<Bundle> search;

        // TODO: It's unclear from the FHIR documentation whether we need to use a URLEncoder.encode call on the embedded system and valueset uris here...
        StringBuilder params = new StringBuilder();

        if (templateId != null && !templateId.equals("")) {
            params.append(String.format("_profile=%s", templateId));
        }

        if (codePath == null && (codes != null || valueSet != null)) {
            throw new IllegalArgumentException("A code path must be provided when filtering on codes or a valueset.");
        }

        if (context != null && context.equals("Patient") && contextValue != null) {
            if (params.length() > 0) {
                params.append("&");
            }

            params.append(String.format("%s=%s", getPatientSearchParam(dataType), URLEncode((String)contextValue)));
        }

        if (codePath != null && !codePath.equals("")) {
            if (params.length() > 0) {
                params.append("&");
            }
            if (valueSet != null && !valueSet.equals("")) {
                if (terminologyProvider != null && expandValueSets) {
                    ValueSetInfo valueSetInfo = new ValueSetInfo().withId(valueSet);
                    codes = terminologyProvider.expand(valueSetInfo);
                }
                else {
                    params.append(String.format("%s:in=%s", convertPathToSearchParam(dataType, codePath), URLEncode(valueSet)));
                }
            }

            if (codes != null) {
                StringBuilder codeList = new StringBuilder();
                for (Code code : codes) {
                    if (codeList.length() > 0) {
                        codeList.append(",");
                    }

                    if (code.getSystem() != null) {
                        codeList.append(URLEncode(code.getSystem()));
                        codeList.append("|");
                    }

                    codeList.append(URLEncode(code.getCode()));
                }
                params.append(String.format("%s=%s", convertPathToSearchParam(dataType, codePath), codeList.toString()));
            }
        }

        if (dateRange != null) {
            if (dateRange.getLow() != null) {
                String lowDatePath = convertPathToSearchParam(dataType, dateLowPath != null ? dateLowPath : datePath);
                if (lowDatePath == null || lowDatePath.equals("")) {
                    throw new IllegalArgumentException("A date path or low date path must be provided when filtering on a date range.");
                }

                params.append(String.format("&%s=%s%s",
                        lowDatePath,
                        dateRange.getLowClosed() ? "ge" : "gt",
                        dateRange.getLow().toString()));
            }

            if (dateRange.getHigh() != null) {
                String highDatePath = convertPathToSearchParam(dataType, dateHighPath != null ? dateHighPath : datePath);
                if (highDatePath == null || highDatePath.equals("")) {
                    throw new IllegalArgumentException("A date path or high date path must be provided when filtering on a date range.");
                }

                params.append(String.format("&%s=%s%s",
                        highDatePath,
                        dateRange.getHighClosed() ? "le" : "lt",
                        dateRange.getHigh().toString()));
            }
        }

        // TODO: Use compartment search for patient context?
        if (params.length() > 0) {
            search = fhirClient.search().byUrl(String.format("%s?%s", dataType, params.toString()));
        }
        else {
            search = fhirClient.search().byUrl(String.format("%s", dataType));
        }

        ca.uhn.fhir.model.dstu2.resource.Bundle results = cleanEntry(search.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class).execute(), dataType);

        return new FhirBundleCursorDstu2(fhirClient, results);
    }

    public IValueSetEnumBinder<Enum<?>> getBinder(Class clazz) {
        try {
            Field field = clazz.getField("VALUESET_BINDER");
            return (IValueSetEnumBinder<Enum<?>>) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Enumeration field access error in class " + clazz.getSimpleName());
        }
    }

    @Override
    public void setValue(Object target, String path, Object value) {
        if (target == null) {
            return;
        }

        Class<?> clazz = target.getClass();

        if (clazz.getSimpleName().contains("Enum") && path.equals("value")) {
            target = getBinder(clazz).fromCodeString(value.toString());
            return;
        }

        try {
            String readAccessorMethodName = String.format("%s%s%s", "get", path.substring(0, 1).toUpperCase(), path.substring(1));
            Method readAccessor = clazz.getMethod(readAccessorMethodName);

            String accessorMethodName = String.format("%s%s%s", "set", path.substring(0, 1).toUpperCase(), path.substring(1));
            Method accessor = clazz.getMethod(accessorMethodName, resolveReturnType(readAccessor.getReturnType(), target, path));
            accessor.invoke(target, fromJavaPrimitive(value, target));
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("Could not determine accessor function for property %s of type %s", path, clazz.getSimpleName()));
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("Errors occurred attempting to invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Could not invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
        }
    }

    protected Object resolveProperty(Object target, String path) {
        if (target == null) { return null; }

        // need this for some interesting mapping in Hapi for dstu2...
        path = mapPath(path);

        Class<?> clazz = target.getClass();

        if (clazz.getSimpleName().contains("Enum") && path.equals("value")) {
            return getBinder(clazz).toCodeString((Enum<?>) target);
        }

        try {
            String accessorMethodName = String.format("%s%s%s", "get", path.substring(0, 1).toUpperCase(), path.substring(1));
            String elementAccessorMethodName = String.format("%sElement", accessorMethodName);
            Method accessor;
            try {
                accessor = clazz.getMethod(elementAccessorMethodName);
            }
            catch (NoSuchMethodException e) {
                accessor = clazz.getMethod(accessorMethodName);
            }

            return mapPrimitive(accessor.invoke(target));

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("Could not determine accessor function for property %s of type %s", path, clazz.getSimpleName()));
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("Errors occurred attempting to invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Could not invoke the accessor function for property %s of type %s", path, clazz.getSimpleName()));
        }
    }

    private String mapPath(String path) {
        // TODO: map all [x] types here...
        switch(path) {
            case "medicationCodeableConcept": return "medication";
            case "medicationReference": return "medication";
        }
        return path;
    }

    private Object mapPrimitive(Object target) {
        if (target instanceof PeriodDt) {
            PeriodDt period = (PeriodDt) target;
            return new Interval(DateTime.fromJavaDate(period.getStart()), true, DateTime.fromJavaDate(period.getEnd()), true);
        }

        else if (target instanceof Date) {
            return DateTime.fromJavaDate((Date) target);
        }

//        else if (target instanceof CodingDt) {
//            CodingDt dt = (CodingDt) target;
//            return new Code()
//                    .withCode(dt.getCode())
//                    .withSystem(dt.getSystem())
//                    .withDisplay(dt.getDisplay())
//                    .withVersion(dt.getVersion());
//        }
//
//        else if (target instanceof Iterable) {
//            List<Object> list = new ArrayList<>();
//            for (Object o : (Iterable) target) {
//                list.add(mapPrimitive(o));
//            }
//            return list;
//        }

//        else if (target instanceof CodeableConceptDt) {
//            CodeableConceptDt dt = (CodeableConceptDt) target;
//            List<Code> codes = new ArrayList<>();
//            for (CodingDt code : dt.getCoding()) {
//                codes.add(new Code()
//                            .withCode(code.getCode())
//                            .withSystem(code.getSystem())
//                            .withDisplay(code.getDisplay())
//                            .withVersion(code.getVersion()));
//            }
//            return new Concept().withCodes(codes);
//        }

        return target;
    }

    private String getPatientSearchParam(String dataType) {
        switch (dataType) {
            case "Patient":
                return "_id";
            case "Observation":
            case "RiskAssessment":
                return "subject";
            default: return "patient";
        }
    }

    private String convertPathToSearchParam(String dataType, String codePath) {
        return codePath.replace('.', '-');
    }

    private String URLEncode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private ca.uhn.fhir.model.dstu2.resource.Bundle cleanEntry(ca.uhn.fhir.model.dstu2.resource.Bundle bundle, String dataType) {
        List<ca.uhn.fhir.model.dstu2.resource.Bundle.Entry> entry = new ArrayList<>();
        for (ca.uhn.fhir.model.dstu2.resource.Bundle.Entry comp : bundle.getEntry()){
            if (comp.getResource().getResourceName().equals(dataType)) {
                entry.add(comp);
            }
        }
        bundle.setEntry(entry);
        return bundle;
    }

    @Override
    public Class resolveType(Object value) {
        if (value == null) {
            return Object.class;
        }

        return value.getClass();
    }

    public Object createInstance(String typeName) {
        Class clazz = resolveType(typeName);
        if (clazz.getSimpleName().contains("Enum")) {
            return EnumUtils.getEnumList(clazz).get(0);
        }
        try {
            Object object = clazz.newInstance();
            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Could not create an instance of class %s.", clazz.getName()));
        }
    }

    @Override
    public Class resolveType(String typeName) {
        String truePackage = "ca.uhn.fhir.model.dstu2.composite";
        try {
            // TODO: Obviously would like to be able to automate this, but there is no programmatic way of which I'm aware
            // For the primitive types, not such a big deal.
            // For the enumerations, the type names are generated from the binding name in the spreadsheet, which doesn't make it to the StructureDefinition,
            // and the schema has no way of indicating whether the enum will be common (i.e. in Enumerations) or per resource
            switch (typeName) {
                // ca.uhn.fhir.model.dstu2.composite
                case "Coding": typeName = "CodingDt"; break;
                case "Quantity": typeName = "QuantityDt"; break;
                case "Period": typeName = "PeriodDt"; break;
                case "Range": typeName = "RangeDt"; break;
                case "CodeableConcept": typeName = "CodeableConceptDt"; break;
                case "AddressType": typeName = "AddressDt"; break;
                case "Timing": typeName = "TimingDt"; break;
                case "Money": typeName = "MoneyDt"; break;
                case "Count": typeName = "CountDt"; break;
                case "Distance": typeName = "DistanceDt"; break;
                case "Duration": typeName = "DurationDt"; break;
                case "SimpleQuantity": typeName = "SimpleQuantityDt"; break;
                case "Age": typeName = "AgeDt"; break;
                // ca.uhn.fhir.model.primitive
                case "base64Binary": typeName = "Base64BinaryDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "boolean": typeName = "BooleanDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "dateTime": typeName = "DateTimeDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "date": typeName = "DateDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "decimal": typeName = "DecimalDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "instant": typeName = "InstantDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "integer": typeName = "IntegerDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "positiveInt": typeName = "PositiveIntDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "unsignedInt": typeName = "UnsignedIntDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "string": typeName = "StringDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "code": typeName = "CodeDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "markdown": typeName = "MarkdownDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "time": typeName = "TimeDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "uri": typeName = "UriDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "uuid": typeName = "UriDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "id": typeName = "IdDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                case "oid": typeName = "OidDt"; truePackage = "ca.uhn.fhir.model.primitive"; break;
                // ca.uhn.fhir.model.dstu2.valueset -- Enums
                default: typeName += "Enum"; truePackage = "ca.uhn.fhir.model.dstu2.valueset"; break;

            }
            return Class.forName(String.format("%s.%s", truePackage, typeName));
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("Could not resolve type %s.%s.", truePackage, typeName));
        }
    }
}
