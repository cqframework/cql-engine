library Types version '1'

codesystem "LOINC": 'http://loinc.org'

context Population

define _0: plan(300)


//Any
define AnyInteger: ok(Equivalent((5),(null)), 'AnyInteger')
define AnyDecimal: ok(Equivalent((5.0),(null)), 'AnyDecimal')
define AnyQuantity: ok(Equivalent((5.0 'g'),(null)), 'AnyQuantity')
define AnyDateTime: ok(Equivalent((DateTime(2012, 4, 4)),(null)), 'AnyDateTime')
define AnyTime: ok(Equivalent((@T09:00:00.000),(null)), 'AnyTime')
define AnyInterval: ok(Equivalent((Interval[2, 7]),(null)), 'AnyInterval')
define AnyList: ok(Equivalent(({1, 2, 3}),(null)), 'AnyList')
define AnyTuple: ok(Equivalent((Tuple { id: 5, name: 'Chris'}),(null)), 'AnyTuple')
define AnyString: ok(Equivalent((AnyTuple.name),(null)), 'AnyString')

//Boolean
define BooleanTestTrue: ok(Equivalent((true),(null)), 'BooleanTestTrue')
define BooleanTestFalse: ok(Equivalent((false),(null)), 'BooleanTestFalse')

//Code
define CodeLiteral: ok(Equivalent((Code '8480-6' from "LOINC" display 'Systolic blood pressure'),(null)), 'CodeLiteral')

//Concept
define ConceptTest: ok(Equivalent((Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'),(null)), 'ConceptTest')

//DateTime
define DateTimeNull: ok(Equivalent((DateTime(null)),(null)), 'DateTimeNull')
define DateTimeUpperBoundExcept: ok(Equivalent((DateTime(10000, 12, 31, 23, 59, 59, 999)),(null)), 'DateTimeUpperBoundExcept')
define DateTimeLowerBoundExcept: ok(Equivalent((DateTime(0000, 1, 1, 0, 0, 0, 0)),(null)), 'DateTimeLowerBoundExcept')
define DateTimeProper: ok(Equivalent((DateTime(2016, 7, 7, 6, 25, 33, 910)),(null)), 'DateTimeProper')
define DateTimeIncomplete: ok(Equivalent((DateTime(2015, 2, 10)),(null)), 'DateTimeIncomplete')
define DateTimeUncertain: ok(Equivalent((days between DateTime(2015, 2, 10) and DateTime(2015, 3)),(null)), 'DateTimeUncertain')
define DateTimeMin: ok(Equivalent((DateTime(0001, 1, 1, 0, 0, 0, 0)),(null)), 'DateTimeMin')
define DateTimeMax: ok(Equivalent((DateTime(9999, 12, 31, 23, 59, 59, 999)),(null)), 'DateTimeMax')

//Decimal
// problem with size of decimal and size of fractional part
define DecimalUpperBoundExcept: ok(Equivalent((10000000000000000000000000000000000.00000000),(null)), 'DecimalUpperBoundExcept')
define DecimalLowerBoundExcept: ok(Equivalent((-10000000000000000000000000000000000.00000000),(null)), 'DecimalLowerBoundExcept')
define DecimalFractionalTooBig: ok(Equivalent((5.999999999),(null)), 'DecimalFractionalTooBig')
define DecimalPi: ok(Equivalent((3.14159265),(null)), 'DecimalPi')

//Integer
// good
/*define IntegerUpperBoundExcept: 2147483649
define IntegerLowerBoundExcept: ok(Equivalent((-2147483649),(null)), 'IntegerLowerBoundExcept')*/
define IntegerProper: ok(Equivalent((5000),(null)), 'IntegerProper')

//Quantity
define QuantityTest: ok(Equivalent((150.2 'lbs'),(null)), 'QuantityTest')
define QuantityTest2: ok(Equivalent((2.5589 'eskimo kisses'),(null)), 'QuantityTest2')
define QuantityFractionalTooBig: ok(Equivalent((5.999999999 'g'),(null)), 'QuantityFractionalTooBig')

//String
// Problem here -- escape chars included in result string
define StringTestEscapeQuotes: ok(Equivalent(('\'I start with a single quote and end with a double quote\"'),(null)), 'StringTestEscapeQuotes')
define StringUnicodeTest: ok(Equivalent(('\u0048\u0069'),(null)), 'StringUnicodeTest')

//Time
// these all correctly result in translation errors
/*define TimeUpperBoundHours: @T24:59:59.999
define TimeUpperBoundMinutes: ok(Equivalent((@T23:60:59.999),(null)), 'TimeUpperBoundMinutes')
define TimeUpperBoundSeconds: ok(Equivalent((@T23:59:60.999),(null)), 'TimeUpperBoundSeconds')
define TimeUpperBoundMillis: ok(Equivalent((@T23:59:59.10000),(null)), 'TimeUpperBoundMillis')*/
define TimeProper: ok(Equivalent((@T10:25:12.863),(null)), 'TimeProper')
define TimeAllMax: ok(Equivalent((@T23:59:59.999),(null)), 'TimeAllMax')
define TimeAllMin: ok(Equivalent((@T00:00:00.000),(null)), 'TimeAllMin')


define function plan(test_count Integer):
    Message(null, true, null as String, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null as String, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
