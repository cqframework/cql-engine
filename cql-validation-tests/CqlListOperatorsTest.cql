library ListOperator version '1'

context Population

define TestPlan: Plan(300)


//Sort
define simpleList: Ok(Equivalent({4, 5, 1, 6, 2, 1}, null), 'simpleList')
define simpleListString: Ok(Equivalent({'back', 'aardvark', 'alligator', 'zebra', 'iguana', 'Wolf', 'Armadillo'}, null), 'simpleListString')
define simpleSortAsc: Ok(Equivalent(simpleList sL sort asc, null), 'simpleSortAsc')
define simpleSortDesc: Ok(Equivalent(simpleList sL sort desc, null), 'simpleSortDesc')
define simpleSortStringAsc: Ok(Equivalent(simpleListString sls sort asc, null), 'simpleSortStringAsc')
define simpleSortStringDesc: Ok(Equivalent(simpleListString sls sort desc, null), 'simpleSortStringDesc')

//Contains
define ContainsABNullHasNull: Ok(Equivalent({ 'a', 'b', null } contains null, null), 'ContainsABNullHasNull')
define ContainsNullFirst: Ok(Equivalent({ null, 'b', 'c' } contains 'a', null), 'ContainsNullFirst')
define ContainsABCHasA: Ok(Equivalent({ 'a', 'b', 'c' } contains 'a', null), 'ContainsABCHasA')
define ContainsJan2012True: Ok(Equivalent({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 1, 1) } contains DateTime(2012, 1, 1), null), 'ContainsJan2012True')
define ContainsJan2012False: Ok(Equivalent({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 1) } contains DateTime(2012, 1, 1), null), 'ContainsJan2012False')
define ContainsTimeTrue: Ok(Equivalent({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 } contains @T05:59:59.999, null), 'ContainsTimeTrue')
define ContainsTimeFalse: Ok(Equivalent({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 } contains @T08:59:59.999, null), 'ContainsTimeFalse')
define ContainsNullLeft: Ok(Equivalent(null contains 'a', null), 'ContainsNullLeft')

//Descendents
define DescendentsEmptyList: Ok(Equivalent((null).descendents(), null), 'DescendentsEmptyList')
// TODO: how to call without fhirpath syntax

//Distinct
define DistinctEmptyList: Ok(Equivalent(distinct {}, null), 'DistinctEmptyList')
define DistinctNullNullNull: Ok(Equivalent(distinct { null, null, null}, null), 'DistinctNullNullNull')
define DistinctANullANull: Ok(Equivalent(distinct { 'a', null, 'a', null}, null), 'DistinctANullANull')
define Distinct112233: Ok(Equivalent(distinct { 1, 1, 2, 2, 3, 3}, null), 'Distinct112233')
define Distinct123123: Ok(Equivalent(distinct { 1, 1, 2, 2, 3, 3}, null), 'Distinct123123')
define DistinctAABBCC: Ok(Equivalent(distinct { 'a', 'a', 'b', 'b', 'c', 'c'}, null), 'DistinctAABBCC')
define DistinctABCABC: Ok(Equivalent(distinct { 'a', 'b', 'c', 'a', 'b', 'c'}, null), 'DistinctABCABC')
define DistinctDateTime: Ok(Equivalent(distinct { DateTime(2012, 10, 5), DateTime(2012, 1, 1), DateTime(2012, 1, 1)}, null), 'DistinctDateTime')
define DistinctTime: Ok(Equivalent(distinct { @T15:59:59.999, @T20:59:59.999, @T20:59:59.999 }, null), 'DistinctTime')

