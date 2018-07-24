library CqlTestSuite version '1'

codesystem "SNOMED-CT": 'http://snomed.info/sct'

/*
    NOTES:
    This test suite is meant to serve as a comprehensive, portable test suite for Any implementation of a CQL execution engine.

    There are 3 components of this suite (of which this file is one):
      1. Type/value tests
      2. Valid operator tests (this file)
      3. Invalid operator tests (All these tests result in a runtime error)

    This suite is an extension of the Bonnie tests, which can be found here:
       https://github.com/cqframework/cql-execution/tree/master/test/elm
*/

define function TestMessage(condition Boolean, testName String, expected String, result String):
  if condition
    then testName + ' TEST PASSED'
  else Message('TEST SUITE FAILED', true, 'Test ' + testName + ' failed', 'Error', 'Expected ' + expected + ' Found ' + result)

/*
*************************
  Start Aggregate Tests
*************************
*/

// Count
define Count_not_null: Count({1,2,3,4,5})
define Count_has_null: Count({1,null,null,null,2})
define Count_empty: Count({})

define test_Count_not_null: TestMessage(Count_not_null = 5, 'Count_not_null', ToString(5), ToString(Count_not_null))
define test_Count_has_null: TestMessage(Count_has_null = 2, 'Count_has_null', ToString(2), ToString(Count_not_null))
define test_Count_empty: TestMessage(Count_empty = 0, 'Count_empty', ToString(0), ToString(Count_empty))

