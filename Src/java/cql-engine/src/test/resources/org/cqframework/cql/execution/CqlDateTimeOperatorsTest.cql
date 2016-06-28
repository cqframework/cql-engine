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
define DateTimeAddLeapYear: DateTime(2012, 2, 29) + 1 years
define DateTimeAdd2YearsByMonths: DateTime(2014) + 24 months
define DateTimeAdd2YearsByDays: DateTime(2014) + 730 days
define DateTimeAdd2YearsByDaysRem5Days: DateTime(2014) + 735 days

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
define DateTimeAfterUncertain: DateTime(2005, 10, 10) after day of DateTime(2005, 9)

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
define DateTimeDifferenceMinute: difference in minutes between DateTime(2005, 12, 10, 5, 16) and DateTime(2005, 12, 10, 5, 25)
define DateTimeDifferenceSecond: difference in seconds between DateTime(2000, 10, 10, 10, 5, 45) and DateTime(2000, 10, 10, 10, 5, 50)
define DateTimeDifferenceMillisecond: difference in milliseconds between DateTime(2000, 10, 10, 10, 5, 45, 500) and DateTime(2000, 10, 10, 10, 5, 45, 900)
define DateTimeDifferenceNegative: difference in years between DateTime(2016) and DateTime(1998)
define DateTimeDifferenceUncertain: difference in months between DateTime(2005) and DateTime(2006, 7) > 5

//Duration
define DateTimeDurationBetweenYear: years between DateTime(2005) and DateTime(2010)
define DateTimeDurationBetweenYearOffset: years between DateTime(2005, 5) and DateTime(2010, 4)
define DateTimeDurationBetweenMonth: months between @2014-01-31 and @2014-02-01
define DateTimeDurationBetweenDaysDiffYears: days between DateTime(2010, 10, 12, 12, 5) and DateTime(2008, 8, 15, 8, 8)
// Uncertainty tests
define DateTimeDurationBetweenUncertainInterval: days between DateTime(2014, 1, 15) and DateTime(2014, 2)
define DateTimeDurationBetweenUncertainInterval2: months between DateTime(2005) and DateTime(2006, 5)
define DateTimeDurationBetweenUncertainAdd: DateTimeDurationBetweenUncertainInterval + DateTimeDurationBetweenUncertainInterval
define DateTimeDurationBetweenUncertainSubtract: DateTimeDurationBetweenUncertainInterval - DateTimeDurationBetweenUncertainInterval2
define DateTimeDurationBetweenUncertainMultiply: DateTimeDurationBetweenUncertainInterval * DateTimeDurationBetweenUncertainInterval
define DateTimeDurationBetweenUncertainDiv: DateTimeDurationBetweenUncertainInterval div DateTimeDurationBetweenUncertainInterval2
define DateTimeDurationBetweenMonthUncertain: months between DateTime(2005) and DateTime(2006, 7) > 5
define DateTimeDurationBetweenMonthUncertain2: months between DateTime(2005) and DateTime(2006, 2) > 5
define DateTimeDurationBetweenMonthUncertain3: months between DateTime(2005) and DateTime(2006, 7) > 25
define DateTimeDurationBetweenMonthUncertain4: months between DateTime(2005) and DateTime(2006, 7) < 24
define DateTimeDurationBetweenMonthUncertain5: months between DateTime(2005) and DateTime(2006, 7) = 24
define DateTimeDurationBetweenMonthUncertain6: months between DateTime(2005) and DateTime(2006, 7) >= 5
define DateTimeDurationBetweenMonthUncertain7: months between DateTime(2005) and DateTime(2006, 7) <= 24

//Now
define DateTimeNow: Now() = Now()

