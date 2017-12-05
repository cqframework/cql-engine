library ClinicalOperator version '1'

codesystem "LOINC": 'http://loinc.org'

valueset "Feeding Intention-Breast": '2.16.840.1.113762.1.4.1045.29'

context Population

define TestPlan: Plan(300)


//Age
/*define PatientExpression: Patient { Name: 'Patrick', DOB: @2014-01-01 }
context Patient
define AgeYears: Ok(Equivalent(Patient.AgeInYears(), null), 'AgeYears')*/

//AgeAt

//CalculateAge
// define CalculateAgeYears: CalculateAgeInYears(DateTime(2000, 1, 1))
define CalculateAgeMonths: Ok(Equivalent(CalculateAgeInMonths(DateTime(2000, 1, 1)), null), 'CalculateAgeMonths')
define CalculateAgeDays: Ok(Equivalent(CalculateAgeInDays(DateTime(2000, 1, 1)), null), 'CalculateAgeDays')
define CalculateAgeHours: Ok(Equivalent(CalculateAgeInHours(DateTime(2000, 1, 1, 0)), null), 'CalculateAgeHours')
define CalculateAgeMinutes: Ok(Equivalent(CalculateAgeInMinutes(DateTime(2000, 1, 1, 0, 0)), null), 'CalculateAgeMinutes')
define CalculateAgeSeconds: Ok(Equivalent(CalculateAgeInSeconds(DateTime(2000, 1, 1, 0, 0, 0)), null), 'CalculateAgeSeconds')
define CalculateAgeUncertain: Ok(Equivalent(CalculateAgeInMonths(DateTime(2000)), null), 'CalculateAgeUncertain')

//CalculateAgeAt
define CalculateAgeAtYears: Ok(Equivalent(CalculateAgeInYearsAt(DateTime(2000, 1, 1), DateTime(2017, 1, 1)), null), 'CalculateAgeAtYears')
define CalculateAgeAtMonths: Ok(Equivalent(CalculateAgeInMonthsAt(DateTime(2000, 1, 1), DateTime(2016, 6, 14)), null), 'CalculateAgeAtMonths')
define CalculateAgeAtDays: Ok(Equivalent(CalculateAgeInDaysAt(DateTime(2000, 1, 1), DateTime(2016, 7, 13)), null), 'CalculateAgeAtDays')
define CalculateAgeAtHours: Ok(Equivalent(CalculateAgeInHoursAt(DateTime(2000, 1, 1, 0), DateTime(2016, 7, 13, 0)), null), 'CalculateAgeAtHours')
define CalculateAgeAtMinutes: Ok(Equivalent(CalculateAgeInMinutesAt(DateTime(2000, 1, 1, 0, 0), DateTime(2016, 7, 13, 0, 0)), null), 'CalculateAgeAtMinutes')
define CalculateAgeAtSeconds: Ok(Equivalent(CalculateAgeInSecondsAt(DateTime(2000, 1, 1, 0, 0, 0), DateTime(2016, 7, 13, 0, 0, 0)), null), 'CalculateAgeAtSeconds')
define CalculateAgeAtUncertain: Ok(Equivalent(CalculateAgeInMonthsAt(DateTime(2000), DateTime(2016, 7, 14)), null), 'CalculateAgeAtUncertain')