//Equal
define EqualNullNull: Ok(Equivalent({null} = {null}, null), 'EqualNullNull')
define EqualEmptyListNull: Ok(Equivalent({} = null, null), 'EqualEmptyListNull')
define EqualNullEmptyList: Ok(Equivalent(null = {}, null), 'EqualNullEmptyList')
define EqualEmptyListAndEmptyList: Ok(Equivalent({} = {}, null), 'EqualEmptyListAndEmptyList')
define Equal12And123: Ok(Equivalent({ 1, 2 } = { 1, 2, 3 }, null), 'Equal12And123')
define Equal123And12: Ok(Equivalent({ 1, 2, 3 } = { 1, 2 }, null), 'Equal123And12')
define Equal123And123: Ok(Equivalent({ 1, 2, 3 } = { 1, 2, 3 }, null), 'Equal123And123')
define EqualDateTimeTrue: Ok(Equivalent({DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)} = {DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)}, null), 'EqualDateTimeTrue')
define EqualDateTimeFalse: Ok(Equivalent({DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)} = {DateTime(2012, 1, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)}, null), 'EqualDateTimeFalse')
define EqualTimeTrue: Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999, @T20:59:59.999 } = { @T15:59:59.999, @T20:59:59.999, @T20:59:59.999 }, null), 'EqualTimeTrue')
define EqualTimeFalse: Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999, @T20:59:59.999 } = { @T10:59:59.999, @T20:59:59.999, @T20:59:59.999 }, null), 'EqualTimeFalse')

//Except
define ExceptEmptyListAndEmptyList: Ok(Equivalent(Except({}, {}), null), 'ExceptEmptyListAndEmptyList')
define Except1234And23: Ok(Equivalent({ 1, 2, 3, 4 } except { 2, 3 }, null), 'Except1234And23')
define Except23And1234: Ok(Equivalent({ 2, 3 } except { 1, 2, 3, 4 }, null), 'Except23And1234')
define ExceptDateTime: Ok(Equivalent({ DateTime(2012, 5, 10), DateTime(2014, 12, 10), DateTime(2010, 1, 1)} except {DateTime(2014, 12, 10), DateTime(2010, 1, 1) }, null), 'ExceptDateTime')
define ExceptTime: Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999, @T12:59:59.999 } except { @T20:59:59.999, @T12:59:59.999 }, null), 'ExceptTime')
define ExceptNullRight: Ok(Equivalent({ 1, 4 } except null, null), 'ExceptNullRight')

//Exists
define ExistsEmpty: Ok(Equivalent(Exists({}), null), 'ExistsEmpty')
define ExistsListNull: Ok(Equivalent(Exists({ null }), null), 'ExistsListNull')
define Exists1: Ok(Equivalent(Exists({ 1 }), null), 'Exists1')
define Exists12: Ok(Equivalent(Exists({ 1, 2 }), null), 'Exists12')
define ExistsDateTime: Ok(Equivalent(Exists({ DateTime(2012, 5, 10), DateTime(2014, 12, 10) }), null), 'ExistsDateTime')
define ExistsTime: Ok(Equivalent(Exists({ @T15:59:59.999, @T20:59:59.999 }), null), 'ExistsTime')
define ExistsNull: Ok(Equivalent(Exists(null), null), 'ExistsNull')

//Flatten
define FlattenEmpty: Ok(Equivalent(Flatten({{},{}}), null), 'FlattenEmpty')
define FlattenListNullAndNull: Ok(Equivalent(Flatten({{null}, {null}}), null), 'FlattenListNullAndNull')
define FlattenList12And34: Ok(Equivalent(Flatten({{1,2}, {3,4}}), null), 'FlattenList12And34')
define FlattenDateTime: Ok(Equivalent(Flatten({ {DateTime(2012, 5, 10)}, {DateTime(2014, 12, 10)} }), null), 'FlattenDateTime')
define FlattenTime: Ok(Equivalent(Flatten({ {@T15:59:59.999}, {@T20:59:59.999} }), null), 'FlattenTime')

//First
define FirstEmpty: Ok(Equivalent(First({}), null), 'FirstEmpty')
define FirstNull1: Ok(Equivalent(First({ null, 1 }), null), 'FirstNull1')
define First1Null: Ok(Equivalent(First({ 1, null }), null), 'First1Null')
define First12: Ok(Equivalent(First({ 1, 2 }), null), 'First12')
define FirstDateTime: Ok(Equivalent(First({ DateTime(2012, 5, 10), DateTime(2014, 12, 10) }), null), 'FirstDateTime')
define FirstTime: Ok(Equivalent(First({ @T15:59:59.999, @T20:59:59.999 }), null), 'FirstTime')

