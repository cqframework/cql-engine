library TestFhirExecution version '1.0'

using FHIR

context Patient

define MedicationRequests:
    [MedicationRequest]

define testCoalesce:
    MedicationRequests M
        return
            Coalesce(M.dosageInstruction[0].timing.repeat.frequencyMax.value, M.dosageInstruction[0].timing.repeat.frequency.value)