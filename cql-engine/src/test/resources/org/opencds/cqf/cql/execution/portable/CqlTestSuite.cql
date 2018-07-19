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