//In
define InNullEmpty: Ok(Equivalent(null in {}, null), 'InNullEmpty')
define InNullAnd1Null: Ok(Equivalent(null in { 1, null }, null), 'InNullAnd1Null')
define In1Null: Ok(Equivalent(1 in null, null), 'In1Null')
define In1And12: Ok(Equivalent(1 in { 1, 2 }, null), 'In1And12')
define In3And12: Ok(Equivalent(3 in { 1, 2 }, null), 'In3And12')
define InDateTimeTrue: Ok(Equivalent(DateTime(2012, 5, 10) in { DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10) }, null), 'InDateTimeTrue')
define InDateTimeFalse: Ok(Equivalent(DateTime(2012, 6, 10) in { DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10) }, null), 'InDateTimeFalse')
define InTimeTrue: Ok(Equivalent(@T15:59:59.999 in { @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 }, null), 'InTimeTrue')
define InTimeFalse: Ok(Equivalent(@T16:59:59.999 in { @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 }, null), 'InTimeFalse')

//Includes
define IncludesEmptyAndEmpty: Ok(Equivalent({} includes {}, null), 'IncludesEmptyAndEmpty')
define IncludesListNullAndListNull: Ok(Equivalent({null} includes {null}, null), 'IncludesListNullAndListNull')
define Includes123AndEmpty: Ok(Equivalent({1, 2, 3} includes {}, null), 'Includes123AndEmpty')
define Includes123And2: Ok(Equivalent({1, 2, 3} includes {2}, null), 'Includes123And2')
define Includes123And4: Ok(Equivalent({1, 2, 3} includes {4}, null), 'Includes123And4')
define IncludesDateTimeTrue: Ok(Equivalent({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} includes {DateTime(2012, 5, 10)}, null), 'IncludesDateTimeTrue')
define IncludesDateTimeFalse: Ok(Equivalent({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} includes {DateTime(2012, 5, 11)}, null), 'IncludesDateTimeFalse')
define IncludesTimeTrue: Ok(Equivalent({ @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 } includes @T15:59:59.999, null), 'IncludesTimeTrue')
define IncludesTimeFalse: Ok(Equivalent({ @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 } includes @T16:59:59.999, null), 'IncludesTimeFalse')
define IncludesNullLeft: Ok(Equivalent(null includes {2}, null), 'IncludesNullLeft')
// this test is going to the ContainsEvaluator
// define IncludesNullRight: {'s', 'a', 'm'} includes null

//IncludedIn
define IncludedInEmptyAndEmpty: Ok(Equivalent({} included in {}, null), 'IncludedInEmptyAndEmpty')
define IncludedInListNullAndListNull: Ok(Equivalent({ null } included in { null }, null), 'IncludedInListNullAndListNull')
define IncludedInEmptyAnd123: Ok(Equivalent({} included in { 1, 2, 3 }, null), 'IncludedInEmptyAnd123')
define IncludedIn2And123: Ok(Equivalent({ 2 } included in { 1, 2, 3 }, null), 'IncludedIn2And123')
define IncludedIn4And123: Ok(Equivalent({ 4 } included in { 1, 2, 3 }, null), 'IncludedIn4And123')
define IncludedInDateTimeTrue: Ok(Equivalent({ DateTime(2012, 5, 10)} included in {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)}, null), 'IncludedInDateTimeTrue')
define IncludedInDateTimeFalse: Ok(Equivalent({DateTime(2012, 5, 11)} included in {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)}, null), 'IncludedInDateTimeFalse')
define IncludedInTimeTrue: Ok(Equivalent(@T15:59:59.999 included in { @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 }, null), 'IncludedInTimeTrue')
define IncludedInTimeFalse: Ok(Equivalent(@T16:59:59.999 included in { @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 }, null), 'IncludedInTimeFalse')
// the following expression is going to the InEvaluator
// define IncludedInNullLeft: null included in {2}
define IncludedInNullRight: Ok(Equivalent({'s', 'a', 'm'} included in null, null), 'IncludedInNullRight')

