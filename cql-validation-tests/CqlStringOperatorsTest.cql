library StringOperator version '1'

codesystem "LOINC": 'http://loinc.org'

context Population

define _0: plan(300)


//Combine
define CombineNull : ok(Equivalent((Combine(null)),(null)), 'CombineNull')
define CombineEmptyList : ok(Equivalent((Combine({})),(null)), 'CombineEmptyList')
define CombineABC : ok(Equivalent((Combine({'a', 'b', 'c'})),(null)), 'CombineABC')
define CombineABCSepDash : ok(Equivalent((Combine({'a', 'b', 'c'}, '-')),(null)), 'CombineABCSepDash')

//Concatenate
define ConcatenateNullNull : ok(Equivalent((Concatenate(null, null)),(null)), 'ConcatenateNullNull')
define ConcatenateANull : ok(Equivalent((Concatenate('a', null)),(null)), 'ConcatenateANull')
define ConcatenateNullB : ok(Equivalent((Concatenate(null, 'b')),(null)), 'ConcatenateNullB')
define ConcatenateAB : ok(Equivalent((Concatenate('a', 'b')),(null)), 'ConcatenateAB')
define ConcatenateABWithAdd: ok(Equivalent(('a' + 'b'),(null)), 'ConcatenateABWithAdd')

//EndsWith
define EndsWithNull: ok(Equivalent((EndsWith(null, null)),(null)), 'EndsWithNull')
define EndsWithTrue: ok(Equivalent((EndsWith('Chris Schuler is the man!!', 'n!!')),(null)), 'EndsWithTrue')
define EndsWithFalse: ok(Equivalent((EndsWith('Chris Schuler is the man!!', 'n!')),(null)), 'EndsWithFalse')

//Indexer
define IndexerNullNull : ok(Equivalent((Indexer(null as String, null)),(null)), 'IndexerNullNull') // cast is required due to ambiguity with Indexer(String) and Indexer(List<Any>)
define IndexerANull : ok(Equivalent((Indexer('a', null)),(null)), 'IndexerANull')
define IndexerNull1 : ok(Equivalent((Indexer(null as String, 1)),(null)), 'IndexerNull1') // cast is required due to ambiguity with Indexer(String) and Indexer(List<Any>)
define IndexerAB0 : ok(Equivalent((Indexer('ab', 0)),(null)), 'IndexerAB0')
define IndexerAB1 : ok(Equivalent((Indexer('ab', 1)),(null)), 'IndexerAB1')
define IndexerAB2 : ok(Equivalent((Indexer('ab', 2)),(null)), 'IndexerAB2')
define IndexerABNeg1 : ok(Equivalent((Indexer('ab', -1)),(null)), 'IndexerABNeg1')

//LastPositionOf
define LastPositionOfNull: ok(Equivalent((LastPositionOf(null, null)),(null)), 'LastPositionOfNull')
define LastPositionOfNull1: ok(Equivalent((LastPositionOf(null, 'hi')),(null)), 'LastPositionOfNull1')
define LastPositionOfNull2: ok(Equivalent((LastPositionOf('hi', null)),(null)), 'LastPositionOfNull2')
define LastPositionOf1: ok(Equivalent((LastPositionOf('hi', 'Ohio is the place to be!')),(null)), 'LastPositionOf1')
define LastPositionOf2: ok(Equivalent((LastPositionOf('hi', 'Say hi to Ohio!')),(null)), 'LastPositionOf2')

//Length
define LengthNull : ok(Equivalent((Length(null as String)),(null)), 'LengthNull') // cast is required due to ambiguity with Length(String) and Length(List<Any>)
define LengthEmpty : ok(Equivalent((Length('')),(null)), 'LengthEmpty')
define LengthA : ok(Equivalent((Length('a')),(null)), 'LengthA')
define LengthAB : ok(Equivalent((Length('ab')),(null)), 'LengthAB')

