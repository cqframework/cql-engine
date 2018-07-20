library CqlTestSuite version '1'

/*
    NOTES:
    This test suite is meant to serve as a comprehensive, portable test suite for any implementation of a CQL execution engine.

    There are 3 components of this suite (of which this file is one):
      1. Type/value tests
      2. Valid operator tests (this file)
      3. Invalid operator tests (all these tests result in a runtime error)

    This suite is an extension of the Bonnie tests, which can be found here:
       https://github.com/cqframework/cql-execution/tree/master/test/elm
*/

define function TestMessage(condition Boolean, testName String, expected String, result String):
  if not condition
    then Message('TEST SUITE FAILED', true, 'Test ' + testName + ' failed', 'Error', 'Expected ' + expected + ' Found ' + result)
  else testName + ' TEST PASSED'

/*
*************************
  Start Aggregate Tests
*************************
*/

// Count
define count_not_null: Count({1,2,3,4,5})
define count_has_null: Count({1,null,null,null,2})
define count_empty: Count({})

define test_count_not_null: TestMessage(count_not_null = 5, 'count_not_null', ToString(5), ToString(count_not_null))
define test_count_has_null: TestMessage(count_has_null = 2, 'count_has_null', ToString(2), ToString(count_not_null))
define test_count_empty: TestMessage(count_empty = 0, 'count_empty', ToString(0), ToString(count_empty))

