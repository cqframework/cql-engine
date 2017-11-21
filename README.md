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

Note: This section is still being written.

The following commands will build the CQL Evaluation Engine and then
execute the CQL engine validation suite against it.
The syntax is specific to Unix-like operating systems.
You need to "cd" into the main directory of the project first.
You need to install Java and Gradle before running the commands.

The "1" argument to test_harness() makes the displayed test results verbose
with one line per individual test; making it "0" will make 1 line per file.

Perl already comes bundled with all Unix-like systems so doesn't need
installing; however, the test harness one-liner using it is subject to be
replaced later with a Java-based solution that may require extra modules.

```
gradle fatJar
chmod 755 ./cql-runner
HARNESS_PERL=./cql-runner perl -MExtUtils::Command::MM -MTest::Harness -e "undef *Test::Harness::Switches; test_harness(1)" ./cql-validation-tests/*.cql
```