//SameAs
define DateTimeSameAsYearTrue: DateTime(2014) same year as DateTime(2014)
define DateTimeSameAsYearFalse: DateTime(2013) same year as DateTime(2014)
define DateTimeSameAsMonthTrue: DateTime(2014, 12) same month as DateTime(2014, 12)
define DateTimeSameAsMonthFalse: DateTime(2014, 12) same month as DateTime(2014, 10)
define DateTimeSameAsDayTrue: DateTime(2014, 12, 10) same day as DateTime(2014, 12, 10)
define DateTimeSameAsDayFalse: DateTime(2014, 10, 10) same day as DateTime(2014, 10, 11)
define DateTimeSameAsHourTrue: DateTime(2014, 12, 10, 20) same hour as DateTime(2014, 12, 10, 20)
define DateTimeSameAsHourFalse: DateTime(2014, 10, 10, 20) same hour as DateTime(2014, 10, 10, 21)
define DateTimeSameAsMinuteTrue: DateTime(2014, 12, 10, 20, 55) same minute as DateTime(2014, 12, 10, 20, 55)
define DateTimeSameAsMinuteFalse: DateTime(2014, 10, 10, 20, 55) same minute as DateTime(2014, 10, 10, 21, 56)
define DateTimeSameAsSecondTrue: DateTime(2014, 12, 10, 20, 55, 45) same second as DateTime(2014, 12, 10, 20, 55, 45)
define DateTimeSameAsSecondFalse: DateTime(2014, 10, 10, 20, 55, 45) same second as DateTime(2014, 10, 10, 21, 55, 44)
define DateTimeSameAsMillisecondTrue: DateTime(2014, 12, 10, 20, 55, 45, 500) same millisecond as DateTime(2014, 12, 10, 20, 55, 45, 500)
define DateTimeSameAsMillisecondFalse: DateTime(2014, 10, 10, 20, 55, 45, 500) same millisecond as DateTime(2014, 10, 10, 21, 55, 45, 501)
define DateTimeSameAsNull: DateTime(2014, 10) same day as DateTime(2014, 10, 12)

//SameOrAfter
define DateTimeSameOrAfterYearTrue1: DateTime(2014) same year or after DateTime(2014)
define DateTimeSameOrAfterYearTrue2: DateTime(2016) same year or after DateTime(2014)
define DateTimeSameOrAfterYearFalse: DateTime(2013) same year or after DateTime(2014)
define DateTimeSameOrAfterMonthTrue1: DateTime(2014, 12) same month or after DateTime(2014, 12)
define DateTimeSameOrAfterMonthTrue2: DateTime(2014, 10) same month or after DateTime(2014, 9)
define DateTimeSameOrAfterMonthFalse: DateTime(2014, 10) same month or after DateTime(2014, 11)
define DateTimeSameOrAfterDayTrue1: DateTime(2014, 12, 20) same day or after DateTime(2014, 12, 20)
define DateTimeSameOrAfterDayTrue2: DateTime(2014, 10, 25) same day or after DateTime(2014, 10, 20)
define DateTimeSameOrAfterDayFalse: DateTime(2014, 10, 20) same day or after DateTime(2014, 10, 25)
define DateTimeSameOrAfterHourTrue1: DateTime(2014, 12, 20, 12) same hour or after DateTime(2014, 12, 20, 12)
define DateTimeSameOrAfterHourTrue2: DateTime(2014, 10, 25, 12) same hour or after DateTime(2014, 10, 25, 10)
define DateTimeSameOrAfterHourFalse: DateTime(2014, 10, 25, 12) same hour or after DateTime(2014, 10, 25, 15)
define DateTimeSameOrAfterMinuteTrue1: DateTime(2014, 12, 20, 12, 30) same minute or after DateTime(2014, 12, 20, 12, 30)
define DateTimeSameOrAfterMinuteTrue2: DateTime(2014, 10, 25, 10, 30) same minute or after DateTime(2014, 10, 25, 10, 25)
define DateTimeSameOrAfterMinuteFalse: DateTime(2014, 10, 25, 15, 30) same minute or after DateTime(2014, 10, 25, 15, 45)
define DateTimeSameOrAfterSecondTrue1: DateTime(2014, 12, 20, 12, 30, 15) same second or after DateTime(2014, 12, 20, 12, 30, 15)
define DateTimeSameOrAfterSecondTrue2: DateTime(2014, 10, 25, 10, 25, 25) same second or after DateTime(2014, 10, 25, 10, 25, 20)
define DateTimeSameOrAfterSecondFalse: DateTime(2014, 10, 25, 15, 45, 20) same second or after DateTime(2014, 10, 25, 15, 45, 21)
define DateTimeSameOrAfterMillisecondTrue1: DateTime(2014, 12, 20, 12, 30, 15, 250) same millisecond or after DateTime(2014, 12, 20, 12, 30, 15, 250)
define DateTimeSameOrAfterMillisecondTrue2: DateTime(2014, 10, 25, 10, 25, 20, 500) same millisecond or after DateTime(2014, 10, 25, 10, 25, 20, 499)
define DateTimeSameOrAfterMillisecondFalse: DateTime(2014, 10, 25, 15, 45, 20, 500) same millisecond or after DateTime(2014, 10, 25, 15, 45, 20, 501)
define DateTimeSameOrAfterNull1: DateTime(2014, 12, 20) same day or after DateTime(2014, 12)

