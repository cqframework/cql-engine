library ConditionalOperator version '1'

context Population

define _0: plan(300)


define x: ok(Equivalent((5),(null)), 'x')
define y: ok(Equivalent((10),(null)), 'y')
define z: ok(Equivalent((null),(null)), 'z')

// if-then-else
define IfTrue1: ok(Equivalent((if y > x then x else y),(null)), 'IfTrue1')
define IfFalse1: ok(Equivalent((if y = x then y + x else y - x),(null)), 'IfFalse1')
define IfNull1: ok(Equivalent((if y = z then x else y),(null)), 'IfNull1')

// standard case
define StandardCase1: ok(Equivalent((case
    when y > x then x
    when x > y then y
    else z
  end),(null)), 'StandardCase1')
define StandardCase2: ok(Equivalent((case
    when x > y then x + y
    when x = y then x
    else y - x
  end),(null)), 'StandardCase2')
define StandardCase3: ok(Equivalent((case
    when z ~ y then z + y
    when z ~ x then x
    else x + y
  end),(null)), 'StandardCase3')

// selected case
define SelectedCase1: ok(Equivalent((case x
    when 5 then 12
    when 10 then y + x
    else y - x
  end),(null)), 'SelectedCase1')
define SelectedCase2: ok(Equivalent((case y
    when 5 then 12
    when 10 then y + x
    else y - x
  end),(null)), 'SelectedCase2')
define SelectedCase3: ok(Equivalent((case y + x
    when 5 then 12
    when 10 then y + x
    else y - x
  end),(null)), 'SelectedCase3')


define function plan(test_count Integer):
    Message(null, true, null as String, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null as String, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