// Sum
define sum_not_null: Sum({1,2,3,4,5})
define sum_has_null: Sum({1,null,null,null,2})
define sum_not_null_q: Sum({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define sum_has_null_q: Sum({1 'ml',null,null,null,2 'ml'})
define sum_unmatched_units_q: Sum({1 'ml',2 'L',3 'ml',4 'ml',5 'ml',0 'ml'})
define sum_empty: Sum(List<Integer>{})
define sum_q_diff_units: Sum({1 'ml',0.002 'l',0.03 'dl',4 'ml',0.005 'l'})

define test_sum_not_null: TestMessage(sum_not_null = 15, 'sum_not_null', ToString(15), ToString(sum_not_null))
define test_sum_has_null: TestMessage(sum_has_null = 3, 'sum_has_null', ToString(3), ToString(sum_has_null))
define test_sum_not_null_q: TestMessage(sum_not_null_q = 15'ml', 'sum_not_null_q', ToString(15'ml'), ToString(sum_not_null_q))
define test_sum_has_null_q: TestMessage(sum_has_null_q = 3'ml', 'sum_has_null_q', ToString(3'ml'), ToString(sum_has_null_q))
// TODO - this behavior has yet to be implemented for Quantity
//define test_sum_unmatched_units_q: TestMessage(sum_unmatched_units_q is null, 'sum_unmatched_units_q', 'null', ToString(sum_unmatched_units_q))
define test_sum_empty: TestMessage(sum_empty is null, 'sum_empty', 'null', ToString(sum_empty))
// TODO - this behavior has yet to be implemented for Quantity
// define test_sum_q_diff_units: TestMessage(sum_q_diff_units = 15'ml', 'sum_q_diff_units', ToString(15'ml'), ToString(sum_q_diff_units))

// Min
define min_not_null: Min({1,2,3,4,5,0})
define min_has_null: Min({1,null,-1,null,2})
define min_empty: Min(List<Integer>{})
define min_not_null_q: Min({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml',0 'ml'})
define min_has_null_q: Min({1 'ml',null,-1 'ml',null,2 'ml'})
define min_q_diff_units: Min({1 'ml',2 'dl',3 'l',4 'l',5 'l',0 'ml'})
// TODO - move to error suite and implement for Quantity
// define q_throw1: Min({1 'ml',2 'm',3 'ml',4 'ml',5 'ml',0 'ml'})
// define q_throw2: Min({1 ,2 'ml',3 'ml',4 'ml',5 'ml',0 'ml'})

define test_min_not_null: TestMessage(min_not_null = 0, 'min_not_null', ToString(0), ToString(min_not_null))
define test_min_has_null: TestMessage(min_has_null = -1, 'min_has_null', ToString(-1), ToString(min_has_null))
define test_min_empty: TestMessage(min_empty is null, 'min_empty', 'null', ToString(min_empty))
define test_min_not_null_q: TestMessage(min_not_null_q = 0 'ml', 'min_not_null_q', ToString(0 'ml'), ToString(min_not_null_q))
define test_min_has_null_q: TestMessage(min_has_null_q = -1 'ml', 'min_has_null_q', ToString(-1 'ml'), ToString(min_has_null_q))
// TODO - this behavior has yet to be implemented for Quantity - this test will succeed by accident (add better)
// define test_min_q_diff_units: TestMessage(min_q_diff_units = 0 'ml', 'min_q_diff_units', ToString(0 'ml'), ToString(min_q_diff_units))

// Max
define max_not_null: Max({10,1,2,3,4,5})
define max_has_null: Max({1,null,null,2})
define max_empty: Max(List<Integer>{})
define max_not_null_q: Max({10 'ml',1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define max_has_null_q: Max({1 'ml',null,null,2 'ml'})
define max_q_diff_units: Max({10 'ml',1 'ml',2 'ml',3 'ml',4 'ml',5 'l'})
// TODO - move to error suite and implement for Quantity
// define max_q_throw1: Max({10 'ml',1 'm',2 'ml',3 'ml',4 'ml',5 'ml'})
// define max_q_throw2: Max({10 ,1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})

define test_max_not_null: TestMessage(max_not_null = 10, 'max_not_null', ToString(10), ToString(max_not_null))
define test_max_has_null: TestMessage(max_has_null = 2, 'max_has_null', ToString(2), ToString(max_has_null))
define test_max_empty: TestMessage(max_empty is null, 'max_empty', 'null', ToString(max_empty))
define test_max_not_null_q: TestMessage(max_not_null_q = 10 'ml', 'max_not_null_q', ToString(10 'ml'), ToString(max_not_null_q))
define test_max_has_null_q: TestMessage(max_has_null_q = 2 'ml', 'max_has_null_q', ToString(2 'ml'), ToString(max_has_null_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_max_q_diff_units: TestMessage(max_q_diff_units = 5000 'ml', 'max_q_diff_units', ToString(5000 'ml'), ToString(max_q_diff_units))

// Avg
define avg_not_null: Avg({1,2,3,4,5})
define avg_has_null: Avg({1,null,null,2})
define avg_empty: Avg(List<Integer>{})
define avg_not_null_q: Avg({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define avg_has_null_q: Avg({1 'ml',null,null,2 'ml'})
define avg_q_diff_units: Avg({1 'ml',0.002 'l',0.03 'dl',4 'ml',5 'ml'})
// TODO - move to error suite and implement for Quantity
// define avg_q_throw1: Avg({1 'ml',0.002 'm',0.03 'dl',4 'ml',5 'ml'})

define test_avg_not_null: TestMessage(avg_not_null = 3.0, 'avg_not_null', ToString(3.0), ToString(avg_not_null))
define test_avg_has_null: TestMessage(avg_has_null = 1.5, 'avg_has_null', ToString(1.5), ToString(avg_has_null))
define test_avg_empty: TestMessage(avg_empty is null, 'avg_empty', 'null', ToString(avg_empty))
define test_avg_not_null_q: TestMessage(avg_not_null_q = 3.0 'ml', 'avg_not_null_q', ToString(3.0 'ml'), ToString(avg_not_null_q))
define test_avg_has_null_q: TestMessage(avg_has_null_q = 1.5 'ml', 'avg_has_null_q', ToString(1.5 'ml'), ToString(avg_has_null_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_avg_q_diff_units: TestMessage(avg_q_diff_units = 3.0 'ml', 'avg_q_diff_units', ToString(3.0 'ml'), ToString(avg_q_diff_units))

// Median
define median_odd: Median({5,1,2,3,4})
define median_even: Median({5,1,2,3,4,6})
define median_odd_q: Median({5 'ml',1 'ml',2 'ml',3 'ml',4 'ml'})
define median_even_q: Median({5 'ml',1 'ml',2 'ml',3 'ml',4 'ml',6 'ml'})
define median_empty: Median(List<Integer>{})
define median_has_null: Median({1,null,null,2})
define median_dup_vals_even: Median({3,1,2,2,2,3,4,5})
define median_dup_vals_odd:  Median({3,1,2,2,2,3,4,5,6})
define median_has_null_q: Median({1 'ml',null,null,2 'ml'})
define median_dup_vals_even_q: Median({3 'ml',1 'ml',2 'ml',2 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define median_dup_vals_odd_q:  Median({3 'ml',1 'ml',2 'ml',2 'ml',2 'ml',3 'ml',4 'ml',5 'ml',6 'ml'})
define median_q_diff_units: Median({5 'ml',0.001 'l',0.02 'dl',3 'ml',4 'ml',6 'ml'})
// TODO - move to error suite and implement for Quantity
// define median_q_throw1: Median({5 'ml',0.001 'l',0.22 'dl',3 'm',4 'h',6 'ml'})
// define median_q_throw2: Median({5 ,1 ,2 ,3 ,4 'ml',6 'ml'})

define test_median_odd: TestMessage(median_odd = 3, 'median_odd', ToString(3), ToString(median_odd))
define test_median_even: TestMessage(median_even = 3.5, 'median_even', ToString(3.5), ToString(median_even))
define test_median_odd_q: TestMessage(median_odd_q = 3 'ml', 'median_odd_q', ToString(3 'ml'), ToString(median_odd_q))
define test_median_even_q: TestMessage(median_even_q = 3.5 'ml', 'median_even_q', ToString(3.5 'ml'), ToString(median_even_q))
define test_median_empty: TestMessage(median_empty is null, 'median_empty', 'null', ToString(median_empty))
define test_median_has_null: TestMessage(median_has_null = 1.5, 'median_has_null', ToString(1.5), ToString(median_has_null))
define test_median_dup_vals_even: TestMessage(median_dup_vals_even = 2.5, 'median_dup_vals_even', ToString(2.5), ToString(median_dup_vals_even))
define test_median_dup_vals_odd: TestMessage(median_dup_vals_odd = 3, 'median_dup_vals_odd', ToString(3), ToString(median_dup_vals_odd))
define test_median_has_null_q: TestMessage(median_has_null_q = 1.5 'ml', 'median_has_null_q', ToString(1.5 'ml'), ToString(median_has_null_q))
define test_median_dup_vals_even_q: TestMessage(median_dup_vals_even_q = 2.5 'ml', 'median_dup_vals_even_q', ToString(2.5 'ml'), ToString(median_dup_vals_even_q))
define test_median_dup_vals_odd_q: TestMessage(median_dup_vals_odd_q = 3 'ml', 'median_dup_vals_odd_q', ToString(3 'ml'), ToString(median_dup_vals_odd_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_median_q_diff_units: TestMessage(median_q_diff_units = 3.5 'ml', 'median_q_diff_units', ToString(3.5 'ml'), ToString(median_q_diff_units))

// Mode
define mode_not_null: Mode({1,2,2,2,3,4,5})
define mode_has_null: Mode({1,null,null,2,2})
define mode_empty: Mode(List<Integer>{})
define mode_bi_modal: Mode({1,2,2,2,3,3,3,4,5})

define test_mode_not_null: TestMessage(mode_not_null = 2, 'mode_not_null', ToString(2), ToString(mode_not_null))
define test_mode_has_null: TestMessage(mode_has_null = 2, 'mode_has_null', ToString(2), ToString(mode_has_null))
define test_mode_empty: TestMessage(mode_empty is null, 'mode_empty', 'null', ToString(mode_empty))
// TODO - CQL doesn't support multimodal operations
// define test_mode_bi_modal: TestMessage(mode_bi_modal = { 2, 3 }, 'mode_bi_modal', '{ 2, 3 }', ToString(mode_bi_modal))

// Variance
define variance_v: Variance({1,2,3,4,5})
define variance_v_q: Variance({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define variance_q_diff_units: Variance({1.0 'ml',0.002 'l',0.003 'l',0.04 'dl',5.0 'ml'})
// TODO - move to error suite and implement for Quantity
// define variance_q_throw1: Variance({1.0 'm',2.0 'l',3.0 'h',4.0 'ml',5.0 'ml'})
// define variance_q_throw2: Variance({1.0 ,2.0 ,3.0 ,4.0 'ml',5.0 'ml'})

define test_variance_v: TestMessage(variance_v = 2.5, 'variance_v', ToString(2.5), ToString(variance_v))
define test_variance_v_q: TestMessage(variance_v_q = 2.5 'ml', 'variance_v_q', ToString(2.5 'ml'), ToString(variance_v_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_variance_q_diff_units: TestMessage(variance_q_diff_units = 2.5 'ml', 'variance_q_diff_units', ToString(2.5 'ml'), ToString(variance_q_diff_units))

// PopulationVariance
define pop_variance_v: PopulationVariance({1.0,2.0,3.0,4.0,5.0})
define pop_variance_v_q: PopulationVariance({1.0 'ml',2.0 'ml',3.0 'ml',4.0 'ml',5.0 'ml'})
define pop_variance_q_diff_units: PopulationVariance({1.0 'ml',0.002 'l',0.003 'l',0.04 'dl',5.0 'ml'})
// TODO - move to error suite and implement for Quantity
// define pop_variance_q_throw1: PopulationVariance({1.0 'm',2.0 'l',3.0 'h',4.0 'ml',5.0 'ml'})
// define pop_variance_q_throw2: PopulationVariance({1.0 ,2.0 ,3.0 ,4.0 'ml',5.0 'ml'})

define test_pop_variance_v: TestMessage(pop_variance_v = 2.0, 'pop_variance_v', ToString(2.0), ToString(pop_variance_v))
define test_pop_variance_v_q: TestMessage(pop_variance_v_q = 2.0 'ml', 'pop_variance_v_q', ToString(2.0 'ml'), ToString(pop_variance_v_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_pop_variance_q_diff_units: TestMessage(pop_variance_q_diff_units = 2.0 'ml', 'pop_variance_q_diff_units', ToString(2.0 'ml'), ToString(pop_variance_q_diff_units))

// StdDev
define std: StdDev({1,2,3,4,5})
define std_q: StdDev({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define std_q_diff_units: StdDev({1 'ml', 0.002 'l',3 'ml',4 'ml', 0.05 'dl'})
// TODO - move to error suite and implement for Quantity
// define std_sq_throw1: StdDev({1 'ml',2 'ml',3 'ml',4 'ml',5 'm'})
// define std_q_throw2: StdDev({1 ,2 ,3 ,4 'ml',5 })

define test_std: TestMessage(std = 1.58113883, 'std', ToString(1.58113883), ToString(std))
define test_std_q: TestMessage(std_q = 1.58113883 'ml', 'std_q', ToString(1.58113883 'ml'), ToString(std_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_std_q_diff_units: TestMessage(std_q_diff_units = 1.58113883 'ml', 'std_q_diff_units', ToString(1.58113883 'ml'), ToString(std_q_diff_units))

// PopulationStdDev
define pop_std: PopulationStdDev({1,2,3,4,5})
define pop_std_q: PopulationStdDev({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define pop_std_q_diff_units: PopulationStdDev({1 'ml', 0.002 'l',3 'ml',4 'ml', 0.05 'dl'})
// TODO - move to error suite and implement for Quantity
// define pop_std_q_throw1: PopulationStdDev({1 'ml',2 'ml',3 'ml',4 'ml',5 'm'})
// define pop_std_q_throw2: PopulationStdDev({1 ,2 ,3 ,4 'ml',5 })

define test_pop_std: TestMessage(pop_std = 1.41421356, 'pop_std', ToString(1.41421356), ToString(pop_std))
define test_pop_std_q: TestMessage(pop_std_q = 1.41421356 'ml', 'pop_std_q', ToString(1.41421356 'ml'), ToString(pop_std_q))
// TODO - this behavior has yet to be implemented for Quantity
// define test_pop_std_q_diff_units: TestMessage(pop_std_q_diff_units = 1.41421356 'ml', 'pop_std_q_diff_units', ToString(1.41421356 'ml'), ToString(pop_std_q_diff_units))


// AllTrue
define all_true: AllTrue({true,true,true,true})
define all_true_wn: AllTrue({true,true,null,null,true,true})
define all_true_f: AllTrue({true,true,true,false})
define all_true_fwn: AllTrue({true,true,null,null,true,false})

define test_all_true: TestMessage(all_true, 'all_true', ToString(true), ToString(all_true))
define test_all_true_wn: TestMessage(all_true_wn, 'all_true_wn', ToString(true), ToString(all_true_wn))
define test_all_true_f: TestMessage(not all_true_f, 'all_true_f', ToString(false), ToString(all_true_f))
define test_all_true_fwn: TestMessage(not all_true_fwn, 'all_true_fwn', ToString(false), ToString(all_true_fwn))

// AnyTrue
define any_true: AnyTrue({true,false,false,true})
define any_true_wn: AnyTrue({true,false,null,null,false,true})
define any_true_f: AnyTrue({false,false,false,false})
define any_true_fwn: AnyTrue({false,false,null,null,false,false})

define test_any_true: TestMessage(any_true, 'any_true', ToString(true), ToString(any_true))
define test_any_true_wn: TestMessage(any_true_wn, 'any_true_wn', ToString(true), ToString(any_true_wn))
define test_any_true_f: TestMessage(not any_true_f, 'any_true_f', ToString(false), ToString(any_true_f))
define test_any_true_fwn: TestMessage(not any_true_fwn, 'any_true_fwn', ToString(false), ToString(any_true_fwn))

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
define Mixed: 1 + 5 * 10 - 15 / 3
define Parenthetical: (1 + 5) * (10 - 15) / 3

define test_Mixed: TestMessage(Mixed = 46, 'Mixed', ToString(46), ToString(Mixed))
define test_Parenthetical: TestMessage(Parenthetical = -10, 'Parenthetical', ToString(-10), ToString(Parenthetical))

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
define Floor: Floor(10.1)
define FloorEven: Floor(10)

define test_Floor: TestMessage(Floor = 10, 'Floor', ToString(10), ToString(Floor))
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
define ln: Ln(4)

define test_ln: TestMessage(ln = 1.38629436, 'ln', ToString(1.38629436), ToString(ln))

// Log
define log: Log(10000,10)

define test_log: TestMessage(log = 4, 'log', ToString(4), ToString(log))

// Successor
define successor_Is: successor of 2
define successor_Rs: successor of 2.2
define successor_y_date: successor of DateTime(2015)
define successor_ym_date: successor of DateTime(2015,01)
define successor_ymd_date: successor of DateTime(2015,01,01)
define successor_ymdh_date: successor of DateTime(2015,01,01,0)
define successor_ymdhm_date: successor of DateTime(2015,01,01,0,0)
define successor_ymdhms_date: successor of DateTime(2015,01,01,0,0,0)
define successor_ymdhmsm_date: successor of  DateTime(2015,01,01,0,0,0,0)
// TODO - add to error tests
// define successor_max_date: successor of DateTime(9999,12,31,23,59,59,999)
// define successor_ofr: successor of 2147483647

define test_successor_Is: TestMessage(successor_Is = 3, 'successor_Is', ToString(3), ToString(successor_Is))
define test_successor_Rs: TestMessage(successor_Rs = 2.20000001, 'successor_Rs', ToString(2.20000001), ToString(successor_Rs))
define test_successor_y_date: TestMessage(successor_y_date = DateTime(2016), 'successor_y_date', ToString(DateTime(2016)), ToString(successor_y_date))
define test_successor_ym_date: TestMessage(successor_ym_date = DateTime(2015, 2), 'successor_ym_date', ToString(DateTime(2015, 2)), ToString(successor_ym_date))
define test_successor_ymd_date: TestMessage(successor_ymd_date = DateTime(2015, 1, 2), 'successor_ymd_date', ToString(DateTime(2015, 1, 2)), ToString(successor_ymd_date))
define test_successor_ymdh_date: TestMessage(successor_ymdh_date = DateTime(2015, 1, 1, 1), 'successor_ymdh_date', ToString(DateTime(2015, 1, 1, 1)), ToString(successor_ymdh_date))
define test_successor_ymdhm_date: TestMessage(successor_ymdhm_date = DateTime(2015, 1, 1, 0, 1), 'successor_ymdhm_date', ToString(DateTime(2015, 1, 1, 0, 1)), ToString(successor_ymdhm_date))
define test_successor_ymdhms_date: TestMessage(successor_ymdhms_date = DateTime(2015, 1, 1, 0, 0, 1), 'successor_ymdhms_date', ToString(DateTime(2015, 1, 1, 0, 0, 1)), ToString(successor_ymdhms_date))
define test_successor_ymdhmsm_date: TestMessage(successor_ymdhmsm_date = DateTime(2015, 1, 1, 0, 0, 0, 1), 'successor_ymdhmsm_date', ToString(DateTime(2015, 1, 1, 0, 0, 0, 1)), ToString(successor_ymdhmsm_date))

// Predecessor
define predecessor_Is: predecessor of 2
define predecessor_Rs: predecessor of 2.2
define predecessor_y_date: predecessor of DateTime(2015)
define predecessor_ym_date: predecessor of DateTime(2015,01)
define predecessor_ymd_date: predecessor of DateTime(2015,01,01)
define predecessor_ymdh_date: predecessor of DateTime(2015,01,01,0)
define predecessor_ymdhm_date: predecessor of DateTime(2015,01,01,0,0)
define predecessor_ymdhms_date: predecessor of DateTime(2015,01,01,0,0,0)
define predecessor_ymdhmsm_date: predecessor of DateTime(2015,01,01,0,0,0,0)
// TODO - add to error tests
// define predecessor_min_date: predecessor of DateTime(0001,01,01,0,0,0,0)
// define predecessor_ufr: predecessor of -2147483648

define test_predecessor_Is: TestMessage(predecessor_Is = 1, 'predecessor_Is', ToString(1), ToString(predecessor_Is))
define test_predecessor_Rs: TestMessage(predecessor_Rs = 2.19999999, 'predecessor_Rs', ToString(2.19999999), ToString(predecessor_Rs))
define test_predecessor_y_date: TestMessage(predecessor_y_date = DateTime(2014), 'predecessor_y_date', ToString(DateTime(2014)), ToString(predecessor_y_date))
define test_predecessor_ym_date: TestMessage(predecessor_ym_date = DateTime(2014, 12), 'predecessor_ym_date', ToString(DateTime(2014, 12)), ToString(predecessor_ym_date))
define test_predecessor_ymd_date: TestMessage(predecessor_ymd_date = DateTime(2014, 12, 31), 'predecessor_ymd_date', ToString(DateTime(2014, 12, 31)), ToString(predecessor_ymd_date))
define test_predecessor_ymdh_date: TestMessage(predecessor_ymdh_date = DateTime(2014, 12, 31, 23), 'predecessor_ymdh_date', ToString(DateTime(2014, 12, 31, 23)), ToString(predecessor_ymdh_date))
define test_predecessor_ymdhm_date: TestMessage(predecessor_ymdhm_date = DateTime(2014, 12, 31, 23, 59), 'predecessor_ymdhm_date', ToString(DateTime(2014, 12, 31, 23, 59)), ToString(predecessor_ymdhm_date))
define test_predecessor_ymdhms_date: TestMessage(predecessor_ymdhms_date = DateTime(2014, 12, 31, 23, 59, 59), 'predecessor_ymdhms_date', ToString(DateTime(2014, 12, 31, 23, 59, 59)), ToString(predecessor_ymdhms_date))
define test_predecessor_ymdhmsm_date: TestMessage(predecessor_ymdhmsm_date = DateTime(2014, 12, 31, 23, 59, 59, 999), 'predecessor_ymdhmsm_date', ToString(DateTime(2014, 12, 31, 23, 59, 59, 999)), ToString(predecessor_ymdhmsm_date))

// Quantity
define quantity_days_10: 10 days
define quantity_QL10Days: Quantity{value: 10, unit: 'days'}
define quantity_QL10Min : Quantity{value: 10, unit: 'min' }
define quantity_Jan1_2000: DateTime(2000, 1, 1)

define quantity_add_q_q : quantity_days_10 + quantity_QL10Days
define quantity_add_d_q : quantity_Jan1_2000 + quantity_days_10
define quantity_sub_q_q : quantity_days_10 - quantity_QL10Days
define quantity_sub_d_q : quantity_Jan1_2000 - quantity_days_10
// TODO - implement for Quantity
// define quantity_add_q_q_diff : quantity_QL10Days + quantity_QL10Min
// define quantity_sub_q_q_diff : quantity_QL10Days - quantity_QL10Min
// define quantity_div_q_q : quantity_days_10 / QL10Days
// define quantity_mul_q_q : 2 'm' * 10 'm'
// define quantity_mul_q_q_diff : 2 'm' * 10 '/d'
// define quantity_MultiplyUcum: (5 'm' * 25 'km') = 125000 'm2'
// define quantity_DivideUcum: (20 'm2' / 5 'm') = 4 'm'
// define quantity_AddUcum: (5 'm' + 5 'km') = 5005 'm'
// define quantity_SubtractUcum: (25 'km' - 5 'm') = 24995 'm'
define quantity_div_q_d : quantity_days_10 / 2
define quantity_mul_q_d : quantity_days_10 * 2
define quantity_mul_d_q : 2 * quantity_QL10Days
define quantity_neg : - quantity_days_10
define quantity_abs : Abs(quantity_neg)

define test_quantity_add_q_q: TestMessage(quantity_add_q_q = 20 days, 'quantity_add_q_q', ToString(20 days), ToString(quantity_add_q_q))
define test_quantity_add_d_q: TestMessage(quantity_add_d_q = DateTime(2000, 1, 11), 'quantity_add_d_q', ToString(DateTime(2000, 1, 11)), ToString(quantity_add_d_q))
define test_quantity_sub_q_q: TestMessage(quantity_sub_q_q = 0 days, 'quantity_sub_q_q', ToString(0 days), ToString(quantity_sub_q_q))
define test_quantity_sub_d_q: TestMessage(quantity_sub_d_q = DateTime(1999, 12, 22), 'quantity_sub_d_q', ToString(DateTime(1999, 12, 22)), ToString(quantity_sub_d_q))
define test_quantity_div_q_d: TestMessage(quantity_div_q_d = 5 days, 'quantity_div_q_d', ToString(5 days), ToString(quantity_div_q_d))
define test_quantity_mul_q_d: TestMessage(quantity_mul_q_d = 20 days, 'quantity_mul_q_d', ToString(20 days), ToString(quantity_mul_q_d))
define test_quantity_mul_d_q: TestMessage(quantity_mul_d_q = 20 days, 'quantity_mul_d_q', ToString(20 days), ToString(quantity_mul_d_q))
define test_quantity_neg: TestMessage(quantity_neg = -10 days, 'quantity_neg', ToString(-10 days), ToString(quantity_neg))
define test_quantity_abs: TestMessage(quantity_abs = 10 days, 'quantity_abs', ToString(10 days), ToString(quantity_abs))

/*
************************
  End Arithmetic Tests
************************
*/