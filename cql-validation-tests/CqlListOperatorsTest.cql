library ListOperator version '1'

context Population

define private ExistenceOfThisEarlierDefineIsWorkaroundForTranslatorDeathWithLaterMessageNullCodeArg: Message(null, true, '', 'Message', '')

define _0: plan(300)


//Sort
define simpleList: ok(Equivalent(({4, 5, 1, 6, 2, 1}),(null)), 'simpleList')
define simpleListString: ok(Equivalent(({'back', 'aardvark', 'alligator', 'zebra', 'iguana', 'Wolf', 'Armadillo'}),(null)), 'simpleListString')
define simpleSortAsc: ok(Equivalent((simpleList sL sort asc),(null)), 'simpleSortAsc')
define simpleSortDesc: ok(Equivalent((simpleList sL sort desc),(null)), 'simpleSortDesc')
define simpleSortStringAsc: ok(Equivalent((simpleListString sls sort asc),(null)), 'simpleSortStringAsc')
define simpleSortStringDesc: ok(Equivalent((simpleListString sls sort desc),(null)), 'simpleSortStringDesc')

//Contains
define ContainsABNullHasNull: ok(Equivalent(({ 'a', 'b', null } contains null),(null)), 'ContainsABNullHasNull')
define ContainsNullFirst: ok(Equivalent(({ null, 'b', 'c' } contains 'a'),(null)), 'ContainsNullFirst')
define ContainsABCHasA: ok(Equivalent(({ 'a', 'b', 'c' } contains 'a'),(null)), 'ContainsABCHasA')
define ContainsJan2012True: ok(Equivalent(({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 1, 1) } contains DateTime(2012, 1, 1)),(null)), 'ContainsJan2012True')
define ContainsJan2012False: ok(Equivalent(({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 1) } contains DateTime(2012, 1, 1)),(null)), 'ContainsJan2012False')
define ContainsTimeTrue: ok(Equivalent(({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 } contains @T05:59:59.999),(null)), 'ContainsTimeTrue')
define ContainsTimeFalse: ok(Equivalent(({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 } contains @T08:59:59.999),(null)), 'ContainsTimeFalse')
define ContainsNullLeft: ok(Equivalent((null contains 'a'),(null)), 'ContainsNullLeft')

//Descendents
define DescendentsEmptyList: ok(Equivalent(((null).descendents()),(null)), 'DescendentsEmptyList')
// TODO: how to call without fhirpath syntax

//Distinct
define DistinctEmptyList: ok(Equivalent((distinct {}),(null)), 'DistinctEmptyList')
define DistinctNullNullNull: ok(Equivalent((distinct { null, null, null}),(null)), 'DistinctNullNullNull')
define DistinctANullANull: ok(Equivalent((distinct { 'a', null, 'a', null}),(null)), 'DistinctANullANull')
define Distinct112233: ok(Equivalent((distinct { 1, 1, 2, 2, 3, 3}),(null)), 'Distinct112233')
define Distinct123123: ok(Equivalent((distinct { 1, 1, 2, 2, 3, 3}),(null)), 'Distinct123123')
define DistinctAABBCC: ok(Equivalent((distinct { 'a', 'a', 'b', 'b', 'c', 'c'}),(null)), 'DistinctAABBCC')
define DistinctABCABC: ok(Equivalent((distinct { 'a', 'b', 'c', 'a', 'b', 'c'}),(null)), 'DistinctABCABC')
define DistinctDateTime: ok(Equivalent((distinct { DateTime(2012, 10, 5), DateTime(2012, 1, 1), DateTime(2012, 1, 1)}),(null)), 'DistinctDateTime')
define DistinctTime: ok(Equivalent((distinct { @T15:59:59.999, @T20:59:59.999, @T20:59:59.999 }),(null)), 'DistinctTime')

