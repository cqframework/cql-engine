library TypeOperator version '1'

context Population

//As
define AsQuantity: 45.5 'g' as Quantity
define CastAsQuantity: cast 45.5 'g' as Quantity
define AsDateTime: DateTime(2014, 01, 01) as DateTime

//Convert
define IntegerToDecimal: convert 5 to Decimal
define IntegerToString: convert 5 to String
define StringToIntegerError: convert 'foo' to Integer
define StringToDateTime: convert '2014-01-01' to DateTime
define StringToTime: convert 'T14:30:00.0' to Time
define StringToDateTimeMalformed: convert '2014/01/01' to DateTime

//Is
define IntegerIsInteger: 5 is Integer
define StringIsInteger: '5' is Integer

//ToBoolean
define StringNoToBoolean: ToBoolean('NO')

//ToConcept
define CodeToConcept1: ToConcept(Code { code: '8480-6' })

//ToDateTime
define ToDateTime1: ToDateTime('2014-01-01')
define ToDateTime2: ToDateTime('2014-01-01T12:05')
define ToDateTime3: ToDateTime('2014-01-01T12:05:05.955')
define ToDateTime4: ToDateTime('2014-01-01T12:05:05.955+01:30')
define ToDateTime5: ToDateTime('2014-01-01T12:05:05.955-01:15')
define ToDateTime6: ToDateTime('2014-01-01T12:05:05.955Z')
define ToDateTimeMalformed: ToDateTime('2014/01/01T12:05:05.955Z')

//ToDecimal
define String25D5ToDecimal: ToDecimal('+25.5')

//ToInteger
define StringNeg25ToInteger: ToInteger('-25')

//ToQuantity
define String5D5CMToQuantity: ToQuantity('5.5 cm')

//ToString
define IntegerNeg5ToString: ToString(-5)
define Decimal18D55ToString: ToString(18.55)
define Quantity5D5CMToString: ToString(5.5 'cm')
define BooleanTrueToString: ToString(true)

//ToTime
define ToTime1: ToTime('T14:30:00.0')
define ToTime2: ToTime('T14:30:00.0+05:30')
define ToTime3: ToTime('T14:30:00.0-05:45')
define ToTime4: ToTime('T14:30:00.0Z')
define ToTimeMalformed: ToTime('T14-30-00.0')
