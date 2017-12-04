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

The CQL engine validation suite is comprised of the CQL and ELM files in
the "cql-validation-tests" directory; running the validation suite involves
executing each file in turn from that directory against the Engine and then
analyzing the output of those files; if every output line starts with "ok"
then the tests succeeded; if any output line starts with "not ok" then its
corresponding test failed.

To ensure that the outputs of the validation suite are correctly formatted
TAP (Test Anything Protocol) so that a standard TAP harness can correctly
parse and summarize the results, you might need to modify the configuration
file "./cql-engine/src/main/resources/log4j.properties" so that only the
verbatim message text of each CQL/ELM Message() call is output.

You do this BEFORE the "gradle fatJar" step, or you repeat that step after,
in order for the altered config file to be used.

The appropriate configuration line should look like this:

```
log4j.appender.STDOUT.layout.ConversionPattern=%m%n
```

One commonly used TAP harness is bundled with every standard Perl
installation; you can use it to run the whole validation suite like this:

```
HARNESS_PERL=./cql-runner perl -MExtUtils::Command::MM -MTest::Harness -e "undef *Test::Harness::Switches; test_harness(0)" ./cql-validation-tests/*.*
```

That would then produce output which looks like this:

```
./cql-validation-tests/one.cql .. ok
./cql-validation-tests/two.cql .. ok
All tests successful.
Files=2, Tests=4,  4 wallclock secs ( 0.01 usr  0.00 sys +  6.91 cusr  0.46 csys =  7.38 CPU)
Result: PASS
```

The argument to test_harness() when "1" makes the displayed results verbose
with one line per individual test; making it "0" will make 1 line per file.

Perl already comes bundled with all Unix-like systems so doesn't need
installing; on Windows it can be installed easily.

However, in the near future, instructions and/or a wrapper script that uses
a TAP harness written in Java instead will be provided, so that one can run
a harness without using Perl; the Java solution may require extra modules.
