---
# Feel free to add content and custom Front Matter to this file.
# To modify the layout, see https://jekyllrb.com/docs/themes/#overriding-theme-defaults

layout: home
title: Home
nav_order: 1
---

## Introduction

[Clinical Quality Language](https://cql.hl7.org/) is a high-level, domain-specific language focused on clinical quality and targeted at measure and decision support artifact authors. CQL provides a way to express clincial logic that can be translated into a machine-readable form (ELM) and evaluated against clinical artifacts.

The [CQL Engine](https://github.com/DBCG/cql_engine) is a java component that allows the evaluation of ELM against data and terminology providers in order to evaluate clinical logic.

## Usage

The CQL Engine is published on Maven Central. You can reference as follows:

```xml
<dependency>
  <groupId>org.opencds.cqf</groupId>
  <artifactId>cql-engine</artifactId>
  <version>1.3.9</version>
</dependency>
```

Here's some sample Java code:

```java
// Translate a CQL Library to ELM. See the CQL Translator project.
Library library = translateLibrary(code, getLibraryManager(), getModelManager());

// Create an evalution context
Context context = new Context(library);

// Register Data and Terminology Providers
context.registerDataProvider("http://hl7.org/fhir", new FHIRDataProvider());
context.registerTerminologyProvider(new FHIRTerminologyProvider());

// Get the results of the Library definitions
for (ExpressionDef def : library.getStatements().getDef())
{
    Object expressionResult = def.getExpression().evaluate(context);
}
```
