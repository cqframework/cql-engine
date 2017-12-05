library ErrorsAndMessaging version '1.0'

context Population

define _0: Plan(300)


define TestMessageInfo: Ok(Equivalent(Message(1, true, '100', 'Message', 'Test Message'), null), 'TestMessageInfo')
define TestMessageWarn: Ok(Equivalent(Message(2, true, '200', 'Warning', 'You have been warned!'), null), 'TestMessageWarn')
define TestMessageTrace: Ok(Equivalent(Message({3, 4, 5}, true, '300', 'Trace', 'This is a trace'), null), 'TestMessageTrace')
define TestMessageError: Ok(Equivalent(Message(3 + 1, true, '400', 'Error', 'This is an error!'), null), 'TestMessageError')


define function Plan(testCount Integer):
    Message(null, true, null as String, 'Message', '1..' + ToString(testCount))

define function Ok(isOk Boolean, message String):
    Message(null, true, null as String, 'Message', (if isOk then 'ok - ' else 'not ok - ') + message)

define function Todo(isOk2 Boolean, message2 String, whyTodo String):
    Ok(isOk2, message2 + ' # TODO - ' + whyTodo)
    // Note: parser/runtime has a scope-breaking bug manifesting if Todo() arg named "message" also.

define function Skipped(isOk2 Boolean, message2 String, whySkipped String):
    Ok(isOk2, message2 + ' # SKIPPED - ' + whySkipped)
    // Note: parser/runtime has a scope-breaking bug manifesting if Skipped() arg named "message" also.
