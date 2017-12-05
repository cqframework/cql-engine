library AggregateFunctions version '1'

context Population

define _0: plan(39)


//AllTrue
define AllTrueAllTrue               : ok(Equivalent((AllTrue({true,true})),(true)), 'AllTrueAllTrue')
define AllTrueTrueFirst             : ok(Equivalent((AllTrue({true,false})),(false)), 'AllTrueTrueFirst')
define AllTrueFalseFirst            : ok(Equivalent((AllTrue({false,true})),(false)), 'AllTrueFalseFirst')
define AllTrueAllTrueFalseTrue      : ok(Equivalent((AllTrue({true,false,true})),(false)), 'AllTrueAllTrueFalseTrue')
define AllTrueAllFalseTrueFalse     : ok(Equivalent((AllTrue({false,true,false})),(false)), 'AllTrueAllFalseTrueFalse')
define AllTrueNullFirst             : ok(Equivalent((AllTrue({null,true,true})),(true)), 'AllTrueNullFirst')
define AllTrueEmptyList             : ok(Equivalent((AllTrue({})),(true)), 'AllTrueEmptyList')

//AnyTrue
define AnyTrueAllTrue               : ok(Equivalent((AnyTrue({true,true})),(true)), 'AnyTrueAllTrue')
define AnyTrueAllFalse              : ok(Equivalent((AnyTrue({false,false})),(false)), 'AnyTrueAllFalse')
define AnyTrueAllTrueFalseTrue      : ok(Equivalent((AnyTrue({true,false,true})),(true)), 'AnyTrueAllTrueFalseTrue')
define AnyTrueAllFalseTrueFalse     : ok(Equivalent((AnyTrue({false,true,false})),(true)), 'AnyTrueAllFalseTrueFalse')
define AnyTrueTrueFirst             : ok(Equivalent((AnyTrue({true,false})),(true)), 'AnyTrueTrueFirst')
define AnyTrueFalseFirst            : ok(Equivalent((AnyTrue({false,true})),(true)), 'AnyTrueFalseFirst')
define AnyTrueNullFirstThenTrue     : ok(Equivalent((AnyTrue({null,true})),(true)), 'AnyTrueNullFirstThenTrue')
define AnyTrueNullFirstThenFalse    : ok(Equivalent((AnyTrue({null,false})),(false)), 'AnyTrueNullFirstThenFalse')
define AnyTrueEmptyList             : ok(Equivalent((AnyTrue({})),(false)), 'AnyTrueEmptyList')

//Avg
define AvgTest1: ok(Equivalent((Avg({ 1.0, 2.0, 3.0, 6.0 })),(3.0)), 'AvgTest1')

//Count
define CountTest1: ok(Equivalent((Count({ 15, 5, 99, null, 1 })),(4)), 'CountTest1')
define CountTestDateTime: ok(Equivalent((Count({ DateTime(2014), DateTime(2001), DateTime(2010) })),(3)), 'CountTestDateTime')
define CountTestTime: ok(Equivalent((Count({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 })),(3)), 'CountTestTime')
define CountTestNull: ok(Equivalent((Count({})),(0)), 'CountTestNull')

//Max
define MaxTestInteger: ok(Equivalent((Max({ 5, 12, 1, 15, 0, 4, 90, 44 })),(90)), 'MaxTestInteger')
define MaxTestString: ok(Equivalent((Max({ 'hi', 'bye', 'zebra' })),('zebra')), 'MaxTestString')
define MaxTestDateTime: ok(Equivalent((Max({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 6) })),(DateTime(2012, 10, 6))), 'MaxTestDateTime')
define MaxTestTime: ok(Equivalent((Max({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 })),(@T20:59:59.999)), 'MaxTestTime')

//Median
define MedianTestDecimal: ok(Equivalent((Median({6.0, 5.0, 4.0, 3.0, 2.0, 1.0})),(3.5)), 'MedianTestDecimal')

//Min
define MinTestInteger: ok(Equivalent((Min({5, 12, 1, 15, 0, 4, 90, 44})),(0)), 'MinTestInteger')
define MinTestString: ok(Equivalent((Min({'hi', 'bye', 'zebra'})),('bye')), 'MinTestString')
define MinTestDateTime: ok(Equivalent((Min({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 6) })),(DateTime(2012, 9, 5))), 'MinTestDateTime')
define MinTestTime: ok(Equivalent((Min({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 })),(@T05:59:59.999)), 'MinTestTime')

//Mode
define ModeTestInteger: ok(Equivalent((Mode({ 2, 1, 8, 2, 9, 1, 9, 9 })),(9)), 'ModeTestInteger')
define ModeTestDateTime: ok(Equivalent((Mode({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 6), DateTime(2012, 9, 5) })),(DateTime(2012, 9, 5))), 'ModeTestDateTime')
define ModeTestTime: ok(Equivalent((Mode({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999, @T05:59:59.999 })),(@T05:59:59.999)), 'ModeTestTime')

//PopulationStdDev
define PopStdDevTest1: ok(Equivalent((PopulationStdDev({ 1.0, 2.0, 3.0, 4.0, 5.0 })),(1.41421356)), 'PopStdDevTest1') //23730951454746218587388284504413604736328125

//PopulationVariance
define PopVarianceTest1: ok(Equivalent((PopulationVariance({ 1.0, 2.0, 3.0, 4.0, 5.0 })),(2.0)), 'PopVarianceTest1')

//StdDev
define StdDevTest1: ok(Equivalent((StdDev({ 1.0, 2.0, 3.0, 4.0, 5.0 })),(1.58113883)), 'StdDevTest1') //00841897613935316257993690669536590576171875

//Sum
define SumTest1: ok(Equivalent((Sum({ 6.0, 2.0, 3.0, 4.0, 5.0 })),(20.0)), 'SumTest1')
define SumTestNull: ok(Equivalent((Sum({ null, 1, null })),(1)), 'SumTestNull')

//Variance
define VarianceTest1: ok(Equivalent((Variance({ 1.0, 2.0, 3.0, 4.0, 5.0 })),(2.5)), 'VarianceTest1')


define function plan(test_count Integer):
    Message(null, true, null as String, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null as String, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)

define function todo(is_ok2 Boolean, message2 String, why_todo String):
    ok(is_ok2, message2 + ' # TODO - ' + why_todo)
    // Note: parser/runtime has a scope-breaking bug manifesting if todo() arg named "message" also.

define function skipped(is_ok2 Boolean, message2 String, why_skipped String):
    ok(is_ok2, message2 + ' # SKIPPED - ' + why_skipped)
    // Note: parser/runtime has a scope-breaking bug manifesting if todo() arg named "message" also.
