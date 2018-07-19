library CqlIntervalValuesTests version '1'

define Open: Interval(DateTime(2012, 1, 1), DateTime(2013, 1, 1))
define LeftOpen: Interval(DateTime(2012, 1, 1), DateTime(2013, 1, 1)]
define RightOpen: Interval[DateTime(2012, 1, 1), DateTime(2013, 1, 1))
define Closed: Interval[DateTime(2012, 1, 1), DateTime(2013, 1, 1)]
