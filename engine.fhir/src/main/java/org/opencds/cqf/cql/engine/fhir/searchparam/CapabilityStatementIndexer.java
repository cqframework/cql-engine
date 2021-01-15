package org.opencds.cqf.cql.engine.fhir.searchparam;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseConformance;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;

public class CapabilityStatementIndexer {

    private FhirContext fhirContext;
    private IFhirPath fhirPath;

    public CapabilityStatementIndexer(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
        this.fhirPath = this.fhirContext.newFhirPath();
    }

    public CapabilityStatementIndex index(IBaseConformance capabilityStatement) {
        Objects.requireNonNull(capabilityStatement, "capabilityStatement can not be null");

        CapabilityStatementIndex index = new CapabilityStatementIndex();
        List<IBase> resources = this.fhirPath.evaluate(capabilityStatement, "CapabilityStatement.rest.where(mode='server').resource", IBase.class);
        if (resources == null || resources.isEmpty()) {
            return index;
        }

        for (IBase resource : resources) {
            indexResource(index, resource);
        }

        return index;
    }

    private void indexResource(CapabilityStatementIndex index, IBase resource) {
        Optional<IBase> type = this.fhirPath.evaluateFirst(resource, "type", IBase.class);
        List<IBase> interactions = this.fhirPath.evaluate(resource, "interaction.code.value", IBase.class);
        List<IBase> searchParams = this.fhirPath.evaluate(resource, "searchParam.name", IBase.class);

        index.putResource(type.get().toString(), convertToSet(interactions), convertToSet(searchParams));
    }

    Set<String> convertToSet(List<IBase> baseStrings) {
        Set<String> result = new HashSet<>();
        for (IBase base : baseStrings) {
            result.add(base.toString());
        }

        return result;
    }
}
