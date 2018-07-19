library CqlListValuesTests version '1'

define Code1: Code { system: 'http://loinc.org', code: '12345', version: '1', display: 'Test Code' }
define Code2: Code { system: 'http://loinc.org', code: '123456', version: '1', display: 'Another Test Code' }

define Three: 1 + 2
define BoolList: { true, false, true }
define IntList: { 9, 7, 8 }
define DecimalList: { 1.0, 2.1, 3.2 }
define StringList: { 'a', 'bee', 'see' }
define DateTimeList: { @2012-02-15T12:10:59.456Z, @2012-03-15T12:10:59.456Z, @2012-04-15T12:10:59.456Z }
define TimeList: { @T12:10:59.456Z, @T13:10:59.456Z, @T14:10:59.456Z }
define QuantityList: { 1.0'm', 2.1'm', 3.2'm' }
define CodeList: { Code1, Code2 }
define ConceptList: { Concept { codes: { Code1 }, display: 'Test Concept' }, Concept { codes: { Code2 }, display: 'Another Test Concept' } }
define TupleList: { Tuple { a: 1, b: '2' }, Tuple { x: 2, z: '3' } }
define ListList: { { 1, 2, 3 }, { 'a', 'b', 'c' } }
define IntervalList: { Interval[1, 5], Interval(5, 9), Interval[8, 10) }
define MixedList: List<Any>{ 1, 'two', Three }
define EmptyList: List<Integer>{}