//Equal
define EqualNullNull: ok(Equivalent(({null} = {null}),(null)), 'EqualNullNull')
define EqualEmptyListNull: ok(Equivalent(({} = null),(null)), 'EqualEmptyListNull')
define EqualNullEmptyList: ok(Equivalent((null = {}),(null)), 'EqualNullEmptyList')
define EqualEmptyListAndEmptyList: ok(Equivalent(({} = {}),(null)), 'EqualEmptyListAndEmptyList')
define Equal12And123: ok(Equivalent(({ 1, 2 } = { 1, 2, 3 }),(null)), 'Equal12And123')
define Equal123And12: ok(Equivalent(({ 1, 2, 3 } = { 1, 2 }),(null)), 'Equal123And12')
define Equal123And123: ok(Equivalent(({ 1, 2, 3 } = { 1, 2, 3 }),(null)), 'Equal123And123')
define EqualDateTimeTrue: ok(Equivalent(({DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)} = {DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)}),(null)), 'EqualDateTimeTrue')
define EqualDateTimeFalse: ok(Equivalent(({DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)} = {DateTime(2012, 1, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)}),(null)), 'EqualDateTimeFalse')
define EqualTimeTrue: ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999, @T20:59:59.999 } = { @T15:59:59.999, @T20:59:59.999, @T20:59:59.999 }),(null)), 'EqualTimeTrue')
define EqualTimeFalse: ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999, @T20:59:59.999 } = { @T10:59:59.999, @T20:59:59.999, @T20:59:59.999 }),(null)), 'EqualTimeFalse')

//Except
define ExceptEmptyListAndEmptyList: ok(Equivalent((Except({}, {})),(null)), 'ExceptEmptyListAndEmptyList')
define Except1234And23: ok(Equivalent(({ 1, 2, 3, 4 } except { 2, 3 }),(null)), 'Except1234And23')
define Except23And1234: ok(Equivalent(({ 2, 3 } except { 1, 2, 3, 4 }),(null)), 'Except23And1234')
define ExceptDateTime: ok(Equivalent(({ DateTime(2012, 5, 10), DateTime(2014, 12, 10), DateTime(2010, 1, 1)} except {DateTime(2014, 12, 10), DateTime(2010, 1, 1) }),(null)), 'ExceptDateTime')
define ExceptTime: ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999, @T12:59:59.999 } except { @T20:59:59.999, @T12:59:59.999 }),(null)), 'ExceptTime')
define ExceptNullRight: ok(Equivalent(({ 1, 4 } except null),(null)), 'ExceptNullRight')

//Exists
define ExistsEmpty: ok(Equivalent((Exists({})),(null)), 'ExistsEmpty')
define ExistsListNull: ok(Equivalent((Exists({ null })),(null)), 'ExistsListNull')
define Exists1: ok(Equivalent((Exists({ 1 })),(null)), 'Exists1')
define Exists12: ok(Equivalent((Exists({ 1, 2 })),(null)), 'Exists12')
define ExistsDateTime: ok(Equivalent((Exists({ DateTime(2012, 5, 10), DateTime(2014, 12, 10) })),(null)), 'ExistsDateTime')
define ExistsTime: ok(Equivalent((Exists({ @T15:59:59.999, @T20:59:59.999 })),(null)), 'ExistsTime')
define ExistsNull: ok(Equivalent((Exists(null)),(null)), 'ExistsNull')

//Flatten
define FlattenEmpty: ok(Equivalent((Flatten({{},{}})),(null)), 'FlattenEmpty')
define FlattenListNullAndNull: ok(Equivalent((Flatten({{null}, {null}})),(null)), 'FlattenListNullAndNull')
define FlattenList12And34: ok(Equivalent((Flatten({{1,2}, {3,4}})),(null)), 'FlattenList12And34')
define FlattenDateTime: ok(Equivalent((Flatten({ {DateTime(2012, 5, 10)}, {DateTime(2014, 12, 10)} })),(null)), 'FlattenDateTime')
define FlattenTime: ok(Equivalent((Flatten({ {@T15:59:59.999}, {@T20:59:59.999} })),(null)), 'FlattenTime')

//First
define FirstEmpty: ok(Equivalent((First({})),(null)), 'FirstEmpty')
define FirstNull1: ok(Equivalent((First({ null, 1 })),(null)), 'FirstNull1')
define First1Null: ok(Equivalent((First({ 1, null })),(null)), 'First1Null')
define First12: ok(Equivalent((First({ 1, 2 })),(null)), 'First12')
define FirstDateTime: ok(Equivalent((First({ DateTime(2012, 5, 10), DateTime(2014, 12, 10) })),(null)), 'FirstDateTime')
define FirstTime: ok(Equivalent((First({ @T15:59:59.999, @T20:59:59.999 })),(null)), 'FirstTime')

