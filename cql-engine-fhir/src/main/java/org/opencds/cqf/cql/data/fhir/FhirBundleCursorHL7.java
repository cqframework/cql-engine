package org.opencds.cqf.cql.data.fhir;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Christopher Schuler on 6/19/2017.
 */
public class FhirBundleCursorHL7 implements Iterable<Object> {

    public FhirBundleCursorHL7(IGenericClient fhirClient, Bundle results) {
        this.fhirClient = fhirClient;
        this.results = results;
    }

    private IGenericClient fhirClient;
    private Bundle results;

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    public Iterator<Object> iterator() {
        return new FhirBundleIterator(fhirClient, results);
    }

    private class FhirBundleIterator implements Iterator<Object> {
        public FhirBundleIterator(IGenericClient fhirClient, Bundle results) {
            this.fhirClient = fhirClient;
            this.results = results;
            this.current = -1;
        }

        private IGenericClient fhirClient;
        private Bundle results;
        private int current;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        public boolean hasNext() {
            return current < results.getEntry().size() - 1
                    || results.getLink(IBaseBundle.LINK_NEXT) != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        public Object next() {
            current++;
            if (current < results.getEntry().size()) {
                return results.getEntry().get(current).getResource();
            } else {
                results = fhirClient.loadPage().next(results).execute();
                current = 0;
                if (current < results.getEntry().size()) {
                    return results.getEntry().get(current).getResource();
                }
            }

            // TODO: It would be possible to get here if the next link was present, but the returned page had 0 entries...
            throw new NoSuchElementException();
        }
    }
}