// Sum
define Sum_not_null: Sum({1,2,3,4,5})
define Sum_has_null: Sum({1,null,null,null,2})
define Sum_not_null_q: Sum({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define Sum_has_null_q: Sum({1 'ml',null,null,null,2 'ml'})
define Sum_unmatched_units_q: Sum({1 'ml',2 'L',3 'ml',4 'ml',5 'ml',0 'ml'})
define Sum_empty: Sum(List<Integer>{})
define Sum_q_diff_units: Sum({1 'ml',0.002 'l',0.03 'dl',4 'ml',0.005 'l'})

define test_Sum_not_null: TestMessage(Sum_not_null = 15, 'Sum_not_null', ToString(15), ToString(Sum_not_null))
define test_Sum_has_null: TestMessage(Sum_has_null = 3, 'Sum_has_null', ToString(3), ToString(Sum_has_null))
define test_Sum_not_null_q: TestMessage(Sum_not_null_q = 15'ml', 'Sum_not_null_q', ToString(15'ml'), ToString(Sum_not_null_q))
define test_Sum_has_null_q: TestMessage(Sum_has_null_q = 3'ml', 'Sum_has_null_q', ToString(3'ml'), ToString(Sum_has_null_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_Sum_unmatched_units_q: TestMessage(Sum_unmatched_units_q is null, 'Sum_unmatched_units_q', 'null', ToString(Sum_unmatched_units_q))
define test_Sum_empty: TestMessage(Sum_empty is null, 'Sum_empty', 'null', ToString(Sum_empty))
// TODO - this behavior has yet to be implemented for Quantity
// define test_Sum_q_diff_units: TestMessage(Sum_q_diff_units = 15'ml', 'Sum_q_diff_units', ToString(15'ml'), ToString(Sum_q_diff_units))

// Min
define Min_not_null: Min({1,2,3,4,5,0})
define Min_has_null: Min({1,null,-1,null,2})
define Min_empty: Min(List<Integer>{})
define Min_not_null_q: Min({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml',0 'ml'})
define Min_has_null_q: Min({1 'ml',null,-1 'ml',null,2 'ml'})
define Min_q_diff_units: Min({1 'ml',2 'dl',3 'l',4 'l',5 'l',0 'ml'})
// TODO - move to error suite and implement for Quantity
// define Min_q_throw1: Min({1 'ml',2 'm',3 'ml',4 'ml',5 'ml',0 'ml'})
// define Min_q_throw2: Min({1 ,2 'ml',3 'ml',4 'ml',5 'ml',0 'ml'})

define test_Min_not_null: TestMessage(Min_not_null = 0, 'Min_not_null', ToString(0), ToString(Min_not_null))
define test_Min_has_null: TestMessage(Min_has_null = -1, 'Min_has_null', ToString(-1), ToString(Min_has_null))
define test_Min_empty: TestMessage(Min_empty is null, 'Min_empty', 'null', ToString(Min_empty))
define test_Min_not_null_q: TestMessage(Min_not_null_q = 0 'ml', 'Min_not_null_q', ToString(0 'ml'), ToString(Min_not_null_q))
define test_Min_has_null_q: TestMessage(Min_has_null_q = -1 'ml', 'Min_has_null_q', ToString(-1 'ml'), ToString(Min_has_null_q))
// TODO - this behavior has yet to be implemented for Quantity - this test will succeed by accident (add better)
// define test_Min_q_diff_units: TestMessage(Min_q_diff_units = 0 'ml', 'Min_q_diff_units', ToString(0 'ml'), ToString(Min_q_diff_units))

// Max
define Max_not_null: Max({10,1,2,3,4,5})
define Max_has_null: Max({1,null,null,2})
define Max_empty: Max(List<Integer>{})
define Max_not_null_q: Max({10 'ml',1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define Max_has_null_q: Max({1 'ml',null,null,2 'ml'})
define Max_q_diff_units: Max({10 'ml',1 'ml',2 'ml',3 'ml',4 'ml',5 'l'})
// TODO - move to error suite and implement for Quantity
// define Max_q_throw1: Max({10 'ml',1 'm',2 'ml',3 'ml',4 'ml',5 'ml'})
// define Max_q_throw2: Max({10 ,1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})

define test_Max_not_null: TestMessage(Max_not_null = 10, 'Max_not_null', ToString(10), ToString(Max_not_null))
define test_Max_has_null: TestMessage(Max_has_null = 2, 'Max_has_null', ToString(2), ToString(Max_has_null))
define test_Max_empty: TestMessage(Max_empty is null, 'Max_empty', 'null', ToString(Max_empty))
define test_Max_not_null_q: TestMessage(Max_not_null_q = 10 'ml', 'Max_not_null_q', ToString(10 'ml'), ToString(Max_not_null_q))
define test_Max_has_null_q: TestMessage(Max_has_null_q = 2 'ml', 'Max_has_null_q', ToString(2 'ml'), ToString(Max_has_null_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_Max_q_diff_units: TestMessage(Max_q_diff_units = 5000 'ml', 'Max_q_diff_units', ToString(5000 'ml'), ToString(Max_q_diff_units))

// Avg
define Avg_not_null: Avg({1,2,3,4,5})
define Avg_has_null: Avg({1,null,null,2})
define Avg_empty: Avg(List<Integer>{})
define Avg_not_null_q: Avg({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define Avg_has_null_q: Avg({1 'ml',null,null,2 'ml'})
define Avg_q_diff_units: Avg({1 'ml',0.002 'l',0.03 'dl',4 'ml',5 'ml'})
// TODO - move to error suite and implement for Quantity
// define Avg_q_throw1: Avg({1 'ml',0.002 'm',0.03 'dl',4 'ml',5 'ml'})

define test_Avg_not_null: TestMessage(Avg_not_null = 3.0, 'Avg_not_null', ToString(3.0), ToString(Avg_not_null))
define test_Avg_has_null: TestMessage(Avg_has_null = 1.5, 'Avg_has_null', ToString(1.5), ToString(Avg_has_null))
define test_Avg_empty: TestMessage(Avg_empty is null, 'Avg_empty', 'null', ToString(Avg_empty))
define test_Avg_not_null_q: TestMessage(Avg_not_null_q = 3.0 'ml', 'Avg_not_null_q', ToString(3.0 'ml'), ToString(Avg_not_null_q))
define test_Avg_has_null_q: TestMessage(Avg_has_null_q = 1.5 'ml', 'Avg_has_null_q', ToString(1.5 'ml'), ToString(Avg_has_null_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_Avg_q_diff_units: TestMessage(Avg_q_diff_units = 3.0 'ml', 'Avg_q_diff_units', ToString(3.0 'ml'), ToString(Avg_q_diff_units))

// Median
define Median_odd: Median({5,1,2,3,4})
define Median_even: Median({5,1,2,3,4,6})
define Median_odd_q: Median({5 'ml',1 'ml',2 'ml',3 'ml',4 'ml'})
define Median_even_q: Median({5 'ml',1 'ml',2 'ml',3 'ml',4 'ml',6 'ml'})
define Median_empty: Median(List<Integer>{})
define Median_has_null: Median({1,null,null,2})
define Median_dup_vals_even: Median({3,1,2,2,2,3,4,5})
define Median_dup_vals_odd:  Median({3,1,2,2,2,3,4,5,6})
define Median_has_null_q: Median({1 'ml',null,null,2 'ml'})
define Median_dup_vals_even_q: Median({3 'ml',1 'ml',2 'ml',2 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define Median_dup_vals_odd_q:  Median({3 'ml',1 'ml',2 'ml',2 'ml',2 'ml',3 'ml',4 'ml',5 'ml',6 'ml'})
define Median_q_diff_units: Median({5 'ml',0.001 'l',0.02 'dl',3 'ml',4 'ml',6 'ml'})
// TODO - move to error suite and implement for Quantity
// define Median_q_throw1: Median({5 'ml',0.001 'l',0.22 'dl',3 'm',4 'h',6 'ml'})
// define Median_q_throw2: Median({5 ,1 ,2 ,3 ,4 'ml',6 'ml'})

define test_Median_odd: TestMessage(Median_odd = 3, 'Median_odd', ToString(3), ToString(Median_odd))
define test_Median_even: TestMessage(Median_even = 3.5, 'Median_even', ToString(3.5), ToString(Median_even))
define test_Median_odd_q: TestMessage(Median_odd_q = 3 'ml', 'Median_odd_q', ToString(3 'ml'), ToString(Median_odd_q))
define test_Median_even_q: TestMessage(Median_even_q = 3.5 'ml', 'Median_even_q', ToString(3.5 'ml'), ToString(Median_even_q))
define test_Median_empty: TestMessage(Median_empty is null, 'Median_empty', 'null', ToString(Median_empty))
define test_Median_has_null: TestMessage(Median_has_null = 1.5, 'Median_has_null', ToString(1.5), ToString(Median_has_null))
define test_Median_dup_vals_even: TestMessage(Median_dup_vals_even = 2.5, 'Median_dup_vals_even', ToString(2.5), ToString(Median_dup_vals_even))
define test_Median_dup_vals_odd: TestMessage(Median_dup_vals_odd = 3, 'Median_dup_vals_odd', ToString(3), ToString(Median_dup_vals_odd))
define test_Median_has_null_q: TestMessage(Median_has_null_q = 1.5 'ml', 'Median_has_null_q', ToString(1.5 'ml'), ToString(Median_has_null_q))
define test_Median_dup_vals_even_q: TestMessage(Median_dup_vals_even_q = 2.5 'ml', 'Median_dup_vals_even_q', ToString(2.5 'ml'), ToString(Median_dup_vals_even_q))
define test_Median_dup_vals_odd_q: TestMessage(Median_dup_vals_odd_q = 3 'ml', 'Median_dup_vals_odd_q', ToString(3 'ml'), ToString(Median_dup_vals_odd_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_Median_q_diff_units: TestMessage(Median_q_diff_units = 3.5 'ml', 'Median_q_diff_units', ToString(3.5 'ml'), ToString(Median_q_diff_units))

// Mode
define Mode_not_null: Mode({1,2,2,2,3,4,5})
define Mode_has_null: Mode({1,null,null,2,2})
define Mode_empty: Mode(List<Integer>{})
define Mode_bi_modal: Mode({1,2,2,2,3,3,3,4,5})

define test_Mode_not_null: TestMessage(Mode_not_null = 2, 'Mode_not_null', ToString(2), ToString(Mode_not_null))
define test_Mode_has_null: TestMessage(Mode_has_null = 2, 'Mode_has_null', ToString(2), ToString(Mode_has_null))
define test_Mode_empty: TestMessage(Mode_empty is null, 'Mode_empty', 'null', ToString(Mode_empty))
// TODO - CQL doesn't support multimodal operations
// define test_Mode_bi_modal: TestMessage(Mode_bi_modal = { 2, 3 }, 'Mode_bi_modal', '{ 2, 3 }', ToString(Mode_bi_modal))

// Variance
define Variance_v: Variance({1,2,3,4,5})
define Variance_v_q: Variance({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define Variance_q_diff_units: Variance({1.0 'ml',0.002 'l',0.003 'l',0.04 'dl',5.0 'ml'})
// TODO - move to error suite and implement for Quantity
// define Variance_q_throw1: Variance({1.0 'm',2.0 'l',3.0 'h',4.0 'ml',5.0 'ml'})
// define Variance_q_throw2: Variance({1.0 ,2.0 ,3.0 ,4.0 'ml',5.0 'ml'})

define test_Variance_v: TestMessage(Variance_v = 2.5, 'Variance_v', ToString(2.5), ToString(Variance_v))
define test_Variance_v_q: TestMessage(Variance_v_q = 2.5 'ml', 'Variance_v_q', ToString(2.5 'ml'), ToString(Variance_v_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_Variance_q_diff_units: TestMessage(Variance_q_diff_units = 2.5 'ml', 'Variance_q_diff_units', ToString(2.5 'ml'), ToString(Variance_q_diff_units))

// PopulationVariance
define PopulationVariance_v: PopulationVariance({1.0,2.0,3.0,4.0,5.0})
define PopulationVariance_v_q: PopulationVariance({1.0 'ml',2.0 'ml',3.0 'ml',4.0 'ml',5.0 'ml'})
define PopulationVariance_q_diff_units: PopulationVariance({1.0 'ml',0.002 'l',0.003 'l',0.04 'dl',5.0 'ml'})
// TODO - move to error suite and implement for Quantity
// define PopulationVariance_q_throw1: PopulationVariance({1.0 'm',2.0 'l',3.0 'h',4.0 'ml',5.0 'ml'})
// define PopulationVariance_q_throw2: PopulationVariance({1.0 ,2.0 ,3.0 ,4.0 'ml',5.0 'ml'})

define test_PopulationVariance_v: TestMessage(PopulationVariance_v = 2.0, 'PopulationVariance_v', ToString(2.0), ToString(PopulationVariance_v))
define test_PopulationVariance_v_q: TestMessage(PopulationVariance_v_q = 2.0 'ml', 'PopulationVariance_v_q', ToString(2.0 'ml'), ToString(PopulationVariance_v_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_PopulationVariance_q_diff_units: TestMessage(PopulationVariance_q_diff_units = 2.0 'ml', 'PopulationVariance_q_diff_units', ToString(2.0 'ml'), ToString(PopulationVariance_q_diff_units))

// StdDev
define Std: StdDev({1,2,3,4,5})
define Std_q: StdDev({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define Std_q_diff_units: StdDev({1 'ml', 0.002 'l',3 'ml',4 'ml', 0.05 'dl'})
// TODO - move to error suite and implement for Quantity
// define Std_sq_throw1: StdDev({1 'ml',2 'ml',3 'ml',4 'ml',5 'm'})
// define Std_q_throw2: StdDev({1 ,2 ,3 ,4 'ml',5 })

define test_Std: TestMessage(Std = 1.58113883, 'Std', ToString(1.58113883), ToString(Std))
define test_Std_q: TestMessage(Std_q = 1.58113883 'ml', 'Std_q', ToString(1.58113883 'ml'), ToString(Std_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_Std_q_diff_units: TestMessage(Std_q_diff_units = 1.58113883 'ml', 'Std_q_diff_units', ToString(1.58113883 'ml'), ToString(Std_q_diff_units))

// PopulationStdDev
define PopulationStd: PopulationStdDev({1,2,3,4,5})
define PopulationStd_q: PopulationStdDev({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define PopulationStd_q_diff_units: PopulationStdDev({1 'ml', 0.002 'l',3 'ml',4 'ml', 0.05 'dl'})
// TODO - move to error suite and implement for Quantity
// define PopulationStd_q_throw1: PopulationStdDev({1 'ml',2 'ml',3 'ml',4 'ml',5 'm'})
// define PopulationStd_q_throw2: PopulationStdDev({1 ,2 ,3 ,4 'ml',5 })

define test_PopulationStd: TestMessage(PopulationStd = 1.41421356, 'PopulationStd', ToString(1.41421356), ToString(PopulationStd))
define test_PopulationStd_q: TestMessage(PopulationStd_q = 1.41421356 'ml', 'PopulationStd_q', ToString(1.41421356 'ml'), ToString(PopulationStd_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_PopulationStd_q_diff_units: TestMessage(PopulationStd_q_diff_units = 1.41421356 'ml', 'PopulationStd_q_diff_units', ToString(1.41421356 'ml'), ToString(PopulationStd_q_diff_units))


// AllTrue
define All_True: AllTrue({true,true,true,true})
define All_True_wn: AllTrue({true,true,null,null,true,true})
define All_True_f: AllTrue({true,true,true,false})
define All_True_fwn: AllTrue({true,true,null,null,true,false})

define test_All_True: TestMessage(All_True, 'All_True', ToString(true), ToString(All_True))
define test_All_True_wn: TestMessage(All_True_wn, 'All_True_wn', ToString(true), ToString(All_True_wn))
define test_All_True_f: TestMessage(not All_True_f, 'All_True_f', ToString(false), ToString(All_True_f))
define test_All_True_fwn: TestMessage(not All_True_fwn, 'All_True_fwn', ToString(false), ToString(All_True_fwn))

// AnyTrue
define Any_True: AnyTrue({true,false,false,true})
define Any_True_wn: AnyTrue({true,false,null,null,false,true})
define Any_True_f: AnyTrue({false,false,false,false})
define Any_True_fwn: AnyTrue({false,false,null,null,false,false})

define test_Any_True: TestMessage(Any_True, 'Any_True', ToString(true), ToString(Any_True))
define test_Any_True_wn: TestMessage(Any_True_wn, 'Any_True_wn', ToString(true), ToString(Any_True_wn))
define test_Any_True_f: TestMessage(not Any_True_f, 'Any_True_f', ToString(false), ToString(Any_True_f))
define test_Any_True_fwn: TestMessage(not Any_True_fwn, 'Any_True_fwn', ToString(false), ToString(Any_True_fwn))

/*
***********************
  End Aggregate Tests
***********************
*/


/*
**************************
  Start Arithmetic Tests
**************************
*/

define Ten: 10
define Eleven: 11

// Add
define OnePlusTwo: 1 + 2
define AddMultiple: 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10
define AddVariables: Ten + Eleven

define test_OnePlusTwo: TestMessage(OnePlusTwo = 3, 'OnePlusTwo', ToString(3), ToString(OnePlusTwo))
define test_AddMultiple: TestMessage(AddMultiple = 55, 'AddMultiple', ToString(55), ToString(AddMultiple))
define test_AddVariables: TestMessage(AddVariables = 21, 'AddVariables', ToString(21), ToString(AddVariables))

// Subtract
define FiveMinusTwo: 5 - 2
define SubtractMultiple: 100 - 50 - 25 - 10
define SubtractVariables: Eleven - Ten

define test_FiveMinusTwo: TestMessage(FiveMinusTwo = 3, 'FiveMinusTwo', ToString(3), ToString(FiveMinusTwo))
define test_SubtractMultiple: TestMessage(SubtractMultiple = 15, 'SubtractMultiple', ToString(15), ToString(SubtractMultiple))
define test_SubtractVariables: TestMessage(SubtractVariables = 1, 'SubtractVariables', ToString(1), ToString(SubtractVariables))

// Multiply
define FiveTimesTwo: 5 * 2
define MultiplyMultiple: 1 * 2 * 3 * 4 * 5
define MultiplyVariables: Eleven * Ten

define test_FiveTimesTwo: TestMessage(FiveTimesTwo = 10, 'FiveTimesTwo', ToString(10), ToString(FiveTimesTwo))
define test_MultiplyMultiple: TestMessage(MultiplyMultiple = 120, 'MultiplyMultiple', ToString(120), ToString(MultiplyMultiple))
define test_MultiplyVariables: TestMessage(MultiplyVariables = 110, 'MultiplyVariables', ToString(110), ToString(MultiplyVariables))

// Divide
define Hundred: 100
define Four: 4
define TenDividedByTwo: 10 / 2
define TenDividedByFour: 10 / 4
define DivideMultiple: 1000 / 4 / 10 / 5
define DivideVariables: Hundred / Four

define test_TenDividedByTwo: TestMessage(TenDividedByTwo = 5, 'TenDividedByTwo', ToString(5), ToString(TenDividedByTwo))
define test_TenDividedByFour: TestMessage(TenDividedByFour = 2.5, 'TenDividedByFour', ToString(2.5), ToString(TenDividedByFour))
define test_DivideMultiple: TestMessage(DivideMultiple = 5, 'DivideMultiple', ToString(5), ToString(DivideMultiple))
define test_DivideVariables: TestMessage(DivideVariables = 25, 'DivideVariables', ToString(25), ToString(DivideVariables))

// Negate
define NegativeOne: -1

define test_NegativeOne: TestMessage(NegativeOne = -1, 'NegativeOne', ToString(-1), ToString(NegativeOne))

// MathPrecedence
define Arithmetic_Mixed: 1 + 5 * 10 - 15 / 3
define Arithmetic_Parenthetical: (1 + 5) * (10 - 15) / 3

define test_Arithmetic_Mixed: TestMessage(Arithmetic_Mixed = 46, 'Arithmetic_Mixed', ToString(46), ToString(Arithmetic_Mixed))
define test_Arithmetic_Parenthetical: TestMessage(Arithmetic_Parenthetical = -10, 'Arithmetic_Parenthetical', ToString(-10), ToString(Arithmetic_Parenthetical))

// Power
define Pow: 3 ^ 4

define test_Pow: TestMessage(Pow = 81, 'Pow', ToString(81), ToString(Pow))

// MinValue
define MinInteger: minimum Integer
define MinDecimal: minimum Decimal
define MinDateTime: minimum  DateTime
define MinTime: minimum Time
// TODO - add to error tests
// define MinWrongType: minimum Quantity

define test_MinInteger: TestMessage(MinInteger = -2147483648, 'MinInteger', ToString(-2147483648), ToString(MinInteger))
define test_MinDecimal: TestMessage(MinDecimal = -9999999999999999999999999999.99999999, 'MinDecimal', ToString(-9999999999999999999999999999.99999999), ToString(MinDecimal))
define test_MinDateTime: TestMessage(MinDateTime = @0001-01-01T00:00:00.000, 'MinDateTime', ToString(@0001-01-01T00:00:00.000), ToString(MinDateTime))
define test_MinTime: TestMessage(MinTime = @T00:00:00.000, 'MinTime', ToString(@T00:00:00.000), ToString(MinTime))

// MaxValue
define MaxInteger: maximum Integer
define MaxDecimal: maximum Decimal
define MaxDateTime: maximum DateTime
define MaxTime: maximum Time
// TODO - add to error tests
// define MaxWrongType: maximum Quantity

define test_MaxInteger: TestMessage(MaxInteger = 2147483647, 'MaxInteger', ToString(2147483647), ToString(MaxInteger))
define test_MaxDecimal: TestMessage(MaxDecimal = 9999999999999999999999999999.99999999, 'MaxDecimal', ToString(9999999999999999999999999999.99999999), ToString(MaxDecimal))
define test_MaxDateTime: TestMessage(MaxDateTime = @9999-12-31T23:59:59.999, 'MaxDateTime', ToString(@9999-12-31T23:59:59.999), ToString(MaxDateTime))
define test_MaxTime: TestMessage(MaxTime = @T23:59:59.999, 'MaxTime', ToString(@T23:59:59.999), ToString(MaxTime))

// TruncatedDivide
define TruncDivide: 10 div 3
define TruncDivideEven: 9 div 3

define test_TruncDivide: TestMessage(TruncDivide = 3, 'TruncDivide', ToString(3), ToString(TruncDivide))
define test_TruncDivideEven: TestMessage(TruncDivideEven = 3, 'TruncDivideEven', ToString(3), ToString(TruncDivideEven))

// Modulo
define Mod: 3 mod 2

define test_Mod: TestMessage(Mod = 1, 'Mod', ToString(1), ToString(Mod))

// Ceiling
define Ceil: Ceiling(10.1)
define CeilEven: Ceiling(10)

define test_Ceil: TestMessage(Ceil = 11, 'Ceil', ToString(11), ToString(Ceil))
define test_CeilEven: TestMessage(CeilEven = 10, 'CeilEven', ToString(10), ToString(CeilEven))

// Floor
define FloorTen: Floor(10.1)
define FloorEven: Floor(10)

define test_FloorTen: TestMessage(FloorTen = 10, 'FloorTen', ToString(10), ToString(FloorTen))
define test_FloorEven: TestMessage(FloorEven = 10, 'FloorEven', ToString(10), ToString(FloorEven))

// Truncate
define Trunc: Truncate(10.1)
define TruncEven: Truncate(10)

define test_Trunc: TestMessage(Trunc = 10, 'Trunc', ToString(10), ToString(Trunc))
define test_TruncEven: TestMessage(TruncEven = 10, 'TruncEven', ToString(10), ToString(TruncEven))

// Abs
define AbsPos: Abs(10)
define AbsNeg: Abs(-10)
define AbsZero: Abs(0)

define test_AbsPos: TestMessage(AbsPos = 10, 'AbsPos', ToString(10), ToString(AbsPos))
define test_AbsNeg: TestMessage(AbsNeg = 10, 'AbsNeg', ToString(10), ToString(AbsNeg))
define test_AbsZero: TestMessage(AbsZero = 0, 'AbsZero', ToString(0), ToString(AbsZero))

// Round
define RoundUp: Round(4.56)
define RoundUp_percent: Round(4.56,1)
define RoundDown: Round(4.49)
define RoundDown_percent: Round(4.43,1)

define test_RoundUp: TestMessage(RoundUp = 5, 'RoundUp', ToString(5), ToString(RoundUp))
define test_RoundUp_percent: TestMessage(RoundUp_percent = 4.6, 'RoundUp_percent', ToString(4.6), ToString(RoundUp_percent))
define test_RoundDown: TestMessage(RoundDown = 4, 'RoundDown', ToString(4), ToString(RoundDown))
define test_RoundDown_percent: TestMessage(RoundDown_percent = 4.4, 'RoundDown_percent', ToString(4.4), ToString(RoundDown_percent))

// Ln
define Ln_Four: Ln(4)

define test_Ln_Four: TestMessage(Ln_Four = 1.38629436, 'Ln_Four', ToString(1.38629436), ToString(Ln_Four))

// Log
define Log_BaseTen: Log(10000,10)

define test_Log_BaseTen: TestMessage(Log_BaseTen = 4, 'Log_BaseTen', ToString(4), ToString(Log_BaseTen))

// Successor
define Successor_Is: successor of 2
define Successor_Rs: successor of 2.2
define Successor_y_date: successor of DateTime(2015)
define Successor_ym_date: successor of DateTime(2015,01)
define Successor_ymd_date: successor of DateTime(2015,01,01)
define Successor_ymdh_date: successor of DateTime(2015,01,01,0)
define Successor_ymdhm_date: successor of DateTime(2015,01,01,0,0)
define Successor_ymdhms_date: successor of DateTime(2015,01,01,0,0,0)
define Successor_ymdhmsm_date: successor of  DateTime(2015,01,01,0,0,0,0)
// TODO - add to error tests
// define Successor_max_date: successor of DateTime(9999,12,31,23,59,59,999)
// define Successor_ofr: successor of 2147483647

define test_Successor_Is: TestMessage(Successor_Is = 3, 'Successor_Is', ToString(3), ToString(Successor_Is))
define test_Successor_Rs: TestMessage(Successor_Rs = 2.20000001, 'Successor_Rs', ToString(2.20000001), ToString(Successor_Rs))
define test_Successor_y_date: TestMessage(Successor_y_date = DateTime(2016), 'Successor_y_date', ToString(DateTime(2016)), ToString(Successor_y_date))
define test_Successor_ym_date: TestMessage(Successor_ym_date = DateTime(2015, 2), 'Successor_ym_date', ToString(DateTime(2015, 2)), ToString(Successor_ym_date))
define test_Successor_ymd_date: TestMessage(Successor_ymd_date = DateTime(2015, 1, 2), 'Successor_ymd_date', ToString(DateTime(2015, 1, 2)), ToString(Successor_ymd_date))
define test_Successor_ymdh_date: TestMessage(Successor_ymdh_date = DateTime(2015, 1, 1, 1), 'Successor_ymdh_date', ToString(DateTime(2015, 1, 1, 1)), ToString(Successor_ymdh_date))
define test_Successor_ymdhm_date: TestMessage(Successor_ymdhm_date = DateTime(2015, 1, 1, 0, 1), 'Successor_ymdhm_date', ToString(DateTime(2015, 1, 1, 0, 1)), ToString(Successor_ymdhm_date))
define test_Successor_ymdhms_date: TestMessage(Successor_ymdhms_date = DateTime(2015, 1, 1, 0, 0, 1), 'Successor_ymdhms_date', ToString(DateTime(2015, 1, 1, 0, 0, 1)), ToString(Successor_ymdhms_date))
define test_Successor_ymdhmsm_date: TestMessage(Successor_ymdhmsm_date = DateTime(2015, 1, 1, 0, 0, 0, 1), 'Successor_ymdhmsm_date', ToString(DateTime(2015, 1, 1, 0, 0, 0, 1)), ToString(Successor_ymdhmsm_date))

// Predecessor
define Predecessor_Is: predecessor of 2
define Predecessor_Rs: predecessor of 2.2
define Predecessor_y_date: predecessor of DateTime(2015)
define Predecessor_ym_date: predecessor of DateTime(2015,01)
define Predecessor_ymd_date: predecessor of DateTime(2015,01,01)
define Predecessor_ymdh_date: predecessor of DateTime(2015,01,01,0)
define Predecessor_ymdhm_date: predecessor of DateTime(2015,01,01,0,0)
define Predecessor_ymdhms_date: predecessor of DateTime(2015,01,01,0,0,0)
define Predecessor_ymdhmsm_date: predecessor of DateTime(2015,01,01,0,0,0,0)
// TODO - add to error tests
// define Predecessor_min_date: predecessor of DateTime(0001,01,01,0,0,0,0)
// define Predecessor_ufr: predecessor of -2147483648

define test_Predecessor_Is: TestMessage(Predecessor_Is = 1, 'Predecessor_Is', ToString(1), ToString(Predecessor_Is))
define test_Predecessor_Rs: TestMessage(Predecessor_Rs = 2.19999999, 'Predecessor_Rs', ToString(2.19999999), ToString(Predecessor_Rs))
define test_Predecessor_y_date: TestMessage(Predecessor_y_date = DateTime(2014), 'Predecessor_y_date', ToString(DateTime(2014)), ToString(Predecessor_y_date))
define test_Predecessor_ym_date: TestMessage(Predecessor_ym_date = DateTime(2014, 12), 'Predecessor_ym_date', ToString(DateTime(2014, 12)), ToString(Predecessor_ym_date))
define test_Predecessor_ymd_date: TestMessage(Predecessor_ymd_date = DateTime(2014, 12, 31), 'Predecessor_ymd_date', ToString(DateTime(2014, 12, 31)), ToString(Predecessor_ymd_date))
define test_Predecessor_ymdh_date: TestMessage(Predecessor_ymdh_date = DateTime(2014, 12, 31, 23), 'Predecessor_ymdh_date', ToString(DateTime(2014, 12, 31, 23)), ToString(Predecessor_ymdh_date))
define test_Predecessor_ymdhm_date: TestMessage(Predecessor_ymdhm_date = DateTime(2014, 12, 31, 23, 59), 'Predecessor_ymdhm_date', ToString(DateTime(2014, 12, 31, 23, 59)), ToString(Predecessor_ymdhm_date))
define test_Predecessor_ymdhms_date: TestMessage(Predecessor_ymdhms_date = DateTime(2014, 12, 31, 23, 59, 59), 'Predecessor_ymdhms_date', ToString(DateTime(2014, 12, 31, 23, 59, 59)), ToString(Predecessor_ymdhms_date))
define test_Predecessor_ymdhmsm_date: TestMessage(Predecessor_ymdhmsm_date = DateTime(2014, 12, 31, 23, 59, 59, 999), 'Predecessor_ymdhmsm_date', ToString(DateTime(2014, 12, 31, 23, 59, 59, 999)), ToString(Predecessor_ymdhmsm_date))

// Quantity
define Quantity_days_10: 10 days
define Quantity_QL10Days: Quantity{value: 10, unit: 'days'}
define Quantity_QL10Min : Quantity{value: 10, unit: 'min' }
define Quantity_Jan1_2000: DateTime(2000, 1, 1)
define Quantity_add_q_q : Quantity_days_10 + Quantity_QL10Days
define Quantity_add_d_q : Quantity_Jan1_2000 + Quantity_days_10
define Quantity_sub_q_q : Quantity_days_10 - Quantity_QL10Days
define Quantity_sub_d_q : Quantity_Jan1_2000 - Quantity_days_10
// TODO - implement for Quantity
// define Quantity_add_q_q_diff : Quantity_QL10Days + Quantity_QL10Min
// define Quantity_sub_q_q_diff : Quantity_QL10Days - Quantity_QL10Min
// define Quantity_div_q_q : Quantity_days_10 / QL10Days
// define Quantity_mul_q_q : 2 'm' * 10 'm'
// define Quantity_mul_q_q_diff : 2 'm' * 10 '/d'
// define Quantity_MultiplyUcum: (5 'm' * 25 'km') = 125000 'm2'
// define Quantity_DivideUcum: (20 'm2' / 5 'm') = 4 'm'
// define Quantity_AddUcum: (5 'm' + 5 'km') = 5005 'm'
// define Quantity_SubtractUcum: (25 'km' - 5 'm') = 24995 'm'
define Quantity_div_q_d : Quantity_days_10 / 2
define Quantity_mul_q_d : Quantity_days_10 * 2
define Quantity_mul_d_q : 2 * Quantity_QL10Days
define Quantity_neg : - Quantity_days_10
define Quantity_abs : Abs(Quantity_neg)

define test_Quantity_add_q_q: TestMessage(Quantity_add_q_q = 20 days, 'Quantity_add_q_q', ToString(20 days), ToString(Quantity_add_q_q))
define test_Quantity_add_d_q: TestMessage(Quantity_add_d_q = DateTime(2000, 1, 11), 'Quantity_add_d_q', ToString(DateTime(2000, 1, 11)), ToString(Quantity_add_d_q))
define test_Quantity_sub_q_q: TestMessage(Quantity_sub_q_q = 0 days, 'Quantity_sub_q_q', ToString(0 days), ToString(Quantity_sub_q_q))
define test_Quantity_sub_d_q: TestMessage(Quantity_sub_d_q = DateTime(1999, 12, 22), 'Quantity_sub_d_q', ToString(DateTime(1999, 12, 22)), ToString(Quantity_sub_d_q))
define test_Quantity_div_q_d: TestMessage(Quantity_div_q_d = 5 days, 'Quantity_div_q_d', ToString(5 days), ToString(Quantity_div_q_d))
define test_Quantity_mul_q_d: TestMessage(Quantity_mul_q_d = 20 days, 'Quantity_mul_q_d', ToString(20 days), ToString(Quantity_mul_q_d))
define test_Quantity_mul_d_q: TestMessage(Quantity_mul_d_q = 20 days, 'Quantity_mul_d_q', ToString(20 days), ToString(Quantity_mul_d_q))
define test_Quantity_neg: TestMessage(Quantity_neg = -10 days, 'Quantity_neg', ToString(-10 days), ToString(Quantity_neg))
define test_Quantity_abs: TestMessage(Quantity_abs = 10 days, 'Quantity_abs', ToString(10 days), ToString(Quantity_abs))

/*
************************
  End Arithmetic Tests
************************
*/


/*
************************
  Start Clinical Tests
************************
*/

// TODO

/*
**********************
  End Clinical Tests
**********************
*/


/*
**************************
  Start Comparison Tests
**************************
*/

// Equal
define Equal_AGtB_Int: 5 = 4
define Equal_AEqB_Int: 5 = 5
define Equal_ALtB_Int: 5 = 6
define Equal_EqTuples: Tuple{a: 1, b: Tuple{c: 1}} = Tuple{a: 1, b: Tuple{c: 1}}
define Equal_UneqTuples: Tuple{a: 1, b: Tuple{c: 1}} = Tuple{a: 1, b: Tuple{c: -1}}
define Equal_EqDateTimes: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) = DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define Equal_UneqDateTimes: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) = DateTime(2000, 3, 15, 13, 30, 25, 201, +1.0)
define Equal_EqDateTimesTZ: DateTime(2000, 3, 15, 23, 30, 25, 200, +1.0) = DateTime(2000, 3, 16, 2, 30, 25, 200, +4.0)
define Equal_UneqDateTimesTZ: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) = DateTime(2000, 3, 15, 13, 30, 25, 200, +2.0)
define Equal_PossiblyEqualDateTimes: DateTime(2000, 3, 15) = DateTime(2000)
define Equal_ImpossiblyEqualDateTimes: DateTime(2000, 3, 15) = DateTime(2000, 4)
define Equal_AGtB_Quantity: 5 'm' = 4 'm'
define Equal_AEqB_Quantity: 5 'm' = 5 'm'
define Equal_ALtB_Quantity: 5 'm' = 6 'm'
// TODO - implement for Quantity
// define Equal_AGtB_Quantity_diff: 5 'm' = 5 'cm'
// define Equal_AEqB_Quantity_diff: 5 'm' = 500 'cm'
// define Equal_ALtB_Quantity_diff: 5 'm' = 5 'km'
// TODO - implement and add to error tests
// define Equal_AGtB_Quantity_incompatible: 5 'Cel' = 4 'm'
// define Equal_AEqB_Quantity_incompatible: 5 'Cel' = 5 'm'
// define Equal_ALtB_Quantity_incompatible: 5 'Cel' = 40 'm'

define test_Equal_AGtB_Int: TestMessage(not Equal_AGtB_Int, 'Equal_AGtB_Int', ToString(false), ToString(Equal_AGtB_Int))
define test_Equal_AEqB_Int: TestMessage(Equal_AEqB_Int, 'Equal_AEqB_Int', ToString(true), ToString(Equal_AEqB_Int))
define test_Equal_ALtB_Int: TestMessage(not Equal_ALtB_Int, 'Equal_ALtB_Int', ToString(false), ToString(Equal_ALtB_Int))
define test_Equal_EqTuples: TestMessage(Equal_EqTuples, 'Equal_EqTuples', ToString(true), ToString(Equal_EqTuples))
define test_Equal_UneqTuples: TestMessage(not Equal_UneqTuples, 'Equal_UneqTuples', ToString(false), ToString(Equal_UneqTuples))
define test_Equal_EqDateTimes: TestMessage(Equal_EqDateTimes, 'Equal_EqDateTimes', ToString(true), ToString(Equal_EqDateTimes))
define test_Equal_UneqDateTimes: TestMessage(not Equal_UneqDateTimes, 'Equal_UneqDateTimes', ToString(false), ToString(Equal_UneqDateTimes))
define test_Equal_EqDateTimesTZ: TestMessage(Equal_EqDateTimesTZ, 'Equal_EqDateTimesTZ', ToString(true), ToString(Equal_EqDateTimesTZ))
define test_Equal_UneqDateTimesTZ: TestMessage(not Equal_UneqDateTimesTZ, 'Equal_UneqDateTimesTZ', ToString(false), ToString(Equal_UneqDateTimesTZ))
define test_Equal_PossiblyEqualDateTimes: TestMessage(Equal_PossiblyEqualDateTimes is null, 'Equal_PossiblyEqualDateTimes', 'null', ToString(Equal_PossiblyEqualDateTimes))
define test_Equal_ImpossiblyEqualDateTimes: TestMessage(not Equal_ImpossiblyEqualDateTimes, 'Equal_ImpossiblyEqualDateTimes', ToString(false), ToString(Equal_ImpossiblyEqualDateTimes))
define test_Equal_AGtB_Quantity: TestMessage(not Equal_AGtB_Quantity, 'Equal_AGtB_Quantity', ToString(false), ToString(Equal_AGtB_Quantity))
define test_Equal_AEqB_Quantity: TestMessage(Equal_AEqB_Quantity, 'Equal_AEqB_Quantity', ToString(true), ToString(Equal_AEqB_Quantity))
define test_Equal_ALtB_Quantity: TestMessage(not Equal_ALtB_Quantity, 'Equal_ALtB_Quantity', ToString(false), ToString(Equal_ALtB_Quantity))

// NotEqual
define NotEqual_AGtB_Int: 5 != 4
define NotEqual_AEqB_Int: 5 != 5
define NotEqual_ALtB_Int: 5 != 6
define NotEqual_EqTuples: Tuple{a: 1, b: Tuple{c: 1}} != Tuple{a: 1, b: Tuple{c: 1}}
define NotEqual_UneqTuples: Tuple{a: 1, b: Tuple{c: 1}} != Tuple{a: 1, b: Tuple{c: -1}}
define NotEqual_EqDateTimes: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) != DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define NotEqual_UneqDateTimes: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) != DateTime(2000, 3, 15, 13, 30, 25, 201, +1.0)
define NotEqual_EqDateTimesTZ: DateTime(2000, 3, 15, 23, 30, 25, 200, +1.0) != DateTime(2000, 3, 16, 2, 30, 25, 200, +4.0)
define NotEqual_UneqDateTimesTZ: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) != DateTime(2000, 3, 15, 13, 30, 25, 200, +2.0)
define NotEqual_PossiblyEqualDateTimes: DateTime(2000, 3, 15) != DateTime(2000)
define NotEqual_ImpossiblyEqualDateTimes: DateTime(2000, 3, 15) != DateTime(2000, 4)
define NotEqual_AGtB_Quantity: 5 'm' != 4 'm'
define NotEqual_AEqB_Quantity: 5 'm' != 5 'm'
define NotEqual_ALtB_Quantity: 5 'm' != 6 'm'
// TODO - implement for Quantity
define NotEqual_AGtB_Quantity_diff: 5 'm' != 5 'cm'
define NotEqual_AEqB_Quantity_diff: 5 'm' != 500 'cm'
define NotEqual_ALtB_Quantity_diff: 5 'm' != 5 'km'
// TODO - implement and add to error tests
define NotEqual_AGtB_Quantity_incompatible: 5 'Cel' != 4 'm'
define NotEqual_AEqB_Quantity_incompatible: 5 'Cel' != 5 'm'
define NotEqual_ALtB_Quantity_incompatible: 5 'Cel' != 40 'm'

define test_NotEqual_AGtB_Int: TestMessage(NotEqual_AGtB_Int, 'NotEqual_AGtB_Int', ToString(true), ToString(NotEqual_AGtB_Int))
define test_NotEqual_AEqB_Int: TestMessage(not NotEqual_AEqB_Int, 'NotEqual_AEqB_Int', ToString(false), ToString(NotEqual_AEqB_Int))
define test_NotEqual_ALtB_Int: TestMessage(NotEqual_ALtB_Int, 'NotEqual_ALtB_Int', ToString(true), ToString(NotEqual_ALtB_Int))
define test_NotEqual_EqTuples: TestMessage(not NotEqual_EqTuples, 'NotEqual_EqTuples', ToString(false), ToString(NotEqual_EqTuples))
define test_NotEqual_UneqTuples: TestMessage(NotEqual_UneqTuples, 'NotEqual_UneqTuples', ToString(true), ToString(NotEqual_UneqTuples))
define test_NotEqual_EqDateTimes: TestMessage(not NotEqual_EqDateTimes, 'NotEqual_EqDateTimes', ToString(false), ToString(NotEqual_EqDateTimes))
define test_NotEqual_UneqDateTimes: TestMessage(NotEqual_UneqDateTimes, 'NotEqual_UneqDateTimes', ToString(true), ToString(NotEqual_UneqDateTimes))
define test_NotEqual_EqDateTimesTZ: TestMessage(not NotEqual_EqDateTimesTZ, 'NotEqual_EqDateTimesTZ', ToString(false), ToString(NotEqual_EqDateTimesTZ))
define test_NotEqual_UneqDateTimesTZ: TestMessage(NotEqual_UneqDateTimesTZ, 'NotEqual_UneqDateTimesTZ', ToString(true), ToString(NotEqual_UneqDateTimesTZ))
define test_NotEqual_PossiblyEqualDateTimes: TestMessage(NotEqual_PossiblyEqualDateTimes is null, 'NotEqual_PossiblyEqualDateTimes', 'null', ToString(NotEqual_PossiblyEqualDateTimes))
define test_NotEqual_ImpossiblyEqualDateTimes: TestMessage(NotEqual_ImpossiblyEqualDateTimes, 'NotEqual_ImpossiblyEqualDateTimes', ToString(true), ToString(NotEqual_ImpossiblyEqualDateTimes))
define test_NotEqual_AGtB_Quantity: TestMessage(NotEqual_AGtB_Quantity, 'NotEqual_AGtB_Quantity', ToString(true), ToString(NotEqual_AGtB_Quantity))
define test_NotEqual_AEqB_Quantity: TestMessage(not NotEqual_AEqB_Quantity, 'NotEqual_AEqB_Quantity', ToString(false), ToString(NotEqual_AEqB_Quantity))
define test_NotEqual_ALtB_Quantity: TestMessage(NotEqual_ALtB_Quantity, 'NotEqual_ALtB_Quantity', ToString(true), ToString(NotEqual_ALtB_Quantity))

// Equivalent
define Equivalent_ANull_BDefined: null ~ 4
define Equivalent_ADefined_BNull: 5 ~ null
define Equivalent_ANull_BNull: null ~ null
define Equivalent_ADefined_BDefined: 3 ~ 3

define test_Equivalent_ANull_BDefined: TestMessage(not Equivalent_ANull_BDefined, 'Equivalent_ANull_BDefined', ToString(false), ToString(Equivalent_ANull_BDefined))
define test_Equivalent_ADefined_BNull: TestMessage(not Equivalent_ADefined_BNull, 'Equivalent_ADefined_BNull', ToString(false), ToString(Equivalent_ADefined_BNull))
define test_Equivalent_ANull_BNull: TestMessage(Equivalent_ANull_BNull, 'Equivalent_ANull_BNull', ToString(true), ToString(Equivalent_ANull_BNull))
define test_Equivalent_ADefined_BDefined: TestMessage(Equivalent_ADefined_BDefined, 'Equivalent_ADefined_BDefined', ToString(true), ToString(Equivalent_ADefined_BDefined))

// Tuple Equivalence
define Equivalent_EmptyTuples: { : } ~ { : }
define Equivalent_SameTuples: Tuple{a: 'a', b: 'b'} ~ Tuple{a: 'a', b: 'b'}
define Equivalent_SameTuplesNull: Tuple{a: null} ~ Tuple{a : null}
define Equivalent_DifferentTuples: Tuple{a: 'a', b: 'b'} ~ Tuple{a: 'x', b: 'b'}
define Equivalent_SameNestedTuples: Tuple{a: 'a', b: Tuple{c: 'c'}} ~ Tuple{a: 'a', b: Tuple{c: 'c'}}
define Equivalent_SameNestedTuplesNull: Tuple{a: 'a', b: Tuple{c: null}} ~ Tuple{a: 'a', b: Tuple{c: null}}

define test_Equivalent_EmptyTuples: TestMessage(Equivalent_EmptyTuples, 'Equivalent_EmptyTuples', ToString(true), ToString(Equivalent_EmptyTuples))
define test_Equivalent_SameTuples: TestMessage(Equivalent_SameTuples, 'Equivalent_SameTuples', ToString(true), ToString(Equivalent_SameTuples))
define test_Equivalent_SameTuplesNull: TestMessage(Equivalent_SameTuplesNull, 'Equivalent_SameTuplesNull', ToString(true), ToString(Equivalent_SameTuplesNull))
define test_Equivalent_DifferentTuples: TestMessage(not Equivalent_DifferentTuples, 'Equivalent_DifferentTuples', ToString(false), ToString(Equivalent_DifferentTuples))
define test_Equivalent_SameNestedTuples: TestMessage(Equivalent_SameNestedTuples, 'Equivalent_SameNestedTuples', ToString(true), ToString(Equivalent_SameNestedTuples))
define test_Equivalent_SameNestedTuplesNull: TestMessage(Equivalent_SameNestedTuplesNull, 'Equivalent_SameNestedTuplesNull', ToString(true), ToString(Equivalent_SameNestedTuplesNull))

// List Equivalence
define Equivalent_EmptyLists: { } ~ { }
define Equivalent_DifferentTypesLists: {'1', '2', '3'} ~ {1, 2, 3}
define Equivalent_DifferentLengthLists: {'a'} ~ {'a', 'a'}
define Equivalent_DifferentOrderLists: {'a', 'b'} ~ {'b', 'a'}
define Equivalent_SameLists: {'a', 'b', 'c'} ~ {'a', 'b', 'c'}
define Equivalent_SameListsNull: {null, null, null} ~ {null, null, null}
define Equivalent_SameNestedLists: {{'a','d'}, {'b', 'c'}} ~ {{'a','d'}, {'b', 'c'}}
define Equivalent_SameNestedListsNull: {null, {null, null}} ~ {null, {null, null}}

define test_Equivalent_EmptyLists: TestMessage(Equivalent_EmptyLists, 'Equivalent_EmptyLists', ToString(true), ToString(Equivalent_EmptyLists))
define test_Equivalent_DifferentTypesLists: TestMessage(not Equivalent_DifferentTypesLists, 'Equivalent_DifferentTypesLists', ToString(false), ToString(Equivalent_DifferentTypesLists))
define test_Equivalent_DifferentLengthLists: TestMessage(not Equivalent_DifferentLengthLists, 'Equivalent_DifferentLengthLists', ToString(false), ToString(Equivalent_DifferentLengthLists))
define test_Equivalent_DifferentOrderLists: TestMessage(not Equivalent_DifferentOrderLists, 'Equivalent_DifferentOrderLists', ToString(false), ToString(Equivalent_DifferentOrderLists))
define test_Equivalent_SameLists: TestMessage(Equivalent_SameLists, 'Equivalent_SameLists', ToString(true), ToString(Equivalent_SameLists))
define test_Equivalent_SameListsNull: TestMessage(Equivalent_SameListsNull, 'Equivalent_SameListsNull', ToString(true), ToString(Equivalent_SameListsNull))
define test_Equivalent_SameNestedLists: TestMessage(Equivalent_SameNestedLists, 'Equivalent_SameNestedLists', ToString(true), ToString(Equivalent_SameNestedLists))
define test_Equivalent_SameNestedListsNull: TestMessage(Equivalent_SameNestedListsNull, 'Equivalent_SameNestedListsNull', ToString(true), ToString(Equivalent_SameNestedListsNull))

// Interval Equivalence
define Equivalent_EmptyInterval: Interval(null, null) ~ Interval(null, null)
define Equivalent_IntervalDifferentPointTypes:  Interval[1, 5] ~ Interval['1', '5']
define Equivalent_IntervalDifferentStarts: Interval[1,3] ~ Interval[2,3]
define Equivalent_IntervalDifferentEndings: Interval[1,3] ~ Interval[1,4]
define Equivalent_SameIntervals: Interval[1,3] ~ Interval[1,3]

define test_Equivalent_EmptyInterval: TestMessage(Equivalent_EmptyInterval, 'Equivalent_EmptyInterval', ToString(true), ToString(Equivalent_EmptyInterval))
define test_Equivalent_IntervalDifferentPointTypes: TestMessage(not Equivalent_IntervalDifferentPointTypes, 'Equivalent_IntervalDifferentPointTypes', ToString(false), ToString(Equivalent_IntervalDifferentPointTypes))
define test_Equivalent_IntervalDifferentStarts: TestMessage(not Equivalent_IntervalDifferentStarts, 'Equivalent_IntervalDifferentStarts', ToString(false), ToString(Equivalent_IntervalDifferentStarts))
define test_Equivalent_IntervalDifferentEndings: TestMessage(not Equivalent_IntervalDifferentEndings, 'Equivalent_IntervalDifferentEndings', ToString(false), ToString(Equivalent_IntervalDifferentEndings))
define test_Equivalent_SameIntervals: TestMessage(Equivalent_SameIntervals, 'Equivalent_SameIntervals', ToString(true), ToString(Equivalent_SameIntervals))

// TODO - add to errors test
// define Equivalent_TupleAndList: {a: '1'} ~ {'1'}
// define Equivalent_ListAndTuple: {'1'} ~ {a: '1'}
// define Equivalent_TupleAndNullList: {a: '1'} ~ {null}
// define Equivalent_NullListAndTuple: {null} ~ {a: '1'}

// Less
define Less_AGtB_Int: 5 < 4
define Less_AEqB_Int: 5 < 5
define Less_ALtB_Int: 5 < 6
define Less_AGtB_Quantity: 5 'm' < 4 'm'
define Less_AEqB_Quantity: 5 'm' < 5 'm'
define Less_ALtB_Quantity: 5 'm' < 6 'm'
// TODO - implement for Quantity
// define Less_AGtB_Quantity_diff: 5 'm' < 5 'cm'
// define Less_AEqB_Quantity_diff: 5 'm' < 500 'cm'
// define Less_ALtB_Quantity_diff: 5 'm' < 5 'km'
// TODO - implement and add to error tests
// define Less_AGtB_Quantity_incompatible: 5 'Cel' < 4 'm'
// define Less_AEqB_Quantity_incompatible: 5 'Cel' < 5 'm'
// define Less_ALtB_Quantity_incompatible: 5 'Cel' < 40 'm'

define test_Less_AGtB_Int: TestMessage(not Less_AGtB_Int, 'Less_AGtB_Int', ToString(false), ToString(Less_AGtB_Int))
define test_Less_AEqB_Int: TestMessage(not Less_AEqB_Int, 'Less_AEqB_Int', ToString(false), ToString(Less_AEqB_Int))
define test_Less_ALtB_Int: TestMessage(Less_ALtB_Int, 'Less_ALtB_Int', ToString(true), ToString(Less_ALtB_Int))
define test_Less_AGtB_Quantity: TestMessage(not Less_AGtB_Quantity, 'Less_AGtB_Quantity', ToString(false), ToString(Less_AGtB_Quantity))
define test_Less_AEqB_Quantity: TestMessage(not Less_AEqB_Quantity, 'Less_AEqB_Quantity', ToString(false), ToString(Less_AEqB_Quantity))
define test_Less_ALtB_Quantity: TestMessage(Less_ALtB_Quantity, 'Less_ALtB_Quantity', ToString(true), ToString(Less_ALtB_Quantity))

// LessOrEqual
define LessOrEqual_AGtB_Int: 5 <= 4
define LessOrEqual_AEqB_Int: 5 <= 5
define LessOrEqual_ALtB_Int: 5 <= 6
define LessOrEqual_AGtB_Quantity: 5 'm' <= 4 'm'
define LessOrEqual_AEqB_Quantity: 5 'm' <= 5 'm'
define LessOrEqual_ALtB_Quantity: 5 'm' <= 6 'm'
// TODO - implement for Quantity
// define AGtB_Quantity_diff: 5 'm' <= 4 'm'
// define AEqB_Quantity_diff: 5 'm' <= 500 'cm'
// define ALtB_Quantity_diff: 5 'm' <= 5 'km'
// TODO - implement and add to error tests
// define AGtB_Quantity_incompatible: 5 'Cel' <= 4 'm'
// define AEqB_Quantity_incompatible: 5 'Cel' <= 5 'm'
// define ALtB_Quantity_incompatible: 5 'Cel' <= 40 'm'

define test_LessOrEqual_AGtB_Int: TestMessage(not LessOrEqual_AGtB_Int, 'LessOrEqual_AGtB_Int', ToString(false), ToString(LessOrEqual_AGtB_Int))
define test_LessOrEqual_AEqB_Int: TestMessage(LessOrEqual_AEqB_Int, 'LessOrEqual_AEqB_Int', ToString(true), ToString(LessOrEqual_AEqB_Int))
define test_LessOrEqual_ALtB_Int: TestMessage(LessOrEqual_ALtB_Int, 'LessOrEqual_ALtB_Int', ToString(true), ToString(LessOrEqual_ALtB_Int))
define test_LessOrEqual_AGtB_Quantity: TestMessage(not LessOrEqual_AGtB_Quantity, 'LessOrEqual_AGtB_Quantity', ToString(false), ToString(LessOrEqual_AGtB_Quantity))
define test_LessOrEqual_AEqB_Quantity: TestMessage(LessOrEqual_AEqB_Quantity, 'LessOrEqual_AEqB_Quantity', ToString(true), ToString(LessOrEqual_AEqB_Quantity))
define test_LessOrEqual_ALtB_Quantity: TestMessage(LessOrEqual_ALtB_Quantity, 'LessOrEqual_ALtB_Quantity', ToString(true), ToString(LessOrEqual_ALtB_Quantity))

// Greater
define Greater_AGtB_Int: 5 > 4
define Greater_AEqB_Int: 5 > 5
define Greater_ALtB_Int: 5 > 6
define Greater_AGtB_Quantity: 5 'm' > 4 'm'
define Greater_AEqB_Quantity: 5 'm' > 5 'm'
define Greater_ALtB_Quantity: 5 'm' > 6 'm'
// TODO - implement for Quantity
// define Greater_AGtB_Quantity_diff: 5 'm' > 5 'cm'
// define Greater_AEqB_Quantity_diff: 5 'm' > 500 'cm'
// define Greater_ALtB_Quantity_diff: 5 'm' > 5 'km'
// TODO - implement and add to error tests
// define Greater_AGtB_Quantity_incompatible: 5 'Cel' > 4 'm'
// define Greater_AEqB_Quantity_incompatible: 5 'Cel' > 5 'm'
// define Greater_ALtB_Quantity_incompatible: 5 'Cel' > 40 'm'

define test_Greater_AGtB_Int: TestMessage(Greater_AGtB_Int, 'Greater_AGtB_Int', ToString(true), ToString(Greater_AGtB_Int))
define test_Greater_AEqB_Int: TestMessage(not Greater_AEqB_Int, 'Greater_AEqB_Int', ToString(false), ToString(Greater_AEqB_Int))
define test_Greater_ALtB_Int: TestMessage(not Greater_ALtB_Int, 'Greater_ALtB_Int', ToString(false), ToString(Greater_ALtB_Int))
define test_Greater_AGtB_Quantity: TestMessage(Greater_AGtB_Quantity, 'Greater_AGtB_Quantity', ToString(true), ToString(Greater_AGtB_Quantity))
define test_Greater_AEqB_Quantity: TestMessage(not Greater_AEqB_Quantity, 'Greater_AEqB_Quantity', ToString(false), ToString(Greater_AEqB_Quantity))
define test_Greater_ALtB_Quantity: TestMessage(not Greater_ALtB_Quantity, 'Greater_ALtB_Quantity', ToString(false), ToString(Greater_ALtB_Quantity))

// GreaterOrEqual
define GreaterOrEqual_AGtB_Int: 5 >= 4
define GreaterOrEqual_AEqB_Int: 5 >= 5
define GreaterOrEqual_ALtB_Int: 5 >= 6
define GreaterOrEqual_AGtB_Quantity: 5 'm' >= 4 'm'
define GreaterOrEqual_AEqB_Quantity: 5 'm' >= 5 'm'
define GreaterOrEqual_ALtB_Quantity: 5 'm' >= 6 'm'
// TODO - implement for Quantity
// define AGtB_Quantity_diff: 5 'm' >= 5 'cm'
// define AEqB_Quantity_diff: 5 'm' >= 500 'cm'
// define ALtB_Quantity_diff: 5 'm' >= 5 'km'
// define DivideUcum: (100 'mg' / 2 '[lb_av]') > 49 'mg/[lb_av]'
// TODO - implement and add to error tests
// define AGtB_Quantity_incompatible: 5 'Cel' >= 4 'm'
// define AEqB_Quantity_incompatible: 5 'Cel' >= 5 'm'
// define ALtB_Quantity_incompatible: 5 'Cel' >= 40 'm'
// define DivideUcum_incompatible: (100 '[nmi_i]' / 2 'h') > 49 'mg/[lb_av]'

define test_GreaterOrEqual_AGtB_Int: TestMessage(GreaterOrEqual_AGtB_Int, 'GreaterOrEqual_AGtB_Int', ToString(true), ToString(GreaterOrEqual_AGtB_Int))
define test_GreaterOrEqual_AEqB_Int: TestMessage(GreaterOrEqual_AEqB_Int, 'GreaterOrEqual_AEqB_Int', ToString(true), ToString(GreaterOrEqual_AEqB_Int))
define test_GreaterOrEqual_ALtB_Int: TestMessage(not GreaterOrEqual_ALtB_Int, 'GreaterOrEqual_ALtB_Int', ToString(false), ToString(GreaterOrEqual_ALtB_Int))
define test_GreaterOrEqual_AGtB_Quantity: TestMessage(GreaterOrEqual_AGtB_Quantity, 'GreaterOrEqual_AGtB_Quantity', ToString(true), ToString(GreaterOrEqual_AGtB_Quantity))
define test_GreaterOrEqual_AEqB_Quantity: TestMessage(GreaterOrEqual_AEqB_Quantity, 'GreaterOrEqual_AEqB_Quantity', ToString(true), ToString(GreaterOrEqual_AEqB_Quantity))
define test_GreaterOrEqual_ALtB_Quantity: TestMessage(not GreaterOrEqual_ALtB_Quantity, 'GreaterOrEqual_ALtB_Quantity', ToString(false), ToString(GreaterOrEqual_ALtB_Quantity))

/*
************************
  End Comparison Tests
************************
*/

/*
***************************
  Start Conditional Tests
***************************
*/

// If
define function IfThenElse(var Boolean): if var then 'true return' else 'false return'

define test_If_true: TestMessage(IfThenElse(true) = 'true return', 'IfThenElse(true)', 'true return', IfThenElse(true))
define test_If_false: TestMessage(IfThenElse(false) = 'false return', 'IfThenElse(false)', 'false return', IfThenElse(false))

// Case

define function Case_selected(var Integer):
  case var
   when 1 then 'one'
   when 2 then 'two'
   else
    '?'
  end

define test_Case_Select_1: TestMessage(Case_selected(1) = 'one', 'Case_selected(1)', 'one', Case_selected(1))
define test_Case_Select_2: TestMessage(Case_selected(2) = 'two', 'Case_selected(2)', 'two', Case_selected(2))
define test_Case_Select_3: TestMessage(Case_selected(3) = '?', 'Case_selected(3)', '?', Case_selected(3))

define function Case_standard(X Integer, Y Integer):
  case
    when X > Y then 'X > Y'
    when X < Y then 'X < Y'
    else 'X == Y'
  end

define test_Case_Standard_1_2: TestMessage(Case_standard(1, 2) = 'X < Y', 'Case_standard(1, 2)', 'X < Y', Case_standard(1, 2))
define test_Case_Standard_2_1: TestMessage(Case_standard(2, 1) = 'X > Y', 'Case_standard(2, 1)', 'X > Y', Case_standard(2, 1))
define test_Case_Standard_1_1: TestMessage(Case_standard(1, 1) = 'X == Y', 'Case_standard(1, 1)', 'X == Y', Case_standard(1, 1))

/*
*************************
  End Conditional Tests
*************************
*/

/*
**************************
  Start Conversion Tests
**************************
*/

// FromString
define FromString_stringStr: convert 'str' to String
define FromString_stringNull: convert null to String
define FromString_boolTrue: convert 'true' to Boolean
define FromString_boolFalse: convert 'false' to Boolean
define FromString_decimalValid: convert '10.2' to Decimal
define FromString_integerValid: convert '10' to Integer
define FromString_integerDropDecimal: convert '10.2' to Integer
define FromString_quantityStr: convert '10 \'A\'' to Quantity
define FromString_posQuantityStr: convert '+10 \'A\'' to Quantity
define FromString_negQuantityStr: convert '-10 \'A\'' to Quantity
define FromString_quantityStrDecimal: convert '10.0 \'mA\'' to Quantity
define FromString_dateStr: convert '2015-01-02' to DateTime
define FromString_timeStr: convert 'T12:30:00.000' to Time
// TODO - add to error tests
// define FromString_decimalInvalid: convert 'abc' to Decimal
// define FromString_integerInvalid: convert 'abc' to Integer

define test_FromString_stringStr: TestMessage(FromString_stringStr = 'str', 'FromString_stringStr', 'str', FromString_stringStr)
define test_FromString_stringNull: TestMessage(FromString_stringNull is null, 'FromString_stringNull', 'null', FromString_stringNull)
define test_FromString_boolTrue: TestMessage(FromString_boolTrue, 'FromString_boolTrue', ToString(true), ToString(FromString_boolTrue))
define test_FromString_boolFalse: TestMessage(not FromString_boolFalse, 'FromString_boolFalse', ToString(false), ToString(FromString_boolFalse))
define test_FromString_decimalValid: TestMessage(FromString_decimalValid = 10.2, 'FromString_decimalValid', ToString(10.2), ToString(FromString_decimalValid))
define test_FromString_integerValid: TestMessage(FromString_integerValid = 10, 'FromString_integerValid', ToString(10), ToString(FromString_integerValid))
define test_FromString_integerDropDecimal: TestMessage(FromString_integerDropDecimal = 10, 'FromString_integerDropDecimal', ToString(10), ToString(FromString_integerDropDecimal))
define test_FromString_quantityStr: TestMessage(FromString_quantityStr = 10 'A', 'FromString_quantityStr', ToString(10 'A'), ToString(FromString_quantityStr))
define test_FromString_posQuantityStr: TestMessage(FromString_posQuantityStr = 10 'A', 'FromString_posQuantityStr', ToString(10 'A'), ToString(FromString_posQuantityStr))
define test_FromString_negQuantityStr: TestMessage(FromString_negQuantityStr = -10 'A', 'FromString_negQuantityStr', ToString(-10 'A'), ToString(FromString_negQuantityStr))
define test_FromString_quantityStrDecimal: TestMessage(FromString_quantityStrDecimal = 10.0 'mA', 'FromString_quantityStrDecimal', ToString(10.0 'mA'), ToString(FromString_quantityStrDecimal))
define test_FromString_dateStr: TestMessage(FromString_dateStr = @2015-01-02, 'FromString_dateStr', ToString(@2015-01-02), ToString(FromString_dateStr))
define test_FromString_timeStr: TestMessage(FromString_timeStr = @T12:30:00.000, 'FromString_timeStr', ToString(@T12:30:00.000), ToString(FromString_timeStr))

// FromInteger
define FromInteger_string10: convert 10 to String
define FromInteger_decimal10: convert 10 to Decimal
define FromInteger_intNull: convert null to Decimal
define FromInteger_intInt: convert 10 to Integer

define test_FromInteger_string10: TestMessage(FromInteger_string10 = '10', 'FromInteger_string10', '10', FromInteger_string10)
define test_FromInteger_decimal10: TestMessage(FromInteger_decimal10 = 10.0, 'FromInteger_decimal10', ToString(10.0), ToString(FromInteger_decimal10))
define test_FromInteger_intNull: TestMessage(FromInteger_intNull is null, 'FromInteger_intNull', 'null', ToString(FromInteger_intNull))
define test_FromInteger_intInt: TestMessage(FromInteger_intInt = 10, 'FromInteger_intInt', ToString(10), ToString(FromInteger_intInt))

// FromQuantity
define FromQuantity_quantityStr: convert 10 'A' to String
define FromQuantity_negQuantityStr: convert -10 'A' to String
define FromQuantity_posQuantityStr: convert +10 'A' to String
define FromQuantity_quantityQuantity: convert 10 'A' to Quantity

define test_FromQuantity_quantityStr: TestMessage(FromQuantity_quantityStr = '10 \'A\'', 'FromQuantity_quantityStr', '10 \'A\'', FromQuantity_quantityStr)
define test_FromQuantity_negQuantityStr: TestMessage(FromQuantity_negQuantityStr = '-10 \'A\'', 'FromQuantity_negQuantityStr', '-10 \'A\'', FromQuantity_negQuantityStr)
define test_FromQuantity_posQuantityStr: TestMessage(FromQuantity_posQuantityStr = '10 \'A\'', 'FromQuantity_posQuantityStr', '10 \'A\'', FromQuantity_posQuantityStr)
define test_FromQuantity_quantityQuantity: TestMessage(FromQuantity_quantityQuantity = 10 'A', 'FromQuantity_quantityQuantity', ToString(10 'A'), ToString(FromQuantity_quantityQuantity))

// FromBoolean
define FromBoolean_booleanTrueStr: convert true to String
define FromBoolean_booleanFalseStr: convert false to String
define FromBoolean_booleanTrueBool: convert true to Boolean
define FromBoolean_booleanFalseBool: convert false to Boolean

define test_FromBoolean_booleanTrueStr: TestMessage(FromBoolean_booleanTrueStr = 'true', 'FromBoolean_booleanTrueStr', 'true', FromBoolean_booleanTrueStr)
define test_FromBoolean_booleanFalseStr: TestMessage(FromBoolean_booleanFalseStr = 'false', 'FromBoolean_booleanFalseStr', 'false', FromBoolean_booleanFalseStr)
define test_FromBoolean_booleanTrueBool: TestMessage(FromBoolean_booleanTrueBool, 'FromBoolean_booleanTrueBool', ToString(true), ToString(FromBoolean_booleanTrueBool))
define test_FromBoolean_booleanFalseBool: TestMessage(not FromBoolean_booleanFalseBool, 'FromBoolean_booleanFalseBool', ToString(false), ToString(FromBoolean_booleanFalseBool))

// FromDateTime
define FromDateTime_dateStr: convert @2015-01-02 to String
define FromDateTime_dateDate: convert @2015-01-02 to DateTime

define test_FromDateTime_dateStr: TestMessage(FromDateTime_dateStr = '2015-01-02', 'FromDateTime_dateStr', '2015-01-02', FromDateTime_dateStr)
define test_FromDateTime_dateDate: TestMessage(FromDateTime_dateDate = @2015-01-02, 'FromDateTime_dateDate', ToString(@2015-01-02), ToString(FromDateTime_dateDate))

// FromTime
define FromTime_timeStr: convert @T11:57 to String
define FromTime_timeTime: convert @T11:57 to Time

define test_FromTime_timeStr: TestMessage(FromTime_timeStr = '11:57', 'FromTime_timeStr', '11:57', FromTime_timeStr)
define test_FromTime_timeTime: TestMessage(FromTime_timeTime = @T11:57, 'FromTime_timeTime', ToString(@T11:57), ToString(FromTime_timeTime))

// FromCode
define hepB_Code: Code '66071002' from "SNOMED-CT" display 'Type B viral hepatitis'
define hepB_Concept: Concept { codes: { hepB_Code } }
define FromCode_codeConcept: convert hepB_Code to Concept
define FromCode_codeCode: convert hepB_Code to Code

// Utility Functions
// define function ToString(concept Concept):
//   'Concept { codes: { ' + ToString(concept.codes) + ' }' + (if concept.display is not null then ', display: ' + concept.display else '') + ' }'

define function ToString(code Code):
  'Code { code: ' + code.code + ', system: ' + code.system + (if code.display is not null then ', display: ' + code.display else '') + ' }'

define function ToString(codes List<Code>):
  codes c return ToString(c)

define test_FromCode_codeCode: TestMessage(FromCode_codeCode ~ hepB_Code, 'FromCode_codeCode', ToString(hepB_Code), ToString(FromCode_codeCode))
// TODO - fix
// define test_FromCode_codeConcept: TestMessage(FromCode_codeConcept = hepB_Concept, 'FromCode_codeConcept', ToString(hepB_Concept), ToString(FromCode_codeConcept))

// ToDecimal
define ToDecimal_NoSign: ToDecimal('0.0')
define ToDecimal_PositiveSign: ToDecimal('+1.1')
define ToDecimal_NegativeSign: ToDecimal('-1.1')
define ToDecimal_NullDecimal: ToDecimal((null as String))
// TODO - add to error tests
// define ToDecimal_TooLargeDec: ToDecimal('444444444444444444444444444444')
// define ToDecimal_TooSmallDec: ToDecimal('-444444444444444444444444444444')
// define ToDecimal_TooPrecise: ToDecimal('.444444444')
// define ToDecimal_WrongFormat: ToDecimal('+.1')

define test_ToDecimal_NoSign: TestMessage(ToDecimal_NoSign = 0.0, 'ToDecimal_NoSign', ToString(0.0), ToString(ToDecimal_NoSign))
define test_ToDecimal_PositiveSign: TestMessage(ToDecimal_PositiveSign = 1.1, 'ToDecimal_PositiveSign', ToString(1.1), ToString(ToDecimal_PositiveSign))
define test_ToDecimal_NegativeSign: TestMessage(ToDecimal_NegativeSign = -1.1, 'ToDecimal_NegativeSign', ToString(-1.1), ToString(ToDecimal_NegativeSign))
define test_ToDecimal_NullDecimal: TestMessage(ToDecimal_NullDecimal is null, 'ToDecimal_NullDecimal', 'null', ToString(ToDecimal_NullDecimal))

// ToInteger
define ToInteger_NoSign: ToInteger('12345')
define ToInteger_PositiveSign: ToInteger('+12345')
define ToInteger_NegativeSign: ToInteger('-12345')
// TODO - add to error tests
// define ToInteger_TooLargeInt: ToInteger('2147483648')
// define ToInteger_TooSmallInt: ToInteger('-2147483649')

define test_ToInteger_NoSign: TestMessage(ToInteger_NoSign = 12345, 'ToInteger_NoSign', ToString(12345), ToString(ToInteger_NoSign))
define test_ToInteger_PositiveSign: TestMessage(ToInteger_PositiveSign = 12345, 'ToInteger_PositiveSign', ToString(12345), ToString(ToInteger_PositiveSign))
define test_ToInteger_NegativeSign: TestMessage(ToInteger_NegativeSign = -12345, 'ToInteger_NegativeSign', ToString(-12345), ToString(ToInteger_NegativeSign))

// ToQuantity
define ToQuantity_NullArg: ToQuantity((null as String))
// TODO - add to error tests
// define ToQuantity_WrongFormatQuantity: ToQuantity('abc')
// define ToQuantity_TooLargeQuantity: ToQuantity('444444444444444444444444444444 \'\'')
// define ToQuantity_TooSmallQuantity: ToQuantity('-444444444444444444444444444444 \'\'')

define test_ToQuantity_NullArg: TestMessage(ToQuantity_NullArg is null, 'ToQuantity_NullArg', 'null', ToString(ToQuantity_NullArg))

// ToTime
define ToTime_NullArgTime: ToTime((null as String))
// TODO - add to error tests
// define ToTime_IncorrectFormatTime: ToTime('10:00PM')
// define ToTime_InvalidTime: ToTime('25:99.000+00.00')

define test_ToTime_NullArgTime: TestMessage(ToTime_NullArgTime is null, 'ToTime_NullArgTime', 'null', ToString(ToTime_NullArgTime))

/*
************************
  End Conversion Tests
************************
*/

/*
************************
  Start DateTime Tests
************************
*/

// Today
define Today_Var: Today()

// NOTE: this test uses the evaluation date/time specified in the Context
define test_Today_Var: TestMessage(Today_Var = @2018-01-01, 'Today_Var', ToString(@2018-01-01), ToString(Today_Var))

// Now
define Now_Var: Now()

// NOTE: this test uses the evaluation date/time specified in the Context
define test_Now_Var: TestMessage(Now_Var = @2018-01-01T07:00:00.000, 'Now_Var', ToString(@2018-01-01T07:00:00.000), ToString(Now_Var))

// TimeOfDay
define TimeOfDay_Var: TimeOfDay()

// NOTE: this test uses the evaluation date/time specified in the Context
define test_TimeOfDay_Var: TestMessage(TimeOfDay_Var = @T07:00:00.000, 'TimeOfDay_Var', ToString(@T07:00:00.000), ToString(TimeOfDay_Var))

// DateTimeComponentFrom
define IdesOfMarch: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define DTCFrom_Year: year from IdesOfMarch
define DTCFrom_Month: month from IdesOfMarch
define DTCFrom_Day: day from IdesOfMarch
define DTCFrom_Hour: hour from IdesOfMarch
define DTCFrom_Minute: minute from IdesOfMarch
define DTCFrom_Second: second from IdesOfMarch
define DTCFrom_Millisecond: millisecond from IdesOfMarch
define ImpreciseIdesOfMarch: DateTime(2000, 3, 15)
define ImpreciseComponentTuple: Tuple {
  Year: year from ImpreciseIdesOfMarch,
  Month: month from ImpreciseIdesOfMarch,
  Day: day from ImpreciseIdesOfMarch,
  Hour: hour from ImpreciseIdesOfMarch,
  Minute: minute from ImpreciseIdesOfMarch,
  Second: second from ImpreciseIdesOfMarch,
  Millisecond: millisecond from ImpreciseIdesOfMarch
}
define DTCFrom_ImpreciseComponentTuple_Hour: ImpreciseComponentTuple.Hour
define DTCFrom_ImpreciseComponentTuple_Minute: ImpreciseComponentTuple.Minute
define DTCFrom_ImpreciseComponentTuple_Second: ImpreciseComponentTuple.Second
define DTCFrom_ImpreciseComponentTuple_Millisecond: ImpreciseComponentTuple.Millisecond
define DTCFrom_NullDate: year from (null as DateTime)

define test_DTCFrom_Year: TestMessage(DTCFrom_Year = 2000, 'DTCFrom_Year', ToString(2000), ToString(DTCFrom_Year))
define test_DTCFrom_Month: TestMessage(DTCFrom_Month = 3, 'DTCFrom_Month', ToString(3), ToString(DTCFrom_Month))
define test_DTCFrom_Day: TestMessage(DTCFrom_Day = 15, 'DTCFrom_Day', ToString(15), ToString(DTCFrom_Day))
define test_DTCFrom_Hour: TestMessage(DTCFrom_Hour = 13, 'DTCFrom_Hour', ToString(13), ToString(DTCFrom_Hour))
define test_DTCFrom_Minute: TestMessage(DTCFrom_Minute = 30, 'DTCFrom_Minute', ToString(30), ToString(DTCFrom_Minute))
define test_DTCFrom_Second: TestMessage(DTCFrom_Second = 25, 'DTCFrom_Second', ToString(25), ToString(DTCFrom_Second))
define test_DTCFrom_Millisecond: TestMessage(DTCFrom_Millisecond = 200, 'DTCFrom_Millisecond', ToString(200), ToString(DTCFrom_Millisecond))
define test_DTCFrom_ImpreciseComponentTuple_Hour: TestMessage(DTCFrom_ImpreciseComponentTuple_Hour is null, 'DTCFrom_ImpreciseComponentTuple_Hour', 'null', ToString(DTCFrom_ImpreciseComponentTuple_Hour))
define test_DTCFrom_ImpreciseComponentTuple_Minute: TestMessage(DTCFrom_ImpreciseComponentTuple_Minute is null, 'DTCFrom_ImpreciseComponentTuple_Minute', 'null', ToString(DTCFrom_ImpreciseComponentTuple_Minute))
define test_DTCFrom_ImpreciseComponentTuple_Second: TestMessage(DTCFrom_ImpreciseComponentTuple_Second is null, 'DTCFrom_ImpreciseComponentTuple_Second', 'null', ToString(DTCFrom_ImpreciseComponentTuple_Second))
define test_DTCFrom_ImpreciseComponentTuple_Millisecond: TestMessage(DTCFrom_ImpreciseComponentTuple_Millisecond is null, 'DTCFrom_ImpreciseComponentTuple_Millisecond', 'null', ToString(DTCFrom_ImpreciseComponentTuple_Millisecond))
define test_DTCFrom_NullDate: TestMessage(DTCFrom_NullDate is null, 'DTCFrom_NullDate', 'null', ToString(DTCFrom_NullDate))

// DateFrom
define DateFrom_Date: date from IdesOfMarch
define DateFrom_ImpreciseDate: date from DateTime(2000)
define DateFrom_NullDate: date from (null as DateTime)

define test_DateFrom_Date: TestMessage(DateFrom_Date same day as @2000-03-15T00:00:00.000+01:00, 'DateFrom_Date', ToString(@2000-03-15T00:00:00.000), ToString(DateFrom_Date))
define test_DateFrom_ImpreciseDate: TestMessage(DateFrom_ImpreciseDate = @2000, 'DateFrom_ImpreciseDate', ToString(@2000), ToString(DateFrom_ImpreciseDate))
define test_DateFrom_NullDate: TestMessage(DateFrom_NullDate is null, 'DateFrom_NullDate', 'null', ToString(DateFrom_NullDate))

// TimeFrom
define TimeFrom_Time: time from IdesOfMarch
define TimeFrom_NoTime: time from DateTime(2000, 3, 15)
define TimeFrom_NullDate: time from null

define test_TimeFrom_Time: TestMessage(TimeFrom_Time = Time(13, 30, 25, 200, +1.0), 'TimeFrom_Time', ToString(Time(13, 30, 25, 200, +1.0)), ToString(TimeFrom_Time))
define test_TimeFrom_NoTime: TestMessage(TimeFrom_NoTime is null, 'TimeFrom_NoTime', 'null', ToString(TimeFrom_NoTime))
define test_TimeFrom_NullDate: TestMessage(TimeFrom_NullDate is null, 'TimeFrom_NullDate', 'null', ToString(TimeFrom_NullDate))

// TimezoneFrom
define TimezoneFrom_CentralEuropean: timezone from IdesOfMarch
define TimezoneFrom_EasternStandard: timezone from DateTime(2000, 3, 15, 13, 30, 25, 200, -5.0)
define TimezoneFrom_NullDate: timezone from (null as DateTime)

define test_TimezoneFrom_CentralEuropean: TestMessage(TimezoneFrom_CentralEuropean = 1.0, 'TimezoneFrom_CentralEuropean', ToString(1.0), ToString(TimezoneFrom_CentralEuropean))
define test_TimezoneFrom_EasternStandard: TestMessage(TimezoneFrom_EasternStandard = -5.0, 'TimezoneFrom_EasternStandard', ToString(-5.0), ToString(TimezoneFrom_EasternStandard))
define test_TimezoneFrom_NullDate: TestMessage(TimezoneFrom_NullDate is null, 'TimezoneFrom_NullDate', 'null', ToString(TimezoneFrom_NullDate))

// SameAs
define SameAs_SameYear: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same year as DateTime(2000, 11, 23, 8, 14, 47, 500, +1.0)
define SameAs_NotSameYear: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same year as DateTime(2001, 11, 23, 8, 14, 47, 500, +1.0)
define SameAs_SameMonth: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same month as DateTime(2000, 3, 23, 8, 14, 47, 500, +1.0)
define SameAs_NotSameMonth: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same month as DateTime(2000, 4, 23, 8, 14, 47, 500, +1.0)
define SameAs_SameMonthWrongYear: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same month as DateTime(2001, 3, 23, 8, 14, 47, 500, +1.0)
define SameAs_SameDay: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day as DateTime(2000, 3, 15, 8, 14, 47, 500, +1.0)
define SameAs_NotSameDay: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day as DateTime(2000, 3, 23, 8, 14, 47, 500, +1.0)
define SameAs_SameDayWrongMonth: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day as DateTime(2000, 4, 15, 8, 14, 47, 500, +1.0)
define SameAs_SameHour: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour as DateTime(2000, 3, 15, 13, 14, 47, 500, +1.0)
define SameAs_NotSameHour: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour as DateTime(2000, 3, 15, 8, 14, 47, 500, +1.0)
define SameAs_SameHourWrongDay: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour as DateTime(2000, 3, 16, 13, 14, 47, 500, +1.0)
define SameAs_SameMinute: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same minute as DateTime(2000, 3, 15, 13, 30, 47, 500, +1.0)
define SameAs_NotSameMinute: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same minute as DateTime(2000, 3, 15, 13, 14, 47, 500, +1.0)
define SameAs_SameMinuteWrongHour: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same minute as DateTime(2000, 3, 15, 14, 30, 47, 500, +1.0)
define SameAs_SameSecond: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same second as DateTime(2000, 3, 15, 13, 30, 25, 500, +1.0)
define SameAs_NotSameSecond: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same second as DateTime(2000, 3, 15, 13, 30, 47, 500, +1.0)
define SameAs_SameSecondWrongMinute: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same second as DateTime(2000, 3, 15, 13, 31, 25, 500, +1.0)
define SameAs_SameMillisecond: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same millisecond as DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define SameAs_NotSameMillisecond: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same millisecond as DateTime(2000, 3, 15, 13, 30, 25, 500, +1.0)
define SameAs_SameMillisecondWrongSecond: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same millisecond as DateTime(2000, 3, 15, 13, 30, 26, 200, +1.0)
define SameAs_Same: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same as DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define SameAs_NotSame: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same as DateTime(2000, 3, 15, 13, 30, 25, 500, +1.0)
define SameAs_SameNormalized: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same as DateTime(2000, 3, 15, 7, 30, 25, 200, -5.0)
define SameAs_SameHourWrongTimezone: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour as DateTime(2000, 3, 15, 13, 30, 25, 200, -5.0)
define SameAs_ImpreciseHour: DateTime(2000, 3, 15, 13, 30, 25, 200) same hour as DateTime(2000, 3, 15)
define SameAs_ImpreciseHourWrongDay: DateTime(2000, 3, 15, 13, 30, 25, 200) same hour as DateTime(2000, 3, 16)
define SameAs_NullLeft: null same as DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define SameAs_NullRight: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same as (null as DateTime)
define SameAs_NullBoth: (null as DateTime) same as null

define test_SameAs_SameYear: TestMessage(SameAs_SameYear, 'SameAs_SameYear', ToString(true), ToString(SameAs_SameYear))
define test_SameAs_NotSameYear: TestMessage(not SameAs_NotSameYear, 'SameAs_NotSameYear', ToString(false), ToString(SameAs_NotSameYear))
define test_SameAs_SameMonth: TestMessage(SameAs_SameMonth, 'SameAs_SameMonth', ToString(true), ToString(SameAs_SameMonth))
define test_SameAs_NotSameMonth: TestMessage(not SameAs_NotSameMonth, 'SameAs_NotSameMonth', ToString(false), ToString(SameAs_NotSameMonth))
define test_SameAs_SameMonthWrongYear: TestMessage(not SameAs_SameMonthWrongYear, 'SameAs_SameMonthWrongYear', ToString(false), ToString(SameAs_SameMonthWrongYear))
define test_SameAs_SameDay: TestMessage(SameAs_SameDay, 'SameAs_SameDay', ToString(true), ToString(SameAs_SameDay))
define test_SameAs_NotSameDay: TestMessage(not SameAs_NotSameDay, 'SameAs_NotSameDay', ToString(false), ToString(SameAs_NotSameDay))
define test_SameAs_SameDayWrongMonth: TestMessage(not SameAs_SameDayWrongMonth, 'SameAs_SameDayWrongMonth', ToString(false), ToString(SameAs_SameDayWrongMonth))
define test_SameAs_SameHour: TestMessage(SameAs_SameHour, 'SameAs_SameHour', ToString(true), ToString(SameAs_SameHour))
define test_SameAs_NotSameHour: TestMessage(not SameAs_NotSameHour, 'SameAs_NotSameHour', ToString(false), ToString(SameAs_NotSameHour))
define test_SameAs_SameHourWrongDay: TestMessage(not SameAs_SameHourWrongDay, 'SameAs_SameHourWrongDay', ToString(false), ToString(SameAs_SameHourWrongDay))
define test_SameAs_SameMinute: TestMessage(SameAs_SameMinute, 'SameAs_SameMinute', ToString(true), ToString(SameAs_SameMinute))
define test_SameAs_NotSameMinute: TestMessage(not SameAs_NotSameMinute, 'SameAs_NotSameMinute', ToString(false), ToString(SameAs_NotSameMinute))
define test_SameAs_SameMinuteWrongHour: TestMessage(not SameAs_SameMinuteWrongHour, 'SameAs_SameMinuteWrongHour', ToString(false), ToString(SameAs_SameMinuteWrongHour))
define test_SameAs_SameSecond: TestMessage(SameAs_SameSecond, 'SameAs_SameSecond', ToString(true), ToString(SameAs_SameSecond))
define test_SameAs_NotSameSecond: TestMessage(not SameAs_NotSameSecond, 'SameAs_NotSameSecond', ToString(false), ToString(SameAs_NotSameSecond))
define test_SameAs_SameSecondWrongMinute: TestMessage(not SameAs_SameSecondWrongMinute, 'SameAs_SameSecondWrongMinute', ToString(false), ToString(SameAs_SameSecondWrongMinute))
define test_SameAs_SameMillisecond: TestMessage(SameAs_SameMillisecond, 'SameAs_SameMillisecond', ToString(true), ToString(SameAs_SameMillisecond))
define test_SameAs_NotSameMillisecond: TestMessage(not SameAs_NotSameMillisecond, 'SameAs_NotSameMillisecond', ToString(false), ToString(SameAs_NotSameMillisecond))
define test_SameAs_SameMillisecondWrongSecond: TestMessage(not SameAs_SameMillisecondWrongSecond, 'SameAs_SameMillisecondWrongSecond', ToString(false), ToString(SameAs_SameMillisecondWrongSecond))
define test_SameAs_Same: TestMessage(SameAs_Same, 'SameAs_Same', ToString(true), ToString(SameAs_Same))
define test_SameAs_NotSame: TestMessage(not SameAs_NotSame, 'SameAs_NotSame', ToString(false), ToString(SameAs_NotSame))
define test_SameAs_SameNormalized: TestMessage(SameAs_SameNormalized, 'SameAs_SameNormalized', ToString(true), ToString(SameAs_SameNormalized))
define test_SameAs_SameHourWrongTimezone: TestMessage(not SameAs_SameHourWrongTimezone, 'SameAs_SameHourWrongTimezone', ToString(false), ToString(SameAs_SameHourWrongTimezone))
define test_SameAs_ImpreciseHour: TestMessage(SameAs_ImpreciseHour is null, 'SameAs_ImpreciseHour', 'null', ToString(SameAs_ImpreciseHour))
define test_SameAs_ImpreciseHourWrongDay: TestMessage(not SameAs_ImpreciseHourWrongDay, 'SameAs_ImpreciseHourWrongDay', ToString(false), ToString(SameAs_ImpreciseHourWrongDay))
define test_SameAs_NullLeft: TestMessage(SameAs_NullLeft is null, 'SameAs_NullLeft', 'null', ToString(SameAs_NullLeft))
define test_SameAs_NullRight: TestMessage(SameAs_NullRight is null, 'SameAs_NullRight', 'null', ToString(SameAs_NullRight))
define test_SameAs_NullBoth: TestMessage(SameAs_NullBoth is null, 'SameAs_NullBoth', 'null', ToString(SameAs_NullBoth))

// SameOrAfter
define SameOrAfter_SameYear: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same year or after DateTime(2000, 11, 23, 8, 14, 47, 500, +1.0)
define SameOrAfter_YearAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same year or after DateTime(1999, 11, 23, 8, 14, 47, 500, +1.0)
define SameOrAfter_YearBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same year or after DateTime(2001, 11, 23, 8, 14, 47, 500, +1.0)
define SameOrAfter_SameMonth: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same month or after DateTime(2000, 3, 23, 8, 14, 47, 500, +1.0)
define SameOrAfter_MonthAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same month or after DateTime(2000, 2, 23, 8, 14, 47, 500, +1.0)
define SameOrAfter_MonthBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same month or after DateTime(2000, 4, 23, 8, 14, 47, 500, +1.0)
define SameOrAfter_SameDay: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day or after DateTime(2000, 3, 15, 8, 14, 47, 500, +1.0)
define SameOrAfter_DayAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day or after DateTime(2000, 3, 14, 8, 14, 47, 500, +1.0)
define SameOrAfter_DayBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day or after DateTime(2000, 3, 16, 8, 14, 47, 500, +1.0)
define SameOrAfter_SameHour: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour or after DateTime(2000, 3, 15, 13, 14, 47, 500, +1.0)
define SameOrAfter_HourAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour or after DateTime(2000, 3, 15, 12, 14, 47, 500, +1.0)
define SameOrAfter_HourBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour or after DateTime(2000, 3, 15, 14, 14, 47, 500, +1.0)
define SameOrAfter_SameMinute: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same minute or after DateTime(2000, 3, 15, 13, 30, 47, 500, +1.0)
define SameOrAfter_MinuteAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same minute or after DateTime(2000, 3, 15, 13, 29, 47, 500, +1.0)
define SameOrAfter_MinuteBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same minute or after DateTime(2000, 3, 15, 13, 31, 47, 500, +1.0)
define SameOrAfter_SameSecond: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same second or after DateTime(2000, 3, 15, 13, 30, 25, 500, +1.0)
define SameOrAfter_SecondAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same second or after DateTime(2000, 3, 15, 13, 30, 24, 500, +1.0)
define SameOrAfter_SecondBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same second or after DateTime(2000, 3, 15, 13, 30, 26, 500, +1.0)
define SameOrAfter_SameMillisecond: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same millisecond or after DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define SameOrAfter_MillisecondAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same millisecond or after DateTime(2000, 3, 15, 13, 30, 25, 199, +1.0)
define SameOrAfter_MillisecondBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same millisecond or after DateTime(2000, 3, 15, 13, 30, 25, 201, +1.0)
define SameOrAfter_Same: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same or after DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define SameOrAfter_After: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same or after DateTime(2000, 3, 15, 13, 30, 25, 199, +1.0)
define SameOrAfter_Before: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same or after DateTime(2000, 3, 15, 13, 30, 25, 201, +1.0)
define SameOrAfter_SameDayMonthBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day or after DateTime(2000, 4, 15, 8, 14, 47, 500, +1.0)
define SameOrAfter_DayAfterMonthBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day or after DateTime(2000, 4, 14, 8, 14, 47, 500, +1.0)
define SameOrAfter_DayBeforeMonthAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day or after DateTime(2000, 2, 16, 8, 14, 47, 500, +1.0)
define SameOrAfter_ImpreciseDay: DateTime(2000, 3, 15, 13, 30, 25, 200) same day or after DateTime(2000, 3)
define SameOrAfter_ImpreciseDayMonthAfter: DateTime(2000, 3, 15, 13, 30, 25, 200) same day or after DateTime(2000, 2)
define SameOrAfter_ImpreciseDayMonthBefore: DateTime(2000, 3, 15, 13, 30, 25, 200) same day or after DateTime(2000, 4)
define SameOrAfter_SameHourNormalizeZones: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour or after DateTime(2000, 3, 15, 7, 30, 25, 200, -5.0)
define SameOrAfter_HourAfterNormalizeZones: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour or after DateTime(2000, 3, 15, 6, 30, 25, 200, -5.0)
define SameOrAfter_HourBeforeNormalizeZones: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour or after DateTime(2000, 3, 15, 8, 30, 25, 200, -5.0)
define SameOrAfter_NullLeft: null same or after DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define SameOrAfter_NullRight: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same or after null
define SameOrAfter_NullBoth: (null as DateTime) same or after null

define test_SameOrAfter_SameYear: TestMessage(SameOrAfter_SameYear, 'SameOrAfter_SameYear', ToString(true), ToString(SameOrAfter_SameYear))
define test_SameOrAfter_YearAfter: TestMessage(SameOrAfter_YearAfter, 'SameOrAfter_YearAfter', ToString(true), ToString(SameOrAfter_YearAfter))
define test_SameOrAfter_YearBefore: TestMessage(not SameOrAfter_YearBefore, 'SameOrAfter_YearBefore', ToString(false), ToString(SameOrAfter_YearBefore))
define test_SameOrAfter_SameMonth: TestMessage(SameOrAfter_SameMonth, 'SameOrAfter_SameMonth', ToString(true), ToString(SameOrAfter_SameMonth))
define test_SameOrAfter_MonthAfter: TestMessage(SameOrAfter_MonthAfter, 'SameOrAfter_MonthAfter', ToString(true), ToString(SameOrAfter_MonthAfter))
define test_SameOrAfter_MonthBefore: TestMessage(not SameOrAfter_MonthBefore, 'SameOrAfter_MonthBefore', ToString(false), ToString(SameOrAfter_MonthBefore))
define test_SameOrAfter_SameDay: TestMessage(SameOrAfter_SameDay, 'SameOrAfter_SameDay', ToString(true), ToString(SameOrAfter_SameDay))
define test_SameOrAfter_DayAfter: TestMessage(SameOrAfter_DayAfter, 'SameOrAfter_DayAfter', ToString(true), ToString(SameOrAfter_DayAfter))
define test_SameOrAfter_DayBefore: TestMessage(not SameOrAfter_DayBefore, 'SameOrAfter_DayBefore', ToString(false), ToString(SameOrAfter_DayBefore))
define test_SameOrAfter_SameHour: TestMessage(SameOrAfter_SameHour, 'SameOrAfter_SameHour', ToString(true), ToString(SameOrAfter_SameHour))
define test_SameOrAfter_HourAfter: TestMessage(SameOrAfter_HourAfter, 'SameOrAfter_HourAfter', ToString(true), ToString(SameOrAfter_HourAfter))
define test_SameOrAfter_HourBefore: TestMessage(not SameOrAfter_HourBefore, 'SameOrAfter_HourBefore', ToString(false), ToString(SameOrAfter_HourBefore))
define test_SameOrAfter_SameMinute: TestMessage(SameOrAfter_SameMinute, 'SameOrAfter_SameMinute', ToString(true), ToString(SameOrAfter_SameMinute))
define test_SameOrAfter_MinuteAfter: TestMessage(SameOrAfter_MinuteAfter, 'SameOrAfter_MinuteAfter', ToString(true), ToString(SameOrAfter_MinuteAfter))
define test_SameOrAfter_MinuteBefore: TestMessage(not SameOrAfter_MinuteBefore, 'SameOrAfter_MinuteBefore', ToString(false), ToString(SameOrAfter_MinuteBefore))
define test_SameOrAfter_SameSecond: TestMessage(SameOrAfter_SameSecond, 'SameOrAfter_SameSecond', ToString(true), ToString(SameOrAfter_SameSecond))
define test_SameOrAfter_SecondAfter: TestMessage(SameOrAfter_SecondAfter, 'SameOrAfter_SecondAfter', ToString(true), ToString(SameOrAfter_SecondAfter))
define test_SameOrAfter_SecondBefore: TestMessage(not SameOrAfter_SecondBefore, 'SameOrAfter_SecondBefore', ToString(false), ToString(SameOrAfter_SecondBefore))
define test_SameOrAfter_SameMillisecond: TestMessage(SameOrAfter_SameMillisecond, 'SameOrAfter_SameMillisecond', ToString(true), ToString(SameOrAfter_SameMillisecond))
define test_SameOrAfter_MillisecondAfter: TestMessage(SameOrAfter_MillisecondAfter, 'SameOrAfter_MillisecondAfter', ToString(true), ToString(SameOrAfter_MillisecondAfter))
define test_SameOrAfter_MillisecondBefore: TestMessage(not SameOrAfter_MillisecondBefore, 'SameOrAfter_MillisecondBefore', ToString(false), ToString(SameOrAfter_MillisecondBefore))
define test_SameOrAfter_Same: TestMessage(SameOrAfter_Same, 'SameOrAfter_Same', ToString(true), ToString(SameOrAfter_Same))
define test_SameOrAfter_After: TestMessage(SameOrAfter_After, 'SameOrAfter_After', ToString(true), ToString(SameOrAfter_After))
define test_SameOrAfter_Before: TestMessage(not SameOrAfter_Before, 'SameOrAfter_Before', ToString(false), ToString(SameOrAfter_Before))
define test_SameOrAfter_SameDayMonthBefore: TestMessage(not SameOrAfter_SameDayMonthBefore, 'SameOrAfter_SameDayMonthBefore', ToString(false), ToString(SameOrAfter_SameDayMonthBefore))
define test_SameOrAfter_DayAfterMonthBefore: TestMessage(not SameOrAfter_DayAfterMonthBefore, 'SameOrAfter_DayAfterMonthBefore', ToString(false), ToString(SameOrAfter_DayAfterMonthBefore))
define test_SameOrAfter_DayBeforeMonthAfter: TestMessage(SameOrAfter_DayBeforeMonthAfter, 'SameOrAfter_DayBeforeMonthAfter', ToString(true), ToString(SameOrAfter_DayBeforeMonthAfter))
define test_SameOrAfter_ImpreciseDay: TestMessage(SameOrAfter_ImpreciseDay is null, 'SameOrAfter_ImpreciseDay', 'null', ToString(SameOrAfter_ImpreciseDay))
define test_SameOrAfter_ImpreciseDayMonthAfter: TestMessage(SameOrAfter_ImpreciseDayMonthAfter, 'SameOrAfter_ImpreciseDayMonthAfter', ToString(true), ToString(SameOrAfter_ImpreciseDayMonthAfter))
define test_SameOrAfter_ImpreciseDayMonthBefore: TestMessage(not SameOrAfter_ImpreciseDayMonthBefore, 'SameOrAfter_ImpreciseDayMonthBefore', ToString(false), ToString(SameOrAfter_ImpreciseDayMonthBefore))
define test_SameOrAfter_SameHourNormalizeZones: TestMessage(SameOrAfter_SameHourNormalizeZones, 'SameOrAfter_SameHourNormalizeZones', ToString(true), ToString(SameOrAfter_SameHourNormalizeZones))
define test_SameOrAfter_HourAfterNormalizeZones: TestMessage(SameOrAfter_HourAfterNormalizeZones, 'SameOrAfter_HourAfterNormalizeZones', ToString(true), ToString(SameOrAfter_HourAfterNormalizeZones))
define test_SameOrAfter_HourBeforeNormalizeZones: TestMessage(not SameOrAfter_HourBeforeNormalizeZones, 'SameOrAfter_HourBeforeNormalizeZones', ToString(false), ToString(SameOrAfter_HourBeforeNormalizeZones))
define test_SameOrAfter_NullLeft: TestMessage(SameOrAfter_NullLeft is null, 'SameOrAfter_NullLeft', 'null', ToString(SameOrAfter_NullLeft))
define test_SameOrAfter_NullRight: TestMessage(SameOrAfter_NullRight is null, 'SameOrAfter_NullRight', 'null', ToString(SameOrAfter_NullRight))
define test_SameOrAfter_NullBoth: TestMessage(SameOrAfter_NullBoth is null, 'SameOrAfter_NullBoth', 'null', ToString(SameOrAfter_NullBoth))

// SameOrBefore
define SameOrBefore_SameYear: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same year or before DateTime(2000, 11, 23, 8, 14, 47, 500, +1.0)
define SameOrBefore_YearAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same year or before DateTime(1999, 11, 23, 8, 14, 47, 500, +1.0)
define SameOrBefore_YearBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same year or before DateTime(2001, 11, 23, 8, 14, 47, 500, +1.0)
define SameOrBefore_SameMonth: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same month or before DateTime(2000, 3, 23, 8, 14, 47, 500, +1.0)
define SameOrBefore_MonthAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same month or before DateTime(2000, 2, 23, 8, 14, 47, 500, +1.0)
define SameOrBefore_MonthBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same month or before DateTime(2000, 4, 23, 8, 14, 47, 500, +1.0)
define SameOrBefore_SameDay: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day or before DateTime(2000, 3, 15, 8, 14, 47, 500, +1.0)
define SameOrBefore_DayAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day or before DateTime(2000, 3, 14, 8, 14, 47, 500, +1.0)
define SameOrBefore_DayBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day or before DateTime(2000, 3, 16, 8, 14, 47, 500, +1.0)
define SameOrBefore_SameHour: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour or before DateTime(2000, 3, 15, 13, 14, 47, 500, +1.0)
define SameOrBefore_HourAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour or before DateTime(2000, 3, 15, 12, 14, 47, 500, +1.0)
define SameOrBefore_HourBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour or before DateTime(2000, 3, 15, 14, 14, 47, 500, +1.0)
define SameOrBefore_SameMinute: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same minute or before DateTime(2000, 3, 15, 13, 30, 47, 500, +1.0)
define SameOrBefore_MinuteAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same minute or before DateTime(2000, 3, 15, 13, 29, 47, 500, +1.0)
define SameOrBefore_MinuteBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same minute or before DateTime(2000, 3, 15, 13, 31, 47, 500, +1.0)
define SameOrBefore_SameSecond: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same second or before DateTime(2000, 3, 15, 13, 30, 25, 500, +1.0)
define SameOrBefore_SecondAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same second or before DateTime(2000, 3, 15, 13, 30, 24, 500, +1.0)
define SameOrBefore_SecondBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same second or before DateTime(2000, 3, 15, 13, 30, 26, 500, +1.0)
define SameOrBefore_SameMillisecond: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same millisecond or before DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define SameOrBefore_MillisecondAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same millisecond or before DateTime(2000, 3, 15, 13, 30, 25, 199, +1.0)
define SameOrBefore_MillisecondBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same millisecond or before DateTime(2000, 3, 15, 13, 30, 25, 201, +1.0)
define SameOrBefore_Same: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same or before DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define SameOrBefore_After: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same or before DateTime(2000, 3, 15, 13, 30, 25, 199, +1.0)
define SameOrBefore_Before: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same or before DateTime(2000, 3, 15, 13, 30, 25, 201, +1.0)
define SameOrBefore_SameDayMonthBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day or before DateTime(2000, 4, 15, 8, 14, 47, 500, +1.0)
define SameOrBefore_DayAfterMonthBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day or before DateTime(2000, 4, 14, 8, 14, 47, 500, +1.0)
define SameOrBefore_DayBeforeMonthAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same day or before DateTime(2000, 2, 16, 8, 14, 47, 500, +1.0)
define SameOrBefore_ImpreciseDay: DateTime(2000, 3, 15, 13, 30, 25, 200) same day or before DateTime(2000, 3)
define SameOrBefore_ImpreciseDayMonthAfter: DateTime(2000, 3, 15, 13, 30, 25, 200) same day or before DateTime(2000, 2)
define SameOrBefore_ImpreciseDayMonthBefore: DateTime(2000, 3, 15, 13, 30, 25, 200) same day or before DateTime(2000, 4)
define SameOrBefore_SameHourNormalizeZones: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour or before DateTime(2000, 3, 15, 7, 30, 25, 200, -5.0)
define SameOrBefore_HourAfterNormalizeZones: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour or before DateTime(2000, 3, 15, 6, 30, 25, 200, -5.0)
define SameOrBefore_HourBeforeNormalizeZones: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same hour or before DateTime(2000, 3, 15, 8, 30, 25, 200, -5.0)
define SameOrBefore_NullLeft: null same or before DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define SameOrBefore_NullRight: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) same or before null
define SameOrBefore_NullBoth: (null as DateTime) same or before null

define test_SameOrBefore_SameYear: TestMessage(SameOrBefore_SameYear, 'SameOrBefore_SameYear', ToString(true), ToString(SameOrBefore_SameYear))
define test_SameOrBefore_YearAfter: TestMessage(not SameOrBefore_YearAfter, 'SameOrBefore_YearAfter', ToString(false), ToString(SameOrBefore_YearAfter))
define test_SameOrBefore_YearBefore: TestMessage(SameOrBefore_YearBefore, 'SameOrBefore_YearBefore', ToString(true), ToString(SameOrBefore_YearBefore))
define test_SameOrBefore_SameMonth: TestMessage(SameOrBefore_SameMonth, 'SameOrBefore_SameMonth', ToString(true), ToString(SameOrBefore_SameMonth))
define test_SameOrBefore_MonthAfter: TestMessage(not SameOrBefore_MonthAfter, 'SameOrBefore_MonthAfter', ToString(false), ToString(SameOrBefore_MonthAfter))
define test_SameOrBefore_MonthBefore: TestMessage(SameOrBefore_MonthBefore, 'SameOrBefore_MonthBefore', ToString(true), ToString(SameOrBefore_MonthBefore))
define test_SameOrBefore_SameDay: TestMessage(SameOrBefore_SameDay, 'SameOrBefore_SameDay', ToString(true), ToString(SameOrBefore_SameDay))
define test_SameOrBefore_DayAfter: TestMessage(not SameOrBefore_DayAfter, 'SameOrBefore_DayAfter', ToString(false), ToString(SameOrBefore_DayAfter))
define test_SameOrBefore_DayBefore: TestMessage(SameOrBefore_DayBefore, 'SameOrBefore_DayBefore', ToString(true), ToString(SameOrBefore_DayBefore))
define test_SameOrBefore_SameHour: TestMessage(SameOrBefore_SameHour, 'SameOrBefore_SameHour', ToString(true), ToString(SameOrBefore_SameHour))
define test_SameOrBefore_HourAfter: TestMessage(not SameOrBefore_HourAfter, 'SameOrBefore_HourAfter', ToString(false), ToString(SameOrBefore_HourAfter))
define test_SameOrBefore_HourBefore: TestMessage(SameOrBefore_HourBefore, 'SameOrBefore_HourBefore', ToString(true), ToString(SameOrBefore_HourBefore))
define test_SameOrBefore_SameMinute: TestMessage(SameOrBefore_SameMinute, 'SameOrBefore_SameMinute', ToString(true), ToString(SameOrBefore_SameMinute))
define test_SameOrBefore_MinuteAfter: TestMessage(not SameOrBefore_MinuteAfter, 'SameOrBefore_MinuteAfter', ToString(false), ToString(SameOrBefore_MinuteAfter))
define test_SameOrBefore_MinuteBefore: TestMessage(SameOrBefore_MinuteBefore, 'SameOrBefore_MinuteBefore', ToString(true), ToString(SameOrBefore_MinuteBefore))
define test_SameOrBefore_SameSecond: TestMessage(SameOrBefore_SameSecond, 'SameOrBefore_SameSecond', ToString(true), ToString(SameOrBefore_SameSecond))
define test_SameOrBefore_SecondAfter: TestMessage(not SameOrBefore_SecondAfter, 'SameOrBefore_SecondAfter', ToString(false), ToString(SameOrBefore_SecondAfter))
define test_SameOrBefore_SecondBefore: TestMessage(SameOrBefore_SecondBefore, 'SameOrBefore_SecondBefore', ToString(true), ToString(SameOrBefore_SecondBefore))
define test_SameOrBefore_SameMillisecond: TestMessage(SameOrBefore_SameMillisecond, 'SameOrBefore_SameMillisecond', ToString(true), ToString(SameOrBefore_SameMillisecond))
define test_SameOrBefore_MillisecondAfter: TestMessage(not SameOrBefore_MillisecondAfter, 'SameOrBefore_MillisecondAfter', ToString(false), ToString(SameOrBefore_MillisecondAfter))
define test_SameOrBefore_MillisecondBefore: TestMessage(SameOrBefore_MillisecondBefore, 'SameOrBefore_MillisecondBefore', ToString(true), ToString(SameOrBefore_MillisecondBefore))
define test_SameOrBefore_Same: TestMessage(SameOrBefore_Same, 'SameOrBefore_Same', ToString(true), ToString(SameOrBefore_Same))
define test_SameOrBefore_After: TestMessage(not SameOrBefore_After, 'SameOrBefore_After', ToString(false), ToString(SameOrBefore_After))
define test_SameOrBefore_Before: TestMessage(SameOrBefore_Before, 'SameOrBefore_Before', ToString(true), ToString(SameOrBefore_Before))
define test_SameOrBefore_SameDayMonthBefore: TestMessage(SameOrBefore_SameDayMonthBefore, 'SameOrBefore_SameDayMonthBefore', ToString(true), ToString(SameOrBefore_SameDayMonthBefore))
define test_SameOrBefore_DayAfterMonthBefore: TestMessage(SameOrBefore_DayAfterMonthBefore, 'SameOrBefore_DayAfterMonthBefore', ToString(true), ToString(SameOrBefore_DayAfterMonthBefore))
define test_SameOrBefore_DayBeforeMonthAfter: TestMessage(not SameOrBefore_DayBeforeMonthAfter, 'SameOrBefore_DayBeforeMonthAfter', ToString(false), ToString(SameOrBefore_DayBeforeMonthAfter))
define test_SameOrBefore_ImpreciseDay: TestMessage(SameOrBefore_ImpreciseDay is null, 'SameOrBefore_ImpreciseDay', 'null', ToString(SameOrBefore_ImpreciseDay))
define test_SameOrBefore_ImpreciseDayMonthAfter: TestMessage(not SameOrBefore_ImpreciseDayMonthAfter, 'SameOrBefore_ImpreciseDayMonthAfter', ToString(false), ToString(SameOrBefore_ImpreciseDayMonthAfter))
define test_SameOrBefore_ImpreciseDayMonthBefore: TestMessage(SameOrBefore_ImpreciseDayMonthBefore, 'SameOrBefore_ImpreciseDayMonthBefore', ToString(true), ToString(SameOrBefore_ImpreciseDayMonthBefore))
define test_SameOrBefore_SameHourNormalizeZones: TestMessage(SameOrBefore_SameHourNormalizeZones, 'SameOrBefore_SameHourNormalizeZones', ToString(true), ToString(SameOrBefore_SameHourNormalizeZones))
define test_SameOrBefore_HourAfterNormalizeZones: TestMessage(not SameOrBefore_HourAfterNormalizeZones, 'SameOrBefore_HourAfterNormalizeZones', ToString(false), ToString(SameOrBefore_HourAfterNormalizeZones))
define test_SameOrBefore_HourBeforeNormalizeZones: TestMessage(SameOrBefore_HourBeforeNormalizeZones, 'SameOrBefore_HourBeforeNormalizeZones', ToString(true), ToString(SameOrBefore_HourBeforeNormalizeZones))
define test_SameOrBefore_NullLeft: TestMessage(SameOrBefore_NullLeft is null, 'SameOrBefore_NullLeft', 'null', ToString(SameOrBefore_NullLeft))
define test_SameOrBefore_NullRight: TestMessage(SameOrBefore_NullRight is null, 'SameOrBefore_NullRight', 'null', ToString(SameOrBefore_NullRight))
define test_SameOrBefore_NullBoth: TestMessage(SameOrBefore_NullBoth is null, 'SameOrBefore_NullBoth', 'null', ToString(SameOrBefore_NullBoth))

// After
define After_SameYear: DateTime(2000, 12, 15, 13, 30, 25, 200, +1.0) after year of DateTime(2000, 11, 23, 8, 14, 47, 500, +1.0)
define After_YearAfter: DateTime(2000, 12, 15, 13, 30, 25, 200, +1.0) after year of DateTime(1999, 11, 23, 8, 14, 47, 500, +1.0)
define After_YearBefore: DateTime(2000, 12, 15, 13, 30, 25, 200, +1.0) after year of DateTime(2001, 11, 23, 8, 14, 47, 500, +1.0)
define After_SameMonth: DateTime(2000, 3, 25, 13, 30, 25, 200, +1.0) after month of DateTime(2000, 3, 23, 8, 14, 47, 500, +1.0)
define After_MonthAfter: DateTime(2000, 3, 25, 13, 30, 25, 200, +1.0) after month of DateTime(2000, 2, 23, 8, 14, 47, 500, +1.0)
define After_MonthBefore: DateTime(2000, 3, 25, 13, 30, 25, 200, +1.0) after month of DateTime(2000, 4, 23, 8, 14, 47, 500, +1.0)
define After_SameDay: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after day of DateTime(2000, 3, 15, 8, 14, 47, 500, +1.0)
define After_DayAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after day of DateTime(2000, 3, 14, 8, 14, 47, 500, +1.0)
define After_DayBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after day of DateTime(2000, 3, 16, 8, 14, 47, 500, +1.0)
define After_SameHour: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after hour of DateTime(2000, 3, 15, 13, 14, 47, 500, +1.0)
define After_HourAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after hour of DateTime(2000, 3, 15, 12, 14, 47, 500, +1.0)
define After_HourBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after hour of DateTime(2000, 3, 15, 14, 14, 47, 500, +1.0)
define After_SameMinute: DateTime(2000, 3, 15, 13, 30, 55, 200, +1.0) after minute of DateTime(2000, 3, 15, 13, 30, 47, 500, +1.0)
define After_MinuteAfter: DateTime(2000, 3, 15, 13, 30, 55, 200, +1.0) after minute of DateTime(2000, 3, 15, 13, 29, 47, 500, +1.0)
define After_MinuteBefore: DateTime(2000, 3, 15, 13, 30, 55, 200, +1.0) after minute of DateTime(2000, 3, 15, 13, 31, 47, 500, +1.0)
define After_SameSecond: DateTime(2000, 3, 15, 13, 30, 25, 700, +1.0) after second of DateTime(2000, 3, 15, 13, 30, 25, 500, +1.0)
define After_SecondAfter: DateTime(2000, 3, 15, 13, 30, 25, 700, +1.0) after second of DateTime(2000, 3, 15, 13, 30, 24, 500, +1.0)
define After_SecondBefore: DateTime(2000, 3, 15, 13, 30, 25, 700, +1.0) after second of DateTime(2000, 3, 15, 13, 30, 26, 500, +1.0)
define After_SameMillisecond: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after millisecond of DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define After_MillisecondAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after millisecond of DateTime(2000, 3, 15, 13, 30, 25, 199, +1.0)
define After_MillisecondBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after millisecond of DateTime(2000, 3, 15, 13, 30, 25, 201, +1.0)
define After_Same: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define After_After: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after DateTime(2000, 3, 15, 13, 30, 25, 199, +1.0)
define After_Before: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after DateTime(2000, 3, 15, 13, 30, 25, 201, +1.0)
define After_ImpreciseDay: DateTime(2000, 3, 15, 13, 30, 25, 200) after day of DateTime(2000, 3)
define After_ImpreciseDayMonthAfter: DateTime(2000, 3, 15, 13, 30, 25, 200) after day of DateTime(2000, 2)
define After_ImpreciseDayMonthBefore: DateTime(2000, 3, 15, 13, 30, 25, 200) after day of DateTime(2000, 4)
define After_SameHourNormalizeZones: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after hour of DateTime(2000, 3, 15, 7, 30, 25, 200, -5.0)
define After_HourAfterNormalizeZones: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after hour of DateTime(2000, 3, 15, 6, 30, 25, 200, -5.0)
define After_HourBeforeNormalizeZones: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after hour of DateTime(2000, 3, 15, 8, 30, 25, 200, -5.0)
define After_NullLeft: null after DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define After_NullRight: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) after null
define After_NullBoth: (null as DateTime) after null

define test_After_SameYear: TestMessage(not After_SameYear, 'After_SameYear', ToString(false), ToString(After_SameYear))
define test_After_YearAfter: TestMessage(After_YearAfter, 'After_YearAfter', ToString(true), ToString(After_YearAfter))
define test_After_YearBefore: TestMessage(not After_YearBefore, 'After_YearBefore', ToString(false), ToString(After_YearBefore))
define test_After_SameMonth: TestMessage(not After_SameMonth, 'After_SameMonth', ToString(false), ToString(After_SameMonth))
define test_After_MonthAfter: TestMessage(After_MonthAfter, 'After_MonthAfter', ToString(true), ToString(After_MonthAfter))
define test_After_MonthBefore: TestMessage(not After_MonthBefore, 'After_MonthBefore', ToString(false), ToString(After_MonthBefore))
define test_After_SameDay: TestMessage(not After_SameDay, 'After_SameDay', ToString(false), ToString(After_SameDay))
define test_After_DayAfter: TestMessage(After_DayAfter, 'After_DayAfter', ToString(true), ToString(After_DayAfter))
define test_After_DayBefore: TestMessage(not After_DayBefore, 'After_DayBefore', ToString(false), ToString(After_DayBefore))
define test_After_SameHour: TestMessage(not After_SameHour, 'After_SameHour', ToString(false), ToString(After_SameHour))
define test_After_HourAfter: TestMessage(After_HourAfter, 'After_HourAfter', ToString(true), ToString(After_HourAfter))
define test_After_HourBefore: TestMessage(not After_HourBefore, 'After_HourBefore', ToString(false), ToString(After_HourBefore))
define test_After_SameMinute: TestMessage(not After_SameMinute, 'After_SameMinute', ToString(false), ToString(After_SameMinute))
define test_After_MinuteAfter: TestMessage(After_MinuteAfter, 'After_MinuteAfter', ToString(true), ToString(After_MinuteAfter))
define test_After_MinuteBefore: TestMessage(not After_MinuteBefore, 'After_MinuteBefore', ToString(false), ToString(After_MinuteBefore))
define test_After_SameSecond: TestMessage(not After_SameSecond, 'After_SameSecond', ToString(false), ToString(After_SameSecond))
define test_After_SecondAfter: TestMessage(After_SecondAfter, 'After_SecondAfter', ToString(true), ToString(After_SecondAfter))
define test_After_SecondBefore: TestMessage(not After_SecondBefore, 'After_SecondBefore', ToString(false), ToString(After_SecondBefore))
define test_After_SameMillisecond: TestMessage(not After_SameMillisecond, 'After_SameMillisecond', ToString(false), ToString(After_SameMillisecond))
define test_After_MillisecondAfter: TestMessage(After_MillisecondAfter, 'After_MillisecondAfter', ToString(true), ToString(After_MillisecondAfter))
define test_After_MillisecondBefore: TestMessage(not After_MillisecondBefore, 'After_MillisecondBefore', ToString(false), ToString(After_MillisecondBefore))
define test_After_Same: TestMessage(not After_Same, 'After_Same', ToString(false), ToString(After_Same))
define test_After_After: TestMessage(After_After, 'After_After', ToString(true), ToString(After_After))
define test_After_Before: TestMessage(not After_Before, 'After_Before', ToString(false), ToString(After_Before))
define test_After_ImpreciseDay: TestMessage(After_ImpreciseDay is null, 'After_ImpreciseDay', 'null', ToString(After_ImpreciseDay))
define test_After_ImpreciseDayMonthAfter: TestMessage(After_ImpreciseDayMonthAfter, 'After_ImpreciseDayMonthAfter', ToString(true), ToString(After_ImpreciseDayMonthAfter))
define test_After_ImpreciseDayMonthBefore: TestMessage(not After_ImpreciseDayMonthBefore, 'After_ImpreciseDayMonthBefore', ToString(false), ToString(After_ImpreciseDayMonthBefore))
define test_After_SameHourNormalizeZones: TestMessage(not After_SameHourNormalizeZones, 'After_SameHourNormalizeZones', ToString(false), ToString(After_SameHourNormalizeZones))
define test_After_HourAfterNormalizeZones: TestMessage(After_HourAfterNormalizeZones, 'After_HourAfterNormalizeZones', ToString(true), ToString(After_HourAfterNormalizeZones))
define test_After_HourBeforeNormalizeZones: TestMessage(not After_HourBeforeNormalizeZones, 'After_HourBeforeNormalizeZones', ToString(false), ToString(After_HourBeforeNormalizeZones))
define test_After_NullLeft: TestMessage(After_NullLeft is null, 'After_NullLeft', 'null', ToString(After_NullLeft))
define test_After_NullRight: TestMessage(After_NullRight is null, 'After_NullRight', 'null', ToString(After_NullRight))
define test_After_NullBoth: TestMessage(After_NullBoth is null, 'After_NullBoth', 'null', ToString(After_NullBoth))

// Before
define Before_SameYear: DateTime(2000, 10, 15, 13, 30, 25, 200, +1.0) before year of DateTime(2000, 11, 23, 8, 14, 47, 500, +1.0)
define Before_YearAfter: DateTime(2000, 10, 15, 13, 30, 25, 200, +1.0) before year of DateTime(1999, 11, 23, 8, 14, 47, 500, +1.0)
define Before_YearBefore: DateTime(2000, 10, 15, 13, 30, 25, 200, +1.0) before year of DateTime(2001, 11, 23, 8, 14, 47, 500, +1.0)
define Before_SameMonth: DateTime(2000, 3, 22, 13, 30, 25, 200, +1.0) before month of DateTime(2000, 3, 23, 8, 14, 47, 500, +1.0)
define Before_MonthAfter: DateTime(2000, 3, 22, 13, 30, 25, 200, +1.0) before month of DateTime(2000, 2, 23, 8, 14, 47, 500, +1.0)
define Before_MonthBefore: DateTime(2000, 3, 22, 13, 30, 25, 200, +1.0) before month of DateTime(2000, 4, 23, 8, 14, 47, 500, +1.0)
define Before_SameDay: DateTime(2000, 3, 15, 6, 30, 25, 200, +1.0) before day of DateTime(2000, 3, 15, 8, 14, 47, 500, +1.0)
define Before_DayAfter: DateTime(2000, 3, 15, 6, 30, 25, 200, +1.0) before day of DateTime(2000, 3, 14, 8, 14, 47, 500, +1.0)
define Before_DayBefore: DateTime(2000, 3, 15, 6, 30, 25, 200, +1.0) before day of DateTime(2000, 3, 16, 8, 14, 47, 500, +1.0)
define Before_SameHour: DateTime(2000, 3, 15, 13, 10, 25, 200, +1.0) before hour of DateTime(2000, 3, 15, 13, 14, 47, 500, +1.0)
define Before_HourAfter: DateTime(2000, 3, 15, 13, 10, 25, 200, +1.0) before hour of DateTime(2000, 3, 15, 12, 14, 47, 500, +1.0)
define Before_HourBefore: DateTime(2000, 3, 15, 13, 10, 25, 200, +1.0) before hour of DateTime(2000, 3, 15, 14, 14, 47, 500, +1.0)
define Before_SameMinute: DateTime(2000, 3, 15, 13, 30, 44, 200, +1.0) before minute of DateTime(2000, 3, 15, 13, 30, 47, 500, +1.0)
define Before_MinuteAfter: DateTime(2000, 3, 15, 13, 30, 44, 200, +1.0) before minute of DateTime(2000, 3, 15, 13, 29, 47, 500, +1.0)
define Before_MinuteBefore: DateTime(2000, 3, 15, 13, 30, 44, 200, +1.0) before minute of DateTime(2000, 3, 15, 13, 31, 47, 500, +1.0)
define Before_SameSecond: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before second of DateTime(2000, 3, 15, 13, 30, 25, 500, +1.0)
define Before_SecondAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before second of DateTime(2000, 3, 15, 13, 30, 24, 500, +1.0)
define Before_SecondBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before second of DateTime(2000, 3, 15, 13, 30, 26, 500, +1.0)
define Before_SameMillisecond: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before millisecond of DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define Before_MillisecondAfter: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before millisecond of DateTime(2000, 3, 15, 13, 30, 25, 199, +1.0)
define Before_MillisecondBefore: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before millisecond of DateTime(2000, 3, 15, 13, 30, 25, 201, +1.0)
define Before_Same: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define Before_After: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before DateTime(2000, 3, 15, 13, 30, 25, 199, +1.0)
define Before_Before: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before DateTime(2000, 3, 15, 13, 30, 25, 201, +1.0)
define Before_ImpreciseDay: DateTime(2000, 3, 15, 13, 30, 25, 200) before day of DateTime(2000, 3)
define Before_ImpreciseDayMonthAfter: DateTime(2000, 3, 15, 13, 30, 25, 200) before day of DateTime(2000, 2)
define Before_ImpreciseDayMonthBefore: DateTime(2000, 3, 15, 13, 30, 25, 200) before day of DateTime(2000, 4)
define Before_SameHourNormalizeZones: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before hour of DateTime(2000, 3, 15, 7, 30, 25, 200, -5.0)
define Before_HourAfterNormalizeZones: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before hour of DateTime(2000, 3, 15, 6, 30, 25, 200, -5.0)
define Before_HourBeforeNormalizeZones: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before hour of DateTime(2000, 3, 15, 8, 30, 25, 200, -5.0)
define Before_NullLeft: null before DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0)
define Before_NullRight: DateTime(2000, 3, 15, 13, 30, 25, 200, +1.0) before null
define Before_NullBoth: (null as DateTime) before null

define test_Before_SameYear: TestMessage(not Before_SameYear, 'Before_SameYear', ToString(false), ToString(Before_SameYear))
define test_Before_YearAfter: TestMessage(not Before_YearAfter, 'Before_YearAfter', ToString(false), ToString(Before_YearAfter))
define test_Before_YearBefore: TestMessage(Before_YearBefore, 'Before_YearBefore', ToString(true), ToString(Before_YearBefore))
define test_Before_SameMonth: TestMessage(not Before_SameMonth, 'Before_SameMonth', ToString(false), ToString(Before_SameMonth))
define test_Before_MonthAfter: TestMessage(not Before_MonthAfter, 'Before_MonthAfter', ToString(false), ToString(Before_MonthAfter))
define test_Before_MonthBefore: TestMessage(Before_MonthBefore, 'Before_MonthBefore', ToString(true), ToString(Before_MonthBefore))
define test_Before_SameDay: TestMessage(not Before_SameDay, 'Before_SameDay', ToString(false), ToString(Before_SameDay))
define test_Before_DayAfter: TestMessage(not Before_DayAfter, 'Before_DayAfter', ToString(false), ToString(Before_DayAfter))
define test_Before_DayBefore: TestMessage(Before_DayBefore, 'Before_DayBefore', ToString(true), ToString(Before_DayBefore))
define test_Before_SameHour: TestMessage(not Before_SameHour, 'Before_SameHour', ToString(false), ToString(Before_SameHour))
define test_Before_HourAfter: TestMessage(not Before_HourAfter, 'Before_HourAfter', ToString(false), ToString(Before_HourAfter))
define test_Before_HourBefore: TestMessage(Before_HourBefore, 'Before_HourBefore', ToString(true), ToString(Before_HourBefore))
define test_Before_SameMinute: TestMessage(not Before_SameMinute, 'Before_SameMinute', ToString(false), ToString(Before_SameMinute))
define test_Before_MinuteAfter: TestMessage(not Before_MinuteAfter, 'Before_MinuteAfter', ToString(false), ToString(Before_MinuteAfter))
define test_Before_MinuteBefore: TestMessage(Before_MinuteBefore, 'Before_MinuteBefore', ToString(true), ToString(Before_MinuteBefore))
define test_Before_SameSecond: TestMessage(not Before_SameSecond, 'Before_SameSecond', ToString(false), ToString(Before_SameSecond))
define test_Before_SecondAfter: TestMessage(not Before_SecondAfter, 'Before_SecondAfter', ToString(false), ToString(Before_SecondAfter))
define test_Before_SecondBefore: TestMessage(Before_SecondBefore, 'Before_SecondBefore', ToString(true), ToString(Before_SecondBefore))
define test_Before_SameMillisecond: TestMessage(not Before_SameMillisecond, 'Before_SameMillisecond', ToString(false), ToString(Before_SameMillisecond))
define test_Before_MillisecondAfter: TestMessage(not Before_MillisecondAfter, 'Before_MillisecondAfter', ToString(false), ToString(Before_MillisecondAfter))
define test_Before_MillisecondBefore: TestMessage(Before_MillisecondBefore, 'Before_MillisecondBefore', ToString(true), ToString(Before_MillisecondBefore))
define test_Before_Same: TestMessage(not Before_Same, 'Before_Same', ToString(false), ToString(Before_Same))
define test_Before_After: TestMessage(not Before_After, 'Before_After', ToString(false), ToString(Before_After))
define test_Before_Before: TestMessage(Before_Before, 'Before_Before', ToString(true), ToString(Before_Before))
define test_Before_ImpreciseDay: TestMessage(Before_ImpreciseDay is null, 'Before_ImpreciseDay', 'null', ToString(Before_ImpreciseDay))
define test_Before_ImpreciseDayMonthAfter: TestMessage(not Before_ImpreciseDayMonthAfter, 'Before_ImpreciseDayMonthAfter', ToString(false), ToString(Before_ImpreciseDayMonthAfter))
define test_Before_ImpreciseDayMonthBefore: TestMessage(Before_ImpreciseDayMonthBefore, 'Before_ImpreciseDayMonthBefore', ToString(true), ToString(Before_ImpreciseDayMonthBefore))
define test_Before_SameHourNormalizeZones: TestMessage(not Before_SameHourNormalizeZones, 'Before_SameHourNormalizeZones', ToString(false), ToString(Before_SameHourNormalizeZones))
define test_Before_HourAfterNormalizeZones: TestMessage(not Before_HourAfterNormalizeZones, 'Before_HourAfterNormalizeZones', ToString(false), ToString(Before_HourAfterNormalizeZones))
define test_Before_HourBeforeNormalizeZones: TestMessage(Before_HourBeforeNormalizeZones, 'Before_HourBeforeNormalizeZones', ToString(true), ToString(Before_HourBeforeNormalizeZones))
define test_Before_NullLeft: TestMessage(Before_NullLeft is null, 'Before_NullLeft', 'null', ToString(Before_NullLeft))
define test_Before_NullRight: TestMessage(Before_NullRight is null, 'Before_NullRight', 'null', ToString(Before_NullRight))
define test_Before_NullBoth: TestMessage(Before_NullBoth is null, 'Before_NullBoth', 'null', ToString(Before_NullBoth))

// DifferenceBetween
define NewYear2013: DateTime(2013, 1, 1, 0, 0, 0, 0)
define NewYear2014: DateTime(2014, 1, 1, 0, 0, 0, 0)
define January2014: DateTime(2014, 1)
define DifferenceBetween_YearsBetween: difference in years between NewYear2013 and NewYear2014
define DifferenceBetween_MonthsBetween: difference in months between NewYear2013 and NewYear2014
define DifferenceBetween_WeeksBetween: difference in weeks between NewYear2013 and NewYear2014
define DifferenceBetween_DaysBetween: difference in days between NewYear2013 and NewYear2014
define DifferenceBetween_HoursBetween: difference in hours between NewYear2013 and NewYear2014
define DifferenceBetween_MinutesBetween: difference in minutes between NewYear2013 and NewYear2014
define DifferenceBetween_SecondsBetween: difference in seconds between NewYear2013 and NewYear2014
define DifferenceBetween_SecondsBetweenReversed: difference in seconds between NewYear2014 and NewYear2013
// TODO - move to error tests
// define DifferenceBetween_MillisecondsBetween: difference in milliseconds between NewYear2013 and NewYear2014
// define DifferenceBetween_MillisecondsBetweenReversed: difference in milliseconds between NewYear2014 and NewYear2013
define DifferenceBetween_YearsBetweenUncertainty: difference in years between NewYear2014 and January2014
define DifferenceBetween_MonthsBetweenUncertainty: difference in months between NewYear2014 and January2014
// TODO - add to values tests for internal validation
define DifferenceBetween_WeeksBetweenUncertainty: difference in weeks between NewYear2014 and January2014
define DifferenceBetween_DaysBetweenUncertainty: difference in days between NewYear2014 and January2014
define DifferenceBetween_HoursBetweenUncertainty: difference in hours between NewYear2014 and January2014
define DifferenceBetween_MinutesBetweenUncertainty: difference in minutes between NewYear2014 and January2014
define DifferenceBetween_SecondsBetweenUncertainty: difference in seconds between NewYear2014 and January2014
define DifferenceBetween_SecondsBetweenReversedUncertainty: difference in seconds between January2014 and NewYear2014
// TODO - move to error tests
// define DifferenceBetween_MillisecondsBetweenUncertainty: difference in milliseconds between NewYear2014 and January2014
// define DifferenceBetween_MillisecondsBetweenReversedUncertainty: difference in milliseconds between January2014 and NewYear2014
define DifferenceBetween_HoursBetween1and3CrossingSpringDST: difference in hours between DateTime(2017, 3, 12, 1, 0, 0, 0, -5.0) and DateTime(2017, 3, 12, 3, 0, 0, 0, -4.0)
define DifferenceBetween_HoursBetween1and3CrossingFallDST: difference in hours between DateTime(2017, 11, 5, 1, 0, 0, 0, -4.0) and DateTime(2017, 11, 5, 3, 0, 0, 0, -5.0)

define test_DifferenceBetween_YearsBetween: TestMessage(DifferenceBetween_YearsBetween = 1, 'DifferenceBetween_YearsBetween', ToString(1), ToString(DifferenceBetween_YearsBetween))
define test_DifferenceBetween_MonthsBetween: TestMessage(DifferenceBetween_MonthsBetween = 12, 'DifferenceBetween_MonthsBetween', ToString(12), ToString(DifferenceBetween_MonthsBetween))
define test_DifferenceBetween_WeeksBetween: TestMessage(DifferenceBetween_WeeksBetween = 52, 'DifferenceBetween_WeeksBetween', ToString(52), ToString(DifferenceBetween_WeeksBetween))
define test_DifferenceBetween_DaysBetween: TestMessage(DifferenceBetween_DaysBetween = 365, 'DifferenceBetween_DaysBetween', ToString(365), ToString(DifferenceBetween_DaysBetween))
define test_DifferenceBetween_HoursBetween: TestMessage(DifferenceBetween_HoursBetween = 8760, 'DifferenceBetween_HoursBetween', ToString(8760), ToString(DifferenceBetween_HoursBetween))
define test_DifferenceBetween_MinutesBetween: TestMessage(DifferenceBetween_MinutesBetween = 525600, 'DifferenceBetween_MinutesBetween', ToString(525600), ToString(DifferenceBetween_MinutesBetween))
define test_DifferenceBetween_SecondsBetween: TestMessage(DifferenceBetween_SecondsBetween = 31536000, 'DifferenceBetween_SecondsBetween', ToString(31536000), ToString(DifferenceBetween_SecondsBetween))
define test_DifferenceBetween_SecondsBetweenReversed: TestMessage(DifferenceBetween_SecondsBetweenReversed = -31536000, 'DifferenceBetween_SecondsBetweenReversed', ToString(-31536000), ToString(DifferenceBetween_SecondsBetweenReversed))
define test_DifferenceBetween_YearsBetweenUncertainty: TestMessage(DifferenceBetween_YearsBetweenUncertainty = 0, 'DifferenceBetween_YearsBetweenUncertainty', ToString(0), ToString(DifferenceBetween_YearsBetweenUncertainty))
define test_DifferenceBetween_MonthsBetweenUncertainty: TestMessage(DifferenceBetween_MonthsBetweenUncertainty = 0, 'DifferenceBetween_MonthsBetweenUncertainty', ToString(0), ToString(DifferenceBetween_MonthsBetweenUncertainty))
define test_DifferenceBetween_WeeksBetweenUncertainty: TestMessage(DifferenceBetween_WeeksBetweenUncertainty > -1 and (DifferenceBetween_WeeksBetweenUncertainty > 0) is null and DifferenceBetween_WeeksBetweenUncertainty < 5 and (DifferenceBetween_WeeksBetweenUncertainty < 4) is null, 'DifferenceBetween_WeeksBetweenUncertainty', ToString(true), ToString(DifferenceBetween_WeeksBetweenUncertainty > -1 and (DifferenceBetween_WeeksBetweenUncertainty > 0) is null and DifferenceBetween_WeeksBetweenUncertainty < 5 and (DifferenceBetween_WeeksBetweenUncertainty < 4) is null))
define test_DifferenceBetween_DaysBetweenUncertainty: TestMessage(DifferenceBetween_DaysBetweenUncertainty > -1 and (DifferenceBetween_DaysBetweenUncertainty > 0) is null and DifferenceBetween_DaysBetweenUncertainty < 31 and (DifferenceBetween_DaysBetweenUncertainty < 30) is null, 'DifferenceBetween_DaysBetweenUncertainty', ToString(true), ToString(DifferenceBetween_DaysBetweenUncertainty > -1 and (DifferenceBetween_DaysBetweenUncertainty > 0) is null and DifferenceBetween_DaysBetweenUncertainty < 31 and (DifferenceBetween_DaysBetweenUncertainty < 30) is null))
define test_DifferenceBetween_HoursBetweenUncertainty: TestMessage(DifferenceBetween_HoursBetweenUncertainty > -1 and (DifferenceBetween_HoursBetweenUncertainty > 0) is null and DifferenceBetween_HoursBetweenUncertainty < 721 and (DifferenceBetween_HoursBetweenUncertainty < 720) is null, 'DifferenceBetween_HoursBetweenUncertainty', ToString(true), ToString(DifferenceBetween_HoursBetweenUncertainty > -1 and (DifferenceBetween_HoursBetweenUncertainty > 0) is null and DifferenceBetween_HoursBetweenUncertainty < 721 and (DifferenceBetween_HoursBetweenUncertainty < 720) is null))
define test_DifferenceBetween_MinutesBetweenUncertainty: TestMessage(DifferenceBetween_MinutesBetweenUncertainty > -1 and (DifferenceBetween_MinutesBetweenUncertainty > 0) is null and DifferenceBetween_MinutesBetweenUncertainty < 43201 and (DifferenceBetween_MinutesBetweenUncertainty < 43200) is null, 'DifferenceBetween_MinutesBetweenUncertainty', ToString(true), ToString(DifferenceBetween_MinutesBetweenUncertainty > -1 and (DifferenceBetween_MinutesBetweenUncertainty > 0) is null and DifferenceBetween_MinutesBetweenUncertainty < 43201 and (DifferenceBetween_MinutesBetweenUncertainty < 43200) is null))
define test_DifferenceBetween_SecondsBetweenUncertainty: TestMessage(DifferenceBetween_SecondsBetweenUncertainty > -1 and (DifferenceBetween_SecondsBetweenUncertainty > 0) is null and DifferenceBetween_SecondsBetweenUncertainty < 2592001 and (DifferenceBetween_SecondsBetweenUncertainty < 2592000) is null, 'DifferenceBetween_SecondsBetweenUncertainty', ToString(true), ToString(DifferenceBetween_SecondsBetweenUncertainty > -1 and (DifferenceBetween_SecondsBetweenUncertainty > 0) is null and DifferenceBetween_SecondsBetweenUncertainty < 2592001 and (DifferenceBetween_SecondsBetweenUncertainty < 2592000) is null))
define test_DifferenceBetween_SecondsBetweenReversedUncertainty: TestMessage(DifferenceBetween_SecondsBetweenReversedUncertainty > -2592001 and (DifferenceBetween_SecondsBetweenReversedUncertainty > -2592000) is null and DifferenceBetween_SecondsBetweenReversedUncertainty < 1 and (DifferenceBetween_SecondsBetweenReversedUncertainty < 0) is null, 'DifferenceBetween_SecondsBetweenReversedUncertainty', ToString(true), ToString(DifferenceBetween_SecondsBetweenReversedUncertainty > -2592001 and (DifferenceBetween_SecondsBetweenReversedUncertainty > -2592000) is null and DifferenceBetween_SecondsBetweenReversedUncertainty < 1 and (DifferenceBetween_SecondsBetweenReversedUncertainty < 0) is null))
define test_DifferenceBetween_HoursBetween1and3CrossingSpringDST: TestMessage(DifferenceBetween_HoursBetween1and3CrossingSpringDST = 1, 'DifferenceBetween_HoursBetween1and3CrossingSpringDST', ToString(1), ToString(DifferenceBetween_HoursBetween1and3CrossingSpringDST))
define test_DifferenceBetween_HoursBetween1and3CrossingFallDST: TestMessage(DifferenceBetween_HoursBetween1and3CrossingFallDST = 3, 'DifferenceBetween_HoursBetween1and3CrossingFallDST', ToString(3), ToString(DifferenceBetween_HoursBetween1and3CrossingFallDST))

// DifferenceBetween Comparisons
define February2014: DateTime(2014, 2)
define DiffComp_GreaterThan25DaysAfter: difference in days between NewYear2014 and February2014 > 25
define DiffComp_GreaterThan40DaysAfter: difference in days between NewYear2014 and February2014 > 40
define DiffComp_GreaterThan80DaysAfter: difference in days between NewYear2014 and February2014 > 80
define DiffComp_GreaterOrEqualTo25DaysAfter: difference in days between NewYear2014 and February2014 >= 25
define DiffComp_GreaterOrEqualTo40DaysAfter: difference in days between NewYear2014 and February2014 >= 40
define DiffComp_GreaterOrEqualTo80DaysAfter: difference in days between NewYear2014 and February2014 >= 80
define DiffComp_EqualTo25DaysAfter: difference in days between NewYear2014 and February2014 = 25
define DiffComp_EqualTo40DaysAfter: difference in days between NewYear2014 and February2014 = 40
define DiffComp_EqualTo80DaysAfter: difference in days between NewYear2014 and February2014 = 80
define DiffComp_LessOrEqualTo25DaysAfter: difference in days between NewYear2014 and February2014 <= 25
define DiffComp_LessOrEqualTo40DaysAfter: difference in days between NewYear2014 and February2014 <= 40
define DiffComp_LessOrEqualTo80DaysAfter: difference in days between NewYear2014 and February2014 <= 80
define DiffComp_LessThan25DaysAfter: difference in days between NewYear2014 and February2014 < 25
define DiffComp_LessThan40DaysAfter: difference in days between NewYear2014 and February2014 < 40
define DiffComp_LessThan80DaysAfter: difference in days between NewYear2014 and February2014 < 80
define DiffComp_TwentyFiveDaysLessThanDaysBetween: 25 < difference in days between NewYear2014 and February2014
define DiffComp_FortyDaysEqualToDaysBetween: 40 = difference in days between NewYear2014 and February2014
define DiffComp_TwentyFiveDaysGreaterThanDaysBetween: 25 > difference in days between NewYear2014 and February2014
define DiffComp_BonnieTestCase: difference in months between DateTime(2012, 9, 13, 14, 50, 0, 0, -4.0) and DateTime(2012, 12, 31, 23, 59, 59, 999, -5.0) <= 3
define DiffComp_BonnieTestCaseZulu: difference in months between DateTime(2012, 9, 13, 14, 50, 0, 0, 0.0) and DateTime(2012, 12, 31, 23, 59, 59, 999, 0.0) <= 2

define test_DiffComp_GreaterThan25DaysAfter: TestMessage(DiffComp_GreaterThan25DaysAfter, 'DiffComp_GreaterThan25DaysAfter', ToString(true), ToString(DiffComp_GreaterThan25DaysAfter))
define test_DiffComp_GreaterThan40DaysAfter: TestMessage(DiffComp_GreaterThan40DaysAfter is null, 'DiffComp_GreaterThan40DaysAfter', 'null', ToString(DiffComp_GreaterThan40DaysAfter))
define test_DiffComp_GreaterThan80DaysAfter: TestMessage(not DiffComp_GreaterThan80DaysAfter, 'DiffComp_GreaterThan80DaysAfter', ToString(false), ToString(DiffComp_GreaterThan80DaysAfter))
define test_DiffComp_GreaterOrEqualTo25DaysAfter: TestMessage(DiffComp_GreaterOrEqualTo25DaysAfter, 'DiffComp_GreaterOrEqualTo25DaysAfter', ToString(true), ToString(DiffComp_GreaterOrEqualTo25DaysAfter))
define test_DiffComp_GreaterOrEqualTo40DaysAfter: TestMessage(DiffComp_GreaterOrEqualTo40DaysAfter is null, 'DiffComp_GreaterOrEqualTo40DaysAfter', 'null', ToString(DiffComp_GreaterOrEqualTo40DaysAfter))
define test_DiffComp_GreaterOrEqualTo80DaysAfter: TestMessage(not DiffComp_GreaterOrEqualTo80DaysAfter, 'DiffComp_GreaterOrEqualTo80DaysAfter', ToString(false), ToString(DiffComp_GreaterOrEqualTo80DaysAfter))
define test_DiffComp_EqualTo25DaysAfter: TestMessage(not DiffComp_EqualTo25DaysAfter, 'DiffComp_EqualTo25DaysAfter', ToString(false), ToString(DiffComp_EqualTo25DaysAfter))
define test_DiffComp_EqualTo40DaysAfter: TestMessage(DiffComp_EqualTo40DaysAfter is null, 'DiffComp_EqualTo40DaysAfter', 'null', ToString(DiffComp_EqualTo40DaysAfter))
define test_DiffComp_EqualTo80DaysAfter: TestMessage(not DiffComp_EqualTo80DaysAfter, 'DiffComp_EqualTo80DaysAfter', ToString(false), ToString(DiffComp_EqualTo80DaysAfter))
define test_DiffComp_LessOrEqualTo25DaysAfter: TestMessage(not DiffComp_LessOrEqualTo25DaysAfter, 'DiffComp_LessOrEqualTo25DaysAfter', ToString(false), ToString(DiffComp_LessOrEqualTo25DaysAfter))
define test_DiffComp_LessOrEqualTo40DaysAfter: TestMessage(DiffComp_LessOrEqualTo40DaysAfter is null, 'DiffComp_LessOrEqualTo40DaysAfter', 'null', ToString(DiffComp_LessOrEqualTo40DaysAfter))
define test_DiffComp_LessOrEqualTo80DaysAfter: TestMessage(DiffComp_LessOrEqualTo80DaysAfter, 'DiffComp_LessOrEqualTo80DaysAfter', ToString(true), ToString(DiffComp_LessOrEqualTo80DaysAfter))
define test_DiffComp_LessThan25DaysAfter: TestMessage(not DiffComp_LessThan25DaysAfter, 'DiffComp_LessThan25DaysAfter', ToString(false), ToString(DiffComp_LessThan25DaysAfter))
define test_DiffComp_LessThan40DaysAfter: TestMessage(DiffComp_LessThan40DaysAfter is null, 'DiffComp_LessThan40DaysAfter', 'null', ToString(DiffComp_LessThan40DaysAfter))
define test_DiffComp_LessThan80DaysAfter: TestMessage(DiffComp_LessThan80DaysAfter, 'DiffComp_LessThan80DaysAfter', ToString(true), ToString(DiffComp_LessThan80DaysAfter))
define test_DiffComp_TwentyFiveDaysLessThanDaysBetween: TestMessage(DiffComp_TwentyFiveDaysLessThanDaysBetween, 'DiffComp_TwentyFiveDaysLessThanDaysBetween', ToString(true), ToString(DiffComp_TwentyFiveDaysLessThanDaysBetween))
define test_DiffComp_FortyDaysEqualToDaysBetween: TestMessage(DiffComp_FortyDaysEqualToDaysBetween is null, 'DiffComp_FortyDaysEqualToDaysBetween', 'null', ToString(DiffComp_FortyDaysEqualToDaysBetween))
define test_DiffComp_TwentyFiveDaysGreaterThanDaysBetween: TestMessage(not DiffComp_TwentyFiveDaysGreaterThanDaysBetween, 'DiffComp_TwentyFiveDaysGreaterThanDaysBetween', ToString(false), ToString(DiffComp_TwentyFiveDaysGreaterThanDaysBetween))
define test_DiffComp_BonnieTestCase: TestMessage(not DiffComp_BonnieTestCase, 'DiffComp_BonnieTestCase', ToString(false), ToString(DiffComp_BonnieTestCase))
define test_DiffComp_BonnieTestCaseZulu: TestMessage(not DiffComp_BonnieTestCaseZulu, 'DiffComp_BonnieTestCaseZulu', ToString(false), ToString(DiffComp_BonnieTestCaseZulu))

// DurationBetween
define DecTen2013: DateTime(2013, 12, 10, 12, 30, 30, 500)
define JanOne2015: DateTime(2015, 1, 1, 0, 0, 0, 0)
define January2015: DateTime(2015, 1)
define DurationBetween_YearsBetween: years between DecTen2013 and JanOne2015
define DurationBetween_MonthsBetween: months between DecTen2013 and JanOne2015
define DurationBetween_WeeksBetween: weeks between DecTen2013 and JanOne2015
define DurationBetween_DaysBetween: days between DecTen2013 and JanOne2015
define DurationBetween_HoursBetween: hours between DecTen2013 and JanOne2015
define DurationBetween_MinutesBetween: minutes between DecTen2013 and JanOne2015
define DurationBetween_SecondsBetween: seconds between DecTen2013 and JanOne2015
define DurationBetween_SecondsBetweenReversed: seconds between JanOne2015 and DecTen2013
// TODO - move to error tests
// define DurationBetween_MillisecondsBetween: milliseconds between DecTen2013 and JanOne2015
// define DurationBetween_MillisecondsBetweenReversed: milliseconds between JanOne2015 and DecTen2013
define DurationBetween_YearsBetweenUncertainty: years between JanOne2015 and January2015
define DurationBetween_MonthsBetweenUncertainty: months between JanOne2015 and January2015
// TODO - add to values tests for internal validation
define DurationBetween_WeeksBetweenUncertainty: weeks between JanOne2015 and January2015
define DurationBetween_DaysBetweenUncertainty: days between JanOne2015 and January2015
define DurationBetween_HoursBetweenUncertainty: hours between JanOne2015 and January2015
define DurationBetween_MinutesBetweenUncertainty: minutes between JanOne2015 and January2015
define DurationBetween_SecondsBetweenUncertainty: seconds between JanOne2015 and January2015
define DurationBetween_SecondsBetweenReversedUncertainty: seconds between January2015 and JanOne2015
// TODO - move to error tests
// define DurationBetween_MillisecondsBetweenUncertainty: milliseconds between JanOne2015 and January2015
// define DurationBetween_MillisecondsBetweenReversedUncertainty: milliseconds between January2015 and JanOne2015
define DurationBetween_HoursBetween1and3CrossingSpringDST: hours between DateTime(2017, 3, 12, 1, 0, 0, 0, -5.0) and DateTime(2017, 3, 12, 3, 0, 0, 0, -4.0)
define DurationBetween_HoursBetween1and3CrossingFallDST: hours between DateTime(2017, 11, 5, 1, 0, 0, 0, -4.0) and DateTime(2017, 11, 5, 3, 0, 0, 0, -5.0)

define test_DurationBetween_YearsBetween: TestMessage(DurationBetween_YearsBetween = 1, 'DurationBetween_YearsBetween', ToString(1), ToString(DurationBetween_YearsBetween))
define test_DurationBetween_MonthsBetween: TestMessage(DurationBetween_MonthsBetween = 12, 'DurationBetween_MonthsBetween', ToString(12), ToString(DurationBetween_MonthsBetween))
define test_DurationBetween_WeeksBetween: TestMessage(DurationBetween_WeeksBetween = 55, 'DurationBetween_WeeksBetween', ToString(55), ToString(DurationBetween_WeeksBetween))
define test_DurationBetween_DaysBetween: TestMessage(DurationBetween_DaysBetween = 386, 'DurationBetween_DaysBetween', ToString(386), ToString(DurationBetween_DaysBetween))
define test_DurationBetween_HoursBetween: TestMessage(DurationBetween_HoursBetween = 9275, 'DurationBetween_HoursBetween', ToString(9275), ToString(DurationBetween_HoursBetween))
define test_DurationBetween_MinutesBetween: TestMessage(DurationBetween_MinutesBetween = 556529, 'DurationBetween_MinutesBetween', ToString(556529), ToString(DurationBetween_MinutesBetween))
define test_DurationBetween_SecondsBetween: TestMessage(DurationBetween_SecondsBetween = 33391769, 'DurationBetween_SecondsBetween', ToString(33391769), ToString(DurationBetween_SecondsBetween))
define test_DurationBetween_SecondsBetweenReversed: TestMessage(DurationBetween_SecondsBetweenReversed = -33391769, 'DurationBetween_SecondsBetweenReversed', ToString(-33391769), ToString(DurationBetween_SecondsBetweenReversed))
define test_DurationBetween_YearsBetweenUncertainty: TestMessage(DurationBetween_YearsBetweenUncertainty = 0, 'DurationBetween_YearsBetweenUncertainty', ToString(0), ToString(DurationBetween_YearsBetweenUncertainty))
define test_DurationBetween_MonthsBetweenUncertainty: TestMessage(DurationBetween_MonthsBetweenUncertainty = 0, 'DurationBetween_MonthsBetweenUncertainty', ToString(0), ToString(DurationBetween_MonthsBetweenUncertainty))
define test_DurationBetween_WeeksBetweenUncertainty: TestMessage(DurationBetween_WeeksBetweenUncertainty > -1 and (DurationBetween_WeeksBetweenUncertainty > 0) is null and DurationBetween_WeeksBetweenUncertainty < 5 and (DurationBetween_WeeksBetweenUncertainty < 4) is null, 'DurationBetween_WeeksBetweenUncertainty', ToString(true), ToString(DurationBetween_WeeksBetweenUncertainty > -1 and (DurationBetween_WeeksBetweenUncertainty > 0) is null and DurationBetween_WeeksBetweenUncertainty < 5 and (DurationBetween_WeeksBetweenUncertainty < 4) is null))
define test_DurationBetween_DaysBetweenUncertainty: TestMessage(DurationBetween_DaysBetweenUncertainty > -1 and (DurationBetween_DaysBetweenUncertainty > 0) is null and DurationBetween_DaysBetweenUncertainty < 31 and (DurationBetween_DaysBetweenUncertainty < 30) is null, 'DurationBetween_DaysBetweenUncertainty', ToString(true), ToString(DurationBetween_DaysBetweenUncertainty > -1 and (DurationBetween_DaysBetweenUncertainty > 0) is null and DurationBetween_DaysBetweenUncertainty < 31 and (DurationBetween_DaysBetweenUncertainty < 30) is null))
define test_DurationBetween_HoursBetweenUncertainty: TestMessage(DurationBetween_HoursBetweenUncertainty > -1 and (DurationBetween_HoursBetweenUncertainty > 0) is null and DurationBetween_HoursBetweenUncertainty < 721 and (DurationBetween_HoursBetweenUncertainty < 720) is null, 'DurationBetween_HoursBetweenUncertainty', ToString(true), ToString(DurationBetween_HoursBetweenUncertainty > -1 and (DurationBetween_HoursBetweenUncertainty > 0) is null and DurationBetween_HoursBetweenUncertainty < 721 and (DurationBetween_HoursBetweenUncertainty < 720) is null))
define test_DurationBetween_MinutesBetweenUncertainty: TestMessage(DurationBetween_MinutesBetweenUncertainty > -1 and (DurationBetween_MinutesBetweenUncertainty > 0) is null and DurationBetween_MinutesBetweenUncertainty < 43201 and (DurationBetween_MinutesBetweenUncertainty < 43200) is null, 'DurationBetween_MinutesBetweenUncertainty', ToString(true), ToString(DurationBetween_MinutesBetweenUncertainty > -1 and (DurationBetween_MinutesBetweenUncertainty > 0) is null and DurationBetween_MinutesBetweenUncertainty < 43201 and (DurationBetween_MinutesBetweenUncertainty < 43200) is null))
define test_DurationBetween_SecondsBetweenUncertainty: TestMessage(DurationBetween_SecondsBetweenUncertainty > -1 and (DurationBetween_SecondsBetweenUncertainty > 0) is null and DurationBetween_SecondsBetweenUncertainty < 2592001 and (DurationBetween_SecondsBetweenUncertainty < 2592000) is null, 'DurationBetween_SecondsBetweenUncertainty', ToString(true), ToString(DurationBetween_SecondsBetweenUncertainty > -1 and (DurationBetween_SecondsBetweenUncertainty > 0) is null and DurationBetween_SecondsBetweenUncertainty < 2592001 and (DurationBetween_SecondsBetweenUncertainty < 2592000) is null))
define test_DurationBetween_SecondsBetweenReversedUncertainty: TestMessage(DurationBetween_SecondsBetweenReversedUncertainty > -2592001 and (DurationBetween_SecondsBetweenReversedUncertainty > -2592000) is null and DurationBetween_SecondsBetweenReversedUncertainty < 1 and (DurationBetween_SecondsBetweenReversedUncertainty < 0) is null, 'DurationBetween_SecondsBetweenReversedUncertainty', ToString(true), ToString(DurationBetween_SecondsBetweenReversedUncertainty > -2592001 and (DurationBetween_SecondsBetweenReversedUncertainty > -2592000) is null and DurationBetween_SecondsBetweenReversedUncertainty < 1 and (DurationBetween_SecondsBetweenReversedUncertainty < 0) is null))
define test_DurationBetween_HoursBetween1and3CrossingSpringDST: TestMessage(DurationBetween_HoursBetween1and3CrossingSpringDST = 1, 'DurationBetween_HoursBetween1and3CrossingSpringDST', ToString(1), ToString(DurationBetween_HoursBetween1and3CrossingSpringDST))
define test_DurationBetween_HoursBetween1and3CrossingFallDST: TestMessage(DurationBetween_HoursBetween1and3CrossingFallDST = 3, 'DurationBetween_HoursBetween1and3CrossingFallDST', ToString(3), ToString(DurationBetween_HoursBetween1and3CrossingFallDST))


// DateMath
define June15th2013: DateTime(2013, 6, 15, 0, 0, 0, 0)
define DateMath_PlusThreeYears: June15th2013 + 3 years
define DateMath_MinusThreeYears: June15th2013 - 3 years
define DateMath_PlusEightMonths: June15th2013 + 8 months
define DateMath_MinusEightMonths: June15th2013 - 8 months
define DateMath_PlusThreeWeeks: June15th2013 + 3 weeks
define DateMath_MinusThreeWeeks: June15th2013 - 3 weeks
define DateMath_PlusTwentyDays: June15th2013 + 20 days
define DateMath_MinusTwentyDays: June15th2013 - 20 days
define DateMath_PlusThreeHours: June15th2013 + 3 hours
define DateMath_MinusThreeHours: June15th2013 - 3 hours
define DateMath_PlusThreeMinutes: June15th2013 + 3 minutes
define DateMath_MinusThreeMinutes: June15th2013 - 3 minutes
define DateMath_PlusThreeSeconds: June15th2013 + 3 seconds
define DateMath_MinusThreeSeconds: June15th2013 - 3 seconds
define DateMath_PlusThreeMilliseconds: June15th2013 + 3 milliseconds
define DateMath_MinusThreeMilliseconds: June15th2013 - 3 milliseconds

define test_DateMath_PlusThreeYears: TestMessage(DateMath_PlusThreeYears = DateTime(2016, 6, 15, 0, 0, 0, 0), 'DateMath_PlusThreeYears', ToString(DateTime(2016, 6, 15, 0, 0, 0, 0)), ToString(DateMath_PlusThreeYears))
define test_DateMath_MinusThreeYears: TestMessage(DateMath_MinusThreeYears = DateTime(2010, 6, 15, 0, 0, 0, 0), 'DateMath_MinusThreeYears', ToString(DateTime(2010, 6, 15, 0, 0, 0, 0)), ToString(DateMath_MinusThreeYears))
define test_DateMath_PlusEightMonths: TestMessage(DateMath_PlusEightMonths = DateTime(2014, 2, 15, 0, 0, 0, 0), 'DateMath_PlusEightMonths', ToString(DateTime(2014, 2, 15, 0, 0, 0, 0)), ToString(DateMath_PlusEightMonths))
define test_DateMath_MinusEightMonths: TestMessage(DateMath_MinusEightMonths = DateTime(2012, 10, 15, 0, 0, 0, 0), 'DateMath_MinusEightMonths', ToString(DateTime(2012, 10, 15, 0, 0, 0, 0)), ToString(DateMath_MinusEightMonths))
define test_DateMath_PlusThreeWeeks: TestMessage(DateMath_PlusThreeWeeks = DateTime(2013, 7, 6, 0, 0, 0, 0), 'DateMath_PlusThreeWeeks', ToString(DateTime(2013, 7, 6, 0, 0, 0, 0)), ToString(DateMath_PlusThreeWeeks))
define test_DateMath_MinusThreeWeeks: TestMessage(DateMath_MinusThreeWeeks = DateTime(2013, 5, 25, 0, 0, 0, 0), 'DateMath_MinusThreeWeeks', ToString(DateTime(2013, 5, 25, 0, 0, 0, 0)), ToString(DateMath_MinusThreeWeeks))
define test_DateMath_PlusTwentyDays: TestMessage(DateMath_PlusTwentyDays = DateTime(2013, 7, 5, 0, 0, 0, 0), 'DateMath_PlusTwentyDays', ToString(DateTime(2013, 7, 5, 0, 0, 0, 0)), ToString(DateMath_PlusTwentyDays))
define test_DateMath_MinusTwentyDays: TestMessage(DateMath_MinusTwentyDays = DateTime(2013, 5, 26, 0, 0, 0, 0), 'DateMath_MinusTwentyDays', ToString(DateTime(2013, 5, 26, 0, 0, 0, 0)), ToString(DateMath_MinusTwentyDays))
define test_DateMath_PlusThreeHours: TestMessage(DateMath_PlusThreeHours = DateTime(2013, 6, 15, 3, 0, 0, 0), 'DateMath_PlusThreeHours', ToString(DateTime(2013, 6, 15, 3, 0, 0, 0)), ToString(DateMath_PlusThreeHours))
define test_DateMath_MinusThreeHours: TestMessage(DateMath_MinusThreeHours = DateTime(2013, 6, 14, 21, 0, 0, 0), 'DateMath_MinusThreeHours', ToString(DateTime(2013, 6, 14, 21, 0, 0, 0)), ToString(DateMath_MinusThreeHours))
define test_DateMath_PlusThreeMinutes: TestMessage(DateMath_PlusThreeMinutes = DateTime(2013, 6, 15, 0, 3, 0, 0), 'DateMath_PlusThreeMinutes', ToString(DateTime(2013, 6, 15, 0, 3, 0, 0)), ToString(DateMath_PlusThreeMinutes))
define test_DateMath_MinusThreeMinutes: TestMessage(DateMath_MinusThreeMinutes = DateTime(2013, 6, 14, 23, 57, 0, 0), 'DateMath_MinusThreeMinutes', ToString(DateTime(2013, 6, 14, 23, 57, 0, 0)), ToString(DateMath_MinusThreeMinutes))
define test_DateMath_PlusThreeSeconds: TestMessage(DateMath_PlusThreeSeconds = DateTime(2013, 6, 15, 0, 0, 3, 0), 'DateMath_PlusThreeSeconds', ToString(DateTime(2013, 6, 15, 0, 0, 3, 0)), ToString(DateMath_PlusThreeSeconds))
define test_DateMath_MinusThreeSeconds: TestMessage(DateMath_MinusThreeSeconds = DateTime(2013, 6, 14, 23, 59, 57, 0), 'DateMath_MinusThreeSeconds', ToString(DateTime(2013, 6, 14, 23, 59, 57, 0)), ToString(DateMath_MinusThreeSeconds))
define test_DateMath_PlusThreeMilliseconds: TestMessage(DateMath_PlusThreeMilliseconds = DateTime(2013, 6, 15, 0, 0, 0, 3), 'DateMath_PlusThreeMilliseconds', ToString(DateTime(2013, 6, 15, 0, 0, 0, 3)), ToString(DateMath_PlusThreeMilliseconds))
define test_DateMath_MinusThreeMilliseconds: TestMessage(DateMath_MinusThreeMilliseconds = DateTime(2013, 6, 14, 23, 59, 59, 997), 'DateMath_MinusThreeMilliseconds', ToString(DateTime(2013, 6, 14, 23, 59, 59, 997)), ToString(DateMath_MinusThreeMilliseconds))

/*
**********************
  End DateTime Tests
**********************
*/

/*
************************
  Start Interval Tests
************************
*/

// TODO

/*
**********************
  End Interval Tests
**********************
*/

/*
************************
  Start Library Tests
************************
*/

// TODO

/*
**********************
  End Library Tests
**********************
*/

/*
*********************
  Start List Tests
*********************
*/

// TODO

/*
*******************
  End List Tests
*******************
*/

/*
************************
  Start Logical Tests
************************
*/

// TODO

/*
**********************
  End Logical Tests
**********************
*/

/*
**************************
  Start Nullogical Tests
**************************
*/

// TODO

/*
************************
  End Nullogical Tests
************************
*/

/*
*************************
  Start Parameter Tests
*************************
*/

// TODO

/*
***********************
  End Parameter Tests
***********************
*/

/*
************************
  Start Quantity Tests
************************
*/

// TODO

/*
**********************
  End Quantity Tests
**********************
*/

/*
*********************
  Start Query Tests
*********************
*/

// TODO

/*
*******************
  End Query Tests
*******************
*/

/*
**********************
  Start String Tests
**********************
*/

// TODO

/*
********************
  End String Tests
********************
*/