//In
define InNullEmpty: ok(Equivalent((null in {}),(null)), 'InNullEmpty')
define InNullAnd1Null: ok(Equivalent((null in { 1, null }),(null)), 'InNullAnd1Null')
define In1Null: ok(Equivalent((1 in null),(null)), 'In1Null')
define In1And12: ok(Equivalent((1 in { 1, 2 }),(null)), 'In1And12')
define In3And12: ok(Equivalent((3 in { 1, 2 }),(null)), 'In3And12')
define InDateTimeTrue: ok(Equivalent((DateTime(2012, 5, 10) in { DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10) }),(null)), 'InDateTimeTrue')
define InDateTimeFalse: ok(Equivalent((DateTime(2012, 6, 10) in { DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10) }),(null)), 'InDateTimeFalse')
define InTimeTrue: ok(Equivalent((@T15:59:59.999 in { @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 }),(null)), 'InTimeTrue')
define InTimeFalse: ok(Equivalent((@T16:59:59.999 in { @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 }),(null)), 'InTimeFalse')

//Includes
define IncludesEmptyAndEmpty: ok(Equivalent(({} includes {}),(null)), 'IncludesEmptyAndEmpty')
define IncludesListNullAndListNull: ok(Equivalent(({null} includes {null}),(null)), 'IncludesListNullAndListNull')
define Includes123AndEmpty: ok(Equivalent(({1, 2, 3} includes {}),(null)), 'Includes123AndEmpty')
define Includes123And2: ok(Equivalent(({1, 2, 3} includes {2}),(null)), 'Includes123And2')
define Includes123And4: ok(Equivalent(({1, 2, 3} includes {4}),(null)), 'Includes123And4')
define IncludesDateTimeTrue: ok(Equivalent(({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} includes {DateTime(2012, 5, 10)}),(null)), 'IncludesDateTimeTrue')
define IncludesDateTimeFalse: ok(Equivalent(({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} includes {DateTime(2012, 5, 11)}),(null)), 'IncludesDateTimeFalse')
define IncludesTimeTrue: ok(Equivalent(({ @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 } includes @T15:59:59.999),(null)), 'IncludesTimeTrue')
define IncludesTimeFalse: ok(Equivalent(({ @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 } includes @T16:59:59.999),(null)), 'IncludesTimeFalse')
define IncludesNullLeft: ok(Equivalent((null includes {2}),(null)), 'IncludesNullLeft')
// this test is going to the ContainsEvaluator
// define IncludesNullRight: {'s', 'a', 'm'} includes null

//IncludedIn
define IncludedInEmptyAndEmpty: ok(Equivalent(({} included in {}),(null)), 'IncludedInEmptyAndEmpty')
define IncludedInListNullAndListNull: ok(Equivalent(({ null } included in { null }),(null)), 'IncludedInListNullAndListNull')
define IncludedInEmptyAnd123: ok(Equivalent(({} included in { 1, 2, 3 }),(null)), 'IncludedInEmptyAnd123')
define IncludedIn2And123: ok(Equivalent(({ 2 } included in { 1, 2, 3 }),(null)), 'IncludedIn2And123')
define IncludedIn4And123: ok(Equivalent(({ 4 } included in { 1, 2, 3 }),(null)), 'IncludedIn4And123')
define IncludedInDateTimeTrue: ok(Equivalent(({ DateTime(2012, 5, 10)} included in {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)}),(null)), 'IncludedInDateTimeTrue')
define IncludedInDateTimeFalse: ok(Equivalent(({DateTime(2012, 5, 11)} included in {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)}),(null)), 'IncludedInDateTimeFalse')
define IncludedInTimeTrue: ok(Equivalent((@T15:59:59.999 included in { @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 }),(null)), 'IncludedInTimeTrue')
define IncludedInTimeFalse: ok(Equivalent((@T16:59:59.999 included in { @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 }),(null)), 'IncludedInTimeFalse')
// the following expression is going to the InEvaluator
// define IncludedInNullLeft: null included in {2}
define IncludedInNullRight: ok(Equivalent(({'s', 'a', 'm'} included in null),(null)), 'IncludedInNullRight')

