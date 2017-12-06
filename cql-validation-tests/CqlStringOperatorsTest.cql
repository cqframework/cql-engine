library StringOperator version '1'

//codesystem "LOINC": 'http://loinc.org'

context Population

define TestPlan: Plan(81)


//Combine
define CombineNull : Ok(Equivalent(Combine(null), null), 'CombineNull')
define CombineEmptyList : Ok(Equivalent(Combine({}), ''), 'CombineEmptyList')
define CombineABC : Ok(Equivalent(Combine({'a', 'b', 'c'}), 'abc'), 'CombineABC')
define CombineABCSepDash : Ok(Equivalent(Combine({'a', 'b', 'c'}, '-'), 'a-b-c'), 'CombineABCSepDash')

//Concatenate
define ConcatenateNullNull : Ok(Equivalent(Concatenate(null, null), null), 'ConcatenateNullNull')
define ConcatenateANull : Ok(Equivalent(Concatenate('a', null), null), 'ConcatenateANull')
define ConcatenateNullB : Ok(Equivalent(Concatenate(null, 'b'), null), 'ConcatenateNullB')
define ConcatenateAB : Ok(Equivalent(Concatenate('a', 'b'), 'ab'), 'ConcatenateAB')
define ConcatenateABWithAdd: Ok(Equivalent('a' + 'b', 'ab'), 'ConcatenateABWithAdd')

//EndsWith
define EndsWithNull: Ok(Equivalent(EndsWith(null, null), null), 'EndsWithNull')
define EndsWithTrue: Ok(Equivalent(EndsWith('Chris Schuler is the man!!', 'n!!'), true), 'EndsWithTrue')
define EndsWithFalse: Ok(Equivalent(EndsWith('Chris Schuler is the man!!', 'n!'), false), 'EndsWithFalse')

//Indexer
define IndexerNullNull : Ok(Equivalent(Indexer(null as String, null), null), 'IndexerNullNull') // cast is required due to ambiguity with Indexer(String) and Indexer(List<Any>)
define IndexerANull : Ok(Equivalent(Indexer('a', null), null), 'IndexerANull')
define IndexerNull1 : Ok(Equivalent(Indexer(null as String, 1), null), 'IndexerNull1') // cast is required due to ambiguity with Indexer(String) and Indexer(List<Any>)
define IndexerAB0 : Ok(Equivalent(Indexer('ab', 0), 'a'), 'IndexerAB0')
define IndexerAB1 : Ok(Equivalent(Indexer('ab', 1), 'b'), 'IndexerAB1')
define IndexerAB2 : Ok(Equivalent(Indexer('ab', 2), null), 'IndexerAB2')
define IndexerABNeg1 : Ok(Equivalent(Indexer('ab', -1), null), 'IndexerABNeg1')

//LastPositionOf
define LastPositionOfNull: Ok(Equivalent(LastPositionOf(null, null), null), 'LastPositionOfNull')
define LastPositionOfNull1: Ok(Equivalent(LastPositionOf(null, 'hi'), null), 'LastPositionOfNull1')
define LastPositionOfNull2: Ok(Equivalent(LastPositionOf('hi', null), null), 'LastPositionOfNull2')
define LastPositionOf1: Ok(Equivalent(LastPositionOf('hi', 'Ohio is the place to be!'), 1), 'LastPositionOf1')
define LastPositionOf2: Ok(Equivalent(LastPositionOf('hi', 'Say hi to Ohio!'), 11), 'LastPositionOf2')

//Length
define LengthNull : Ok(Equivalent(Length(null as String), 0), 'LengthNull') // cast is required due to ambiguity with Length(String) and Length(List<Any>)
define LengthEmpty : Ok(Equivalent(Length(''), 0), 'LengthEmpty')
define LengthA : Ok(Equivalent(Length('a'), 1), 'LengthA')
define LengthAB : Ok(Equivalent(Length('ab'), 2), 'LengthAB')