//SameOrBefore
define DateTimeSameOrBeforeYearTrue1: DateTime(2014) same year or before DateTime(2014)
define DateTimeSameOrBeforeYearTrue2: DateTime(2013) same year or before DateTime(2014)
define DateTimeSameOrBeforeYearFalse: DateTime(2015) same year or before DateTime(2014)
define DateTimeSameOrBeforeMonthTrue1: DateTime(2014, 12) same month or before DateTime(2014, 12)
define DateTimeSameOrBeforeMonthTrue2: DateTime(2014, 8) same month or before DateTime(2014, 9)
define DateTimeSameOrBeforeMonthFalse: DateTime(2014, 12) same month or before DateTime(2014, 11)
define DateTimeSameOrBeforeDayTrue1: DateTime(2014, 12, 20) same day or before DateTime(2014, 12, 20)
define DateTimeSameOrBeforeDayTrue2: DateTime(2014, 10, 15) same day or before DateTime(2014, 10, 20)
define DateTimeSameOrBeforeDayFalse: DateTime(2014, 10, 30) same day or before DateTime(2014, 10, 25)
define DateTimeSameOrBeforeHourTrue1: DateTime(2014, 12, 20, 12) same hour or before DateTime(2014, 12, 20, 12)
define DateTimeSameOrBeforeHourTrue2: DateTime(2014, 10, 25, 5) same hour or before DateTime(2014, 10, 25, 10)
define DateTimeSameOrBeforeHourFalse: DateTime(2014, 10, 25, 20) same hour or before DateTime(2014, 10, 25, 15)
define DateTimeSameOrBeforeMinuteTrue1: DateTime(2014, 12, 20, 12, 30) same minute or before DateTime(2014, 12, 20, 12, 30)
define DateTimeSameOrBeforeMinuteTrue2: DateTime(2014, 10, 25, 10, 20) same minute or before DateTime(2014, 10, 25, 10, 25)
define DateTimeSameOrBeforeMinuteFalse: DateTime(2014, 10, 25, 15, 55) same minute or before DateTime(2014, 10, 25, 15, 45)
define DateTimeSameOrBeforeSecondTrue1: DateTime(2014, 12, 20, 12, 30, 15) same second or before DateTime(2014, 12, 20, 12, 30, 15)
define DateTimeSameOrBeforeSecondTrue2: DateTime(2014, 10, 25, 10, 25, 15) same second or before DateTime(2014, 10, 25, 10, 25, 20)
define DateTimeSameOrBeforeSecondFalse: DateTime(2014, 10, 25, 15, 45, 25) same second or before DateTime(2014, 10, 25, 15, 45, 21)
define DateTimeSameOrBeforeMillisecondTrue1: DateTime(2014, 12, 20, 12, 30, 15, 250) same millisecond or before DateTime(2014, 12, 20, 12, 30, 15, 250)
define DateTimeSameOrBeforeMillisecondTrue2: DateTime(2014, 10, 25, 10, 25, 20, 450) same millisecond or before DateTime(2014, 10, 25, 10, 25, 20, 499)
define DateTimeSameOrBeforeMillisecondFalse: DateTime(2014, 10, 25, 15, 45, 20, 505) same millisecond or before DateTime(2014, 10, 25, 15, 45, 20, 501)
define DateTimeSameOrBeforeNull1: DateTime(2014, 12, 20) same minute or before DateTime(2014, 12, 20, 15)

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
define DateTimeSubtract2YearsAsMonths: DateTime(2014) - 24 months

//Time

//TimeOfDay

//Today
define DateTimeSameOrBeforeTodayTrue1: Today() same day or before Today()
define DateTimeSameOrBeforeTodayTrue2: Today() same day or before Today() + 1 days
define DateTimeSameOrBeforeTodayFalse: Today() + 1 years same day or before Today()
define DateTimeAddTodayTrue: Today() + 1 days > Today()
