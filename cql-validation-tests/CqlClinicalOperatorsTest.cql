library ClinicalOperator version '1'

codesystem "LOINC": 'http://loinc.org'

valueset "Feeding Intention-Breast": '2.16.840.1.113762.1.4.1045.29'

context Population

define _0: plan(300)


//Age
/*define PatientExpression: Patient { Name: 'Patrick', DOB: @2014-01-01 }
context Patient
define AgeYears: ok(Equivalent((Patient.AgeInYears()),(null)), 'AgeYears')*/

//AgeAt

//CalculateAge
// define CalculateAgeYears: CalculateAgeInYears(DateTime(2000, 1, 1))
define CalculateAgeMonths: ok(Equivalent((CalculateAgeInMonths(DateTime(2000, 1, 1))),(null)), 'CalculateAgeMonths')
define CalculateAgeDays: ok(Equivalent((CalculateAgeInDays(DateTime(2000, 1, 1))),(null)), 'CalculateAgeDays')
define CalculateAgeHours: ok(Equivalent((CalculateAgeInHours(DateTime(2000, 1, 1, 0))),(null)), 'CalculateAgeHours')
define CalculateAgeMinutes: ok(Equivalent((CalculateAgeInMinutes(DateTime(2000, 1, 1, 0, 0))),(null)), 'CalculateAgeMinutes')
define CalculateAgeSeconds: ok(Equivalent((CalculateAgeInSeconds(DateTime(2000, 1, 1, 0, 0, 0))),(null)), 'CalculateAgeSeconds')
define CalculateAgeUncertain: ok(Equivalent((CalculateAgeInMonths(DateTime(2000))),(null)), 'CalculateAgeUncertain')

//CalculateAgeAt
define CalculateAgeAtYears: ok(Equivalent((CalculateAgeInYearsAt(DateTime(2000, 1, 1), DateTime(2017, 1, 1))),(null)), 'CalculateAgeAtYears')
define CalculateAgeAtMonths: ok(Equivalent((CalculateAgeInMonthsAt(DateTime(2000, 1, 1), DateTime(2016, 6, 14))),(null)), 'CalculateAgeAtMonths')
define CalculateAgeAtDays: ok(Equivalent((CalculateAgeInDaysAt(DateTime(2000, 1, 1), DateTime(2016, 7, 13))),(null)), 'CalculateAgeAtDays')
define CalculateAgeAtHours: ok(Equivalent((CalculateAgeInHoursAt(DateTime(2000, 1, 1, 0), DateTime(2016, 7, 13, 0))),(null)), 'CalculateAgeAtHours')
define CalculateAgeAtMinutes: ok(Equivalent((CalculateAgeInMinutesAt(DateTime(2000, 1, 1, 0, 0), DateTime(2016, 7, 13, 0, 0))),(null)), 'CalculateAgeAtMinutes')
define CalculateAgeAtSeconds: ok(Equivalent((CalculateAgeInSecondsAt(DateTime(2000, 1, 1, 0, 0, 0), DateTime(2016, 7, 13, 0, 0, 0))),(null)), 'CalculateAgeAtSeconds')
define CalculateAgeAtUncertain: ok(Equivalent((CalculateAgeInMonthsAt(DateTime(2000), DateTime(2016, 7, 14))),(null)), 'CalculateAgeAtUncertain')

//Equal
define Code1: ok(Equivalent((Code '8480-6' from "LOINC" display 'Systolic blood pressure'),(null)), 'Code1')
define Code2: ok(Equivalent((Code '8480-5' from "LOINC" display 'Systolic blood pressure'),(null)), 'Code2')
define Concept1: ok(Equivalent((Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'),(null)), 'Concept1')
define Concept2: ok(Equivalent((Concept { Code '66071003' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'),(null)), 'Concept2')
define CodeEqualTrue: ok(Equivalent((Code1 = Code1),(null)), 'CodeEqualTrue')
define CodeEqualFalse: ok(Equivalent((Code1 = Code2),(null)), 'CodeEqualFalse')
define ConceptEqualTrue: ok(Equivalent((Concept1 = Concept1),(null)), 'ConceptEqualTrue')
define ConceptEqualFalse: ok(Equivalent((Concept1 = Concept2),(null)), 'ConceptEqualFalse')
define CodeEqualNull: ok(Equivalent((Code1 = null),(null)), 'CodeEqualNull')
define ConceptEqualNull: ok(Equivalent((Concept1 = null),(null)), 'ConceptEqualNull')

//Equivalent
define CodeEquivalentTrue: ok(Equivalent((Code1 ~ Code1),(null)), 'CodeEquivalentTrue')
define CodeEquivalentFalse: ok(Equivalent((Code1 ~ Code2),(null)), 'CodeEquivalentFalse')
define ConceptEquivalentTrue: ok(Equivalent((Concept1 ~ Concept1),(null)), 'ConceptEquivalentTrue')
define ConceptEquivalentFalse: ok(Equivalent((Concept1 ~ Concept2),(null)), 'ConceptEquivalentFalse')
define CodeEquivalentNull: ok(Equivalent((Code1 ~ null),(null)), 'CodeEquivalentNull')
define ConceptEquivalentNull: ok(Equivalent((Concept1 ~ null),(null)), 'ConceptEquivalentNull')
// TODO: These throw translator errors
/*define Code3: Code null from "LOINC" display 'Systolic blood pressure'
define Concept3: ok(Equivalent((Concept { Code '66071003' from "LOINC", Code null from "LOINC" } display 'Type B viral hepatitis'),(null)), 'Concept3')
define CodeEquivalentNullTrue: ok(Equivalent((Code3 ~ Code3),(null)), 'CodeEquivalentNullTrue')
define CodeEquivalentNullFalse: ok(Equivalent((Code2 ~ Code3),(null)), 'CodeEquivalentNullFalse')
define ConceptEquivalentNullTrue: ok(Equivalent((Concept3 ~ Concept3),(null)), 'ConceptEquivalentNullTrue')
define ConceptEquivalentNullFalse: ok(Equivalent((Concept2 ~ Concept3),(null)), 'ConceptEquivalentNullFalse')*/

//In (Codesystem) --> MOVED TO FHIR ENGINE
// TODO: additional tests once terminology provider is running
/*define InCodeSystemStringNull: '66071002' in "LOINC"
define InCodeSystemCodeNull: ok(Equivalent((Code '8480-6' from "LOINC" display 'Systolic blood pressure' in "LOINC"),(null)), 'InCodeSystemCodeNull')
define InCodeSystemConceptNull: ok(Equivalent((Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'
    in "LOINC"),(null)), 'InCodeSystemConceptNull')*/

//In (Valueset) --> MOVED TO FHIR ENGINE
// TODO: additional tests once terminology provider is running
/*define InValueSetFalseString: '1963-2' in "Feeding Intention-Breast"
define InValueSetFalseCode: ok(Equivalent((Code '8480-6' from "LOINC" display 'Systolic blood pressure' in "Feeding Intention-Breast"),(null)), 'InValueSetFalseCode')
define InValueSetFalseConcept: ok(Equivalent((Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'
    in "Feeding Intention-Breast"),(null)), 'InValueSetFalseConcept')*/


define function plan(test_count Integer):
    Message(null, true, null as String, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null as String, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
