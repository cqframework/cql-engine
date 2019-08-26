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
