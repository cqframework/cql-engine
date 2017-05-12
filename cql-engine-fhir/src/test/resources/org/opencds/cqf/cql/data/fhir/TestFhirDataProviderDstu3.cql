library TestFhirDataProviderDstu3 version '1.0'

using FHIR version '3.0.0'

context Patient

define MedReqs: [MedicationRequest]

define testChoiceTypes:
    MedReqs M
        return
            M.dosageInstruction[0].asNeeded