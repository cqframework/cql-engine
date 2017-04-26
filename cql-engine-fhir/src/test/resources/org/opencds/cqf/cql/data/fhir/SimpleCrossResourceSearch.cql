library XResourceSearch version '1'

using FHIR

define xRefSearch:
    from [Condition] C, [Procedure] P
    	where C.code.coding[0].code.value = P.code.coding[0].code.value