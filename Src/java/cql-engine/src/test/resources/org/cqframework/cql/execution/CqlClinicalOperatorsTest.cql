library ClinicalOperator version '1'

codesystem "LOINC": 'http://loinc.org'

context Population

//Age

//AgeAt

//CalculateAge
//define CalculateAgeYears: CalculateAgeInYears(DateTime(2000, 1, 1, 0, 0, 0, 0), Today())

//CalculateAgeAt

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

//In (Codesystem)

//In (Valueset)
