[![Build Status](https://travis-ci.org/powerflows/powerflows-dmn-model.svg?branch=master)](https://travis-ci.org/powerflows/powerflows-dmn-model)

# About Power Flows DMN Model
Power Flows DMN Model

# DMN model
Power Flows model has been designed as a YAML file. The file contains information about input and output data. The additional division is sections with fields and decisions.

```yaml
id: sample_decision_id
name: Sample Decision Name
hit-policy: UNIQUE
expression-type: groovy
fields:
  in:
    age:
      expression-type: feel
      expression: toYear(now()) - toYear(birthDate)
      type: integer
      description: This is something about age
    colour:
      type: string
      description: This is something about colour
  out:
    allow:
      type: boolean
      description: rule
decisions:
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
        expression-type: feel
        expresion: not("blue", "purple")
      age: 10
    out:
      allow: true
 - description: Formatted expression usage
    in:
      colour:
        expression-type: feel
        expresion: '''not(
          "red",
          "pink"
        )'''
      age: 20
    out:
      allow: true
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
