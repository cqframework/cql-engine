# CQL Evaluation Engine

The HL7 Clinical Quality Language specification is designed to enable accurate authoring and automated sharing of computable knowledge artifacts including quality measures, decision support rules, orders sets and documentation templates. The specification is constructed using a layered approach informed by modern programming language and compiler design which facilitates the development of language processing applications including static analysis, translation, and evaluation.

The specification includes informative open source tooling that can be used to verify the semantics of CQL libraries; to produce the sharable format, Expression Logical Model (ELM), of those libraries; and to evaluate the resulting libraries using a prototypical JavaScript-based ELM interpreter.

This project builds on that tooling to provide an open source Java-based evaluation engine capable of evaluating the result of any CQL expression.

# License

Copyright 2016 University of Utah

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

# How to Use

The following commands will build the CQL Evaluation Engine and then
execute a CQL/ELM file or the CQL engine validation suite against it.
The syntax is specific to Unix-like operating systems.
You need to "cd" into the main directory of the project first.
You need to install Java and Gradle before running the commands.

This is all you need to do to simply build the CQL Evaluation Engine:

```
gradle fatJar
```

After the Engine is built, this is the most direct way to execute any CQL
or ELM file:

```
java -jar ./cql-engine/build/libs/cql-engine-all-1.2.37-SNAPSHOT.jar filename
```

A simple wrapper script exists for the above so you can say this instead:

```
./cql-runner filename
```

However you have to mark that wrapper script as executable first (once),
such as with this command:

```
chmod 755 ./cql-runner
```

# Testing the Engine

The CQL Evaluation Engine comes with a semi-comprehensive validation suite
that is intended to be portable across all CQL implementations.

While the suite is not yet fully comprehensive, it should have enough
coverage such that users of any CQL implementation passing it should not
encounter any gaps or bugs during typical usage.

(A potential future usage of the test suite would be to define CQL itself,
such that the very definition of a valid CQL implementation is one that
passes all of the tests in the suite; but that is not an official plan.)

At present the validation suite itself is comprised entirely of the set of
XML files in this sub-directory of the project:

```
./cql-engine/src/test/resources/org/opencds/cqf/cql/execution/TestCqlExprsAndLibs
```

Those files are intended to be moved to a more convenient and non-buried
location later, but for now that is where they live.

The vast majority of the validation suite, called *TestIsolatedCqlExprs*,
is fundamentally composed of a large set of isolated CQL expressions such
that each expression Q is paired with the CQL value A we expect Q to
evaluate to.  Here is an example:

```
<test name="Add1D1D">
    <expression>1.0 + 1.0</expression>
    <output>2.0</output>
</test>
```

For most *TestIsolatedCqlExprs* tests, the definition of a passing test is
one where Q (the `expression`) evaluates without error, and A (the
`output`) evaluates without error, and `Equivalent(Q,A)` both evaluates
without error and results in `true`.

A complementary subset of tests however are for situations where we expect
an error WILL occur.  Here is an example:

```
<test name="PredecessorUnderflowT">
    <expression invalid="true">predecessor of @T00:00:00.000</expression>
    <!-- EXPECT: The result of the predecessor operation precedes the minimum value allowed for the type -->
</test>
```

For these tests, only those having `invalid="true"`, the definition of a
passing test is one where Q evaluates WITH an error.

A smaller subset of the validation suite, called *TestHolisticCqlLibs*, is
fundamentally composed of a set of isolated CQL library definitions such
that each library defines 1 or more boolean-resulting `define` statements
that are expected to evaluate to `true`.  Here is an example:

```
<test name="Issue33">
    <expression>
        define private TestDateTime1: @2017-12-20T11:00:00

        define private function ToDate(Value DateTime):
            DateTime(year from Value, month from Value, day from Value, 0, 0, 0, 0, timezone from Value)

        define private function CalendarDayOf(Value DateTime):
            Interval[Value, ToDate((Value + 1 day)))

        define private Issue33: CalendarDayOf(TestDateTime1)

        define private Issue33_A: Interval [ @2017-12-20T11:00:00, @2017-12-20T23:59:59.999 ]

        define public Issue33_C: Equivalent(Issue33,Issue33_A)
    </expression>
</test>
```

For these tests, the definition of a passing test is a niladic `define`
that is `public` which results in the Boolean `true` value when evaluated.
Any other entities declared in the CQL library are just intended to support
those, and where applicable need to be declared `private`, so there is no
attempt to evaluate them directly.

At present the formats of the validation suite limit its expressivity.
For the *TestIsolatedCqlExprs* format, only the features of CQL that are
definable as a single self-contained anonymous value expression can be
tested.  The *TestHolisticCqlLibs* format is a proper superset of that in
capability, adding support for testing features of CQL that require the
interaction of multiple named `define` and/or an entire CQL library.
However, any CQL feature that uses external resources can not be tested by
this suite.  But an extension of the validation suite is intended in the
future which covers these features.

See the following Java program, which is a test framework for the
validation suite:

```
./cql-engine/src/test/java/org/opencds/cqf/cql/execution/TestCqlExprsAndLibs.java
```

Executing the above will evaluate the CQL Evaluation Engine against the
validation suite and produce a report of which tests it passes or fails
along with how it failed when such things happen.

That program is written against the TestNG Java testing framework.  As far
as TestNG is concerned, the entire suite is 1 test which passes if the
framework completes without throwing an exception, which should always be
the case.  Thus when the test suite is run via `gradle build` it will most
likely report success, even if some individual tests failed.  To see the
actual test results you will need to read the program's standard output.

A recommended way to test the CQL Evaluation Engine with the validation
suite is to open the project in an IDE such as IntelliJ IDEA, have it
execute `TestCqlExprsAndLibs.java` (IntelliJ IDEA automatically knows how
to deal with TestNG programs), and examine its output.

To use the validation suite with your own CQL implementation, just copy the
set of XML files to a location appropriate for you and reimplement the main
logic of `TestCqlExprsAndLibs.java` appropriately for your implementation.

In the future we may also have a separate option such that the set of XML
files can be automatically reformatted into a single file of some kind that
may be more conducive to its intended use than the current set of files is.
However, the present multi-file organization is its canonical format, and
more conducive to development of the test suite itself, which has a 3-level
namespace for organizing the tests.