//Equal
define Code1: Ok(Equivalent(Code '8480-6' from "LOINC" display 'Systolic blood pressure', null), 'Code1')
define Code2: Ok(Equivalent(Code '8480-5' from "LOINC" display 'Systolic blood pressure', null), 'Code2')
define Concept1: Ok(Equivalent(Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis', null), 'Concept1')
define Concept2: Ok(Equivalent(Concept { Code '66071003' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis', null), 'Concept2')
define CodeEqualTrue: Ok(Equivalent(Code1 = Code1, null), 'CodeEqualTrue')
define CodeEqualFalse: Ok(Equivalent(Code1 = Code2, null), 'CodeEqualFalse')
define ConceptEqualTrue: Ok(Equivalent(Concept1 = Concept1, null), 'ConceptEqualTrue')
define ConceptEqualFalse: Ok(Equivalent(Concept1 = Concept2, null), 'ConceptEqualFalse')
define CodeEqualNull: Ok(Equivalent(Code1 = null, null), 'CodeEqualNull')
define ConceptEqualNull: Ok(Equivalent(Concept1 = null, null), 'ConceptEqualNull')

//Equivalent
define CodeEquivalentTrue: Ok(Equivalent(Code1 ~ Code1, null), 'CodeEquivalentTrue')
define CodeEquivalentFalse: Ok(Equivalent(Code1 ~ Code2, null), 'CodeEquivalentFalse')
define ConceptEquivalentTrue: Ok(Equivalent(Concept1 ~ Concept1, null), 'ConceptEquivalentTrue')
define ConceptEquivalentFalse: Ok(Equivalent(Concept1 ~ Concept2, null), 'ConceptEquivalentFalse')
define CodeEquivalentNull: Ok(Equivalent(Code1 ~ null, null), 'CodeEquivalentNull')
define ConceptEquivalentNull: Ok(Equivalent(Concept1 ~ null, null), 'ConceptEquivalentNull')
// TODO: These throw translator errors
/*define Code3: Code null from "LOINC" display 'Systolic blood pressure'
define Concept3: Ok(Equivalent(Concept { Code '66071003' from "LOINC", Code null from "LOINC" } display 'Type B viral hepatitis', null), 'Concept3')
define CodeEquivalentNullTrue: Ok(Equivalent(Code3 ~ Code3, null), 'CodeEquivalentNullTrue')
define CodeEquivalentNullFalse: Ok(Equivalent(Code2 ~ Code3, null), 'CodeEquivalentNullFalse')
define ConceptEquivalentNullTrue: Ok(Equivalent(Concept3 ~ Concept3, null), 'ConceptEquivalentNullTrue')
define ConceptEquivalentNullFalse: Ok(Equivalent(Concept2 ~ Concept3, null), 'ConceptEquivalentNullFalse')*/

//In (Codesystem) --> MOVED TO FHIR ENGINE
// TODO: additional tests once terminology provider is running
/*define InCodeSystemStringNull: '66071002' in "LOINC"
define InCodeSystemCodeNull: Ok(Equivalent(Code '8480-6' from "LOINC" display 'Systolic blood pressure' in "LOINC", null), 'InCodeSystemCodeNull')
define InCodeSystemConceptNull: Ok(Equivalent((Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'
    in "LOINC"), null), 'InCodeSystemConceptNull')*/

//In (Valueset) --> MOVED TO FHIR ENGINE
// TODO: additional tests once terminology provider is running
/*define InValueSetFalseString: '1963-2' in "Feeding Intention-Breast"
define InValueSetFalseCode: Ok(Equivalent(Code '8480-6' from "LOINC" display 'Systolic blood pressure' in "Feeding Intention-Breast", null), 'InValueSetFalseCode')
define InValueSetFalseConcept: Ok(Equivalent((Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'
    in "Feeding Intention-Breast"), null), 'InValueSetFalseConcept')*/


define function Plan(testCount Integer):
    Message(null, true, null as String, 'Message', '1..' + ToString(testCount))

define function Ok(isOk Boolean, message String):
    Message(null, true, null as String, 'Message', (if isOk then 'ok - ' else 'not ok - ') + message)

define function Todo(isOk2 Boolean, message2 String, whyTodo String):
    Ok(isOk2, message2 + ' # TODO - ' + whyTodo)
    // Note: parser/runtime has a scope-breaking bug manifesting if Todo() arg named "message" also.

define function Skipped(isOk2 Boolean, message2 String, whySkipped String):
    Ok(isOk2, message2 + ' # SKIPPED - ' + whySkipped)
    // Note: parser/runtime has a scope-breaking bug manifesting if Skipped() arg named "message" also.