//Indexer
define IndexerNull1 : ok(Equivalent(((null as List<System.Any>)[1]),(null)), 'IndexerNull1') // Cast is required due to ambiguity with Indexer(String) and Indexer(List<Any>)
define Indexer0Of12 : ok(Equivalent(({ 1, 2 }[0]),(null)), 'Indexer0Of12')
define Indexer1Of12 : ok(Equivalent(({ 1, 2 }[1]),(null)), 'Indexer1Of12')
define Indexer2Of12 : ok(Equivalent(({ 1, 2 }[2]),(null)), 'Indexer2Of12')
define IndexerNeg1Of12 : ok(Equivalent(({ 1, 2 }[-1]),(null)), 'IndexerNeg1Of12')
define IndexerDateTime: ok(Equivalent(({ DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10) }[1]),(null)), 'IndexerDateTime')
define IndexerTime: ok(Equivalent(({ @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 }[1]),(null)), 'IndexerTime')

//IndexOf
define IndexOfEmptyNull : ok(Equivalent((IndexOf({}, null)),(null)), 'IndexOfEmptyNull')
define IndexOfNullEmpty : ok(Equivalent((IndexOf(null, {})),(null)), 'IndexOfNullEmpty')
define IndexOfNullIn1Null : ok(Equivalent((IndexOf({ 1, null }, null)),(null)), 'IndexOfNullIn1Null')
define IndexOf1In12 : ok(Equivalent((IndexOf({ 1, 2 }, 1)),(null)), 'IndexOf1In12')
define IndexOf2In12 : ok(Equivalent((IndexOf({ 1, 2 }, 2)),(null)), 'IndexOf2In12')
define IndexOf3In12 : ok(Equivalent((IndexOf({ 1, 2 }, 3)),(null)), 'IndexOf3In12')
define IndexOfDateTime: ok(Equivalent((IndexOf({ DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10) }, DateTime(2014, 12, 10))),(null)), 'IndexOfDateTime')
define IndexOfTime: ok(Equivalent((IndexOf({ @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 }, @T15:59:59.999)),(null)), 'IndexOfTime')

//Intersect
define IntersectEmptyListAndEmptyList: ok(Equivalent(({} intersect {}),(null)), 'IntersectEmptyListAndEmptyList')
define Intersect1234And23: ok(Equivalent(({ 1, 2, 3, 4 } intersect { 2, 3 }),(null)), 'Intersect1234And23')
define Intersect23And1234: ok(Equivalent(({2, 3} intersect { 1, 2, 3, 4 }),(null)), 'Intersect23And1234')
define IntersectDateTime: ok(Equivalent(({ DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10) } intersect { DateTime(2012, 5, 10), DateTime(2014, 12, 10), DateTime(2000, 5, 5) }),(null)), 'IntersectDateTime')
define IntersectTime: ok(Equivalent(({ @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 } intersect { @T01:29:15.156, @T15:59:59.999, @T20:59:59.999 }),(null)), 'IntersectTime')

//Last
define LastEmpty: ok(Equivalent((Last({})),(null)), 'LastEmpty')
define LastNull1: ok(Equivalent((Last({null, 1})),(null)), 'LastNull1')
define Last1Null: ok(Equivalent((Last({1, null})),(null)), 'Last1Null')
define Last12: ok(Equivalent((Last({1, 2})),(null)), 'Last12')
define LastDateTime: ok(Equivalent((Last({DateTime(2012, 5, 10), DateTime(2014, 12, 10)})),(null)), 'LastDateTime')
define LastTime: ok(Equivalent((Last({ @T15:59:59.999, @T20:59:59.999 })),(null)), 'LastTime')

