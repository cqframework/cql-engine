package org.opencds.cqf.cql.retrieve;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.Bundle;
import org.opencds.cqf.cql.exception.UnknownElement;

import java.util.Iterator;
import java.util.List;

public class FhirBundlesCursor implements Iterable<Object> {

    public  FhirBundlesCursor(IGenericClient fhirClient, List<Bundle> bundles) {
        this(fhirClient, bundles, null);
    }
    public FhirBundlesCursor(IGenericClient fhirClient, List<Bundle> bundles, String dataType) {
        this.fhirClient = fhirClient;
        this.bundles = bundles;
        this.dataType = dataType;
    }

    private IGenericClient fhirClient;
    private List<Bundle> bundles;
    private String dataType;

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    public Iterator<Object> iterator() {
        return new FhirBundlesIterator(fhirClient, bundles);
    }

    private class FhirBundlesIterator implements Iterator<Object> {
        public FhirBundlesIterator(IGenericClient fhirClient, List<Bundle> bundles) {
            this.fhirClient = fhirClient;
            this.bundles = bundles;
            this.currentBundle = -1;
        }

        private IGenericClient fhirClient;
        private List<Bundle> bundles;
        private int currentBundle;
        private Iterator<Object> currentIterator;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        public boolean hasNext() {
            return currentBundle < bundles.size() || 
                (currentIterator != null && currentIterator.hasNext());
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws UnknownElement if the iteration has no more elements
         */
        public Object next() {
            if (currentIterator != null && currentIterator.hasNext()) {
                return currentIterator.next();
            }

            while (currentBundle < bundles.size()) {
                currentBundle++;
                this.currentIterator = new FhirBundleCursor(this.fhirClient, bundles.get(currentBundle), dataType).iterator();
                if (this.currentIterator.hasNext()) {
                    return currentIterator.next();
                }
            }

            throw new UnknownElement("The iteration has no more elements.");
        }
    }
}
