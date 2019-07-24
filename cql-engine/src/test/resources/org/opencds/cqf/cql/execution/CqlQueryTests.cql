library CqlQueryTests version '1'

context Patient

// #204: Unexpected results
// list [1:1] [A, B, C, D]
// RightShift [4:1] [null, A, B, C]
// LeftShift [9:1] [null, A, B, C]
// LeftShift2 [14:1] [B, C, D, null]
define list:
{ 'A', 'B', 'C', 'D' }

define RightShift:
  list L1
    let L2: list[IndexOf(list, L1) - 1]
  return L2

define LeftShift:
  list L1
    let L2: list[IndexOf(list, L1) + 1]
  return L2

define LeftShift2:
  list L1
    let L3: list[IndexOf(list, L1) + 1]
  return L3

// #196: Unexpected results
// Multisource [10:1] Index: 0, Size: 0
define "a":
  {
    { code: 1, periods: {Interval[1, 2], Interval[3, 4]} }
  }
define "b":
  {
    { code: 1, periods: {Interval[1, 2], Interval[3, 4]} }
  }

define "Multisource":
from "a" A, "b" B

// More complex scenario
define "aa":
  {
    { code: 1, periods: {Interval[1, 2], Interval[3, 4]} },
    { code: 2, periods: {Interval[1, 2], Interval[3, 4]} }
  }
define "bb":
  {
    { code: 1, periods: {Interval[1, 2], Interval[3, 4]} },
    { code: 11, periods: {Interval[1, 2], Interval[3, 4]} }
  }

define "Complex Multisource":
from "aa" A, "bb" B

define "List 1":
  ({1, 2, 3}) i
    return i

define "List 2":
  ({4, 5, 6}) i
    return i

define "List of Lists":
  { "List 1", "List 2" }

// Issue #208: Could not resolve let expression reference 'renamedIntegerList' in library 'repro_let_tuple'.
define "Let Test Fails":
  "List of Lists" IntegerList
    let renamedIntegerList: (IntegerList i where 1 = 1)
    //let renamedIntegerList: IntegerList
  return renamedIntegerList
