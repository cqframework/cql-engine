library ErrorsAndMessaging version '1.0'

context Population

define TestPlan: Message(null, true, null as String, 'Message', '1..4')


//define TestMessageInfo: Message(1, true, '100', 'Message', 'ok - TestMessageInfo - Test Message')
define TestMessageInfo: Message(1, true, null as String, 'Message', 'ok - TestMessageInfo - 100: Test Message')
//define TestMessageWarn: Message(2, true, '200', 'Warning', 'ok - TestMessageWarn - You have been warned!')
define TestMessageWarn: Message(2, true, null as String, 'Warning', 'ok - TestMessageWarn - 200: You have been warned!')
//define TestMessageTrace: Message({3, 4, 5}, true, '300', 'Trace', 'ok - TestMessageTrace - This is a trace')
define TestMessageTrace: Message('', true, null as String, 'Trace', 'ok - TestMessageTrace - 300: This is a trace')
//define TestMessageError: Message(3 + 1, true, '400', 'Error', 'ok - TestMessageError - This is an error!')
define TestMessageError: Message(3 + 1, true, null as String, 'Message', 'ok - TestMessageError - 400: This is an error! # SKIPPED - can not trap expected error')
