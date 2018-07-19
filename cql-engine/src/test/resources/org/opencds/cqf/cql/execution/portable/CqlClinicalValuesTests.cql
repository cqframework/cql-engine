library CqlClinicalValuesTests version '1'

define QuantityA:
  Quantity {
    unit: 'a',
    value: 12
  }

define CodeA:
  Code {
    system: 'http://loinc.org',
    code: '12345',
    version: '1',
    display: 'Test Code'
  }

define ConceptA:
  Concept {
    codes: { CodeA },
    display: 'Test Concept'
  }