//Indexer
define IndexerNull1 : Ok(Equivalent((null as List<System.Any>)[1], null), 'IndexerNull1') // Cast is required due to ambiguity with Indexer(String) and Indexer(List<Any>)
define Indexer0Of12 : Ok(Equivalent({ 1, 2 }[0], null), 'Indexer0Of12')
define Indexer1Of12 : Ok(Equivalent({ 1, 2 }[1], null), 'Indexer1Of12')
define Indexer2Of12 : Ok(Equivalent({ 1, 2 }[2], null), 'Indexer2Of12')
define IndexerNeg1Of12 : Ok(Equivalent({ 1, 2 }[-1], null), 'IndexerNeg1Of12')
define IndexerDateTime: Ok(Equivalent({ DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10) }[1], null), 'IndexerDateTime')
define IndexerTime: Ok(Equivalent({ @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 }[1], null), 'IndexerTime')

//IndexOf
define IndexOfEmptyNull : Ok(Equivalent(IndexOf({}, null), null), 'IndexOfEmptyNull')
define IndexOfNullEmpty : Ok(Equivalent(IndexOf(null, {}), null), 'IndexOfNullEmpty')
define IndexOfNullIn1Null : Ok(Equivalent(IndexOf({ 1, null }, null), null), 'IndexOfNullIn1Null')
define IndexOf1In12 : Ok(Equivalent(IndexOf({ 1, 2 }, 1), null), 'IndexOf1In12')
define IndexOf2In12 : Ok(Equivalent(IndexOf({ 1, 2 }, 2), null), 'IndexOf2In12')
define IndexOf3In12 : Ok(Equivalent(IndexOf({ 1, 2 }, 3), null), 'IndexOf3In12')
define IndexOfDateTime: Ok(Equivalent(IndexOf({ DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10) }, DateTime(2014, 12, 10)), null), 'IndexOfDateTime')
define IndexOfTime: Ok(Equivalent(IndexOf({ @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 }, @T15:59:59.999), null), 'IndexOfTime')

//Intersect
define IntersectEmptyListAndEmptyList: Ok(Equivalent({} intersect {}, null), 'IntersectEmptyListAndEmptyList')
define Intersect1234And23: Ok(Equivalent({ 1, 2, 3, 4 } intersect { 2, 3 }, null), 'Intersect1234And23')
define Intersect23And1234: Ok(Equivalent({2, 3} intersect { 1, 2, 3, 4 }, null), 'Intersect23And1234')
define IntersectDateTime: Ok(Equivalent({ DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10) } intersect { DateTime(2012, 5, 10), DateTime(2014, 12, 10), DateTime(2000, 5, 5) }, null), 'IntersectDateTime')
define IntersectTime: Ok(Equivalent({ @T02:29:15.156, @T15:59:59.999, @T20:59:59.999 } intersect { @T01:29:15.156, @T15:59:59.999, @T20:59:59.999 }, null), 'IntersectTime')

//Last
define LastEmpty: Ok(Equivalent(Last({}), null), 'LastEmpty')
define LastNull1: Ok(Equivalent(Last({null, 1}), null), 'LastNull1')
define Last1Null: Ok(Equivalent(Last({1, null}), null), 'Last1Null')
define Last12: Ok(Equivalent(Last({1, 2}), null), 'Last12')
define LastDateTime: Ok(Equivalent(Last({DateTime(2012, 5, 10), DateTime(2014, 12, 10)}), null), 'LastDateTime')
define LastTime: Ok(Equivalent(Last({ @T15:59:59.999, @T20:59:59.999 }), null), 'LastTime')

