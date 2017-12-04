library AggregateFunctions version '1'

context Population

define private ExistenceOfThisEarlierDefineIsWorkaroundForTranslatorDeathWithLaterMessageNullCodeArg: Message(null, true, '', 'Message', '')

define _0: plan(300)


//AllTrue
define AllTrueAllTrue               : ok(Equivalent((AllTrue({true,true})),(null)), 'AllTrueAllTrue')
define AllTrueTrueFirst             : ok(Equivalent((AllTrue({true,false})),(null)), 'AllTrueTrueFirst')
define AllTrueFalseFirst            : ok(Equivalent((AllTrue({false,true})),(null)), 'AllTrueFalseFirst')
define AllTrueAllTrueFalseTrue      : ok(Equivalent((AllTrue({true,false,true})),(null)), 'AllTrueAllTrueFalseTrue')
define AllTrueAllFalseTrueFalse     : ok(Equivalent((AllTrue({false,true,false})),(null)), 'AllTrueAllFalseTrueFalse')
define AllTrueNullFirst             : ok(Equivalent((AllTrue({null,true,true})),(null)), 'AllTrueNullFirst')
define AllTrueEmptyList             : ok(Equivalent((AllTrue({})),(null)), 'AllTrueEmptyList')

//AnyTrue
define AnyTrueAllTrue               : ok(Equivalent((AnyTrue({true,true})),(null)), 'AnyTrueAllTrue')
define AnyTrueAllFalse              : ok(Equivalent((AnyTrue({false,false})),(null)), 'AnyTrueAllFalse')
define AnyTrueAllTrueFalseTrue      : ok(Equivalent((AnyTrue({true,false,true})),(null)), 'AnyTrueAllTrueFalseTrue')
define AnyTrueAllFalseTrueFalse     : ok(Equivalent((AnyTrue({false,true,false})),(null)), 'AnyTrueAllFalseTrueFalse')
define AnyTrueTrueFirst             : ok(Equivalent((AnyTrue({true,false})),(null)), 'AnyTrueTrueFirst')
define AnyTrueFalseFirst            : ok(Equivalent((AnyTrue({false,true})),(null)), 'AnyTrueFalseFirst')
define AnyTrueNullFirstThenTrue     : ok(Equivalent((AnyTrue({null,true})),(null)), 'AnyTrueNullFirstThenTrue')
define AnyTrueNullFirstThenFalse    : ok(Equivalent((AnyTrue({null,false})),(null)), 'AnyTrueNullFirstThenFalse')
define AnyTrueEmptyList             : ok(Equivalent((AnyTrue({})),(null)), 'AnyTrueEmptyList')

//Avg
define AvgTest1: ok(Equivalent((Avg({ 1.0, 2.0, 3.0, 6.0 })),(null)), 'AvgTest1')

//Count
define CountTest1: ok(Equivalent((Count({ 15, 5, 99, null, 1 })),(null)), 'CountTest1')
define CountTestDateTime: ok(Equivalent((Count({ DateTime(2014), DateTime(2001), DateTime(2010) })),(null)), 'CountTestDateTime')
define CountTestTime: ok(Equivalent((Count({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 })),(null)), 'CountTestTime')
define CountTestNull: ok(Equivalent((Count({})),(null)), 'CountTestNull')

//Max
define MaxTestInteger: ok(Equivalent((Max({ 5, 12, 1, 15, 0, 4, 90, 44 })),(null)), 'MaxTestInteger')
define MaxTestString: ok(Equivalent((Max({ 'hi', 'bye', 'zebra' })),(null)), 'MaxTestString')
define MaxTestDateTime: ok(Equivalent((Max({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 6) })),(null)), 'MaxTestDateTime')
define MaxTestTime: ok(Equivalent((Max({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 })),(null)), 'MaxTestTime')

//Median
define MedianTestDecimal: ok(Equivalent((Median({6.0, 5.0, 4.0, 3.0, 2.0, 1.0})),(null)), 'MedianTestDecimal')

//Min
define MinTestInteger: ok(Equivalent((Min({5, 12, 1, 15, 0, 4, 90, 44})),(null)), 'MinTestInteger')
define MinTestString: ok(Equivalent((Min({'hi', 'bye', 'zebra'})),(null)), 'MinTestString')
define MinTestDateTime: ok(Equivalent((Min({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 6) })),(null)), 'MinTestDateTime')
define MinTestTime: ok(Equivalent((Min({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 })),(null)), 'MinTestTime')

//Mode
define ModeTestInteger: ok(Equivalent((Mode({ 2, 1, 8, 2, 9, 1, 9, 9 })),(null)), 'ModeTestInteger')
define ModeTestDateTime: ok(Equivalent((Mode({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 6), DateTime(2012, 9, 5) })),(null)), 'ModeTestDateTime')
define ModeTestTime: ok(Equivalent((Mode({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999, @T05:59:59.999 })),(null)), 'ModeTestTime')

//PopulationStdDev
define PopStdDevTest1: ok(Equivalent((PopulationStdDev({ 1.0, 2.0, 3.0, 4.0, 5.0 })),(null)), 'PopStdDevTest1')

//PopulationVariance
define PopVarianceTest1: ok(Equivalent((PopulationVariance({ 1.0, 2.0, 3.0, 4.0, 5.0 })),(null)), 'PopVarianceTest1')

//StdDev
define StdDevTest1: ok(Equivalent((StdDev({ 1.0, 2.0, 3.0, 4.0, 5.0 })),(null)), 'StdDevTest1')

//Sum
define SumTest1: ok(Equivalent((Sum({ 6.0, 2.0, 3.0, 4.0, 5.0 })),(null)), 'SumTest1')
define SumTestNull: ok(Equivalent((Sum({ null, 1, null })),(null)), 'SumTestNull')

//Variance
define VarianceTest1: ok(Equivalent((Variance({ 1.0, 2.0, 3.0, 4.0, 5.0 })),(null)), 'VarianceTest1')


define function plan(test_count Integer):
    Message(null, true, null, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
