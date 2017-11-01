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

define testExpressionSortDateTime:
    [Procedure] P sort by performed."start".value

define testExpressionSortEnumString:
    [ActivityDefinition] A
        where A.kind.value is not null
        return
            Tuple {theKind: A.kind.value}
            sort by theKind desc

define testExpressionSortInt:
    [Observation] O
        where O.issued.value is not null
        sort by year from issued.value desc

