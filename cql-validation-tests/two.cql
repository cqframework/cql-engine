library CQL_Validation_Tests version '1'

context Population

define _0: plan(1)


define _1: ok(true, 'this should pass')


define function plan(test_count Integer):
    Message(null, true, null as String, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null as String, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
