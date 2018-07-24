library CqlSimpleValuesTests version '1'

// Basic
define BoolTrue: true
define BoolFalse: false
define IntOne: 1
define DecimalTenth: 0.1
define StringTrue: 'true'
define DateTimeX: @2012-02-15T12:10:59.456Z
define DateTimeFX: DateTime(2012, 2, 15, 12, 10, 59, 456, 0)
define TimeX: @T12:10:59.456Z
define TimeFX: Time(12, 10, 59, 456, 0)

// DateTime
define DateTime_Year: DateTime(2012)
define DateTime_Month: DateTime(2012, 2)
define DateTime_Day: DateTime(2012, 2, 15)
define DateTime_Hour: DateTime(2012, 2, 15, 12)
define DateTime_Minute: DateTime(2012, 2, 15, 12, 10)
define DateTime_Second: DateTime(2012, 2, 15, 12, 10, 59)
define DateTime_Millisecond: DateTime(2012, 2, 15, 12, 10, 59, 456)
define DateTime_TimezoneOffset: DateTime(2012, 2, 15, 12, 10, 59, 456, -8.0)

// Time
define Hour: Time(12)
define Minute: Time(12, 10)
define Second: Time(12, 10, 59)
define Millisecond: Time(12, 10, 59, 456)
define TimezoneOffset: Time(12, 10, 59, 456, -8.0)