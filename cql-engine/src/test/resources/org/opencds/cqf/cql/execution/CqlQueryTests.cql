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