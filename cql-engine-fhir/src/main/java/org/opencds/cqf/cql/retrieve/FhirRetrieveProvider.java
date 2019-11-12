package org.opencds.cqf.cql.retrieve;

import org.opencds.cqf.cql.terminology.TerminologyProvider;

import ca.uhn.fhir.context.FhirContext;

public abstract class FhirRetrieveProvider implements RetrieveProvider {

    protected FhirContext fhirContext;
    protected TerminologyProvider terminologyProvider;

    public FhirRetrieveProvider(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    // TODO: Think about how to best handle the decision to expand value sets... Should it be part of the
    // terminology provider if it detects support for "code:in"? How does that feed back to the retriever?
    protected boolean expandValueSets;
    public boolean isExpandValueSets() {
        return expandValueSets;
    }
    public FhirRetrieveProvider setExpandValueSets(boolean expandValueSets) {
        this.expandValueSets = expandValueSets;
        return this;
    }

    public TerminologyProvider getTerminologyProvider() {
        return this.terminologyProvider;
    }

    public void setTerminologyProvider(TerminologyProvider terminologyProvider) {
        this.terminologyProvider = terminologyProvider;
    }
}

