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


// #152: Without not working with precision between
define dummyA:
  {
    billablePeriod: {
      "start": DateTime(2012,1,1,12,0,0,0),
      "end": DateTime(2012,1,31,23,59,59,999)
    }
  }

define dummyB:
  {
    period: {
      "start": DateTime(2012,1,1,13,0,0,0),
      "end": DateTime(2012,1,1,14,0,0,0)
    }
  }

define ClaimWithQualifiyingPOSWithoutEncounter:
  dummyA C
    without dummyB E
      such that hours between C.billablePeriod."start"
        and E.period."start" <= 24