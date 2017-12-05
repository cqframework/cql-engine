library Types version '1'

codesystem "LOINC": 'http://loinc.org'

context Population

define _0: Plan(300)


//Any
define AnyInteger: Ok(Equivalent(5, null), 'AnyInteger')
define AnyDecimal: Ok(Equivalent(5.0, null), 'AnyDecimal')
define AnyQuantity: Ok(Equivalent(5.0 'g', null), 'AnyQuantity')
define AnyDateTime: Ok(Equivalent(DateTime(2012, 4, 4), null), 'AnyDateTime')
define AnyTime: Ok(Equivalent(@T09:00:00.000, null), 'AnyTime')
define AnyInterval: Ok(Equivalent(Interval[2, 7], null), 'AnyInterval')
define AnyList: Ok(Equivalent({1, 2, 3}, null), 'AnyList')
define AnyTuple: Ok(Equivalent(Tuple { id: 5, name: 'Chris'}, null), 'AnyTuple')
define AnyString: Ok(Equivalent(AnyTuple.name, null), 'AnyString')

//Boolean
define BooleanTestTrue: Ok(Equivalent(true, null), 'BooleanTestTrue')
define BooleanTestFalse: Ok(Equivalent(false, null), 'BooleanTestFalse')

//Code
define CodeLiteral: Ok(Equivalent(Code '8480-6' from "LOINC" display 'Systolic blood pressure', null), 'CodeLiteral')

//Concept
define ConceptTest: Ok(Equivalent(Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis', null), 'ConceptTest')

//DateTime
define DateTimeNull: Ok(Equivalent(DateTime(null), null), 'DateTimeNull')
define DateTimeUpperBoundExcept: Ok(Equivalent(DateTime(10000, 12, 31, 23, 59, 59, 999), null), 'DateTimeUpperBoundExcept')
define DateTimeLowerBoundExcept: Ok(Equivalent(DateTime(0000, 1, 1, 0, 0, 0, 0), null), 'DateTimeLowerBoundExcept')
define DateTimeProper: Ok(Equivalent(DateTime(2016, 7, 7, 6, 25, 33, 910), null), 'DateTimeProper')
define DateTimeIncomplete: Ok(Equivalent(DateTime(2015, 2, 10), null), 'DateTimeIncomplete')
define DateTimeUncertain: Ok(Equivalent(days between DateTime(2015, 2, 10) and DateTime(2015, 3), null), 'DateTimeUncertain')
define DateTimeMin: Ok(Equivalent(DateTime(0001, 1, 1, 0, 0, 0, 0), null), 'DateTimeMin')
define DateTimeMax: Ok(Equivalent(DateTime(9999, 12, 31, 23, 59, 59, 999), null), 'DateTimeMax')

//Decimal
// problem with size of decimal and size of fractional part
define DecimalUpperBoundExcept: Ok(Equivalent(10000000000000000000000000000000000.00000000, null), 'DecimalUpperBoundExcept')
define DecimalLowerBoundExcept: Ok(Equivalent(-10000000000000000000000000000000000.00000000, null), 'DecimalLowerBoundExcept')
define DecimalFractionalTooBig: Ok(Equivalent(5.999999999, null), 'DecimalFractionalTooBig')
define DecimalPi: Ok(Equivalent(3.14159265, null), 'DecimalPi')

//Integer
// good
/*define IntegerUpperBoundExcept: 2147483649
define IntegerLowerBoundExcept: Ok(Equivalent(-2147483649, null), 'IntegerLowerBoundExcept')*/
define IntegerProper: Ok(Equivalent(5000, null), 'IntegerProper')

//Quantity
define QuantityTest: Ok(Equivalent(150.2 'lbs', null), 'QuantityTest')
define QuantityTest2: Ok(Equivalent(2.5589 'eskimo kisses', null), 'QuantityTest2')
define QuantityFractionalTooBig: Ok(Equivalent(5.999999999 'g', null), 'QuantityFractionalTooBig')

//String
// Problem here -- escape chars included in result string
define StringTestEscapeQuotes: Ok(Equivalent('\'I start with a single quote and end with a double quote\"', null), 'StringTestEscapeQuotes')
define StringUnicodeTest: Ok(Equivalent('\u0048\u0069', null), 'StringUnicodeTest')

//Time
// these all correctly result in translation errors
/*define TimeUpperBoundHours: @T24:59:59.999
define TimeUpperBoundMinutes: Ok(Equivalent(@T23:60:59.999, null), 'TimeUpperBoundMinutes')
define TimeUpperBoundSeconds: Ok(Equivalent(@T23:59:60.999, null), 'TimeUpperBoundSeconds')
define TimeUpperBoundMillis: Ok(Equivalent(@T23:59:59.10000, null), 'TimeUpperBoundMillis')*/
define TimeProper: Ok(Equivalent(@T10:25:12.863, null), 'TimeProper')
define TimeAllMax: Ok(Equivalent(@T23:59:59.999, null), 'TimeAllMax')
define TimeAllMin: Ok(Equivalent(@T00:00:00.000, null), 'TimeAllMin')


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