//Length
define LengthEmpty: ok(Equivalent((Length({})),(null)), 'LengthEmpty')
define LengthNull1: ok(Equivalent((Length({null, 1})),(null)), 'LengthNull1')
define Length1Null: ok(Equivalent((Length({1, null})),(null)), 'Length1Null')
define Length12: ok(Equivalent((Length({1, 2})),(null)), 'Length12')
define LengthDateTime: ok(Equivalent((Length({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)})),(null)), 'LengthDateTime')
define LengthTime: ok(Equivalent((Length({ @T15:59:59.999, @T20:59:59.999, @T15:59:59.999, @T20:59:59.999, @T15:59:59.999, @T20:59:59.999 })),(null)), 'LengthTime')
define LengthNull: ok(Equivalent((Length(null as String)),(null)), 'LengthNull')

//Equivalent
define EquivalentEmptyAndEmpty          : ok(Equivalent(({} ~ {}),(null)), 'EquivalentEmptyAndEmpty')
define EquivalentABCAndABC              : ok(Equivalent(({ 'a', 'b', 'c' } ~ { 'a', 'b', 'c' }),(null)), 'EquivalentABCAndABC')
define EquivalentABCAndAB               : ok(Equivalent(({ 'a', 'b', 'c' } ~ { 'a', 'b' }),(null)), 'EquivalentABCAndAB')
define EquivalentABCAnd123              : ok(Equivalent(({ 'a', 'b', 'c' } ~ { 1, 2, 3 }),(null)), 'EquivalentABCAnd123')
define Equivalent123AndABC              : ok(Equivalent(({ 1, 2, 3 } ~ { 'a', 'b', 'c' }),(null)), 'Equivalent123AndABC')
define Equivalent123AndString123        : ok(Equivalent(({ 1, 2, 3 } ~ { '1', '2', '3' }),(null)), 'Equivalent123AndString123')
define EquivalentDateTimeTrue           : ok(Equivalent(({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10), null} ~ {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10), null}),(null)), 'EquivalentDateTimeTrue')
define EquivalentDateTimeNull           : ok(Equivalent(({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} ~ {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10), null}),(null)), 'EquivalentDateTimeNull')
define EquivalentDateTimeFalse          : ok(Equivalent(({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} ~ {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 1)}),(null)), 'EquivalentDateTimeFalse')
define EquivalentTimeTrue               : ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999 } ~ { @T15:59:59.999, @T20:59:59.999 }),(null)), 'EquivalentTimeTrue')
define EquivalentTimeNull               : ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999 } ~ { @T15:59:59.999, @T20:59:59.999, null }),(null)), 'EquivalentTimeNull')
define EquivalentTimeFalse              : ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999 } ~ { @T15:59:59.999, @T20:59:59.995 }),(null)), 'EquivalentTimeFalse')

//NotEqual
define NotEqualEmptyAndEmpty : ok(Equivalent(({} != {}),(null)), 'NotEqualEmptyAndEmpty')
define NotEqualABCAndABC : ok(Equivalent(({ 'a', 'b', 'c' } != { 'a', 'b', 'c' }),(null)), 'NotEqualABCAndABC')
define NotEqualABCAndAB : ok(Equivalent(({ 'a', 'b', 'c' } != { 'a', 'b' }),(null)), 'NotEqualABCAndAB')
define NotEqualABCAnd123: ok(Equivalent(({ 'a', 'b', 'c' } != { 1, 2, 3 }),(null)), 'NotEqualABCAnd123')
define NotEqual123AndABC: ok(Equivalent(({ 1, 2, 3 } != { 'a', 'b', 'c' }),(null)), 'NotEqual123AndABC')
define NotEqual123AndString123: ok(Equivalent(({ 1, 2, 3 } != { '1', '2', '3' }),(null)), 'NotEqual123AndString123')
define NotEqualDateTimeTrue: ok(Equivalent(({DateTime(2001, 9, 11, 0, 0, 0, 0), DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)} != {DateTime(2001, 9, 11, 0, 0, 0, 0), DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 1, 0, 0, 0, 0)}),(null)), 'NotEqualDateTimeTrue')
define NotEqualDateTimeFalse: ok(Equivalent(({DateTime(2001, 9, 11, 0, 0, 0, 0), DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)} != {DateTime(2001, 9, 11, 0, 0, 0, 0), DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)}),(null)), 'NotEqualDateTimeFalse')
define NotEqualTimeTrue: ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999 } = { @T15:59:59.999, @T20:59:59.999 }),(null)), 'NotEqualTimeTrue')
define NotEqualTimeFalse: ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999 } = { @T15:59:59.999, @T20:59:49.999 }),(null)), 'NotEqualTimeFalse')

