library TestFhirDataProviderDstu2 version '1.0'

using FHIR version '1.0.2'

define testString:
    [Encounter] E where E.status.value = 'arrived'

define testCode:
    [Procedure] P where P.code.coding[0].code.value = '93000'

define testDate:
    [Appointment] A where A."start".value before year of DateTime(2014, 10, 10)