//Lower
define LowerNull : Ok(Equivalent(Lower(null), null), 'LowerNull')
define LowerEmpty : Ok(Equivalent(Lower(''), ''), 'LowerEmpty')
define LowerA : Ok(Equivalent(Lower('A'), 'a'), 'LowerA')
define LowerB : Ok(Equivalent(Lower('b'), 'b'), 'LowerB')
define LowerAB : Ok(Equivalent(Lower('Ab'), 'ab'), 'LowerAB')

//Matches
define MatchesNull: Ok(Equivalent(Matches('Not all who wander are lost', null), null), 'MatchesNull')
define MatchesNumberFalse: Ok(Equivalent(Matches('Not all who wander are lost', '.*\\d+'), false), 'MatchesNumberFalse')
define MatchesNumberTrue: Ok(Equivalent(Matches('Not all who wander are lost - circa 2017', '.*\\d+'), true), 'MatchesNumberTrue')
define MatchesAllTrue: Ok(Equivalent(Matches('Not all who wander are lost', '.*'), true), 'MatchesAllTrue')
define MatchesWordsAndSpacesTrue: Ok(Equivalent(Matches('Not all who wander are lost', '[\\w|\\s]+'), true), 'MatchesWordsAndSpacesTrue')
define MatchesWordsAndSpacesFalse: Ok(Equivalent(Matches('Not all who wander are lost - circa 2017', '[\\w]+'), false), 'MatchesWordsAndSpacesFalse')
define MatchesNotWords: Ok(Equivalent(Matches('   ', '\\W+'), true), 'MatchesNotWords')
define MatchesWhiteSpace: Ok(Equivalent(Matches(' \n\t', '\\s+'), true), 'MatchesWhiteSpace')

//PositionOf
define PositionOfNullNull : Ok(Equivalent(PositionOf(null, null), null), 'PositionOfNullNull')
define PositionOfANull : Ok(Equivalent(PositionOf('a', null), null), 'PositionOfANull')
define PositionOfNullA : Ok(Equivalent(PositionOf(null, 'a'), null), 'PositionOfNullA')
define PositionOfAInAB : Ok(Equivalent(PositionOf('a', 'ab'), 0), 'PositionOfAInAB')
define PositionOfBInAB : Ok(Equivalent(PositionOf('b', 'ab'), 1), 'PositionOfBInAB')
define PositionOfCInAB : Ok(Equivalent(PositionOf('c', 'ab'), -1), 'PositionOfCInAB')

//ReplaceMatches
define ReplaceMatchesNull: Ok(Equivalent(ReplaceMatches('Not all who wander are lost', null, 'But I am...'), null), 'ReplaceMatchesNull')
define ReplaceMatchesAll: Ok(Equivalent(ReplaceMatches('Not all who wander are lost', 'Not all who wander are lost', 'But still waters run deep'), 'But still waters run deep'), 'ReplaceMatchesAll')
define ReplaceMatchesMany: Ok(Equivalent(ReplaceMatches('Who put the bop in the bop she bop she bop?', 'bop', 'bang'), 'Who put the bang in the bang she bang she bang?'), 'ReplaceMatchesMany')
define ReplaceMatchesSpaces: Ok(Equivalent(ReplaceMatches('All that glitters is not gold', '\\s', '\\$'), 'All$that$glitters$is$not$gold'), 'ReplaceMatchesSpaces')

//Split
define SplitNullNull : Ok(Equivalent(Split(null, null), null), 'SplitNullNull')
define SplitNullComma : Ok(Equivalent(Split(null, ','), null), 'SplitNullComma')
define SplitABNull : Ok(Equivalent(Split('a,b', null), 'a,b'), 'SplitABNull')
define SplitABDash : Ok(Equivalent(Split('a,b', '-'), 'a,b'), 'SplitABDash')
define SplitABComma : Ok(Equivalent(Split('a,b', ','), {'a','b'}), 'SplitABComma')

