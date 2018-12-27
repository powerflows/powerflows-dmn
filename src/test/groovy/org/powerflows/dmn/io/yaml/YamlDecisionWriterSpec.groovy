/*
 * Copyright (c) 2018-present PowerFlows.org - all rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.powerflows.dmn.io.yaml

import org.powerflows.dmn.engine.model.decision.Decision
import org.powerflows.dmn.engine.model.decision.EvaluationMode
import org.powerflows.dmn.engine.model.decision.HitPolicy
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.ValueType
import org.powerflows.dmn.engine.writer.DecisionWriteException
import spock.lang.Specification

class YamlDecisionWriterSpec extends Specification {
    final String someTableId1 = 'some_table_id_1'
    final String someTableId2 = 'some_table_id_2'
    final String someTableName = 'Some Table Name'
    final HitPolicy someHitPolicy = HitPolicy.UNIQUE
    final ExpressionType someExpressionType = ExpressionType.GROOVY
    final EvaluationMode someEvaluationMode1 = EvaluationMode.BOOLEAN
    final EvaluationMode someEvaluationMode2 = EvaluationMode.INPUT_COMPARISON
    final ValueType someInput1Type = ValueType.INTEGER
    final String someInput1Name = 'inputOne'
    final String someInput1Description = 'Some Input 1 Description'
    final ExpressionType someInput1Expression1Type = ExpressionType.FEEL
    final String someInput1Expression1Value = '> 5'
    final ValueType someInput2Type = ValueType.INTEGER
    final String someInput2Name = 'inputTwo'

    final ValueType someOutput1Type = ValueType.BOOLEAN
    final String someOutput1Name = 'outputOne'
    final String someOutput1Description = 'Some Output 1 Description'
    final ValueType someOutput2Type = ValueType.STRING
    final String someOutput2Name = 'outputTwo'
    final String someOutput2Description = 'Some Output 2 Description'
    final String someRule1Description = 'Some Rule 1 Description'
    final ExpressionType someRule1InputEntry1ExpressionType = ExpressionType.GROOVY
    final String someRule1InputEntry1ExpressionValue = '> 20'
    final ExpressionType someRule1InputEntry2ExpressionType = ExpressionType.FEEL
    final String someRule1InputEntry2ExpressionValue = 'not("blue", "purple")'
    final ExpressionType someRule1OutputEntry1ExpressionType = ExpressionType.GROOVY
    final String someRule1OutputEntry1ExpressionValue = 'someVariable1 || someVariable2'
    final int someInput1LiteralValue = 5
    final List<String> someInput2LiteralValue = ['one', 'two']
    final boolean someOutput1LiteralValue = true
    final String someOutput2LiteralValue = "The output"

    void 'should write single model'() {
        given:
        final Decision decision = createDecision(someTableId1)
        final YamlDecisionWriter writer = new YamlDecisionWriter()
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        final List<String> referenceResult = loadFileLines('reference-single.yml')

        when:
        writer.write(decision, outputStream)
        final List<String> result = []
        outputStream.toString().eachLine { result << it }

        then:
        result.size() == referenceResult.size()
        for (int i = 0; i < result.size(); i++) {
            assert result[i] == referenceResult[i]
        }
    }

    void 'should wrap exceptions in framework ones when reading single decision'() {
        given:
        final Decision decision = createDecision(someTableId1)
        final YamlDecisionWriter writer = new YamlDecisionWriter()
        final OutputStream outputStream = Mock() {
            write(_, _, _) >>> { byte[] buffer, int offset, int count ->
                throw new RuntimeException('Error')
            }
        }

        when:
        writer.write(decision, outputStream)

        then:
        thrown(DecisionWriteException)
    }

    void 'should write multiple models'() {
        given:
        final Decision decision1 = createDecision(someTableId1)
        final Decision decision2 = createDecision(someTableId2)
        final YamlDecisionWriter writer = new YamlDecisionWriter()
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        final List<String> referenceResult = loadFileLines('reference-multiple.yml')

        when:
        writer.writeAll([decision1, decision2], outputStream)
        final List<String> result = []
        outputStream.toString().eachLine { result << it }

        then:
        result.size() == referenceResult.size()
        for (int i = 0; i < result.size(); i++) {
            assert result[i] == referenceResult[i]
        }
    }

    void 'should wrap exceptions in framework ones when reading multiple decisions'() {
        given:
        final Decision decision = createDecision(someTableId1)
        final YamlDecisionWriter writer = new YamlDecisionWriter()
        final OutputStream outputStream = Mock() {
            write(_, _, _) >>> { byte[] buffer, int offset, int count ->
                throw new RuntimeException('Error')
            }
        }

        when:
        writer.writeAll([decision], outputStream)

        then:
        thrown(DecisionWriteException)
    }

    void 'should do perfect round trip of data with identical files'() {
        given:
        final Decision decision = createDecision(someTableId1)
        final YamlDecisionWriter writer = new YamlDecisionWriter()
        final YamlDecisionReader reader = new YamlDecisionReader()
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()

        when:
        writer.write(decision, outputStream)
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())
        final Decision result = reader.read(inputStream)

        println result.inputs
        println decision.inputs
        then:
        result.id == decision.id
        result.evaluationMode == decision.evaluationMode
        result.expressionType == decision.expressionType
        result.hitPolicy == decision.hitPolicy
        result.name == decision.name
        result.inputs[0].name == decision.inputs[0].name
        result.inputs[0].evaluationMode == null
        result.inputs[0].type == decision.inputs[0].type
        result.inputs[0].description == decision.inputs[0].description
        result.inputs[0].expression == decision.inputs[0].expression
        result.inputs[1].name == decision.inputs[1].name
        result.inputs[1].evaluationMode == null
        result.inputs[1].type == decision.inputs[1].type
        result.inputs[1].description == decision.inputs[1].description
        result.inputs[1].expression == decision.inputs[1].expression
        result.outputs[0].name == decision.outputs[0].name
        result.outputs[0].type == decision.outputs[0].type
        result.outputs[0].description == decision.outputs[0].description
        result.outputs[1].name == decision.outputs[1].name
        result.outputs[1].type == decision.outputs[1].type
        result.outputs[1].description == decision.outputs[1].description

        result.rules[0].description == decision.rules[0].description
        result.rules[0].inputEntries.size() == 2
        result.rules[0].inputEntries[0].expression == decision.rules[0].inputEntries[0].expression
        result.rules[0].inputEntries[0].evaluationMode == null
        result.rules[0].inputEntries[0].name == decision.rules[0].inputEntries[0].name
        result.rules[0].inputEntries[1].expression == decision.rules[0].inputEntries[1].expression
        result.rules[0].inputEntries[1].evaluationMode == null
        result.rules[0].inputEntries[1].name == decision.rules[0].inputEntries[1].name
        result.rules[0].outputEntries.size() == 1
        result.rules[0].outputEntries[0].expression == decision.rules[0].outputEntries[0].expression
        result.rules[0].outputEntries[0].name == decision.rules[0].outputEntries[0].name

        result.rules[1].description == decision.rules[1].description
        result.rules[1].inputEntries.size() == 2
        result.rules[1].inputEntries[0].expression == decision.rules[1].inputEntries[0].expression
        result.rules[1].inputEntries[0].evaluationMode == null
        result.rules[1].inputEntries[0].name == decision.rules[1].inputEntries[0].name
        result.rules[1].inputEntries[1].expression == decision.rules[1].inputEntries[1].expression
        result.rules[1].inputEntries[1].evaluationMode == null
        result.rules[1].inputEntries[1].name == decision.rules[1].inputEntries[1].name
        result.rules[1].outputEntries.size() == 2
        result.rules[1].outputEntries[0].expression == decision.rules[1].outputEntries[0].expression
        result.rules[1].outputEntries[0].name == decision.rules[1].outputEntries[0].name
        result.rules[1].outputEntries[1].expression == decision.rules[1].outputEntries[1].expression
        result.rules[1].outputEntries[1].name == decision.rules[1].outputEntries[1].name
    }

    List<String> loadFileLines(final String filename) {
        final List<String> referenceResult = []
        new File(this.class.getResource(filename).toURI()).eachLine { referenceResult << it }

        return referenceResult
    }

    Decision createDecision(final String id) {
        Decision.builder()
                .id(id)
                .name(someTableName)
                .hitPolicy(someHitPolicy)
                .expressionType(someExpressionType)
                .evaluationMode(someEvaluationMode2)
                .withInput(
                { inputsBuilder ->
                    inputsBuilder.name(someInput1Name)
                            .description(someInput1Description)
                            .type(someInput1Type)
                            .evaluationMode(someEvaluationMode1)
                            .withExpression(
                            { expressionBuilder ->
                                expressionBuilder
                                        .type(someInput1Expression1Type)
                                        .value(someInput1Expression1Value)
                                        .build()
                            })
                            .build()
                })
                .withInput(
                { inputsBuilder ->
                    inputsBuilder
                            .name(someInput2Name)
                            .type(someInput2Type)
                            .evaluationMode(someEvaluationMode1)
                            .build()
                })
                .withOutput(
                { outputsBuilder ->
                    outputsBuilder
                            .name(someOutput1Name)
                            .description(someOutput1Description)
                            .type(someOutput1Type)
                            .build()
                }).withOutput(
                { outputsBuilder ->
                    outputsBuilder
                            .name(someOutput2Name)
                            .description(someOutput2Description)
                            .type(someOutput2Type)
                            .build()
                })
                .withRule(
                { rulesBuilder ->
                    rulesBuilder.description(someRule1Description)
                            .withInputEntry(
                            { inputEntryBuilder ->
                                inputEntryBuilder
                                        .name(someInput1Name)
                                        .evaluationMode(someEvaluationMode1)
                                        .withExpression(
                                        { expressionBuilder ->
                                            expressionBuilder
                                                    .type(someRule1InputEntry1ExpressionType)
                                                    .value(someRule1InputEntry1ExpressionValue)
                                                    .build()
                                        })
                                        .build()
                            })
                            .withInputEntry(
                            { inputEntryBuilder ->
                                inputEntryBuilder
                                        .name(someInput2Name)
                                        .evaluationMode(someEvaluationMode1)
                                        .withExpression(
                                        { expressionBuilder ->
                                            expressionBuilder
                                                    .type(someRule1InputEntry2ExpressionType)
                                                    .value(someRule1InputEntry2ExpressionValue)
                                                    .build()
                                        })
                                        .build()
                            })
                            .withOutputEntry(
                            { outputEntryBuilder ->
                                outputEntryBuilder
                                        .name(someOutput1Name)
                                        .withExpression(
                                        { expressionBuilder ->
                                            expressionBuilder
                                                    .type(someRule1OutputEntry1ExpressionType)
                                                    .value(someRule1OutputEntry1ExpressionValue)
                                                    .build()
                                        })
                                        .build()
                            })
                            .build()
                })
                .withRule(
                { rulesBuilder ->
                    rulesBuilder
                            .withInputEntry(
                            { inputEntryBuilder ->
                                inputEntryBuilder
                                        .name(someInput1Name)
                                        .evaluationMode(someEvaluationMode1)
                                        .withLiteralValue(someInput1LiteralValue)
                                        .build()
                            })
                            .withInputEntry(
                            { inputEntryBuilder ->
                                inputEntryBuilder
                                        .name(someInput2Name)
                                        .evaluationMode(someEvaluationMode1)
                                        .withLiteralValue(someInput2LiteralValue)
                                        .build()
                            })
                            .withOutputEntry(
                            { outputEntryBuilder ->
                                outputEntryBuilder
                                        .name(someOutput1Name)
                                        .withLiteralValue(someOutput1LiteralValue)
                                        .build()
                            })
                            .withOutputEntry(
                            { outputEntryBuilder ->
                                outputEntryBuilder
                                        .name(someOutput2Name)
                                        .withLiteralValue(someOutput2LiteralValue)
                                        .build()
                            })
                            .build()
                })
                .build()
    }
}
