library LogicalOperator version '1'

context Population

define private ExistenceOfThisEarlierDefineIsWorkaroundForTranslatorDeathWithLaterMessageNullCodeArg: Message(null, true, '', 'Message', '')

define _0: plan(300)


//And
define TrueAndTrue : ok(Equivalent((true and true),(null)), 'TrueAndTrue')
define TrueAndFalse : ok(Equivalent((true and false),(null)), 'TrueAndFalse')
define TrueAndNull : ok(Equivalent((true and null),(null)), 'TrueAndNull')
define FalseAndTrue : ok(Equivalent((false and true),(null)), 'FalseAndTrue')
define FalseAndFalse : ok(Equivalent((false and false),(null)), 'FalseAndFalse')
define FalseAndNull : ok(Equivalent((false and null),(null)), 'FalseAndNull')
define NullAndTrue : ok(Equivalent((null and true),(null)), 'NullAndTrue')
define NullAndFalse : ok(Equivalent((null and false),(null)), 'NullAndFalse')
define NullAndNull : ok(Equivalent((null and null),(null)), 'NullAndNull')

//Implies
/*define TrueImpliesTrue : true implies true
define TrueImpliesFalse : ok(Equivalent((true implies false),(null)), 'TrueImpliesFalse')
define TrueImpliesNull : ok(Equivalent((true implies null),(null)), 'TrueImpliesNull')
define FalseImpliesTrue : ok(Equivalent((false implies true),(null)), 'FalseImpliesTrue')
define FalseImpliesFalse : ok(Equivalent((false implies false),(null)), 'FalseImpliesFalse')
define FalseImpliesNull : ok(Equivalent((false implies null),(null)), 'FalseImpliesNull')
define NullImpliesTrue : ok(Equivalent((null implies true),(null)), 'NullImpliesTrue')
define NullImpliesFalse : ok(Equivalent((null implies false),(null)), 'NullImpliesFalse')
define NullImpliesNull : ok(Equivalent((null implies null),(null)), 'NullImpliesNull')*/

//Not
define NotTrue : ok(Equivalent((not true),(null)), 'NotTrue')
define NotFalse : ok(Equivalent((not false),(null)), 'NotFalse')
define NotNull : ok(Equivalent((not null),(null)), 'NotNull')

//Or
define TrueOrTrue : ok(Equivalent((true or true),(null)), 'TrueOrTrue')
define TrueOrFalse : ok(Equivalent((true or false),(null)), 'TrueOrFalse')
define TrueOrNull : ok(Equivalent((true or null),(null)), 'TrueOrNull')
define FalseOrTrue : ok(Equivalent((false or true),(null)), 'FalseOrTrue')
define FalseOrFalse : ok(Equivalent((false or false),(null)), 'FalseOrFalse')
define FalseOrNull : ok(Equivalent((false or null),(null)), 'FalseOrNull')
define NullOrTrue : ok(Equivalent((null or true),(null)), 'NullOrTrue')
define NullOrFalse : ok(Equivalent((null or false),(null)), 'NullOrFalse')
define NullOrNull : ok(Equivalent((null or null),(null)), 'NullOrNull')

//Xor
define TrueXorTrue : ok(Equivalent((true xor true),(null)), 'TrueXorTrue')
define TrueXorFalse : ok(Equivalent((true xor false),(null)), 'TrueXorFalse')
define TrueXorNull : ok(Equivalent((true xor null),(null)), 'TrueXorNull')
define FalseXorTrue : ok(Equivalent((false xor true),(null)), 'FalseXorTrue')
define FalseXorFalse : ok(Equivalent((false xor false),(null)), 'FalseXorFalse')
define FalseXorNull : ok(Equivalent((false xor null),(null)), 'FalseXorNull')
define NullXorTrue : ok(Equivalent((null xor true),(null)), 'NullXorTrue')
define NullXorFalse : ok(Equivalent((null xor false),(null)), 'NullXorFalse')
define NullXorNull : ok(Equivalent((null xor null),(null)), 'NullXorNull')


define function plan(test_count Integer):
    Message(null, true, null, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
