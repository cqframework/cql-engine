library ErrorsAndMessaging version '1.0'

context Population

define private ExistenceOfThisEarlierDefineIsWorkaroundForTranslatorDeathWithLaterMessageNullCodeArg: Message(null, true, '', 'Message', '')

define _0: plan(300)


define TestMessageInfo: ok(Equivalent((Message(1, true, '100', 'Message', 'Test Message')),(null)), 'TestMessageInfo')
define TestMessageWarn: ok(Equivalent((Message(2, true, '200', 'Warning', 'You have been warned!')),(null)), 'TestMessageWarn')
define TestMessageTrace: ok(Equivalent((Message({3, 4, 5}, true, '300', 'Trace', 'This is a trace')),(null)), 'TestMessageTrace')
define TestMessageError: ok(Equivalent((Message(3 + 1, true, '400', 'Error', 'This is an error!')),(null)), 'TestMessageError')


define function plan(test_count Integer):
    Message(null, true, null, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
