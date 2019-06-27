# DON'T MERGE JUST TESTING

# CQL Evaluation Engine

The HL7 Clinical Quality Language specification is designed to enable accurate authoring and automated sharing of computable knowledge artifacts including quality measures, decision support rules, orders sets and documentation templates. The specification is constructed using a layered approach informed by modern programming language and compiler design which facilitates the development of language processing applications including static analysis, translation, and evaluation.

The specification includes informative open source tooling that can be used to verify the semantics of CQL libraries; to produce the sharable format, Expression Logical Model (ELM), of those libraries; and to evaluate the resulting libraries using a prototypical JavaScript-based ELM interpreter.

This project builds on that tooling to provide an open source Java-based evaluation engine capable of evaluating the result of any CQL expression.

# Repository

This repository uses stable trunk methodology:

|Branch|Description|Status|
|v12|CQL 1.2 Engine|Maintenance|
|master|CQL 1.3 Engine|Release|

# Commit Policy

Changes to new features branches may be committed directly if desired, but PRs are preferred. Changes to master and maintenance branches must be done through an approved PR. Delete branches after merging to keep the repository clean.

# Release Policy

This project uses [Semantic Versioning](http://semver.org), with the caveat that we track to the version of CQL the engine supports. Releases are published to Maven snapshot and public directories under the org.opencds.cqframework group id. Each release SHALL have a Release in Github. Pre-releases SHALL be marked as such and use the -SNAPSHOT version indicator. For any new release, a SNAPSHOT is released first and must pass integration testing in at least one external system prior to being promoted to a release. SNAPSHOTs may be published from any branch, but SHALL have incremented version numbers consistent with the branch and semantic versioning policies. Releases may only be published from the master or maintenance branch. Only one prior version is maintained at any given time.

# Roadmap

* 1.3.8 - Improved exception handling and stable
* 1.3.9 - Debugging/tracing/coverage
* 1.3.10 - FHIR R4 Support
* 1.4 - CQL 1.4 Support

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
