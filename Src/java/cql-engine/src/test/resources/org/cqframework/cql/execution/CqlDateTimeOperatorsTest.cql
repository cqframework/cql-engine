library DateTimeOperator version '1'

context Population

//Add
define DateTimeAdd5Years: DateTime(2005, 10, 10) + 5 years
define DateTimeAddInvalidYears: DateTime(2005, 10, 10) + 8000 years
define DateTimeAdd5Months: DateTime(2005, 5, 10) + 5 months
define DateTimeAddMonthsOverflow: DateTime(2005, 5, 10) + 10 months
define DateTimeAdd5Days: DateTime(2005, 5, 10) + 5 days
define DateTimeAddDaysOverflow: DateTime(2016, 6, 10) + 21 days
define DateTimeAdd5Hours: DateTime(2005, 5, 10, 5) + 5 hours
define DateTimeAddHoursOverflow: DateTime(2016, 6, 10, 5) + 19 hours
define DateTimeAdd5Minutes: DateTime(2005, 5, 10, 5, 5) + 5 minutes
define DateTimeAddMinutesOverflow: DateTime(2016, 6, 10, 5, 5) + 55 minutes
define DateTimeAdd5Seconds: DateTime(2005, 5, 10, 5, 5, 5) + 5 seconds
define DateTimeAddSecondsOverflow: DateTime(2016, 6, 10, 5, 5, 5) + 55 seconds
define DateTimeAdd5Milliseconds: DateTime(2005, 5, 10, 5, 5, 5, 5) + 5 milliseconds
define DateTimeAddMillisecondsOverflow: DateTime(2016, 6, 10, 5, 5, 5, 5) + 995 milliseconds
define DateTimeAddLeapYear: DateTime(2012, 2, 29) + 1 year

//After
define DateTimeAfterYearTrue: DateTime(2005, 10, 10) after year of DateTime(2004, 10, 10)
define DateTimeAfterYearFalse: DateTime(2004, 11, 10) after year of DateTime(2004, 10, 10)
define DateTimeAfterMonthTrue: DateTime(2004, 12, 10) after month of DateTime(2004, 11, 10)
define DateTimeAfterMonthFalse: DateTime(2004, 9, 10) after month of DateTime(2004, 10, 10)
define DateTimeAfterDayTrue: DateTime(2004, 12, 11) after day of DateTime(2004, 10, 10)
define DateTimeAfterDayFalse: DateTime(2004, 10, 9) after day of DateTime(2004, 10, 10)
define DateTimeAfterHourTrue: DateTime(2004, 10, 10, 10) after hour of DateTime(2004, 10, 10, 5)
define DateTimeAfterHourFalse: DateTime(2004, 10, 10, 20) after hour of DateTime(2004, 10, 10, 21)
define DateTimeAfterMinuteTrue: DateTime(2004, 10, 10, 20, 30) after minute of DateTime(2004, 10, 10, 20, 29)
define DateTimeAfterMinuteFalse: DateTime(2004, 10, 10, 20, 30) after minute of DateTime(2004, 10, 10, 20, 31)
define DateTimeAfterSecondTrue: DateTime(2004, 10, 10, 20, 30, 15) after second of DateTime(2004, 10, 10, 20, 30, 14)
define DateTimeAfterSecondFalse: DateTime(2004, 10, 10, 20, 30, 15) after second of DateTime(2004, 10, 10, 20, 30, 16)
define DateTimeAfterMillisecondTrue: DateTime(2004, 10, 10, 20, 30, 15, 512) after millisecond of DateTime(2004, 10, 10, 20, 30, 15, 510)
define DateTimeAfterMillisecondFalse: DateTime(2004, 10, 10, 20, 30, 15, 512) after millisecond of DateTime(2004, 10, 10, 20, 30, 15, 513)

//Before

//DateTime

//DateTimeComponentFrom

//Difference

//Duration

//Now

//SameAs

//SameOrAfter

//SameOrBefore

//Subtract
define DateTimeSubtract5Years: DateTime(2005, 10, 10) - 5 years
define DateTimeSubtractInvalidYears: DateTime(2005, 10, 10) - 2005 years
define DateTimeSubtract5Months: DateTime(2005, 6, 10) - 5 months
define DateTimeSubtractMonthsUnderflow: DateTime(2005, 5, 10) - 6 months
define DateTimeSubtract5Days: DateTime(2005, 5, 10) - 5 days
define DateTimeSubtractDaysUnderflow: DateTime(2016, 6, 10) - 11 days
define DateTimeSubtract5Hours: DateTime(2005, 5, 10, 10) - 5 hours
define DateTimeSubtractHoursUnderflow: DateTime(2016, 6, 10, 5) - 6 hours
define DateTimeSubtract5Minutes: DateTime(2005, 5, 10, 5, 10) - 5 minutes
define DateTimeSubtractMinutesUnderflow: DateTime(2016, 6, 10, 5, 5) - 6 minutes
define DateTimeSubtract5Seconds: DateTime(2005, 5, 10, 5, 5, 10) - 5 seconds
define DateTimeSubtractSecondsUnderflow: DateTime(2016, 6, 10, 5, 5, 5) - 6 seconds
define DateTimeSubtract5Milliseconds: DateTime(2005, 5, 10, 5, 5, 5, 10) - 5 milliseconds
define DateTimeSubtractMillisecondsUnderflow: DateTime(2016, 6, 10, 5, 5, 5, 5) - 6 milliseconds

//Time

//TimeOfDay

//Today
