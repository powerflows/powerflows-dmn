[![Build Status](https://travis-ci.org/powerflows/powerflows-dmn.svg?branch=master)](https://travis-ci.org/powerflows/powerflows-dmn)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=org.powerflows%3Admn&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.powerflows%3Admn)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.powerflows%3Admn&metric=coverage)](https://sonarcloud.io/component_measures?id=org.powerflows%3Admn&metric=coverage)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.powerflows/dmn-engine/badge.svg)](https://mvnrepository.com/artifact/org.powerflows)
[![Java Doc](http://www.javadoc.io/badge/org.powerflows/dmn-engine.svg?color=brightgreen)](http://www.javadoc.io/doc/org.powerflows/dmn-engine)
# About Power Flows DMN
Power Flows DMN - a powerful decision engine.


# Getting Started
Depending on you would like to use the version 2.x.x (recommended as the latest version) or 1.x.x 
there are 2 different ways of importing dependencies.

## Version 2.x.x

The Power Flows DMN can be imported as a dependency via:

### Maven
```xml
<dependency>
    <groupId>org.powerflows</groupId>
    <artifactId>dmn-engine</artifactId>
    <version>2.1.0</version>
</dependency>
```
Since in version 2.0.0 Power Flows DMN has been divided into modules,
a developer can decide what features have to be imported to his project. 
Therefore, the first decision will be which modeling format of decision tables to be supported. 
There is a choice between:
* YAML file - requires **dmn-io-yaml** dependency
* XML file - requires **dmn-io-xml** dependency
* Java/Groovy file (fluent and functional) - delivered with **dmn-engine** dependency. In order to use Groovy style, dependency to Groovy is required as well.
* Kotlin DSL - requires **dmn-kotlin-dsl** dependency

**Surely, Power Flows DMN supports all of them within a single application.**

The next step is to add dependencies of expression evaluation languages. 
The developer can use the all of some of them:
* Literals - delivered with **dmn-engine** dependency
* FEEL - requires **dmn-feel-evaluation-provider** dependency
* JUEL - requires **dmn-juel-evaluation-provider** dependency
* Groovy - requires **dmn-groovy-evaluation-provider** dependency
* MVEL - requires **dmn-mvel-evaluation-provider** dependency
* JavaScript - requires **dmn-javascript-evaluation-provider** dependency

Optional dependencies:
```xml
<dependency>
    <groupId>org.powerflows</groupId>
    <artifactId>dmn-io-yaml</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>org.powerflows</groupId>
    <artifactId>dmn-io-xml</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>org.powerflows</groupId>
    <artifactId>dmn-kotlin-dsl</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>org.powerflows</groupId>
    <artifactId>dmn-feel-evaluation-provider</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>org.powerflows</groupId>
    <artifactId>dmn-juel-evaluation-provider</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>org.powerflows</groupId>
    <artifactId>dmn-groovy-evaluation-provider</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>org.powerflows</groupId>
    <artifactId>dmn-mvel-evaluation-provider</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>org.powerflows</groupId>
    <artifactId>dmn-javascript-evaluation-provider</artifactId>
    <version>2.1.0</version>
</dependency>
```

### Gradle
```gradle    
compile group: 'org.powerflows', name: 'dmn-engine', version: '2.1.0'
```

Optional dependencies:
```gradle    
compile group: 'org.powerflows', name: 'dmn-io-yaml', version: '2.1.0'
compile group: 'org.powerflows', name: 'dmn-io-xml', version: '2.1.0'
compile group: 'org.powerflows', name: 'dmn-kotlin-dsl', version: '2.1.0'
compile group: 'org.powerflows', name: 'dmn-feel-evaluation-provider', version: '2.1.0'
compile group: 'org.powerflows', name: 'dmn-juel-evaluation-provider', version: '2.1.0'
compile group: 'org.powerflows', name: 'dmn-groovy-evaluation-provider', version: '2.1.0'
compile group: 'org.powerflows', name: 'dmn-mvel-evaluation-provider', version: '2.1.0'
compile group: 'org.powerflows', name: 'dmn-javascript-evaluation-provider', version: '2.1.0'
```
### Grape
```groovy
@Grapes(
    @Grab(group='org.powerflows', module='dmn-engine', version='2.1.0')
)
```   
Optional dependencies:
```groovy
@Grapes([
    @Grab(group='org.powerflows', module='dmn-io-yaml', version='2.1.0'),
    @Grab(group='org.powerflows', module='dmn-io-xml', version='2.1.0'),
    @Grab(group='org.powerflows', module='dmn-kotlin-dsl', version='2.1.0'),
    @Grab(group='org.powerflows', module='dmn-feel-evaluation-provider', version='2.1.0'),
    @Grab(group='org.powerflows', module='dmn-juel-evaluation-provider', version='2.1.0'), 
    @Grab(group='org.powerflows', module='dmn-groovy-evaluation-provider', version='2.1.0'),
    @Grab(group='org.powerflows', module='dmn-mvel-evaluation-provider', version='2.1.0'),
    @Grab(group='org.powerflows', module='dmn-javascript-evaluation-provider', version='2.1.0')   
])
```  

## Version 1.x.x

The Power Flows DMN can be imported as a dependency via:

### Maven
```xml
<dependency>
    <groupId>org.powerflows</groupId>
    <artifactId>dmn</artifactId>
    <version>1.1.1</version>
</dependency>
```

### Gradle
```gradle    
compile group: 'org.powerflows', name: 'dmn', version: '1.1.1'
```

### Grape
```groovy
@Grapes(
    @Grab(group='org.powerflows', module='dmn', version='1.1.1')
)
```   
 
Other ways to import, visit Maven Central repo [https://mvnrepository.com/artifact/org.powerflows/dmn](https://mvnrepository.com/artifact/org.powerflows/dmn) 



# Define your DMN model
Power Flows model has been designed as an easy to describe and maintain file. The file contains information about input and output data. The additional division is section with rules and their input and output entries.

Power Flows supports the model in the following formats:
* YAML file;
* XML file;
* Java / Groovy file.
    * Fluent
    * Functional
* Kotlin DSL
## YAML file

```yaml
id: loan_qualifier
name: Loan qualifier
hit-policy: COLLECT
expression-type: FEEL
fields:
  in:
    age:
      type: INTEGER
    activeLoansNumber:
      description: Number of active loans on user's account
      type: INTEGER
      expression-type: LITERAL
    startDate:
      type: DATE
  out:
    loanAmount:
      description: Loan amount in Euro
      type: DOUBLE
    loanTerm:
      description: Loan term in months
      type: INTEGER
rules:
- description: Loan for 18 years
  in:
    age: 18
    activeLoansNumber: 0
    startDate: '[date and time("2019-01-01T12:00:00")..date and time("2019-12-31T12:00:00")]'
  out:
    loanAmount: 10000
    loanTerm: 12
- in:
    age: 18
    startDate: '[date and time("2019-03-01T12:00:00")..date and time("2019-03-31T12:00:00")]'
  out:
    loanAmount: 15000
    loanTerm: 6
- description: Loan for older than 18 years
  in:
    age: '>18'
  out:
    loanAmount: 20000
    loanTerm: 12

```

## Java / Groovy file
### Using fluent style
```groovy
Decision decision = Decision.fluentBuilder()
.id("loan_qualifier")
.name("Loan qualifier")
.hitPolicy(HitPolicy.COLLECT)
.expressionType(ExpressionType.FEEL)
.withInputs()
    .name("age")
    .type(ValueType.INTEGER)
    .next()
    .name("activeLoansNumber")
    .description("Number of active loans on user's account")
    .type(ValueType.INTEGER)
    .withExpression()
        .type(ExpressionType.LITERAL)
        .and()
    .next()
    .name("startDate")
    .type(ValueType.DATE)
.end()
.withOutputs()
    .name("loanAmount")
    .description("Loan amount in Euro")
    .type(ValueType.DOUBLE)
    .next()
    .name("loanTerm")
    .description("Loan term in months")
    .type(ValueType.INTEGER)
.end()
.withRules()
    .description("Loan for 18 years")
    .withInputEntries()
        .name("age")
        .withExpression()
            .type(ExpressionType.FEEL)
            .value(18)
            .and()
        .next()
        .name("activeLoansNumber")
        .evaluationMode(EvaluationMode.INPUT_COMPARISON)
        .withExpression()
            .type(ExpressionType.LITERAL)
            .value(0)
            .and()
        .next()
        .name("startDate")
        .withExpression()
            .type(ExpressionType.FEEL)
            .value("[date and time(\"2019-01-01T12:00:00\")..date and time(\"2019-12-31T12:00:00\")]")
            .and()
    .end()
    .withOutputEntries()
        .name("loanAmount")
        .withExpression()
            .type(ExpressionType.LITERAL)
            .value(10000)
            .and()
        .next()
        .name("loanTerm")
        .withExpression()
            .type(ExpressionType.LITERAL)
            .value(12)
            .and()
    .end()
    .next()
    .withInputEntries()
        .name("age")
        .evaluationMode(EvaluationMode.INPUT_COMPARISON)
        .withExpression()
            .type(ExpressionType.LITERAL)
            .value(18)
            .and()
        .next()
        .name("startDate")
        .withExpression()
            .type(ExpressionType.FEEL)
            .value("[date and time(\"2019-03-01T12:00:00\")..date and time(\"2019-03-31T12:00:00\")]")
            .and()
    .end()
    .withOutputEntries()
        .name("loanAmount")
        .withExpression()
            .type(ExpressionType.LITERAL)
            .value(15000)
.           and()
        .next()
        .name("loanTerm")
        .withExpression()
            .type(ExpressionType.LITERAL)
            .value(6)
            .and()
    .end()
    .next()
    .withInputEntries()
        .name("age")
        .withExpression()
            .type(ExpressionType.FEEL)
            .value(">18")
            .and()
    .end()
    .withOutputEntries()
        .name("loanAmount")
        .withExpression()
            .type(ExpressionType.LITERAL)
            .value(20000)
            .and()
        .next()
        .name("loanTerm")
        .withExpression()
            .type(ExpressionType.LITERAL)
            .value(12)
            .and()
    .end()
.end()
.build();
```
### Using functional style
```java
Decision decision = Decision.builder()
.id("loan_qualifier")
.name("Loan qualifier")
.hitPolicy(HitPolicy.COLLECT)
.expressionType(ExpressionType.FEEL)
.withInput(in -> in
        .name("age")
        .type(ValueType.INTEGER)
        .build())
.withInput(in -> in
        .name("activeLoansNumber")
        .description("Number of active loans on user's account")
        .type(ValueType.INTEGER)
        .withExpression(ex -> ex
                .type(ExpressionType.LITERAL)
                .build())
        .build())
.withInput(in -> in
        .name("startDate")
        .type(ValueType.DATE)
        .build())
.withOutput(out -> out
        .name("loanAmount")
        .description("Loan amount in Euro")
        .type(ValueType.DOUBLE)
        .build())
.withOutput(out -> out
        .name("loanTerm")
        .description("Loan term in months")
        .type(ValueType.INTEGER)
        .build())
.withRule(rule -> rule
        .description("Loan for 18 years")
        .withInputEntry(in -> in
                .name("age")
                .evaluationMode(EvaluationMode.INPUT_COMPARISON)
                .withExpression(ex -> ex
                        .type(ExpressionType.LITERAL)
                        .value(18)
                        .build())
                .build())
        .withInputEntry(in -> in
                .name("activeLoansNumber")
                .evaluationMode(EvaluationMode.INPUT_COMPARISON)
                .withExpression(ex -> ex
                        .type(ExpressionType.LITERAL)
                        .value(0)
                        .build())
                .build())
        .withInputEntry(in -> in
                .name("startDate")
                .withExpression(ex -> ex
                        .type(ExpressionType.FEEL)
                        .value("[date and time(\"2019-01-01T12:00:00\")..date and time(\"2019-12-31T12:00:00\")]")
                        .build())
                .build())
        .withOutputEntry(out -> out
                .name("loanAmount")
                .withExpression(ex -> ex
                        .type(ExpressionType.LITERAL)
                        .value(10000)
                        .build())
                .build())
        .withOutputEntry(out -> out
                .name("loanTerm")
                .withExpression(ex -> ex
                        .type(ExpressionType.LITERAL)
                        .value(12)
                        .build())
                .build())
        .build())
.withRule(rule -> rule
        .withInputEntry(in -> in
                .name("age")
                .withExpression(ex -> ex
                        .type(ExpressionType.FEEL)
                        .value(18)
                        .build())
                .build())
        .withInputEntry(in -> in
                .name("startDate")
                .withExpression(ex -> ex
                        .type(ExpressionType.FEEL)
                        .value("[date and time(\"2019-03-01T12:00:00\")..date and time(\"2019-03-31T12:00:00\")]")
                        .build())
                .build())
        .withOutputEntry(out -> out
                .name("loanAmount")
                .withExpression(ex -> ex
                        .type(ExpressionType.LITERAL)
                        .value(15000)
                        .build())
                .build())
        .withOutputEntry(out -> out
                .name("loanTerm")
                .withExpression(ex -> ex
                        .type(ExpressionType.LITERAL)
                        .value(6)
                        .build())
                .build())
        .build())
.withRule(rule -> rule
        .withInputEntry(in -> in
                .name("age")
                .withExpression(ex -> ex
                        .type(ExpressionType.FEEL)
                        .value(">18")
                        .build())
                .build())
        .withOutputEntry(out -> out
                .name("loanAmount")
                .withExpression(ex -> ex
                        .type(ExpressionType.LITERAL)
                        .value(20000)
                        .build())
                .build())
        .withOutputEntry(out -> out
                .name("loanTerm")
                .withExpression(ex -> ex
                        .type(ExpressionType.LITERAL)
                        .value(12)
                        .build())
                .build())
        .build())
.build();
```
## Kotlin DSL
```kotlin
decision {
    id = "loan_qualifier"
    name = "Loan qualifier"
    hitPolicy = HitPolicy.COLLECT
    expressionType = ExpressionType.FEEL
    inputs {
        input("age") {
            type = ValueType.INTEGER
        }
        input("activeLoansNumber") {
            description = "Number of active loans on user's account"
            type = ValueType.INTEGER
            expression(ExpressionType.LITERAL)
        }
        input("startDate") {
            type = ValueType.DATE
        }
    }
    outputs {
        output("loanAmount") {
            description = "Loan amount in Euro"
            type = ValueType.DOUBLE
        }
        output("loanTerm") {
            description = "Loan term in months"
            type = ValueType.INTEGER
        }
    }
    rules {
        rule {
            input("age") {
                value = 18
            }
            input("activeLoansNumber")
            input("startDate") {
                expression("[date and time(\"2019-01-01T12:00:00\")..date and time(\"2019-12-31T12:00:00\")]")
            }
            output("loanAmount") {
                value = 10000
            }
            output("loanTerm") {
                value = 12
            }
        }
        rule {
            input("age") {
                value = 18
            }
            input("startDate") {
                expression("[date and time(\"2019-03-01T12:00:00\")..date and time(\"2019-03-31T12:00:00\")]")
            }
            output("loanAmount") {
                value = 15000
            }
            output("loanTerm") {
                value = 6
            }
        }
        rule {
            description = "Loan for older than 18 years"
            input("age") {
                expression(">18")
            }
            output("loanAmount") {
                value = 20000
            }
            output("loanTerm") {
                value = 12
            }
        }
    }
}
```
# Use IO to import/export your model
Thanks to IO module there is a possibility to:
* Read decisions from Power Flows *.yml files;
* Read decisions from DMN 1.1 *.xml files;
* Write decisions to *.yml files.

## Reading
The follow example shows how to read *.yml file and get decision object.
First of all input stream is needed. Then, using YamlDecisionReader class a developer can read the decision from the input stream.

Create input stream from file:

```java
File loanQualifierFile = new File("loan-qualifier.yml");
InputStream loanQualifierInputStream = new FileInputStream(loanQualifierFile);
```

or from resource:

```java
InputStream loanQualifierInputStream = this.class.getResourceAsStream("loan-qualifier.yml");
```

And read the input stream:

```java
Optional<Decision> loanQualifierDecision = new YamlDecisionReader().read(loanQualifierInputStream);
```

Another source of Decision may be OMG defined DMN 1.1 compatible XML file.

Create input stream from file:

```java
File loanQualifierFile = new File("loan-qualifier.xml");
InputStream loanQualifierInputStream = new FileInputStream(loanQualifierFile);
```

or from resource:

```java
InputStream loanQualifierInputStream = this.class.getResourceAsStream("loan-qualifier.xml");
```

And read the input stream:

```java
Optional<Decision> loanQualifierDecision = new XmlDecisionReader().read(loanQualifierInputStream);
```
Currently only reading of decision tables from _decision_ tags is supported.

## Writing
The IO module can be used to conversion between different formats. For now the only one supported is *.yml.

```java
Decision decision = ... //here developer has to build a valid decision object or read from *.yml or *.xml file

DecisionWriter writer = new YamlDecisionWriter();
FileOutputStream outputStream = new FileOutputStream("your-decision-file-name.yml");

writer.write(decision, outputStream);
```

# Decision Engine for decisions evaluation
A decision engine is a service that allows evaluation of decision tables. Default decision engine instance can be created using
DefaultDecisionEngineConfiguration class. Decision engine expects decision object and decision variables.

## Evaluation
The result of an evaluation process is decision result object.

```java
Decision decision = ... //here developer has to build a valid decision object or read from *.yml or *.xml file
DecisionEngine decisionEngine = new DefaultDecisionEngineConfiguration().configure();

SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

Map<String, Serializable> variables = new HashMap<>();
variables.put("age", 18);
variables.put("activeLoansNumber", 0);
variables.put("startDate", format.parse("2019-01-05"));
DecisionVariables decisionVariables = new DecisionVariables(variables);

DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables);
```
The decision result has methods like follows:
```java
decisionResult.isSingleEntryResult();
decisionResult.isSingleRuleResult();
decisionResult.isCollectionRulesResult();
decisionResult.getSingleEntryResult();
decisionResult.getSingleRuleResult();
decisionResult.getCollectionRulesResult();
```

## Supported languages
Power Flows supports evaluation in following languages:
* FEEL
* JUEL
* Groovy
* MVEL
* JavaScript

## Much more features
Power Flows brings with it lots of features described in [WIKI](https://github.com/powerflows/powerflows-dmn/wiki).

### [How to contribute to the repository](CONTRIBUTING.md)

# License
Copyright (c) 2018-present Power Flows. All rights reserved.

Power Flows DMN Model is Open Source software released under the [Apache 2.0 license.](http://www.apache.org/licenses/LICENSE-2.0.html)
