library ConditionalOperator version '1'

context Population

define TestPlan: Plan(9)


define x: 5
define y: 10
define z: null

// if-then-else
define IfTrue1: Ok(Equivalent(
  if y > x then x else y, 5), 'IfTrue1')

define IfFalse1: Ok(Equivalent(
  if y = x then y + x else y - x, 5), 'IfFalse1')

define IfNull1: Ok(Equivalent(
  if y = z then x else y, 10), 'IfNull1')

// standard case
define StandardCase1: Ok(Equivalent(
  (case
    when y > x then x
    when x > y then y
    else z
  end), 5), 'StandardCase1')

define StandardCase2: Ok(Equivalent(
  (case
    when x > y then x + y
    when x = y then x
    else y - x
  end), 5), 'StandardCase2')

define StandardCase3: Ok(Equivalent(
  (case
    when z ~ y then z + y
    when z ~ x then x
    else x + y
  end), 15), 'StandardCase3')

// selected case
define SelectedCase1: Ok(Equivalent(
  (case x
    when 5 then 12
    when 10 then y + x
    else y - x
  end), 12), 'SelectedCase1')

define SelectedCase2: Ok(Equivalent(
  (case y
    when 5 then 12
    when 10 then y + x
    else y - x
  end), 15), 'SelectedCase2')

define SelectedCase3: Ok(Equivalent(
  (case y + x
    when 5 then 12
    when 10 then y + x
    else y - x
  end), 5), 'SelectedCase3')


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
