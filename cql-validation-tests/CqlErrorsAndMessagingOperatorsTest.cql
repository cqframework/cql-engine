library ErrorsAndMessaging version '1.0'

context Population

define TestPlan: Message(null, true, null as String, 'Message', '1..4')


define TestMessageInfo: Message(1, true, null as String, 'Message', 'ok - TestMessageInfo - 100: Test Message')
define TestMessageWarn: Message(2, true, null as String, 'Warning', 'ok - TestMessageWarn - 200: You have been warned!')
define TestMessageTrace: Message({3, 4, 5}, true, null as String, 'Trace', 'ok - TestMessageTrace - 300: This is a trace')
define TestMessageErrorExpectToDie: Message(3 + 1, true, null as String, 'Error', 'ok - TestMessageError - 400: This is an error!')
    // Note: The above seems to work as-is for TAP because the message is printed to STDOUT
    // normally even though an exception is thrown, as the first line of the exception.
