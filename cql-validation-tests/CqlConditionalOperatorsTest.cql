library ConditionalOperator version '1'

context Population

define TestPlan: Plan(300)


define x: Ok(Equivalent(5, null), 'x')
define y: Ok(Equivalent(10, null), 'y')
define z: Ok(Equivalent(null, null), 'z')

// if-then-else
define IfTrue1: Ok(Equivalent(if y > x then x else y, null), 'IfTrue1')
define IfFalse1: Ok(Equivalent(if y = x then y + x else y - x, null), 'IfFalse1')
define IfNull1: Ok(Equivalent(if y = z then x else y, null), 'IfNull1')

// standard case
define StandardCase1: Ok(Equivalent((case
    when y > x then x
    when x > y then y
    else z
  end), null), 'StandardCase1')
define StandardCase2: Ok(Equivalent((case
    when x > y then x + y
    when x = y then x
    else y - x
  end), null), 'StandardCase2')
define StandardCase3: Ok(Equivalent((case
    when z ~ y then z + y
    when z ~ x then x
    else x + y
  end), null), 'StandardCase3')

// selected case
define SelectedCase1: Ok(Equivalent((case x
    when 5 then 12
    when 10 then y + x
    else y - x
  end), null), 'SelectedCase1')
define SelectedCase2: Ok(Equivalent((case y
    when 5 then 12
    when 10 then y + x
    else y - x
  end), null), 'SelectedCase2')
define SelectedCase3: Ok(Equivalent((case y + x
    when 5 then 12
    when 10 then y + x
    else y - x
  end), null), 'SelectedCase3')


define function Plan(testCount Integer):
    Message(null, true, null as String, 'Message', '1..' + ToString(testCount))

define function Ok(isOk Boolean, message String):
    Message(null, true, null as String, 'Message', (if isOk then 'ok - ' else 'not ok - ') + message)

define function ExpectToDie(livingResult Boolean, message2 String):
    Ok(false, message2 + ' (if you see this, the test actually failed)')

define function AssumePrevDied(message2 String, assumedPrevError String):
    Ok(true, message2 + ' - expected error: ' + assumedPrevError)

define function Todo(isOk2 Boolean, message2 String, whyTodo String):
    Ok(isOk2, message2 + ' # TODO - ' + whyTodo)
    // Note: parser/runtime has a scope-breaking bug manifesting if Todo() arg named "message" also.

define function Skipped(isOk2 Boolean, message2 String, whySkipped String):
    Ok(isOk2, message2 + ' # SKIPPED - ' + whySkipped)