//StartsWith
define StartsWithNull: Ok(Equivalent(StartsWith(null, null), null), 'StartsWithNull')
define StartsWithNull1: Ok(Equivalent(StartsWith('hi', null), null), 'StartsWithNull1')
define StartsWithNull2: Ok(Equivalent(StartsWith(null, 'hi'), null), 'StartsWithNull2')
define StartsWithTrue1: Ok(Equivalent(StartsWith('Breathe deep the gathering gloom', 'Bre'), true), 'StartsWithTrue1')
define StartsWithFalse1: Ok(Equivalent(StartsWith('Breathe deep the gathering gloom', 'bre'), false), 'StartsWithFalse1')

//Substring
define SubstringNullNull : Ok(Equivalent(Substring(null, null), null), 'SubstringNullNull')
define SubstringANull : Ok(Equivalent(Substring('a', null), null), 'SubstringANull')
define SubstringNull1 : Ok(Equivalent(Substring(null, 1), null), 'SubstringNull1')
define SubstringAB0 : Ok(Equivalent(Substring('ab', 0), 'ab'), 'SubstringAB0')
define SubstringAB1 : Ok(Equivalent(Substring('ab', 1), 'b'), 'SubstringAB1')
define SubstringAB2 : Ok(Equivalent(Substring('ab', 2), null), 'SubstringAB2')
define SubstringABNeg1 : Ok(Equivalent(Substring('ab', -1), null), 'SubstringABNeg1')
define SubstringAB0To1 : Ok(Equivalent(Substring('ab', 0, 1), 'a'), 'SubstringAB0To1')
define SubstringABC1To1 : Ok(Equivalent(Substring('abc', 1, 1), 'b'), 'SubstringABC1To1')
define SubstringAB0To3 : Ok(Equivalent(Substring('ab', 0, 3), 'ab'), 'SubstringAB0To3')

//Upper
define UpperNull : Ok(Equivalent(Upper(null), null), 'UpperNull')
define UpperEmpty : Ok(Equivalent(Upper(''), ''), 'UpperEmpty')
define UpperA : Ok(Equivalent(Upper('a'), 'A'), 'UpperA')
define UpperB : Ok(Equivalent(Upper('B'), 'B'), 'UpperB')
define UpperAB : Ok(Equivalent(Upper('aB'), 'AB'), 'UpperAB')

// toString tests
define QuantityToString: Ok(Equivalent(ToString(125 'cm'), '125cm'), 'QuantityToString')
define DateTimeToString1: Ok(Equivalent(ToString(DateTime(2000, 1, 1)), '2000-01-01'), 'DateTimeToString1')
define DateTimeToString2: Ok(Equivalent(ToString(DateTime(2000, 1, 1, 15, 25, 25, 300)), '2000-01-01T15:25:25.300'), 'DateTimeToString2')
define DateTimeToString3: Ok(Equivalent(ToString(DateTime(2000, 1, 1, 8, 25, 25, 300, -7)), '2000-01-01T08:25:25.300'), 'DateTimeToString3')
define TimeToString1: Ok(Equivalent(ToString(@T09:30:01.003), '09:30:01.003'), 'TimeToString1')
//define TupleToString: Ok(Equivalent(ToString(Tuple { this: 'is', a: 123, test: 25 'g' }), ...), 'TupleToString')
//define IntervalToString: Ok(Equivalent(ToString(Interval [ DateTime(1999, 12, 31), DateTime(2000, 1, 1) ]), ...), 'IntervalToString')
//define UncertaintyToString: Ok(Equivalent(ToString(difference in months between DateTime(2005) and DateTime(2006, 7)), ...), 'UncertaintyToString')
//define CodeToString: Ok(Equivalent(ToString(Code { code: '4225-6', system: 'http://loinc.org', version: '2.1', display: 'loinc' }), ...), 'CodeToString')
//define ConceptToString: Ok(Equivalent(ToString(Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'), ...), 'ConceptToString')


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
