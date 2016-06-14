library AggregateFunctions version '1'

context Population

//AllTrue
define AllTrueAllTrue               : AllTrue({true,true})
define AllTrueTrueFirst             : AllTrue({true,false})
define AllTrueFalseFirst            : AllTrue({false,true})
define AllTrueAllTrueFalseTrue      : AllTrue({true,false,true})
define AllTrueAllFalseTrueFalse     : AllTrue({false,true,false})
define AllTrueNullFirst             : AllTrue({null,true,true})
define AllTrueEmptyList             : AllTrue({})

//AnyTrue
define AnyTrueAllTrue               : AnyTrue({true,true})
define AnyTrueAllFalse              : AnyTrue({false,false})
define AnyTrueAllTrueFalseTrue      : AnyTrue({true,false,true})
define AnyTrueAllFalseTrueFalse     : AnyTrue({false,true,false})
define AnyTrueTrueFirst             : AnyTrue({true,false})
define AnyTrueFalseFirst            : AnyTrue({false,true})
define AnyTrueNullFirstThenTrue     : AnyTrue({null,true})
define AnyTrueNullFirstThenFalse    : AnyTrue({null,false})
define AnyTrueEmptyList             : AnyTrue({})

//Avg
define AvgTest1: Avg({1.0, 2.0, 3.0, 6.0})

//Count
define CountTest1: Count({15, 5, 99, null, 1})

//Max
define MaxTest1: Max({5, 12, 1, 15, 0, 4, 90, 44})
define MaxTest2: Max({'hi', 'bye', 'zebra'})

//Median
define MedianTest1: Median({5, 4, 3, 2, 1})
define MedianTest2: Median({6.0, 5.0, 4.0, 3.0, 2.0, 1.0})

//Min
define MinTest1: Min({5, 12, 1, 15, 0, 4, 90, 44})
define MinTest2: Min({'hi', 'bye', 'zebra'})

//Mode
define ModeTest1: Mode({2, 1, 8, 2, 9, 1, 9, 9})

//PopulationStdDev
define PopStdDevTest1: PopulationStdDev({1.0, 2.0, 3.0, 4.0, 5.0})

//PopulationVariance
define PopVarianceTest1: PopulationVariance({1.0, 2.0, 3.0, 4.0, 5.0})

//StdDev
define StdDevTest1: StdDev({1.0, 2.0, 3.0, 4.0, 5.0})

//Sum
define SumTest1: Sum({6.0, 2.0, 3.0, 4.0, 5.0})

//Variance
define VarianceTest1: Variance({1.0, 2.0, 3.0, 4.0, 5.0})
