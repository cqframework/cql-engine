# CQL Evaluation Engine

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.opencds.cqf.cql/engine/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.opencds.cqf.cql/engine) [![Build Status](https://www.travis-ci.com/DBCG/cql_engine.svg?branch=master)](https://www.travis-ci.com/DBCG/cql_engine) [![project chat](https://img.shields.io/badge/zulip-join_chat-brightgreen.svg)](https://chat.fhir.org/#narrow/stream/179220-cql)

The HL7 Clinical Quality Language specification is designed to enable accurate authoring and automated sharing of computable knowledge artifacts including quality measures, decision support rules, orders sets and documentation templates. The specification is constructed using a layered approach informed by modern programming language and compiler design which facilitates the development of language processing applications including static analysis, translation, and evaluation.

The specification includes informative open source tooling that can be used to verify the semantics of CQL libraries; to produce the sharable format, Expression Logical Model (ELM), of those libraries; and to evaluate the resulting libraries using a prototypical JavaScript-based ELM interpreter.

This project builds on that tooling to provide an open source Java-based evaluation engine capable of evaluating the result of any CQL expression.

## Repository

This repository uses stable trunk methodology:

|Branch|Description|Status|Version
|----|----|----|----|
|v12|CQL 1.2 Engine|Obsolete|1.2.20|
|v13|CQL 1.3 Engine|Maintenance|1.3.12.2|
|v14|CQL 1.4 Engine|Maintenance|1.4.0|
|master|CQL 1.5 Engine|Active Development|1.5.1|

## Commit Policy

All new development takes place on `<feature>` branches off `master`. Once feature development on the branch is complete, the feature branch is submitted to `master` as a PR. The PR is reviewed by maintainers and regression testing by the CI build occurs.

Changes to the `master` branch must be done through an approved PR. Delete branches after merging to keep the repository clean.

Merges to `master` trigger a deployment to the Maven Snapshots repositories. Once ready for a release, the `master` branch is updated with the correct version number and is tagged. Tags trigger a full release to Maven Central and a corresponding release to Github. Releases SHALL NOT have a SNAPSHOT version, nor any SNAPSHOT dependencies.

On release, committers must ensure that:

1. The `major`, `minor`, and `patch` build properties are incremented appropriately.
2. Increment the `minor` build property on the `master` branch (to be automated by [#316](https://github.com/DBCG/cql_engine/issues/316))

## Release Policy

This project uses [Semantic Versioning](http://semver.org), with the caveat that we track to the version of CQL the engine supports. Releases are published to Maven snapshot and public directories under the org.opencds.cqf.cql group id. Each release SHALL have a Release in Github. Pre-releases SHALL be marked as such and use the -SNAPSHOT version indicator. For any new release, a SNAPSHOT is released first and must pass integration testing in at least one external system prior to being promoted to a release. SNAPSHOTs may be published from any branch, but SHALL have incremented version numbers consistent with the branch and semantic versioning policies. Releases may only be published from the master or maintenance branches. Only one prior version is maintained at any given time.

## Roadmap

* 1.4 - CQL 1.4 Support
* 1.5 - CQL 1.5 Support
* 1.5.0 - Initial release candidate
* 1.5.1 - Minor maintenance/stability fixes
* 1.5.2 - PHI Obfuscation, Long data type, performance enhancements, bug fixes
* 1.5.3 - Enhanced retrieve: profile-retrieve, context-based retrieves, includes, search parameters, inferred expression support, FHIRPath test suite, improved debugging capabilities, code coverage, date filter support for data providers
* 1.5.N - Support for concept mapping with versioned manifest usage and/or concept map configuration, CQL specification test suite, data provenance propagation, result meta-data tagging

## Getting Help

Bugs and feature requests can be filed with [Github Issues](https://github.com/DBCG/cql_engine/issues).

The implementers are active on the official FHIR [Zulip chat for CQL](https://chat.fhir.org/#narrow/stream/179220-cql).

Inquires for commercial support can be directed to [info@alphora.com](info@alphora.com).

## Related Projects

[Clinical Quality Language](https://github.com/cqframework/clinical_quality_language) - Tooling in support of the CQL specification, including the CQL verifier/translator used in this project.

[CQL Evaluator](https://github.com/DBCG/cql-evaluator) - Integrates the CQL Translator and this CQL Engine into an execution environment, and provides implementations of operations defined by FHIR IGs.

[CQL Support for Atom](https://atom.io/packages/language-cql) - Open source CQL IDE with syntax highlighting, linting, and local CQL evaluation.

[CQF Ruler](https://github.com/DBCG/cqf-ruler) - Integrates this CQL Engine into the HAPI FHIR server, providing CQL Library evaluation, among other functionality.

## License

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