//ProperlyIncludes
define ProperIncludesEmptyAndEmpty: ok(Equivalent(({} properly includes {}),(null)), 'ProperIncludesEmptyAndEmpty')
define ProperIncludesListNullAndListNull: ok(Equivalent(({null} properly includes {null}),(null)), 'ProperIncludesListNullAndListNull')
define ProperIncludes123AndEmpty: ok(Equivalent(({1, 2, 3} properly includes {}),(null)), 'ProperIncludes123AndEmpty')
define ProperIncludes123And2: ok(Equivalent(({1, 2, 3} properly includes {2}),(null)), 'ProperIncludes123And2')
define ProperIncludes123And4: ok(Equivalent(({1, 2, 3} properly includes {4}),(null)), 'ProperIncludes123And4')
define ProperIncludesDateTimeTrue: ok(Equivalent(({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} properly includes {DateTime(2012, 5, 10), DateTime(2014, 12, 10)}),(null)), 'ProperIncludesDateTimeTrue')
define ProperIncludesDateTimeFalse: ok(Equivalent(({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} properly includes {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)}),(null)), 'ProperIncludesDateTimeFalse')
define ProperIncludesTimeTrue: ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999, @T20:59:49.999 } properly includes { @T15:59:59.999, @T20:59:59.999 }),(null)), 'ProperIncludesTimeTrue')
define ProperIncludesTimeFalse: ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999, @T20:59:49.999 } properly includes { @T15:59:59.999, @T20:59:59.999, @T14:59:22.999 }),(null)), 'ProperIncludesTimeFalse')
define ProperlyIncludesNullLeft: ok(Equivalent((null properly includes {2}),(null)), 'ProperlyIncludesNullLeft')
// the following expression is going to the ProperContains ELM
// define ProperlyIncludesNulRight: {'s', 'u', 'n'} properly includes null

//ProperlyIncludedIn
define ProperIncludedInEmptyAndEmpty: ok(Equivalent(({} properly included in {}),(null)), 'ProperIncludedInEmptyAndEmpty')
define ProperIncludedInListNullAndListNull: ok(Equivalent(({null} properly included in {null}),(null)), 'ProperIncludedInListNullAndListNull')
define ProperIncludedInEmptyAnd123: ok(Equivalent(({} properly included in {1, 2, 3}),(null)), 'ProperIncludedInEmptyAnd123')
define ProperIncludedIn2And123: ok(Equivalent(({2} properly included in {1, 2, 3}),(null)), 'ProperIncludedIn2And123')
define ProperIncludedIn4And123: ok(Equivalent(({4} properly included in {1, 2, 3}),(null)), 'ProperIncludedIn4And123')
define ProperIncludedInDateTimeTrue: ok(Equivalent(({DateTime(2012, 5, 10), DateTime(2014, 12, 10)} properly included in {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)}),(null)), 'ProperIncludedInDateTimeTrue')
define ProperIncludedInDateTimeFalse: ok(Equivalent(({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} properly included in {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)}),(null)), 'ProperIncludedInDateTimeFalse')
define ProperIncludedInTimeTrue: ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999 } properly included in { @T15:59:59.999, @T20:59:59.999, @T20:59:49.999 }),(null)), 'ProperIncludedInTimeTrue')
define ProperIncludedInTimeFalse: ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999, @T14:59:22.999 } properly included in { @T15:59:59.999, @T20:59:59.999, @T20:59:49.999 }),(null)), 'ProperIncludedInTimeFalse')
// the following expression is going to the ProperIn ELM
// define ProperlyIncludedInNullLeft: null properly included in {2}
define ProperlyIncludedInNulRight: ok(Equivalent(({'s', 'u', 'n'} properly included in null),(null)), 'ProperlyIncludedInNulRight')

