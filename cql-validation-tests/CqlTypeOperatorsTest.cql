library TypeOperator version '1'

codesystem "LOINC": 'http://loinc.org'

context Population

define _0: Plan(300)


//As
define AsQuantity: Ok(Equivalent(45.5 'g' as Quantity, null), 'AsQuantity')
define CastAsQuantity: Ok(Equivalent(cast 45.5 'g' as Quantity, null), 'CastAsQuantity')
define AsDateTime: Ok(Equivalent(DateTime(2014, 01, 01) as DateTime, null), 'AsDateTime')

//Convert
define IntegerToDecimal: Ok(Equivalent(convert 5 to Decimal, null), 'IntegerToDecimal')
define IntegerToString: Ok(Equivalent(convert 5 to String, null), 'IntegerToString')
define StringToIntegerError: Ok(Equivalent(convert 'foo' to Integer, null), 'StringToIntegerError')
define StringToDateTime: Ok(Equivalent(convert '2014-01-01' to DateTime, null), 'StringToDateTime')
define StringToTime: Ok(Equivalent(convert 'T14:30:00.0' to Time, null), 'StringToTime')
define StringToDateTimeMalformed: Ok(Equivalent(convert '2014/01/01' to DateTime, null), 'StringToDateTimeMalformed')

//Is
define IntegerIsInteger: Ok(Equivalent(5 is Integer, null), 'IntegerIsInteger')
define StringIsInteger: Ok(Equivalent('5' is Integer, null), 'StringIsInteger')

//ToBoolean
define StringNoToBoolean: Ok(Equivalent(ToBoolean('NO'), null), 'StringNoToBoolean')

//ToConcept
define CodeToConcept1: Ok(Equivalent(ToConcept(Code '8480-6' from "LOINC" display 'Systolic blood pressure'), null), 'CodeToConcept1')

//ToDateTime
define ToDateTime1: Ok(Equivalent(ToDateTime('2014-01-01'), null), 'ToDateTime1')
define ToDateTime2: Ok(Equivalent(ToDateTime('2014-01-01T12:05'), null), 'ToDateTime2')
define ToDateTime3: Ok(Equivalent(ToDateTime('2014-01-01T12:05:05.955'), null), 'ToDateTime3')
define ToDateTime4: Ok(Equivalent(ToDateTime('2014-01-01T12:05:05.955+01:30'), null), 'ToDateTime4')
define ToDateTime5: Ok(Equivalent(ToDateTime('2014-01-01T12:05:05.955-01:15'), null), 'ToDateTime5')
define ToDateTime6: Ok(Equivalent(ToDateTime('2014-01-01T12:05:05.955Z'), null), 'ToDateTime6')
define ToDateTimeMalformed: Ok(Equivalent(ToDateTime('2014/01/01T12:05:05.955Z'), null), 'ToDateTimeMalformed')

//ToDecimal
define String25D5ToDecimal: Ok(Equivalent(ToDecimal('+25.5'), null), 'String25D5ToDecimal')

//ToInteger
define StringNeg25ToInteger: Ok(Equivalent(ToInteger('-25'), null), 'StringNeg25ToInteger')

//ToQuantity
define String5D5CMToQuantity: Ok(Equivalent(ToQuantity('5.5 cm'), null), 'String5D5CMToQuantity')

//ToString
define IntegerNeg5ToString: Ok(Equivalent(ToString(-5), null), 'IntegerNeg5ToString')
define Decimal18D55ToString: Ok(Equivalent(ToString(18.55), null), 'Decimal18D55ToString')
define Quantity5D5CMToString: Ok(Equivalent(ToString(5.5 'cm'), null), 'Quantity5D5CMToString')
define BooleanTrueToString: Ok(Equivalent(ToString(true), null), 'BooleanTrueToString')

//ToTime
define ToTime1: Ok(Equivalent(ToTime('T14:30:00.0'), null), 'ToTime1')
define ToTime2: Ok(Equivalent(ToTime('T14:30:00.0+05:30'), null), 'ToTime2')
define ToTime3: Ok(Equivalent(ToTime('T14:30:00.0-05:45'), null), 'ToTime3')
define ToTime4: Ok(Equivalent(ToTime('T14:30:00.0Z'), null), 'ToTime4')
define ToTimeMalformed: Ok(Equivalent(ToTime('T14-30-00.0'), null), 'ToTimeMalformed')


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
