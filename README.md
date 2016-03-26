# QCSP schedule evaluator

***A Java8 library to calculate the makespan of a schedule for the Quay Crane Scheduling Problem for Contianer Group***

**Continuous Integration:**<br />
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.robertotru/quay-crane-schedule-evaluator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.robertotru/quay-crane-schedule-evaluator)
<br />

[![Build Status](https://travis-ci.org/robertotru/quay-crane-schedule-evaluator.svg?branch=master)](https://travis-ci.org/robertotru/quay-crane-schedule-evaluator) [![Coverage Status](https://coveralls.io/repos/robertotru/quay-crane-schedule-evaluator/badge.svg?branch=master&service=github)](https://coveralls.io/github/robertotru/quay-crane-schedule-evaluator?branch=master)
[![codecov.io](https://codecov.io/github/robertotru/quay-crane-schedule-evaluator/coverage.svg?branch=master)](https://codecov.io/github/robertotru/quay-crane-schedule-evaluator?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/7a4364b93df6473fb18a597e900edceb)](https://www.codacy.com/app/roberto-trunfio/quay-crane-schedule-evaluator)

![codecov.io](https://codecov.io/github/robertotru/quay-crane-schedule-evaluator/branch.svg?branch=master)

## How to get the dependency
Embed the dependency from Maven

```xml
<dependency>
  <groupId>com.github.robertotru</groupId>
  <artifactId>quay-crane-schedule-evaluator</artifactId>
  <version>0.1.0</version>
</dependency>
```

You can also download the jar from the release tab in _GitHub_.

## How to use
Create an instance of  `AssignmentExactEvaluator`. The constructor takes all the instance required data (see _Bierwirth and Meisel 2009_ for more details on the adopted notation). Use the method `getMakespan` to compute a makespan for a given task-to-quay crane assignment (embedded in an instance of class `Assignment`).
