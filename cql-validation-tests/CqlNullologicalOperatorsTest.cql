library NullologicalOperator version '1'

context Population

define _0: plan(300)


//Coalesce
define CoalesceANull : ok(Equivalent((Coalesce('a', null)),(null)), 'CoalesceANull')
define CoalesceNullA : ok(Equivalent((Coalesce(null, 'a')),(null)), 'CoalesceNullA')
define CoalesceEmptyList : ok(Equivalent((Coalesce({})),(null)), 'CoalesceEmptyList')
define CoalesceListFirstA : ok(Equivalent((Coalesce({'a', null, null})),(null)), 'CoalesceListFirstA')
define CoalesceListLastA : ok(Equivalent((Coalesce({null, null, 'a'})),(null)), 'CoalesceListLastA')
define CoalesceFirstList : ok(Equivalent((Coalesce({'a'},null, null)),(null)), 'CoalesceFirstList')
define CoalesceLastList : ok(Equivalent((Coalesce(null, null, {'a'})),(null)), 'CoalesceLastList')
define DateTimeCoalesce: ok(Equivalent((Coalesce(null, null, DateTime(2012, 5, 18))),(null)), 'DateTimeCoalesce')
define DateTimeListCoalesce: ok(Equivalent((Coalesce({ null, null, DateTime(2012, 5, 18) })),(null)), 'DateTimeListCoalesce')
define TimeCoalesce: ok(Equivalent((Coalesce(null, null, @T05:15:33.556)),(null)), 'TimeCoalesce')
define TimeListCoalesce: ok(Equivalent((Coalesce({ null, null, @T05:15:33.556 })),(null)), 'TimeListCoalesce')

//IsNull
define IsNullTrue                   : ok(Equivalent((IsNull(null)),(null)), 'IsNullTrue')
define IsNullFalseEmptyString       : ok(Equivalent((IsNull('')),(null)), 'IsNullFalseEmptyString')
define IsNullAlsoFalseAbcString     : ok(Equivalent((IsNull('abc')),(null)), 'IsNullAlsoFalseAbcString')
define IsNullAlsoFalseNumber1       : ok(Equivalent((IsNull(1)),(null)), 'IsNullAlsoFalseNumber1')
define IsNullAlsoFalseNumberZero    : ok(Equivalent((IsNull(0)),(null)), 'IsNullAlsoFalseNumberZero')

//IsFalse
define IsFalseFalse : ok(Equivalent((IsFalse(false)),(null)), 'IsFalseFalse')
define IsFalseTrue  : ok(Equivalent((IsFalse(true)),(null)), 'IsFalseTrue')
define IsFalseNull  : ok(Equivalent((IsFalse(null)),(null)), 'IsFalseNull')

//IsTrue
define IsTrueTrue   : ok(Equivalent((IsTrue(true)),(null)), 'IsTrueTrue')
define IsTrueFalse  : ok(Equivalent((IsTrue(false)),(null)), 'IsTrueFalse')
define IsTrueNull   : ok(Equivalent((IsTrue(null)),(null)), 'IsTrueNull')


define function plan(test_count Integer):
    Message(null, true, null as String, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null as String, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
