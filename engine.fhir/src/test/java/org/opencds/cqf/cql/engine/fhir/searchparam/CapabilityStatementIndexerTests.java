package org.opencds.cqf.cql.engine.fhir.searchparam;

import org.hl7.fhir.r4.model.CapabilityStatement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class CapabilityStatementIndexerTests {
    FhirContext fhirContext;
    CapabilityStatementIndexer capabilityStatementIndexer;
    IParser jsonParser;

    @BeforeClass
    public void setup() {
        this.fhirContext = FhirContext.forR4();
        this.jsonParser = this.fhirContext.newJsonParser();
        this.capabilityStatementIndexer = new CapabilityStatementIndexer(fhirContext);

    }

    protected CapabilityStatementIndex getIndex(String resourceName) {
        CapabilityStatement capabilityStatement = this.jsonParser.parseResource(CapabilityStatement.class,
                CapabilityStatementIndexerTests.class.getResourceAsStream(resourceName));

        return this.capabilityStatementIndexer.index(capabilityStatement);
    }

    @Test
    public void testEpicCapabilityStatement() {
        CapabilityStatementIndex index = this.getIndex("sample-epic-capability-statement.json");

        assertFalse(index.supportsResource("NotAResource"));

        assertTrue(index.supportsResource("Patient"));

        assertFalse(index.supportsInteraction("Patient", "gumbo"));

        assertTrue(index.supportsInteraction("Patient", "read"));

        assertFalse(index.supportsSearchParam("Patient", "_id"));

        assertTrue(index.supportsSearchParam("Patient", "address"));
    }


    @Test
    public void testCernerCapabilityStatement() {
        CapabilityStatementIndex index = this.getIndex("sample-cerner-capability-statement.json");

        assertFalse(index.supportsResource("NotAResource"));

        assertTrue(index.supportsResource("Patient"));

        assertFalse(index.supportsInteraction("Patient", "gumbo"));

        assertTrue(index.supportsInteraction("Patient", "read"));

        assertFalse(index.supportsSearchParam("Patient", "address"));

        assertTrue(index.supportsSearchParam("Patient", "_id"));
    }
}