//Length
define LengthEmpty: Ok(Equivalent(Length({}), null), 'LengthEmpty')
define LengthNull1: Ok(Equivalent(Length({null, 1}), null), 'LengthNull1')
define Length1Null: Ok(Equivalent(Length({1, null}), null), 'Length1Null')
define Length12: Ok(Equivalent(Length({1, 2}), null), 'Length12')
define LengthDateTime: Ok(Equivalent(Length({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)}), null), 'LengthDateTime')
define LengthTime: Ok(Equivalent(Length({ @T15:59:59.999, @T20:59:59.999, @T15:59:59.999, @T20:59:59.999, @T15:59:59.999, @T20:59:59.999 }), null), 'LengthTime')
define LengthNull: Ok(Equivalent(Length(null as String), null), 'LengthNull')

//Equivalent
define EquivalentEmptyAndEmpty          : Ok(Equivalent({} ~ {}, null), 'EquivalentEmptyAndEmpty')
define EquivalentABCAndABC              : Ok(Equivalent({ 'a', 'b', 'c' } ~ { 'a', 'b', 'c' }, null), 'EquivalentABCAndABC')
define EquivalentABCAndAB               : Ok(Equivalent({ 'a', 'b', 'c' } ~ { 'a', 'b' }, null), 'EquivalentABCAndAB')
define EquivalentABCAnd123              : Ok(Equivalent({ 'a', 'b', 'c' } ~ { 1, 2, 3 }, null), 'EquivalentABCAnd123')
define Equivalent123AndABC              : Ok(Equivalent({ 1, 2, 3 } ~ { 'a', 'b', 'c' }, null), 'Equivalent123AndABC')
define Equivalent123AndString123        : Ok(Equivalent({ 1, 2, 3 } ~ { '1', '2', '3' }, null), 'Equivalent123AndString123')
define EquivalentDateTimeTrue           : Ok(Equivalent({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10), null} ~ {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10), null}, null), 'EquivalentDateTimeTrue')
define EquivalentDateTimeNull           : Ok(Equivalent({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} ~ {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10), null}, null), 'EquivalentDateTimeNull')
define EquivalentDateTimeFalse          : Ok(Equivalent({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} ~ {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 1)}, null), 'EquivalentDateTimeFalse')
define EquivalentTimeTrue               : Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999 } ~ { @T15:59:59.999, @T20:59:59.999 }, null), 'EquivalentTimeTrue')
define EquivalentTimeNull               : Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999 } ~ { @T15:59:59.999, @T20:59:59.999, null }, null), 'EquivalentTimeNull')
define EquivalentTimeFalse              : Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999 } ~ { @T15:59:59.999, @T20:59:59.995 }, null), 'EquivalentTimeFalse')

//NotEqual
define NotEqualEmptyAndEmpty : Ok(Equivalent({} != {}, null), 'NotEqualEmptyAndEmpty')
define NotEqualABCAndABC : Ok(Equivalent({ 'a', 'b', 'c' } != { 'a', 'b', 'c' }, null), 'NotEqualABCAndABC')
define NotEqualABCAndAB : Ok(Equivalent({ 'a', 'b', 'c' } != { 'a', 'b' }, null), 'NotEqualABCAndAB')
define NotEqualABCAnd123: Ok(Equivalent({ 'a', 'b', 'c' } != { 1, 2, 3 }, null), 'NotEqualABCAnd123')
define NotEqual123AndABC: Ok(Equivalent({ 1, 2, 3 } != { 'a', 'b', 'c' }, null), 'NotEqual123AndABC')
define NotEqual123AndString123: Ok(Equivalent({ 1, 2, 3 } != { '1', '2', '3' }, null), 'NotEqual123AndString123')
define NotEqualDateTimeTrue: Ok(Equivalent({DateTime(2001, 9, 11, 0, 0, 0, 0), DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)} != {DateTime(2001, 9, 11, 0, 0, 0, 0), DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 1, 0, 0, 0, 0)}, null), 'NotEqualDateTimeTrue')
define NotEqualDateTimeFalse: Ok(Equivalent({DateTime(2001, 9, 11, 0, 0, 0, 0), DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)} != {DateTime(2001, 9, 11, 0, 0, 0, 0), DateTime(2012, 5, 10, 0, 0, 0, 0), DateTime(2014, 12, 10, 0, 0, 0, 0)}, null), 'NotEqualDateTimeFalse')
define NotEqualTimeTrue: Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999 } = { @T15:59:59.999, @T20:59:59.999 }, null), 'NotEqualTimeTrue')
define NotEqualTimeFalse: Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999 } = { @T15:59:59.999, @T20:59:49.999 }, null), 'NotEqualTimeFalse')

