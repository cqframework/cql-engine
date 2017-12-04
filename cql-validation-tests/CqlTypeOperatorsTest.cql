library TypeOperator version '1'

codesystem "LOINC": 'http://loinc.org'

context Population

define private ExistenceOfThisEarlierDefineIsWorkaroundForTranslatorDeathWithLaterMessageNullCodeArg: Message(null, true, '', 'Message', '')

define _0: plan(300)


//As
define AsQuantity: ok(Equivalent((45.5 'g' as Quantity),(null)), 'AsQuantity')
define CastAsQuantity: ok(Equivalent((cast 45.5 'g' as Quantity),(null)), 'CastAsQuantity')
define AsDateTime: ok(Equivalent((DateTime(2014, 01, 01) as DateTime),(null)), 'AsDateTime')

//Convert
define IntegerToDecimal: ok(Equivalent((convert 5 to Decimal),(null)), 'IntegerToDecimal')
define IntegerToString: ok(Equivalent((convert 5 to String),(null)), 'IntegerToString')
define StringToIntegerError: ok(Equivalent((convert 'foo' to Integer),(null)), 'StringToIntegerError')
define StringToDateTime: ok(Equivalent((convert '2014-01-01' to DateTime),(null)), 'StringToDateTime')
define StringToTime: ok(Equivalent((convert 'T14:30:00.0' to Time),(null)), 'StringToTime')
define StringToDateTimeMalformed: ok(Equivalent((convert '2014/01/01' to DateTime),(null)), 'StringToDateTimeMalformed')

//Is
define IntegerIsInteger: ok(Equivalent((5 is Integer),(null)), 'IntegerIsInteger')
define StringIsInteger: ok(Equivalent(('5' is Integer),(null)), 'StringIsInteger')

//ToBoolean
define StringNoToBoolean: ok(Equivalent((ToBoolean('NO')),(null)), 'StringNoToBoolean')

//ToConcept
define CodeToConcept1: ok(Equivalent((ToConcept(Code '8480-6' from "LOINC" display 'Systolic blood pressure')),(null)), 'CodeToConcept1')

//ToDateTime
define ToDateTime1: ok(Equivalent((ToDateTime('2014-01-01')),(null)), 'ToDateTime1')
define ToDateTime2: ok(Equivalent((ToDateTime('2014-01-01T12:05')),(null)), 'ToDateTime2')
define ToDateTime3: ok(Equivalent((ToDateTime('2014-01-01T12:05:05.955')),(null)), 'ToDateTime3')
define ToDateTime4: ok(Equivalent((ToDateTime('2014-01-01T12:05:05.955+01:30')),(null)), 'ToDateTime4')
define ToDateTime5: ok(Equivalent((ToDateTime('2014-01-01T12:05:05.955-01:15')),(null)), 'ToDateTime5')
define ToDateTime6: ok(Equivalent((ToDateTime('2014-01-01T12:05:05.955Z')),(null)), 'ToDateTime6')
define ToDateTimeMalformed: ok(Equivalent((ToDateTime('2014/01/01T12:05:05.955Z')),(null)), 'ToDateTimeMalformed')

//ToDecimal
define String25D5ToDecimal: ok(Equivalent((ToDecimal('+25.5')),(null)), 'String25D5ToDecimal')

//ToInteger
define StringNeg25ToInteger: ok(Equivalent((ToInteger('-25')),(null)), 'StringNeg25ToInteger')

//ToQuantity
define String5D5CMToQuantity: ok(Equivalent((ToQuantity('5.5 cm')),(null)), 'String5D5CMToQuantity')

//ToString
define IntegerNeg5ToString: ok(Equivalent((ToString(-5)),(null)), 'IntegerNeg5ToString')
define Decimal18D55ToString: ok(Equivalent((ToString(18.55)),(null)), 'Decimal18D55ToString')
define Quantity5D5CMToString: ok(Equivalent((ToString(5.5 'cm')),(null)), 'Quantity5D5CMToString')
define BooleanTrueToString: ok(Equivalent((ToString(true)),(null)), 'BooleanTrueToString')

//ToTime
define ToTime1: ok(Equivalent((ToTime('T14:30:00.0')),(null)), 'ToTime1')
define ToTime2: ok(Equivalent((ToTime('T14:30:00.0+05:30')),(null)), 'ToTime2')
define ToTime3: ok(Equivalent((ToTime('T14:30:00.0-05:45')),(null)), 'ToTime3')
define ToTime4: ok(Equivalent((ToTime('T14:30:00.0Z')),(null)), 'ToTime4')
define ToTimeMalformed: ok(Equivalent((ToTime('T14-30-00.0')),(null)), 'ToTimeMalformed')


define function plan(test_count Integer):
    Message(null, true, null, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
