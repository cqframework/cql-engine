library TestFhirExecution version '1.0'

using FHIR

parameter MAXYEAR Integer

context Patient

define MedicationRequests:
    [MedicationRequest]

define testCoalesce:
    MedicationRequests M
        return
            Coalesce(M.dosageInstruction[0].timing.repeat.frequencyMax.value, M.dosageInstruction[0].timing.repeat.frequency.value)

define MeasurementPeriod:
    Interval[DateTime(MAXYEAR - 1, 10, 1), DateTime(MAXYEAR, 4, 30)]

define testMonthFrom:
    [Immunization] I
        where I.date.value in MeasurementPeriod
