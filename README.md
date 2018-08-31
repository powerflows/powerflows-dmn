[![Build Status](https://travis-ci.org/powerflows/powerflows-dmn.svg?branch=master)](https://travis-ci.org/powerflows/powerflows-dmn)

# About Power Flows DMN
Power Flows DMN - a powerful decision engine.

# DMN model
Power Flows model has been designed as an easy to describe and maintain file. The file contains information about input and output data. The additional division is sections with fields and rules.

Power Flows supports the model in the following formats:
* YAML file;
* Java / Groovy file.
    * Fluent
    * Functional

## YAML file

```yaml
id: sample_decision_id
name: Sample Decision Name
hit-policy: UNIQUE
expression-type: GROOVY
fields:
  in:
    age:
      expression-type: FEEL
      expression: toYear(now()) - toYear(birthDate)
      type: INTEGER
      description: This is something about age
    colour:
      type: STRING
      description: This is something about colour
  out:
    allow:
      type: BOOLEAN
      description: We expect a decision, if we have access to do it
rules:
  - description: 3 allows always
    in:
      age: 3
    out:
      allow: true
  - in:
      age: 8
      colour: red
    out:
      allow: true
  - description: Green allows always
    in:
      colour: green
    out:
      allow: true
  - description: Expression usage
    in:
      colour:
        expression-type: FEEL
        expression: not("blue", "purple")
      age: 10
    out:
      allow: true
  - description: Formatted expression usage
    in:
      colour:
        expression-type: FEEL
        expression: not(
            "red",
            "pink"
          )
      age: 20
    out:
      allow: true
```

## Java / Groovy file
#### Using fluent style
```groovy

Decision decision = Decision.builder()
                .id("sample_decision_id")
                .name("Sample Decision Name")
                .hitPolicy(HitPolicy.UNIQUE)
                .expressionType(ExpressionType.GROOVY)
                .withInputs()
                    .name("age")
                    .description("This is something about age")
                    .type(ValueType.STRING)
                    .withExpression()
                        .type(ExpressionType.FEEL)
                        .value("toYear(now()) - toYear(birthDate)")
                        .and()
                    .next()
                    .name("colour")
                    .description("This is something about colour")
                    .type(ValueType.STRING)
                    .end()
                .withOutputs()
                    .name("allow")
                    .description("We expect a decision, if we have access to do it")
                    .type(ValueType.BOOLEAN)
                    .end()
                .withRules()
                    .description("3 allows always")
                    .withInputEntries()
                        .name("age")
                        .withExpression()
                            .type(ExpressionType.LITERAL)
                            .value(3)
                            .and()
                        .end()
                    .withOutputEntries()
                        .name("allow")
                        .withExpression()
                            .type(ExpressionType.LITERAL)
                            .value(true)
                            .and()
                        .end()
                    .next()
                    .withInputEntries()
                        .name("age")
                        .withExpression()
                            .type(ExpressionType.LITERAL)
                            .value(8)
                            .and()
                        .next()
                        .name("colour")
                        .withExpression()
                            .type(ExpressionType.LITERAL)
                            .value("red")
                            .and()
                        .end()
                    .withOutputEntries()
                        .name("allow")
                        .withExpression()
                            .type(ExpressionType.LITERAL)
                            .value(true)
                            .and()
                        .end()
                    .next()
                    .description("Green allows always")
                    .withInputEntries()
                        .name("colour")
                        .withExpression()
                            .type(ExpressionType.LITERAL)
                            .value("green")
                            .and()
                        .end()
                    .withOutputEntries()
                        .name("allow")
                        .withExpression()
                            .type(ExpressionType.LITERAL)
                            .value(true)
                            .and()
                        .end()
                    .next()
                    .description("Expression usage")
                    .withInputEntries()
                        .name("colour")
                        .withExpression()
                            .type(ExpressionType.FEEL)
                            .value("not('blue', 'purple')")
                            .and()
                        .next()
                       .name("age")
                        .withExpression()
                            .type(ExpressionType.LITERAL)
                            .value(10)
                            .and()
                        .end()
                    .withOutputEntries()
                        .name("allow")
                        .withExpression()
                            .type(ExpressionType.LITERAL)
                            .value(true)
                            .and()
                        .end()
                    .next()
                    .description("Formatted expression usage")
                    .withInputEntries()
                        .name("colour")
                        .withExpression()
                            .type(ExpressionType.FEEL)
                            .value("not("+
                                "'red',"+
                                "'pink'"+
                                ")")
                            .and()
                        .next()
                        .name("age")
                        .withExpression()
                            .type(ExpressionType.LITERAL)
                            .value(20)
                            .and()
                        .end()
                    .withOutputEntries()
                        .name("allow")
                        .withExpression()
                            .type(ExpressionType.LITERAL)
                            .value(true)
                            .and()
                        .end()
                    .end()
                .build()
```
#### Using functional style
```java
Decision decision = Decision.builder()
                .id("sample_decision_id")
                .name("Sample Decision Name")
                .hitPolicy(HitPolicy.UNIQUE)
                .expressionType(ExpressionType.GROOVY)
                .withInput(in -> in
                        .name("age")
                        .description("This is something about age")
                        .type(ValueType.STRING)
                        .withExpression(ex -> ex
                                .type(ExpressionType.FEEL)
                                .value("toYear(now()) - toYear(birthDate)")
                                .build())
                        .build())
                .withInput(in -> in
                        .name("colour")
                        .description("This is something about colour")
                        .type(ValueType.STRING)
                        .build())
                .withOutput(out -> out
                        .name("allow")
                        .description("We expect a decision, if we have access to do it")
                        .type(ValueType.BOOLEAN)
                        .build())
                .withRule(rule -> rule
                        .description("3 allows always")
                        .withInputEntry(in -> in
                                .name("age")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(3)
                                        .build())
                                .build())
                        .withOutputEntry(out -> out
                                .name("allow")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(true)
                                        .build())
                                .build())

                        .build())
                .withRule(rule -> rule
                        .withInputEntry(out -> out
                                .name("age")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(8)
                                        .build())
                                .build())
                        .withInputEntry(in -> in

                                .name("colour")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value("red")
                                        .build())
                                .build()).withOutputEntry(out -> out


                                .name("allow")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(true)
                                        .build())
                                .build())
                        .build())
                .withRule(rule -> rule
                        .description("Green allows always")
                        .withInputEntry(in -> in
                                .name("colour")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value("green")
                                        .build())
                                .build())
                        .withOutputEntry(out -> out
                                .name("allow")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(true)
                                        .build())
                                .build())
                        .build())
                .withRule(rule -> rule
                        .description("Expression usage")
                        .withInputEntry(in -> in
                                .name("colour")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.FEEL)
                                        .value("not('blue', 'purple')")
                                        .build())
                                .build())
                        .withInputEntry(in -> in
                                .name("age")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(10)
                                        .build())
                                .build())
                        .withOutputEntry(out -> out
                                .name("allow")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(true)
                                        .build())
                                .build())
                        .build())
                .withRule(rule -> rule
                        .description("Formatted expression usage")
                        .withInputEntry(in -> in
                                .name("colour")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.FEEL)
                                        .value("not(" +
                                                "'red'," +
                                                "'pink'" +
                                                ")")
                                        .build())
                                .build())
                        .withInputEntry(in -> in
                                .name("age")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(20)
                                        .build())
                                .build())
                        .withOutputEntry(out -> out
                                .name("allow")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(true)
                                        .build())
                                .build())
                        .build())
                .build();
```
# How to contribute to the repository
Contributors wishing to join Power Flows project have to comply with a few rules: 

* A preferred IDE is IntelliJ IDEA with default formatting styles;
* Committed source code must be high quality;
* Committed source code must be formatted;
* A commit has to solve an issue from the product backlog. If the issue is missing, please add one before creating the pull request. Refer the issue in commit summary;
* Unit tests coverage should be at 80% or higher;
* Files encoding should be UTF-8 with LF as EOL;
* In order to start contribution, a contributor has to fork the project;
* Once an issue is fixed, a contributor has to create a commit to his forked repository;
* In order to merge changes to the project repository, contributor has to create a pull request with single commit.  

# License
Copyright (c) 2018-present Power Flows. All rights reserved.

Power Flows DMN Model is Open Source software released under the [Apache 2.0 license.](http://www.apache.org/licenses/LICENSE-2.0.html)