//ProperlyIncludes
define ProperIncludesEmptyAndEmpty: Ok(Equivalent({} properly includes {}, null), 'ProperIncludesEmptyAndEmpty')
define ProperIncludesListNullAndListNull: Ok(Equivalent({null} properly includes {null}, null), 'ProperIncludesListNullAndListNull')
define ProperIncludes123AndEmpty: Ok(Equivalent({1, 2, 3} properly includes {}, null), 'ProperIncludes123AndEmpty')
define ProperIncludes123And2: Ok(Equivalent({1, 2, 3} properly includes {2}, null), 'ProperIncludes123And2')
define ProperIncludes123And4: Ok(Equivalent({1, 2, 3} properly includes {4}, null), 'ProperIncludes123And4')
define ProperIncludesDateTimeTrue: Ok(Equivalent({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} properly includes {DateTime(2012, 5, 10), DateTime(2014, 12, 10)}, null), 'ProperIncludesDateTimeTrue')
define ProperIncludesDateTimeFalse: Ok(Equivalent({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} properly includes {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)}, null), 'ProperIncludesDateTimeFalse')
define ProperIncludesTimeTrue: Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999, @T20:59:49.999 } properly includes { @T15:59:59.999, @T20:59:59.999 }, null), 'ProperIncludesTimeTrue')
define ProperIncludesTimeFalse: Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999, @T20:59:49.999 } properly includes { @T15:59:59.999, @T20:59:59.999, @T14:59:22.999 }, null), 'ProperIncludesTimeFalse')
define ProperlyIncludesNullLeft: Ok(Equivalent(null properly includes {2}, null), 'ProperlyIncludesNullLeft')
// the following expression is going to the ProperContains ELM
// define ProperlyIncludesNulRight: {'s', 'u', 'n'} properly includes null

//ProperlyIncludedIn
define ProperIncludedInEmptyAndEmpty: Ok(Equivalent({} properly included in {}, null), 'ProperIncludedInEmptyAndEmpty')
define ProperIncludedInListNullAndListNull: Ok(Equivalent({null} properly included in {null}, null), 'ProperIncludedInListNullAndListNull')
define ProperIncludedInEmptyAnd123: Ok(Equivalent({} properly included in {1, 2, 3}, null), 'ProperIncludedInEmptyAnd123')
define ProperIncludedIn2And123: Ok(Equivalent({2} properly included in {1, 2, 3}, null), 'ProperIncludedIn2And123')
define ProperIncludedIn4And123: Ok(Equivalent({4} properly included in {1, 2, 3}, null), 'ProperIncludedIn4And123')
define ProperIncludedInDateTimeTrue: Ok(Equivalent({DateTime(2012, 5, 10), DateTime(2014, 12, 10)} properly included in {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)}, null), 'ProperIncludedInDateTimeTrue')
define ProperIncludedInDateTimeFalse: Ok(Equivalent({DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)} properly included in {DateTime(2001, 9, 11), DateTime(2012, 5, 10), DateTime(2014, 12, 10)}, null), 'ProperIncludedInDateTimeFalse')
define ProperIncludedInTimeTrue: Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999 } properly included in { @T15:59:59.999, @T20:59:59.999, @T20:59:49.999 }, null), 'ProperIncludedInTimeTrue')
define ProperIncludedInTimeFalse: Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999, @T14:59:22.999 } properly included in { @T15:59:59.999, @T20:59:59.999, @T20:59:49.999 }, null), 'ProperIncludedInTimeFalse')
// the following expression is going to the ProperIn ELM
// define ProperlyIncludedInNullLeft: null properly included in {2}
define ProperlyIncludedInNulRight: Ok(Equivalent({'s', 'u', 'n'} properly included in null, null), 'ProperlyIncludedInNulRight')

