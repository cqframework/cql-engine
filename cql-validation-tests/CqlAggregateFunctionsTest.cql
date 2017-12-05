library AggregateFunctions version '1'

context Population

define _0: Plan(39)


//AllTrue
define AllTrueAllTrue               : Ok(Equivalent((AllTrue({true,true})),(true)), 'AllTrueAllTrue')
define AllTrueTrueFirst             : Ok(Equivalent((AllTrue({true,false})),(false)), 'AllTrueTrueFirst')
define AllTrueFalseFirst            : Ok(Equivalent((AllTrue({false,true})),(false)), 'AllTrueFalseFirst')
define AllTrueAllTrueFalseTrue      : Ok(Equivalent((AllTrue({true,false,true})),(false)), 'AllTrueAllTrueFalseTrue')
define AllTrueAllFalseTrueFalse     : Ok(Equivalent((AllTrue({false,true,false})),(false)), 'AllTrueAllFalseTrueFalse')
define AllTrueNullFirst             : Ok(Equivalent((AllTrue({null,true,true})),(true)), 'AllTrueNullFirst')
define AllTrueEmptyList             : Ok(Equivalent((AllTrue({})),(true)), 'AllTrueEmptyList')

//AnyTrue
define AnyTrueAllTrue               : Ok(Equivalent((AnyTrue({true,true})),(true)), 'AnyTrueAllTrue')
define AnyTrueAllFalse              : Ok(Equivalent((AnyTrue({false,false})),(false)), 'AnyTrueAllFalse')
define AnyTrueAllTrueFalseTrue      : Ok(Equivalent((AnyTrue({true,false,true})),(true)), 'AnyTrueAllTrueFalseTrue')
define AnyTrueAllFalseTrueFalse     : Ok(Equivalent((AnyTrue({false,true,false})),(true)), 'AnyTrueAllFalseTrueFalse')
define AnyTrueTrueFirst             : Ok(Equivalent((AnyTrue({true,false})),(true)), 'AnyTrueTrueFirst')
define AnyTrueFalseFirst            : Ok(Equivalent((AnyTrue({false,true})),(true)), 'AnyTrueFalseFirst')
define AnyTrueNullFirstThenTrue     : Ok(Equivalent((AnyTrue({null,true})),(true)), 'AnyTrueNullFirstThenTrue')
define AnyTrueNullFirstThenFalse    : Ok(Equivalent((AnyTrue({null,false})),(false)), 'AnyTrueNullFirstThenFalse')
define AnyTrueEmptyList             : Ok(Equivalent((AnyTrue({})),(false)), 'AnyTrueEmptyList')

//Avg
define AvgTest1: Ok(Equivalent((Avg({ 1.0, 2.0, 3.0, 6.0 })),(3.0)), 'AvgTest1')

//Count
define CountTest1: Ok(Equivalent((Count({ 15, 5, 99, null, 1 })),(4)), 'CountTest1')
define CountTestDateTime: Ok(Equivalent((Count({ DateTime(2014), DateTime(2001), DateTime(2010) })),(3)), 'CountTestDateTime')
define CountTestTime: Ok(Equivalent((Count({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 })),(3)), 'CountTestTime')
define CountTestNull: Ok(Equivalent((Count({})),(0)), 'CountTestNull')

//Max
define MaxTestInteger: Ok(Equivalent((Max({ 5, 12, 1, 15, 0, 4, 90, 44 })),(90)), 'MaxTestInteger')
define MaxTestString: Ok(Equivalent((Max({ 'hi', 'bye', 'zebra' })),('zebra')), 'MaxTestString')
define MaxTestDateTime: Ok(Equivalent((Max({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 6) })),(DateTime(2012, 10, 6))), 'MaxTestDateTime')
define MaxTestTime: Ok(Equivalent((Max({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 })),(@T20:59:59.999)), 'MaxTestTime')

//Median
define MedianTestDecimal: Ok(Equivalent((Median({6.0, 5.0, 4.0, 3.0, 2.0, 1.0})),(3.5)), 'MedianTestDecimal')

//Min
define MinTestInteger: Ok(Equivalent((Min({5, 12, 1, 15, 0, 4, 90, 44})),(0)), 'MinTestInteger')
define MinTestString: Ok(Equivalent((Min({'hi', 'bye', 'zebra'})),('bye')), 'MinTestString')
define MinTestDateTime: Ok(Equivalent((Min({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 6) })),(DateTime(2012, 9, 5))), 'MinTestDateTime')
define MinTestTime: Ok(Equivalent((Min({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 })),(@T05:59:59.999)), 'MinTestTime')

//Mode
define ModeTestInteger: Ok(Equivalent((Mode({ 2, 1, 8, 2, 9, 1, 9, 9 })),(9)), 'ModeTestInteger')
define ModeTestDateTime: Ok(Equivalent((Mode({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 6), DateTime(2012, 9, 5) })),(DateTime(2012, 9, 5))), 'ModeTestDateTime')
define ModeTestTime: Ok(Equivalent((Mode({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999, @T05:59:59.999 })),(@T05:59:59.999)), 'ModeTestTime')

//PopulationStdDev
define PopStdDevTest1: Ok(Equivalent((PopulationStdDev({ 1.0, 2.0, 3.0, 4.0, 5.0 })),(1.41421356)), 'PopStdDevTest1') //23730951454746218587388284504413604736328125

//PopulationVariance
define PopVarianceTest1: Ok(Equivalent((PopulationVariance({ 1.0, 2.0, 3.0, 4.0, 5.0 })),(2.0)), 'PopVarianceTest1')

//StdDev
define StdDevTest1: Ok(Equivalent((StdDev({ 1.0, 2.0, 3.0, 4.0, 5.0 })),(1.58113883)), 'StdDevTest1') //00841897613935316257993690669536590576171875

//Sum
define SumTest1: Ok(Equivalent((Sum({ 6.0, 2.0, 3.0, 4.0, 5.0 })),(20.0)), 'SumTest1')
define SumTestNull: Ok(Equivalent((Sum({ null, 1, null })),(1)), 'SumTestNull')

//Variance
define VarianceTest1: Ok(Equivalent((Variance({ 1.0, 2.0, 3.0, 4.0, 5.0 })),(2.5)), 'VarianceTest1')


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
