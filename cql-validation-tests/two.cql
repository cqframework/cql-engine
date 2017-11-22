library CQL_Validation_Tests version '1'

context Population

define _0: plan(1)


define _1: ok(true, 'this should pass')


define private function plan(test_count Integer):
    Message(0, true, '0', 'Message', '1..' + ToString(test_count))

define private function ok(is_ok Boolean, message String):
    Message(0, true, '0', 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