//Lower
define LowerNull : ok(Equivalent((Lower(null)),(null)), 'LowerNull')
define LowerEmpty : ok(Equivalent((Lower('')),(null)), 'LowerEmpty')
define LowerA : ok(Equivalent((Lower('A')),(null)), 'LowerA')
define LowerB : ok(Equivalent((Lower('b')),(null)), 'LowerB')
define LowerAB : ok(Equivalent((Lower('Ab')),(null)), 'LowerAB')

//Matches
define MatchesNull: ok(Equivalent((Matches('Not all who wander are lost', null)),(null)), 'MatchesNull')
define MatchesNumberFalse: ok(Equivalent((Matches('Not all who wander are lost', '.*\\d+')),(null)), 'MatchesNumberFalse')
define MatchesNumberTrue: ok(Equivalent((Matches('Not all who wander are lost - circa 2017', '.*\\d+')),(null)), 'MatchesNumberTrue')
define MatchesAllTrue: ok(Equivalent((Matches('Not all who wander are lost', '.*')),(null)), 'MatchesAllTrue')
define MatchesWordsAndSpacesTrue: ok(Equivalent((Matches('Not all who wander are lost', '[\\w|\\s]+')),(null)), 'MatchesWordsAndSpacesTrue')
define MatchesWordsAndSpacesFalse: ok(Equivalent((Matches('Not all who wander are lost - circa 2017', '[\\w]+')),(null)), 'MatchesWordsAndSpacesFalse')
define MatchesNotWords: ok(Equivalent((Matches('   ', '\\W+')),(null)), 'MatchesNotWords')
define MatchesWhiteSpace: ok(Equivalent((Matches(' \n\t', '\\s+')),(null)), 'MatchesWhiteSpace')

//PositionOf
define PositionOfNullNull : ok(Equivalent((PositionOf(null, null)),(null)), 'PositionOfNullNull')
define PositionOfANull : ok(Equivalent((PositionOf('a', null)),(null)), 'PositionOfANull')
define PositionOfNullA : ok(Equivalent((PositionOf(null, 'a')),(null)), 'PositionOfNullA')
define PositionOfAInAB : ok(Equivalent((PositionOf('a', 'ab')),(null)), 'PositionOfAInAB')
define PositionOfBInAB : ok(Equivalent((PositionOf('b', 'ab')),(null)), 'PositionOfBInAB')
define PositionOfCInAB : ok(Equivalent((PositionOf('c', 'ab')),(null)), 'PositionOfCInAB')

//ReplaceMatches
define ReplaceMatchesNull: ok(Equivalent((ReplaceMatches('Not all who wander are lost', null, 'But I am...')),(null)), 'ReplaceMatchesNull')
define ReplaceMatchesAll: ok(Equivalent((ReplaceMatches('Not all who wander are lost', 'Not all who wander are lost', 'But still waters run deep')),(null)), 'ReplaceMatchesAll')
define ReplaceMatchesMany: ok(Equivalent((ReplaceMatches('Who put the bop in the bop she bop she bop?', 'bop', 'bang')),(null)), 'ReplaceMatchesMany')
define ReplaceMatchesSpaces: ok(Equivalent((ReplaceMatches('All that glitters is not gold', '\\s', '\\$')),(null)), 'ReplaceMatchesSpaces')

//Split
define SplitNullNull : ok(Equivalent((Split(null, null)),(null)), 'SplitNullNull')
define SplitNullComma : ok(Equivalent((Split(null, ',')),(null)), 'SplitNullComma')
define SplitABNull : ok(Equivalent((Split('a,b', null)),(null)), 'SplitABNull')
define SplitABDash : ok(Equivalent((Split('a,b', '-')),(null)), 'SplitABDash')
define SplitABComma : ok(Equivalent((Split('a,b', ',')),(null)), 'SplitABComma')

//StartsWith
define StartsWithNull: ok(Equivalent((StartsWith(null, null)),(null)), 'StartsWithNull')
define StartsWithNull1: ok(Equivalent((StartsWith('hi', null)),(null)), 'StartsWithNull1')
define StartsWithNull2: ok(Equivalent((StartsWith(null, 'hi')),(null)), 'StartsWithNull2')
define StartsWithTrue1: ok(Equivalent((StartsWith('Breathe deep the gathering gloom', 'Bre')),(null)), 'StartsWithTrue1')
define StartsWithFalse1: ok(Equivalent((StartsWith('Breathe deep the gathering gloom', 'bre')),(null)), 'StartsWithFalse1')