//SingletonFrom
define SingletonFromEmpty: Ok(Equivalent(singleton from {}, null), 'SingletonFromEmpty')
define SingletonFromListNull: Ok(Equivalent(singleton from {null}, null), 'SingletonFromListNull')
define SingletonFrom1: Ok(Equivalent(singleton from { 1 }, null), 'SingletonFrom1')
define SingletonFrom12: Ok(Equivalent(singleton from { 1, 2 }, null), 'SingletonFrom12')
define SingletonFromDateTime: Ok(Equivalent(singleton from { DateTime(2012, 5, 10) }, null), 'SingletonFromDateTime')
define SingletonFromTime: Ok(Equivalent(singleton from { @T15:59:59.999 }, null), 'SingletonFromTime')

//Skip
define SkipNull: Ok(Equivalent(Skip(null, 3), null), 'SkipNull')
define SkipEven: Ok(Equivalent(Skip({1,2,3,4,5}, 2), null), 'SkipEven')
define SkipOdd: Ok(Equivalent(Skip({1,2,3,4,5}, 3), null), 'SkipOdd')
define SkipNone: Ok(Equivalent(Skip({1,2,3,4,5}, 0), null), 'SkipNone')
define SkipAll: Ok(Equivalent(Skip({1,2,3,4,5}, 5), null), 'SkipAll')

//Tail
define TailNull: Ok(Equivalent(Tail(null), null), 'TailNull')
define TailEven: Ok(Equivalent(Tail({1,2,3,4}), null), 'TailEven')
define TailOdd: Ok(Equivalent(Tail({1,2,3,4,5}), null), 'TailOdd')
define TailEmpty: Ok(Equivalent(Tail({}), null), 'TailEmpty')
define TailOneElement: Ok(Equivalent(Tail({1}), null), 'TailOneElement')

//Take
define TakeNull: Ok(Equivalent(Take(null, 3), null), 'TakeNull')
define TakeNullEmpty: Ok(Equivalent(Take({1,2,3}, null), null), 'TakeNullEmpty')
define TakeEmpty: Ok(Equivalent(Take({1,2,3}, 0), null), 'TakeEmpty')
define TakeEven: Ok(Equivalent(Take({1,2,3,4}, 2), null), 'TakeEven')
define TakeOdd: Ok(Equivalent(Take({1,2,3,4}, 3), null), 'TakeOdd')
define TakeAll: Ok(Equivalent(Take({1,2,3,4}, 4), null), 'TakeAll')

//Union
define UnionEmptyAndEmpty: Ok(Equivalent({} union {}, null), 'UnionEmptyAndEmpty')
define UnionListNullAndListNull: Ok(Equivalent({ null } union { null }, null), 'UnionListNullAndListNull')
define Union123AndEmpty: Ok(Equivalent({ 1, 2, 3 } union {}, null), 'Union123AndEmpty')
define Union123And2: Ok(Equivalent({ 1, 2, 3 } union { 2 }, null), 'Union123And2')
define Union123And4: Ok(Equivalent({ 1, 2, 3 } union { 4 }, null), 'Union123And4')
define UnionDateTime: Ok(Equivalent({ DateTime(2001, 9, 11)} union {DateTime(2012, 5, 10), DateTime(2014, 12, 10) }, null), 'UnionDateTime')
define UnionTime: Ok(Equivalent({ @T15:59:59.999, @T20:59:59.999, @T12:59:59.999 } union { @T10:59:59.999 }, null), 'UnionTime')

// Sort
/*define intList: { 3, 2, 1 }
define decimalList: Ok(Equivalent({ 3.8, 2.4, 1.9 }, null), 'decimalList')
define quantityList: Ok(Equivalent({ 19.99 'lbs', 17.33 'lbs', 10.66 'lbs' }, null), 'quantityList')
define dateTimeList: Ok(Equivalent({ DateTime(2016), DateTime(2015), DateTime(2010) }, null), 'dateTimeList')
define timeList: Ok(Equivalent({ @T15:59:59.999, @T15:12:59.999, @T15:12:13.999 }, null), 'timeList')*/


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
