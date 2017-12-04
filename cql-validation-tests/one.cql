library CQL_Validation_Tests version '1'

context Population

define private ExistenceOfThisEarlierDefineIsWorkaroundForTranslatorDeathWithLaterMessageNullCodeArg: Message(null, true, '', 'Message', '')

define _0: plan(3)


define _1: ok((2+2) = 4, '2+2 = 4')

define _2: ok(false = false, 'false = false')

define _3: ok(start of Interval[5,7] = 5, 'start of interval 5..7 is 5')


define function plan(test_count Integer):
    Message(null, true, null, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
