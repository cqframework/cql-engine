library LogicalOperator version '1'

context Population

define TestPlan: Plan(300)


//And
define TrueAndTrue : Ok(Equivalent(true and true, null), 'TrueAndTrue')
define TrueAndFalse : Ok(Equivalent(true and false, null), 'TrueAndFalse')
define TrueAndNull : Ok(Equivalent(true and null, null), 'TrueAndNull')
define FalseAndTrue : Ok(Equivalent(false and true, null), 'FalseAndTrue')
define FalseAndFalse : Ok(Equivalent(false and false, null), 'FalseAndFalse')
define FalseAndNull : Ok(Equivalent(false and null, null), 'FalseAndNull')
define NullAndTrue : Ok(Equivalent(null and true, null), 'NullAndTrue')
define NullAndFalse : Ok(Equivalent(null and false, null), 'NullAndFalse')
define NullAndNull : Ok(Equivalent(null and null, null), 'NullAndNull')

//Implies
/*define TrueImpliesTrue : true implies true
define TrueImpliesFalse : Ok(Equivalent(true implies false, null), 'TrueImpliesFalse')
define TrueImpliesNull : Ok(Equivalent(true implies null, null), 'TrueImpliesNull')
define FalseImpliesTrue : Ok(Equivalent(false implies true, null), 'FalseImpliesTrue')
define FalseImpliesFalse : Ok(Equivalent(false implies false, null), 'FalseImpliesFalse')
define FalseImpliesNull : Ok(Equivalent(false implies null, null), 'FalseImpliesNull')
define NullImpliesTrue : Ok(Equivalent(null implies true, null), 'NullImpliesTrue')
define NullImpliesFalse : Ok(Equivalent(null implies false, null), 'NullImpliesFalse')
define NullImpliesNull : Ok(Equivalent(null implies null, null), 'NullImpliesNull')*/

//Not
define NotTrue : Ok(Equivalent(not true, null), 'NotTrue')
define NotFalse : Ok(Equivalent(not false, null), 'NotFalse')
define NotNull : Ok(Equivalent(not null, null), 'NotNull')

//Or
define TrueOrTrue : Ok(Equivalent(true or true, null), 'TrueOrTrue')
define TrueOrFalse : Ok(Equivalent(true or false, null), 'TrueOrFalse')
define TrueOrNull : Ok(Equivalent(true or null, null), 'TrueOrNull')
define FalseOrTrue : Ok(Equivalent(false or true, null), 'FalseOrTrue')
define FalseOrFalse : Ok(Equivalent(false or false, null), 'FalseOrFalse')
define FalseOrNull : Ok(Equivalent(false or null, null), 'FalseOrNull')
define NullOrTrue : Ok(Equivalent(null or true, null), 'NullOrTrue')
define NullOrFalse : Ok(Equivalent(null or false, null), 'NullOrFalse')
define NullOrNull : Ok(Equivalent(null or null, null), 'NullOrNull')

//Xor
define TrueXorTrue : Ok(Equivalent(true xor true, null), 'TrueXorTrue')
define TrueXorFalse : Ok(Equivalent(true xor false, null), 'TrueXorFalse')
define TrueXorNull : Ok(Equivalent(true xor null, null), 'TrueXorNull')
define FalseXorTrue : Ok(Equivalent(false xor true, null), 'FalseXorTrue')
define FalseXorFalse : Ok(Equivalent(false xor false, null), 'FalseXorFalse')
define FalseXorNull : Ok(Equivalent(false xor null, null), 'FalseXorNull')
define NullXorTrue : Ok(Equivalent(null xor true, null), 'NullXorTrue')
define NullXorFalse : Ok(Equivalent(null xor false, null), 'NullXorFalse')
define NullXorNull : Ok(Equivalent(null xor null, null), 'NullXorNull')


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
