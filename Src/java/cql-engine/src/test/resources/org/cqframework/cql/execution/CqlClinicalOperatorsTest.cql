library ClinicalOperator version '1'

codesystem "LOINC": 'http://loinc.org'

context Population

//Age
/*define PatientExpression: Patient { Name: 'Patrick', DOB: @2014-01-01 }
context Patient
define AgeYears: Patient.AgeInYears()*/

//AgeAt

//CalculateAge
// define CalculateAgeYears: CalculateAgeInYears(DateTime(2000, 1, 1))
define CalculateAgeMonths: CalculateAgeInMonths(DateTime(2000, 1, 1))
define CalculateAgeDays: CalculateAgeInDays(DateTime(2000, 1, 1))
define CalculateAgeHours: CalculateAgeInHours(DateTime(2000, 1, 1, 0))
define CalculateAgeMinutes: CalculateAgeInMinutes(DateTime(2000, 1, 1, 0, 0))
define CalculateAgeSeconds: CalculateAgeInSeconds(DateTime(2000, 1, 1, 0, 0, 0))
define CalculateAgeUncertain: CalculateAgeInMonths(DateTime(2000))

//CalculateAgeAt
define CalculateAgeAtYears: CalculateAgeInYearsAt(DateTime(2000, 1, 1), DateTime(2017, 1, 1))
define CalculateAgeAtMonths: CalculateAgeInMonthsAt(DateTime(2000, 1, 1), DateTime(2016, 6, 14))
define CalculateAgeAtDays: CalculateAgeInDaysAt(DateTime(2000, 1, 1), DateTime(2016, 7, 13))
define CalculateAgeAtHours: CalculateAgeInHoursAt(DateTime(2000, 1, 1, 0), DateTime(2016, 7, 13, 0))
define CalculateAgeAtMinutes: CalculateAgeInMinutesAt(DateTime(2000, 1, 1, 0, 0), DateTime(2016, 7, 13, 0, 0))
define CalculateAgeAtSeconds: CalculateAgeInSecondsAt(DateTime(2000, 1, 1, 0, 0, 0), DateTime(2016, 7, 13, 0, 0, 0))
define CalculateAgeAtUncertain: CalculateAgeInMonthsAt(DateTime(2000), DateTime(2016, 7, 14))

//Equal
define Code1: Code '8480-6' from "LOINC" display 'Systolic blood pressure'
define Code2: Code '8480-5' from "LOINC" display 'Systolic blood pressure'
define Concept1: Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'
define Concept2: Concept { Code '66071003' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'
define CodeEqualTrue: Code1 = Code1
define CodeEqualFalse: Code1 = Code2
define ConceptEqualTrue: Concept1 = Concept1
define ConceptEqualFalse: Concept1 = Concept2
define CodeEqualNull: Code1 = null
define ConceptEqualNull: Concept1 = null

//Equivalent
define CodeEquivalentTrue: Code1 ~ Code1
define CodeEquivalentFalse: Code1 ~ Code2
define ConceptEquivalentTrue: Concept1 ~ Concept1
define ConceptEquivalentFalse: Concept1 ~ Concept2
define CodeEquivalentNull: Code1 ~ null
define ConceptEquivalentNull: Concept1 ~ null
// TODO: These throw translator errors
/*define Code3: Code null from "LOINC" display 'Systolic blood pressure'
define Concept3: Concept { Code '66071003' from "LOINC", Code null from "LOINC" } display 'Type B viral hepatitis'
define CodeEquivalentNullTrue: Code3 ~ Code3
define CodeEquivalentNullFalse: Code2 ~ Code3
define ConceptEquivalentNullTrue: Concept3 ~ Concept3
define ConceptEquivalentNullFalse: Concept2 ~ Concept3*/

//In (Codesystem)
define InCodeSystemStringTrue: '66071002' in "LOINC"

//In (Valueset)
