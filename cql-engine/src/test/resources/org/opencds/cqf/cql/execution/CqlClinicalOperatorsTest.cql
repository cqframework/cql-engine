library ClinicalOperator version '1'

codesystem "LOINC": 'http://loinc.org'

valueset "Feeding Intention-Breast": '2.16.840.1.113762.1.4.1045.29'

context Population

//Age
/*define PatientExpression: Patient { Name: 'Patrick', DOB: @2014-01-01 }
context Patient
define AgeYears: Patient.AgeInYears()*/

//AgeAt

//CalculateAge
define CalculateAgeYears: CalculateAgeInYears(DateTime(2000, 1, 1))
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

// CodeAttrsCompare
/* The document "CTO Errata Letter-CQL R1.2.pdf" dated 2017-10-14,
section "Code Equivalence Semantics - STU Comment 1431",
prescribes that Equivalent(Code,Code) should ignore "version" attributes,
as it already ignores "display" attributes;
but Equal(Code,Code) should still respect both.
See also CQL spec section 9.5.3 Equivalent.
See also http://www.hl7.org/dstucomments/showdetail_comment.cfm?commentid=1431 */
// TODO: Revisit later to test all combinations of the 4 attributes being same or different.
define private C0000: Code { code: 'ABC', display: 'Code ABC', system: 'http://example.com', version: '2017-01' }
define private C0000b: Code { code: 'ABC', display: 'Code ABC', system: 'http://example.com', version: '2017-01' }
define private C0001: Code { code: 'ABC', display: 'Code ABC', system: 'http://example.com', version: '2017-05' }
define private C0010: Code { code: 'ABC', display: 'Code ABC', system: 'http://another.example.com', version: '2017-01' }
define private C0100: Code { code: 'ABC', display: 'Var Desc', system: 'http://example.com', version: '2017-01' }
define private C1000: Code { code: 'CBA', display: 'Code ABC', system: 'http://example.com', version: '2017-01' }
define private C0101: Code { code: 'ABC', display: 'Var Desc', system: 'http://example.com', version: '2017-05' }
define CodeAllSameAttrsEqualTrue: C0000 = C0000b
define CodeAllSameAttrsEquivTrue: C0000 ~ C0000b
define CodeDiffVersionEqualFalse: C0000 = C0001
define CodeDiffVersionEquivTrue : C0000 ~ C0001
define CodeDiffSystemEqualFalse : C0000 = C0010
define CodeDiffSystemEquivFalse : C0000 ~ C0010
define CodeDiffDisplayEqualFalse: C0000 = C0100
define CodeDiffDisplayEquivTrue : C0000 ~ C0100
define CodeDiffCodeEqualFalse   : C0000 = C1000
define CodeDiffCodeEquivFalse   : C0000 ~ C1000
define CodeDiffDispVerEqualFalse: C0000 = C0101
define CodeDiffDispVerEquivTrue : C0000 ~ C0101

//In (Codesystem) --> MOVED TO FHIR ENGINE
// TODO: additional tests once terminology provider is running
/*define InCodeSystemStringNull: '66071002' in "LOINC"
define InCodeSystemCodeNull: Code '8480-6' from "LOINC" display 'Systolic blood pressure' in "LOINC"
define InCodeSystemConceptNull:
  Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'
    in "LOINC"*/

//In (Valueset) --> MOVED TO FHIR ENGINE
// TODO: additional tests once terminology provider is running
/*define InValueSetFalseString: '1963-2' in "Feeding Intention-Breast"
define InValueSetFalseCode:
  Code '8480-6' from "LOINC" display 'Systolic blood pressure' in "Feeding Intention-Breast"
define InValueSetFalseConcept:
  Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'
    in "Feeding Intention-Breast"*/
