library XResourceSearch version '1'

using FHIR version '3.0.0'

define xRefSearch:
    from [Condition] C, [Procedure] P
    	where C.code.coding[0].code.value = P.code.coding[0].code.value

define testLet:
    from [Observation] O, [Claim] C
        let
            randomExpression: year from O.issued.value
        where
            randomExpression < year from C.created.value