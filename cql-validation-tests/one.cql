library CQL_Validation_Tests version '1'

context Population

define _0: plan(3)


define _1: ok((2+2) = 4, '2+2 = 4')

define _2: ok(false = false, 'false = false')

define _3: ok(start of Interval[5,7] = 5, 'start of interval 5..7 is 5')


define private function plan(test_count Integer):
    Message(0, true, '0', 'Message', '1..' + ToString(test_count))

define private function ok(is_ok Boolean, message String):
    Message(0, true, '0', 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
