---
layout: page
title: eCQM Architecture
nav_order: 2
---

## eCQM Overview

Clinical Quality Measure calculation involves the following conceptual components:

**Model** - The structured representation of clinical information. Examples include FHIR and QDM.

**Logic** - The description of the measure criteria.

**Data Access** - Allows access to instances of clinical information.

**Terminology** - Is concerned with membership testing and value set expansion.

**Engine** - The runtime system that performs the calculations by executing that logic against the data.

**Libraries** - Allow the reuse of logic.

These components fit together as shown in this diagram.

![eCQM Components](/images/ecqm_components.drawio.png)

## Typical eCQM Calculation Architecture

These components are present in current calculation systems, though they may be implemented differently in different environments. For example, a simple MSSQL Server implementation may have:

**Model** defined as tables in the database.

**Logic** defined as stored procedures in the database.

**Data Access** is then performed by the SQL Server itself via index access.

**Terminology** may be represented as additional tables and applied using filters and joins in the logic. 

**Engine** in this case is then the Microsoft SQL Server itself

**Libraries** of commonly used patterns in the measure definitions may be abstracted as additional stored procedures.

As another example, the measure calculation may be performed in a service layer in a platform such as .NET. In this case:

**Model** is defined as .NET Classes representing clinical information.

**Logic** is defined as methods in a .NET language such as C#.

**Data Access** is then performed by a service layer to access documents or tables.

**Terminology** may be provided by a full terminology service, or by catching relevant terminologies.

**Engine** in this case is then the middleware service layer in the .NET that actually performs the calculations.

**Libraries** in this case are just .NET assemblies containing commonly used calculation methods.
  
## Current HQMF eCQM Calculation Architecture

Building on this example for an HQMF calculation environment specifically:

**Model** may be generated HL7 V3 classes or tooling.

**Data Access** is then performed by a service layer to access documents or tables.

**Terminology** may be provided by a full terminology service, or by caching relevant terminologies.

**Libraries** in this case are just .NET assemblies containing commonly used calculation methods.

**Engine** in this case is then the middleware service layer in .NET that actually performs the calculations.

**Translator** Provides conversion from an input format (currently HQMF) to the target logic. 

![CQL Diagram 3](/images/cql_diagram3.png)

## Near Term HQMF/CQL eCQM Architecture

If the environment already has a translation component, the transition to CQL involves changing the translator to use ELM, rather than HQMF as the source for themeasure definitions. All other Components should be able to remain the same in this environment:

**Model** may be generated HL7 V3 classes or tooling. 

**Data Access** is then performed by a service layer to access documents or tablets. 

**Terminology** may be provided by a full terminology service, or by caching relevant terminologies.

**Libraries** in this case are just .NET assemblies containing commonly used calculation methods.

**Engine** in this case is then the middleware service layer in .NET that actually performs the calculations. 

**Translator** provides conversion from an input format to the target logic. 

![CQL Diagram 4](/images/cql_diagram4.png)

## Alternative HQMF/CQL eCQM Architecture

An alternative enabled by using CQL is to use a native CQL/ELM engine. In this alternative, the vendor focus would be on development of the Data Access layer component, and using an open souce engine implementation:

**Model** may be generated HL7 V3 classes or tooling. 

**Data Access** is then performed by a service layer to access documents or tables. 

**Terminology** may be provided by a full terminology service, or by caching relevant terminologies.

**Engine** in this case is then a specific implementation with plug-ins for each of the conceptual components. 

**Libraries** in this case are just .NET assemblies containing commonly used calculation methods. 

These components fit together as shown in this diagram.

![CQL Diagram 5](/images/cql_diagram5.png)

## CQL Execution 

![CQL Diagram 6](/images/cql_diagram6.png)

## HAPI JPA Plugin

![CQL Diagram 7](/images/cql_diagram7.png)









