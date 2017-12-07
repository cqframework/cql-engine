library LogicalOperator version '1'

context Population

define TestPlan: Plan(39)


//And
define TrueAndTrue : Ok(Equivalent(true and true, true), 'TrueAndTrue')
define TrueAndFalse : Ok(Equivalent(true and false, false), 'TrueAndFalse')
define TrueAndNull : Ok(Equivalent(true and null, null), 'TrueAndNull')
define FalseAndTrue : Ok(Equivalent(false and true, false), 'FalseAndTrue')
define FalseAndFalse : Ok(Equivalent(false and false, false), 'FalseAndFalse')
define FalseAndNull : Ok(Equivalent(false and null, false), 'FalseAndNull')
define NullAndTrue : Ok(Equivalent(null and true, null), 'NullAndTrue')
define NullAndFalse : Ok(Equivalent(null and false, false), 'NullAndFalse')
define NullAndNull : Ok(Equivalent(null and null, null), 'NullAndNull')

//Implies
//define FalseImpliesFalse : Ok(Equivalent(false implies false, null), 'FalseImpliesFalse')
define FalseImpliesFalse : Todo(false, 'FalseImpliesFalse', 'make Translator recognize "implies" operator')
//define FalseImpliesNull : Ok(Equivalent(false implies null, null), 'FalseImpliesNull')
define FalseImpliesNull : Todo(false, 'FalseImpliesNull', 'make Translator recognize "implies" operator')
//define FalseImpliesTrue : Ok(Equivalent(false implies true, null), 'FalseImpliesTrue')
define FalseImpliesTrue : Todo(false, 'FalseImpliesTrue', 'make Translator recognize "implies" operator')
//define NullImpliesFalse : Ok(Equivalent(null implies false, null), 'NullImpliesFalse')
define NullImpliesFalse : Todo(false, 'NullImpliesFalse', 'make Translator recognize "implies" operator')
//define NullImpliesNull : Ok(Equivalent(null implies null, null), 'NullImpliesNull')
define NullImpliesNull : Todo(false, 'NullImpliesNull', 'make Translator recognize "implies" operator')
//define NullImpliesTrue : Ok(Equivalent(null implies true, null), 'NullImpliesTrue')
define NullImpliesTrue : Todo(false, 'NullImpliesTrue', 'make Translator recognize "implies" operator')
//define TrueImpliesFalse : Ok(Equivalent(true implies false, null), 'TrueImpliesFalse')
define TrueImpliesFalse : Todo(false, 'TrueImpliesFalse', 'make Translator recognize "implies" operator')
//define TrueImpliesNull : Ok(Equivalent(true implies null, null), 'TrueImpliesNull')
define TrueImpliesNull : Todo(false, 'TrueImpliesNull', 'make Translator recognize "implies" operator')
//define TrueImpliesTrue : Ok(Equivalent(true implies true, null), 'TrueImpliesFalse')
define TrueImpliesTrue : Todo(false, 'TrueImpliesFalse', 'make Translator recognize "implies" operator')

//Not
define NotTrue : Ok(Equivalent(not true, false), 'NotTrue')
define NotFalse : Ok(Equivalent(not false, true), 'NotFalse')
define NotNull : Ok(Equivalent(not null, null), 'NotNull')

//Or
define TrueOrTrue : Ok(Equivalent(true or true, true), 'TrueOrTrue')
define TrueOrFalse : Ok(Equivalent(true or false, true), 'TrueOrFalse')
define TrueOrNull : Ok(Equivalent(true or null, true), 'TrueOrNull')
define FalseOrTrue : Ok(Equivalent(false or true, true), 'FalseOrTrue')
define FalseOrFalse : Ok(Equivalent(false or false, false), 'FalseOrFalse')
define FalseOrNull : Ok(Equivalent(false or null, null), 'FalseOrNull')
define NullOrTrue : Ok(Equivalent(null or true, true), 'NullOrTrue')
define NullOrFalse : Ok(Equivalent(null or false, null), 'NullOrFalse')
define NullOrNull : Ok(Equivalent(null or null, null), 'NullOrNull')

//Xor
define TrueXorTrue : Ok(Equivalent(true xor true, false), 'TrueXorTrue')
define TrueXorFalse : Ok(Equivalent(true xor false, true), 'TrueXorFalse')
define TrueXorNull : Ok(Equivalent(true xor null, null), 'TrueXorNull')
define FalseXorTrue : Ok(Equivalent(false xor true, true), 'FalseXorTrue')
define FalseXorFalse : Ok(Equivalent(false xor false, false), 'FalseXorFalse')
define FalseXorNull : Ok(Equivalent(false xor null, null), 'FalseXorNull')
define NullXorTrue : Ok(Equivalent(null xor true, null), 'NullXorTrue')
define NullXorFalse : Ok(Equivalent(null xor false, null), 'NullXorFalse')
define NullXorNull : Ok(Equivalent(null xor null, null), 'NullXorNull')


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