//Substring
define SubstringNullNull : ok(Equivalent((Substring(null, null)),(null)), 'SubstringNullNull')
define SubstringANull : ok(Equivalent((Substring('a', null)),(null)), 'SubstringANull')
define SubstringNull1 : ok(Equivalent((Substring(null, 1)),(null)), 'SubstringNull1')
define SubstringAB0 : ok(Equivalent((Substring('ab', 0)),(null)), 'SubstringAB0')
define SubstringAB1 : ok(Equivalent((Substring('ab', 1)),(null)), 'SubstringAB1')
define SubstringAB2 : ok(Equivalent((Substring('ab', 2)),(null)), 'SubstringAB2')
define SubstringABNeg1 : ok(Equivalent((Substring('ab', -1)),(null)), 'SubstringABNeg1')
define SubstringAB0To1 : ok(Equivalent((Substring('ab', 0, 1)),(null)), 'SubstringAB0To1')
define SubstringABC1To1 : ok(Equivalent((Substring('abc', 1, 1)),(null)), 'SubstringABC1To1')
define SubstringAB0To3 : ok(Equivalent((Substring('ab', 0, 3)),(null)), 'SubstringAB0To3')

//Upper
define UpperNull : ok(Equivalent((Upper(null)),(null)), 'UpperNull')
define UpperEmpty : ok(Equivalent((Upper('')),(null)), 'UpperEmpty')
define UpperA : ok(Equivalent((Upper('a')),(null)), 'UpperA')
define UpperB : ok(Equivalent((Upper('B')),(null)), 'UpperB')
define UpperAB : ok(Equivalent((Upper('aB')),(null)), 'UpperAB')

// toString tests
define QuantityToString: ok(Equivalent((125 'cm'),(null)), 'QuantityToString')
define DateTimeToString1: ok(Equivalent((DateTime(2000, 1, 1)),(null)), 'DateTimeToString1')
define DateTimeToString2: ok(Equivalent((DateTime(2000, 1, 1, 15, 25, 25, 300)),(null)), 'DateTimeToString2')
define DateTimeToString3: ok(Equivalent((DateTime(2000, 1, 1, 8, 25, 25, 300, -7)),(null)), 'DateTimeToString3')
define TimeToString1: ok(Equivalent((@T09:30:01.003),(null)), 'TimeToString1')
define TupleToString: ok(Equivalent((Tuple { this: 'is', a: 123, test: 25 'g' }),(null)), 'TupleToString')
define IntervalToString: ok(Equivalent((Interval [ DateTime(1999, 12, 31), DateTime(2000, 1, 1) ]),(null)), 'IntervalToString')
define UncertaintyToString: ok(Equivalent((difference in months between DateTime(2005) and DateTime(2006, 7)),(null)), 'UncertaintyToString')
define CodeToString: ok(Equivalent((Code { code: '4225-6', system: 'http://loinc.org', version: '2.1', display: 'loinc' }),(null)), 'CodeToString')
define ConceptToString: ok(Equivalent((Concept { Code '66071002' from "LOINC", Code 'B18.1' from "LOINC"} display 'Type B viral hepatitis'),(null)), 'ConceptToString')


define function plan(test_count Integer):
    Message(null, true, null as String, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null as String, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)

define function todo(is_ok2 Boolean, message2 String, why_todo String):
    ok(is_ok2, message2 + ' # TODO - ' + why_todo)
    // Note: parser/runtime has a scope-breaking bug manifesting if todo() arg named "message" also.

define function skipped(is_ok2 Boolean, message2 String, why_skipped String):
    ok(is_ok2, message2 + ' # SKIPPED - ' + why_skipped)
    // Note: parser/runtime has a scope-breaking bug manifesting if todo() arg named "message" also.
