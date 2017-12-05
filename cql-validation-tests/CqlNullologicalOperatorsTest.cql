library NullologicalOperator version '1'

context Population

define _0: Plan(300)


//Coalesce
define CoalesceANull : Ok(Equivalent((Coalesce('a', null)),(null)), 'CoalesceANull')
define CoalesceNullA : Ok(Equivalent((Coalesce(null, 'a')),(null)), 'CoalesceNullA')
define CoalesceEmptyList : Ok(Equivalent((Coalesce({})),(null)), 'CoalesceEmptyList')
define CoalesceListFirstA : Ok(Equivalent((Coalesce({'a', null, null})),(null)), 'CoalesceListFirstA')
define CoalesceListLastA : Ok(Equivalent((Coalesce({null, null, 'a'})),(null)), 'CoalesceListLastA')
define CoalesceFirstList : Ok(Equivalent((Coalesce({'a'},null, null)),(null)), 'CoalesceFirstList')
define CoalesceLastList : Ok(Equivalent((Coalesce(null, null, {'a'})),(null)), 'CoalesceLastList')
define DateTimeCoalesce: Ok(Equivalent((Coalesce(null, null, DateTime(2012, 5, 18))),(null)), 'DateTimeCoalesce')
define DateTimeListCoalesce: Ok(Equivalent((Coalesce({ null, null, DateTime(2012, 5, 18) })),(null)), 'DateTimeListCoalesce')
define TimeCoalesce: Ok(Equivalent((Coalesce(null, null, @T05:15:33.556)),(null)), 'TimeCoalesce')
define TimeListCoalesce: Ok(Equivalent((Coalesce({ null, null, @T05:15:33.556 })),(null)), 'TimeListCoalesce')

//IsNull
define IsNullTrue                   : Ok(Equivalent((IsNull(null)),(null)), 'IsNullTrue')
define IsNullFalseEmptyString       : Ok(Equivalent((IsNull('')),(null)), 'IsNullFalseEmptyString')
define IsNullAlsoFalseAbcString     : Ok(Equivalent((IsNull('abc')),(null)), 'IsNullAlsoFalseAbcString')
define IsNullAlsoFalseNumber1       : Ok(Equivalent((IsNull(1)),(null)), 'IsNullAlsoFalseNumber1')
define IsNullAlsoFalseNumberZero    : Ok(Equivalent((IsNull(0)),(null)), 'IsNullAlsoFalseNumberZero')

//IsFalse
define IsFalseFalse : Ok(Equivalent((IsFalse(false)),(null)), 'IsFalseFalse')
define IsFalseTrue  : Ok(Equivalent((IsFalse(true)),(null)), 'IsFalseTrue')
define IsFalseNull  : Ok(Equivalent((IsFalse(null)),(null)), 'IsFalseNull')

//IsTrue
define IsTrueTrue   : Ok(Equivalent((IsTrue(true)),(null)), 'IsTrueTrue')
define IsTrueFalse  : Ok(Equivalent((IsTrue(false)),(null)), 'IsTrueFalse')
define IsTrueNull   : Ok(Equivalent((IsTrue(null)),(null)), 'IsTrueNull')


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
