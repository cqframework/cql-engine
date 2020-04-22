library Issue39 version '1'

context Population

// Interval
define x:
    Interval[
        @2017-12-20T11:00:00,
        DateTime(
            year from (@2017-12-20T11:00:00 + 1 day),
            month from (@2017-12-20T11:00:00 + 1 day),
            day from (@2017-12-20T11:00:00 + 1 day),
            0, 0, 0, 0,
            timezone from (@2017-12-20T11:00:00 + 1 day)
        )
    )
define y: Interval [ @2017-12-20T11:00:00, @2017-12-20T23:59:59.999 ]
define EquivalentIntervals: Equivalent(x,y)
