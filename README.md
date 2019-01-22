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
