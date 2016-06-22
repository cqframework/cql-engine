library DateTimeOperator version '1'

context Population

// TODO: need Time tests

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
define DateTimeBeforeYearTrue: DateTime(2003) before year of DateTime(2004, 10, 10)
define DateTimeBeforeYearFalse: DateTime(2004, 11, 10) before year of DateTime(2003, 10, 10)
define DateTimeBeforeMonthTrue: DateTime(2004, 10, 10) before month of DateTime(2004, 11, 10)
define DateTimeBeforeMonthFalse: DateTime(2004, 11, 10) before month of DateTime(2004, 10, 10)
define DateTimeBeforeDayTrue: DateTime(2004, 10, 1) before day of DateTime(2004, 10, 10)
define DateTimeBeforeDayFalse: DateTime(2004, 10, 11) before day of DateTime(2004, 10, 10)
define DateTimeBeforeHourTrue: DateTime(2004, 10, 10, 1) before hour of DateTime(2004, 10, 10, 5)
define DateTimeBeforeHourFalse: DateTime(2004, 10, 10, 23) before hour of DateTime(2004, 10, 10, 21)
define DateTimeBeforeMinuteTrue: DateTime(2004, 10, 10, 20, 28) before minute of DateTime(2004, 10, 10, 20, 29)
define DateTimeBeforeMinuteFalse: DateTime(2004, 10, 10, 20, 35) before minute of DateTime(2004, 10, 10, 20, 31)
define DateTimeBeforeSecondTrue: DateTime(2004, 10, 10, 20, 30, 12) before second of DateTime(2004, 10, 10, 20, 30, 14)
define DateTimeBeforeSecondFalse: DateTime(2004, 10, 10, 20, 30, 55) before second of DateTime(2004, 10, 10, 20, 30, 16)
define DateTimeBeforeMillisecondTrue: DateTime(2004, 10, 10, 20, 30, 15, 508) before millisecond of DateTime(2004, 10, 10, 20, 30, 15, 510)
define DateTimeBeforeMillisecondFalse: DateTime(2004, 10, 10, 20, 30, 15, 599) before millisecond of DateTime(2004, 10, 10, 20, 30, 15, 513)

//DateTime
define DateTimeYear: DateTime(2003)
define DateTimeMonth: DateTime(2003, 10)
define DateTimeDay: DateTime(2003, 10, 29)
define DateTimeHour: DateTime(2003, 10, 29, 20)
define DateTimeMinute: DateTime(2003, 10, 29, 20, 50)
define DateTimeSecond: DateTime(2003, 10, 29, 20, 50, 33)
define DateTimeMillisecond: DateTime(2003, 10, 29, 20, 50, 33, 955)

//DateTimeComponentFrom
define DateTimeComponentFromYear: year from DateTime(2003, 10, 29, 20, 50, 33, 955)
define DateTimeComponentFromMonth: month from DateTime(2003, 10, 29, 20, 50, 33, 955)
define DateTimeComponentFromDay: day from DateTime(2003, 10, 29, 20, 50, 33, 955)
define DateTimeComponentFromHour: hour from DateTime(2003, 10, 29, 20, 50, 33, 955)
define DateTimeComponentFromMinute: minute from DateTime(2003, 10, 29, 20, 50, 33, 955)
define DateTimeComponentFromSecond: second from DateTime(2003, 10, 29, 20, 50, 33, 955)
define DateTimeComponentFromMillisecond: millisecond from DateTime(2003, 10, 29, 20, 50, 33, 955)
define DateTimeComponentFromTimezone: timezone from DateTime(2003, 10, 29, 20, 50, 33, 955, 1)
define DateTimeComponentFromDate: date from DateTime(2003, 10, 29, 20, 50, 33, 955, 1)

//Difference
define DateTimeDifferenceYear: difference in years between DateTime(2000) and DateTime(2005)
define DateTimeDifferenceMonth: difference in months between DateTime(2000, 2) and DateTime(2000, 10)
define DateTimeDifferenceDay: difference in days between DateTime(2000, 10, 15) and DateTime(2000, 10, 25)
define DateTimeDifferenceHour: difference in hours between DateTime(2000, 4, 1, 12) and DateTime(2000, 4, 1, 20)
define DateTimeDifferenceMinute: difference in minutes between DateTime(2000, 12, 10, 5, 16) and DateTime(2005, 12, 10, 5, 25)
define DateTimeDifferenceSecond: difference in seconds between DateTime(2000, 10, 10, 10, 5, 45) and DateTime(2000, 10, 10, 10, 5, 50)
define DateTimeDifferenceMillisecond: difference in milliseconds between DateTime(2000, 10, 10, 10, 5, 45, 500) and DateTime(2000, 10, 10, 10, 5, 45, 900)
define DateTimeDifferenceNegative: difference in years between DateTime(2016) and DateTime(1998)

//Duration
define DateTimeDurationBetweenYear: years between DateTime(2005) and DateTime(2010)
define DateTimeDurationBetweenYearOffset: years between DateTime(2005, 5) and DateTime(2010, 4)
define DateTimeDurationBetweenMonth: months between @2014-01-31 and @2014-02-01
define DateTimeDurationBetweenMonthUncertain: months between DateTime(2005) and DateTime(2006, 7) > 5
define DateTimeDurationBetweenMonthUncertain2: months between DateTime(2005) and DateTime(2006, 2) > 5
define DateTimeDurationBetweenMonthUncertain3: months between DateTime(2005) and DateTime(2006, 7) > 25

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
