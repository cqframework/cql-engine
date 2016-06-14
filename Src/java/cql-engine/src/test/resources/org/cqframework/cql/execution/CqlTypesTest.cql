library Types version '1'

codesystem "LOINC": 'http://loinc.org'

context Population

//Any

//Boolean

//Code
define "CodeLiteral": Code '8480-6' from "LOINC" display 'Systolic blood pressure'

//Concept
/*define CodeList: {Code '8480-6', Code '8462-4'}
define ConceptTest: Concept(Code CodeList display 'Blood pressure codes')*/

//DateTime

//Decimal

//Integer

//Quantity

//String

//Time
