library CqlTestSuite version '1'

define function TestMessage(condition Boolean, testName String, expected String, result String):
  if not condition
    then Message('TEST SUITE FAILED', true, 'Test ' + testName + ' failed', 'Error', 'Expected ' + expected + ' Found ' + result)
  else testName + ' PASSED'

/*
*******************
  Aggregate Tests
*******************
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
/*
define not_null: Min({1,2,3,4,5,0})
define has_null: Min({1,null,-1,null,2})
define empty: Min(List<Integer>{})
define not_null_q: Min({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml',0 'ml'})
define has_null_q: Min({1 'ml',null,-1 'ml',null,2 'ml'})
define q_diff_units: Min({1 'ml',2 'dl',3 'l',4 'l',5 'l',0 'ml'})
define q_throw1: Min({1 'ml',2 'm',3 'ml',4 'ml',5 'ml',0 'ml'})
define q_throw2: Min({1 ,2 'ml',3 'ml',4 'ml',5 'ml',0 'ml'})

// Max
define not_null: Max({10,1,2,3,4,5})
define has_null: Max({1,null,null,2})
define not_null_q: Max({10 'ml',1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define has_null_q: Max({1 'ml',null,null,2 'ml'})
define q_diff_units: Max({10 'ml',1 'ml',2 'ml',3 'ml',4 'ml',5 'l'})
define q_throw1: Max({10 'ml',1 'm',2 'ml',3 'ml',4 'ml',5 'ml'})
define q_throw2: Max({10 ,1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})

define empty: Max(List<Integer>{})

// Avg
define not_null: Avg({1,2,3,4,5})
define has_null: Avg({1,null,null,2})
define not_null_q: Avg({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define has_null_q: Avg({1 'ml',null,null,2 'ml'})
define empty: Avg(List<Integer>{})
define q_diff_units: Avg({1 'ml',0.002 'l',0.03 'dl',4 'ml',5 'ml'})
define q_throw1: Avg({1 'ml',0.002 'm',0.03 'dl',4 'ml',5 'ml'})

// Median
define odd: Median({5,1,2,3,4})
define even: Median({5,1,2,3,4,6})
define odd_q: Median({5 'ml',1 'ml',2 'ml',3 'ml',4 'ml'})
define even_q: Median({5 'ml',1 'ml',2 'ml',3 'ml',4 'ml',6 'ml'})
define q_diff_units: Median({5 'ml',0.001 'l',0.02 'dl',3 'ml',4 'ml',6 'ml'})
define q_throw1: Median({5 'ml',0.001 'l',0.22 'dl',3 'm',4 'h',6 'ml'})
define q_throw2: Median({5 ,1 ,2 ,3 ,4 'ml',6 'ml'})

define empty: Median(List<Integer>{})
define has_null: Median({1,null,null,2})
define dup_vals_even: Median({3,1,2,2,2,3,4,5})
define dup_vals_odd:  Median({3,1,2,2,2,3,4,5,6})
define has_null_q: Median({1 'ml',null,null,2 'ml'})
define dup_vals_even_q: Median({3 'ml',1 'ml',2 'ml',2 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define dup_vals_odd_q:  Median({3 'ml',1 'ml',2 'ml',2 'ml',2 'ml',3 'ml',4 'ml',5 'ml',6 'ml'})

// Mode
define not_null: Mode({1,2,2,2,3,4,5})
define has_null: Mode({1,null,null,2,2})
define empty: Mode({})

define bi_modal: Mode({1,2,2,2,3,3,3,4,5})

// Variance
define v: Variance({1,2,3,4,5})
define v_q: Variance({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define q_diff_units: Variance({1.0 'ml',0.002 'l',0.003 'l',0.04 'dl',5.0 'ml'})
define q_throw1: Variance({1.0 'm',2.0 'l',3.0 'h',4.0 'ml',5.0 'ml'})
define q_throw2: Variance({1.0 ,2.0 ,3.0 ,4.0 'ml',5.0 'ml'})

// PopulationVariance
define v: PopulationVariance({1.0,2.0,3.0,4.0,5.0})
define v_q: PopulationVariance({1.0 'ml',2.0 'ml',3.0 'ml',4.0 'ml',5.0 'ml'})
define q_diff_units: PopulationVariance({1.0 'ml',0.002 'l',0.003 'l',0.04 'dl',5.0 'ml'})
define q_throw1: PopulationVariance({1.0 'm',2.0 'l',3.0 'h',4.0 'ml',5.0 'ml'})
define q_throw2: PopulationVariance({1.0 ,2.0 ,3.0 ,4.0 'ml',5.0 'ml'})

// StdDev
define std: StdDev({1,2,3,4,5})
define std_q: StdDev({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define q_diff_units: StdDev({1 'ml', 0.002 'l',3 'ml',4 'ml', 0.05 'dl'})
define sq_throw1: StdDev({1 'ml',2 'ml',3 'ml',4 'ml',5 'm'})
define q_throw2: StdDev({1 ,2 ,3 ,4 'ml',5 })

// PopulationStdDev
define dev: PopulationStdDev({1,2,3,4,5})
define dev_q: PopulationStdDev({1 'ml',2 'ml',3 'ml',4 'ml',5 'ml'})
define q_diff_units: PopulationStdDev({1 'ml', 0.002 'l',3 'ml',4 'ml', 0.05 'dl'})
define q_throw1: PopulationStdDev({1 'ml',2 'ml',3 'ml',4 'ml',5 'm'})
define q_throw2: PopulationStdDev({1 ,2 ,3 ,4 'ml',5 })

// AllTrue
define at: AllTrue({true,true,true,true})
define atwn: AllTrue({true,true,null,null,true,true})

define atf: AllTrue({true,true,true,false})
define atfwn: AllTrue({true,true,null,null,true,false})

// AnyTrue
define at: AnyTrue({true,false,false,true})
define atwn: AnyTrue({true,false,null,null,false,true})

define atf: AnyTrue({false,false,false,false})
define atfwn: AnyTrue({false,false,null,null,false,false})
*/