//SingletonFrom
define SingletonFromEmpty: ok(Equivalent((singleton from {}),(null)), 'SingletonFromEmpty')
define SingletonFromListNull: ok(Equivalent((singleton from {null}),(null)), 'SingletonFromListNull')
define SingletonFrom1: ok(Equivalent((singleton from { 1 }),(null)), 'SingletonFrom1')
define SingletonFrom12: ok(Equivalent((singleton from { 1, 2 }),(null)), 'SingletonFrom12')
define SingletonFromDateTime: ok(Equivalent((singleton from { DateTime(2012, 5, 10) }),(null)), 'SingletonFromDateTime')
define SingletonFromTime: ok(Equivalent((singleton from { @T15:59:59.999 }),(null)), 'SingletonFromTime')

//Skip
define SkipNull: ok(Equivalent((Skip(null, 3)),(null)), 'SkipNull')
define SkipEven: ok(Equivalent((Skip({1,2,3,4,5}, 2)),(null)), 'SkipEven')
define SkipOdd: ok(Equivalent((Skip({1,2,3,4,5}, 3)),(null)), 'SkipOdd')
define SkipNone: ok(Equivalent((Skip({1,2,3,4,5}, 0)),(null)), 'SkipNone')
define SkipAll: ok(Equivalent((Skip({1,2,3,4,5}, 5)),(null)), 'SkipAll')

//Tail
define TailNull: ok(Equivalent((Tail(null)),(null)), 'TailNull')
define TailEven: ok(Equivalent((Tail({1,2,3,4})),(null)), 'TailEven')
define TailOdd: ok(Equivalent((Tail({1,2,3,4,5})),(null)), 'TailOdd')
define TailEmpty: ok(Equivalent((Tail({})),(null)), 'TailEmpty')
define TailOneElement: ok(Equivalent((Tail({1})),(null)), 'TailOneElement')

//Take
define TakeNull: ok(Equivalent((Take(null, 3)),(null)), 'TakeNull')
define TakeNullEmpty: ok(Equivalent((Take({1,2,3}, null)),(null)), 'TakeNullEmpty')
define TakeEmpty: ok(Equivalent((Take({1,2,3}, 0)),(null)), 'TakeEmpty')
define TakeEven: ok(Equivalent((Take({1,2,3,4}, 2)),(null)), 'TakeEven')
define TakeOdd: ok(Equivalent((Take({1,2,3,4}, 3)),(null)), 'TakeOdd')
define TakeAll: ok(Equivalent((Take({1,2,3,4}, 4)),(null)), 'TakeAll')

//Union
define UnionEmptyAndEmpty: ok(Equivalent(({} union {}),(null)), 'UnionEmptyAndEmpty')
define UnionListNullAndListNull: ok(Equivalent(({ null } union { null }),(null)), 'UnionListNullAndListNull')
define Union123AndEmpty: ok(Equivalent(({ 1, 2, 3 } union {}),(null)), 'Union123AndEmpty')
define Union123And2: ok(Equivalent(({ 1, 2, 3 } union { 2 }),(null)), 'Union123And2')
define Union123And4: ok(Equivalent(({ 1, 2, 3 } union { 4 }),(null)), 'Union123And4')
define UnionDateTime: ok(Equivalent(({ DateTime(2001, 9, 11)} union {DateTime(2012, 5, 10), DateTime(2014, 12, 10) }),(null)), 'UnionDateTime')
define UnionTime: ok(Equivalent(({ @T15:59:59.999, @T20:59:59.999, @T12:59:59.999 } union { @T10:59:59.999 }),(null)), 'UnionTime')

// Sort
/*define intList: { 3, 2, 1 }
define decimalList: ok(Equivalent(({ 3.8, 2.4, 1.9 }),(null)), 'decimalList')
define quantityList: ok(Equivalent(({ 19.99 'lbs', 17.33 'lbs', 10.66 'lbs' }),(null)), 'quantityList')
define dateTimeList: ok(Equivalent(({ DateTime(2016), DateTime(2015), DateTime(2010) }),(null)), 'dateTimeList')
define timeList: ok(Equivalent(({ @T15:59:59.999, @T15:12:59.999, @T15:12:13.999 }),(null)), 'timeList')*/


define function plan(test_count Integer):
    Message(null, true, null, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
