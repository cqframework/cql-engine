library XResourceSearch version '1'

using FHIR

include FHIRHelpers version '1.6' called FHIRHelpers

valueset "Diabetes": '2.16.840.1.113883.3.464.1003.103.12.1001'

context Patient

define conditions:
    List<Any> {First([Condition: code in "Diabetes"] C
                    where C.code.coding[0].code.value = '190390000')}

define observations:
    List<Any> {First([Observation: code in "Diabetes"] O
                    where O.code.coding[0].code.value = '190390000')}

define crossResourceSearch:
    conditions union observations

define getCondition:
    First(crossResourceSearch) as List<Condition>

define getObservation:
    crossResourceSearch[1] as List<Observation>

define processCondition:
    getCondition C
       return C.code.coding[0].code

define processObservation:
    getObservation O
        return O.code.coding[0].code