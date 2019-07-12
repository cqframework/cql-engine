library CqlQueryTests version '1'

context Patient

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


define "List 1":
  ({1, 2, 3}) i
    return i

define "List 2":
  ({4, 5, 6}) i
    return i

define "List of Lists":
  { "List 1", "List 2" }

define "Let Test Fails":
  "List of Lists" IntegerList
    let renamedIntegerList: (IntegerList i where 1 = 1)
    //let renamedIntegerList: IntegerList
  return renamedIntegerList