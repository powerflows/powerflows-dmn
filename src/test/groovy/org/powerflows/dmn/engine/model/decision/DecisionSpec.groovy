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

package org.powerflows.dmn.engine.model.decision

import org.powerflows.dmn.engine.model.decision.expression.Expression
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.Input
import org.powerflows.dmn.engine.model.decision.field.Output
import org.powerflows.dmn.engine.model.decision.field.ValueType
import org.powerflows.dmn.engine.model.decision.rule.Rule
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry
import spock.lang.Specification

class DecisionSpec extends Specification {

    final String someTableId = 'some_table_id'
    final String someTableName = 'Some Table Name'
    final HitPolicy someHitPolicy = HitPolicy.UNIQUE
    final ExpressionType someExpressionType = ExpressionType.GROOVY
    final ValueType someInput1Type = ValueType.INTEGER
    final String someInput1Name = 'Some Input 1 Name'
    final String someInput1Description = 'Some Input 1 Description'
    final ExpressionType someInput1Expression1Type = ExpressionType.LITERAL
    final int someInput1Expression1Value = 5
    final ValueType someInput2Type = ValueType.INTEGER
    final String someInput2Name = 'Some Input 2 Name'
    final String someInput2Description = 'Some Input 2 Description'
    final ValueType someOutput1Type = ValueType.BOOLEAN
    final ValueType someInput3Type = ValueType.STRING
    final String someInput3Name = 'Some Input 3 Name'
    final String someOutput1Name = 'Some Input 1 Name'
    final String someOutput1Description = 'Some Output 1 Description'
    final ValueType someOutput2Type = ValueType.STRING
    final String someOutput2Name = 'Some Input 2 Name'
    final String someOutput2Description = 'Some Output 2 Description'
    final String someRule1Description = 'Some Rule 1 Description'
    final String someRule1InputEntry1Name = someInput1Name
    final ExpressionType someRule1InputEntry1ExpressionType = ExpressionType.GROOVY
    final String someRule1InputEntry1ExpressionValue = '> 20'
    final String someRule1InputEntry2Name = someInput2Name
    final ExpressionType someRule1InputEntry2ExpressionType = ExpressionType.FEEL
    final String someRule1InputEntry2ExpressionValue = 'not("blue", "purple")'
    final String someRule1OutputEntry1Name = 'Some Rule 1 Output Entry 1 Name'
    final ExpressionType someRule1OutputEntry1ExpressionType = ExpressionType.GROOVY
    final String someRule1OutputEntry1ExpressionValue = 'someVariable1 || someVariable2'
    final String someRule2Description = 'Some Rule 2 Description'
    final String someRule2InputEntry1Name = someInput1Name
    final String someRule2InputEntry2Name = someInput2Name
    final String someRule2OutputEntry1Name = 'Some Rule 2 Output Entry 1 Name'
    final String someRule2OutputEntry2Name = 'Some Rule 2 Output Entry 2 Name'
    final Integer someRule2OutputEntry1ExpressionValue = 1
    final Integer someRule2OutputEntry2ExpressionValue = 2

    void 'should build decision with fluent API'() {
        given:

        when:
        // @formatter:off
        final Decision decision = Decision.fluentBuilder()
                .id(someTableId)
                .name(someTableName)
                .hitPolicy(someHitPolicy)
                .expressionType(someExpressionType)
                .withInputs()
                    .name(someInput1Name)
                    .description(someInput1Description)
                    .type(someInput1Type)
                    .withExpression()
                        .type(someInput1Expression1Type)
                        .value(someInput1Expression1Value)
                        .and()
                    .next()
                    .name(someInput2Name)
                    .description(someInput2Description)
                    .type(someInput2Type)
                    .end()
                .withOutputs()
                    .name(someOutput1Name)
                    .description(someOutput1Description)
                    .type(someOutput1Type)
                    .next()
                    .name(someOutput2Name)
                    .description(someOutput2Description)
                    .type(someOutput2Type)
                    .end()
                .withRules()
                    .description(someRule1Description)
                    .withInputEntries()
                        .name(someRule1InputEntry1Name)
                        .withExpression()
                            .type(someRule1InputEntry1ExpressionType)
                            .value(someRule1InputEntry1ExpressionValue)
                            .and()
                        .next()
                        .name(someRule1InputEntry2Name)
                        .withExpression()
                            .type(someRule1InputEntry2ExpressionType)
                            .value(someRule1InputEntry2ExpressionValue)
                            .and()
                        .end()
                    .withOutputEntries()
                        .name(someRule1OutputEntry1Name)
                        .withExpression()
                            .type(someRule1OutputEntry1ExpressionType)
                            .value(someRule1OutputEntry1ExpressionValue)
                            .and()
                        .end()
                    .next()
                    .description(someRule2Description)
                    .withInputEntries()
                        .name(someRule2InputEntry1Name)
                        .end()
                    .withInputEntries()
                        .name(someRule2InputEntry2Name)
                        .end()
                    .withOutputEntries()
                        .name(someRule2OutputEntry1Name)
                        .withLiteralValue(someRule2OutputEntry1ExpressionValue)
                        .next()
                        .name(someRule2OutputEntry2Name)
                        .withLiteralValue(someRule2OutputEntry2ExpressionValue)
                        .end()
                    .end()
                .build()
        // @formatter:on

        then:
        decision != null

        with(decision) {
            getId() == someTableId
            getName() == someTableName
            getHitPolicy() == someHitPolicy
            getInputs().size() == 2
            getOutputs().size() == 2
            getRules().size() == 2
        }

        final Input input1 = decision.getInputs().get(0)
        with(input1) {
            getName() == someInput1Name
            getDescription() == someInput1Description
            getType() == someInput1Type

        }

        final Expression input1Expression1 = input1.getExpression()
        with(input1Expression1) {
            getType() == someInput1Expression1Type
            getValue() == someInput1Expression1Value
        }

        final Input input2 = decision.getInputs().get(1)
        with(input2) {
            getName() == someInput2Name
            getDescription() == someInput2Description
            getType() == someInput2Type
        }

        final Expression input1Expression2 = input2.getExpression()
        with(input1Expression2) {
            getType() == ExpressionType.LITERAL
            getValue() == null
        }


        final Output output1 = decision.getOutputs().get(0)
        with(output1) {
            getName() == someOutput1Name
            getDescription() == someOutput1Description
            getType() == someOutput1Type
        }

        final Output output2 = decision.getOutputs().get(1)
        with(output2) {
            getName() == someOutput2Name
            getDescription() == someOutput2Description
            getType() == someOutput2Type
        }

        final Rule rule1 = decision.getRules().get(0)
        with(rule1) {
            getDescription() == someRule1Description
            getInputEntries().size() == 2
            getOutputEntries().size() == 1
        }

        final InputEntry rule1InputEntry1 = rule1.getInputEntries().get(0)
        rule1InputEntry1.getName() == someRule1InputEntry1Name

        final Expression rule1InputEntry1Expression = rule1InputEntry1.getExpression()
        with(rule1InputEntry1Expression) {
            getType() == someRule1InputEntry1ExpressionType
            getValue() == someRule1InputEntry1ExpressionValue
        }

        final InputEntry rule1InputEntry2 = rule1.getInputEntries().get(1)
        rule1InputEntry2.getName() == someRule1InputEntry2Name

        final Expression rule1InputEntry2Expression = rule1InputEntry2.getExpression()
        with(rule1InputEntry2Expression) {
            getType() == someRule1InputEntry2ExpressionType
            getValue() == someRule1InputEntry2ExpressionValue
        }

        final OutputEntry rule1OutputEntry1 = rule1.getOutputEntries().get(0)
        rule1OutputEntry1.getName() == someRule1OutputEntry1Name

        final Expression rule1OutputEntry1Expression = rule1OutputEntry1.getExpression()
        with(rule1OutputEntry1Expression) {
            getType() == someRule1OutputEntry1ExpressionType
            getValue() == someRule1OutputEntry1ExpressionValue
        }

        final Rule rule2 = decision.getRules().get(1)
        with(rule2) {
            getDescription() == someRule2Description
            getInputEntries().size() == 2
            getOutputEntries().size() == 2
        }

        final InputEntry rule2InputEntry1 = rule2.getInputEntries().get(0)
        rule2InputEntry1.getName() == someRule2InputEntry1Name

        final InputEntry rule2InputEntry2 = rule2.getInputEntries().get(1)
        rule2InputEntry2.getName() == someRule2InputEntry2Name

        final OutputEntry rule2OutputEntry1 = rule2.getOutputEntries().get(0)
        rule2OutputEntry1.getName() == someRule2OutputEntry1Name

        final OutputEntry rule2OutputEntry2 = rule2.getOutputEntries().get(1)
        rule2OutputEntry2.getName() == someRule2OutputEntry2Name
    }

    void 'should build decision with fluent API using lambdas'() {
        given:
        //nothing

        when:
        final Decision decision = Decision.builder()
                .id(someTableId)
                .name(someTableName)
                .hitPolicy(someHitPolicy)
                .expressionType(someExpressionType)
                .withInput(
                { inputsBuilder ->
                    inputsBuilder.name(someInput1Name)
                            .description(someInput1Description)
                            .type(someInput1Type)
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
                            .description(someInput2Description)
                            .type(someInput2Type)
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
                                        .name(someRule1InputEntry1Name)
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
                                inputEntryBuilder.name(someRule1InputEntry2Name)
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
                                        .name(someRule1OutputEntry1Name)
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
                            .description(someRule2Description)
                            .withInputEntry(
                            { inputEntryBuilder ->
                                inputEntryBuilder
                                        .name(someRule2InputEntry1Name)
                                        .build()
                            })
                            .withInputEntry(
                            { inputEntryBuilder ->
                                inputEntryBuilder
                                        .name(someRule2InputEntry2Name)
                                        .build()
                            })
                            .withOutputEntry(
                            { outputEntryBuilder ->
                                outputEntryBuilder
                                        .name(someRule2OutputEntry1Name)
                                        .withLiteralValue(someRule2OutputEntry1ExpressionValue)
                                        .build()
                            }).withOutputEntry(
                            { outputEntryBuilder ->
                                outputEntryBuilder
                                        .name(someRule2OutputEntry2Name)
                                        .withLiteralValue(someRule2OutputEntry2ExpressionValue)
                                        .build()
                            })
                            .build()
                })
                .build()

        then:
        decision != null

        with(decision) {
            getId() == someTableId
            getName() == someTableName
            getHitPolicy() == someHitPolicy
            getExpressionType() == someExpressionType
            getInputs().size() == 2
            getOutputs().size() == 2
            getRules().size() == 2
        }

        final Input input1 = decision.getInputs().get(0)
        with(input1) {
            getName() == someInput1Name
            getDescription() == someInput1Description
            getType() == someInput1Type

        }

        final Expression input1Expression1 = input1.getExpression()
        with(input1Expression1) {
            getType() == someInput1Expression1Type
            getValue() == someInput1Expression1Value
        }

        final Input input2 = decision.getInputs().get(1)
        with(input2) {
            getName() == someInput2Name
            getDescription() == someInput2Description
            getType() == someInput2Type
        }

        final Output output1 = decision.getOutputs().get(0)
        with(output1) {
            getName() == someOutput1Name
            getDescription() == someOutput1Description
            getType() == someOutput1Type
        }

        final Output output2 = decision.getOutputs().get(1)
        with(output2) {
            getName() == someOutput2Name
            getDescription() == someOutput2Description
            getType() == someOutput2Type
        }

        final Rule rule1 = decision.getRules().get(0)
        with(rule1) {
            getDescription() == someRule1Description
            getInputEntries().size() == 2
            getOutputEntries().size() == 1
        }

        final InputEntry rule1InputEntry1 = rule1.getInputEntries().get(0)
        rule1InputEntry1.getName() == someRule1InputEntry1Name

        final Expression rule1InputEntry1Expression = rule1InputEntry1.getExpression()
        with(rule1InputEntry1Expression) {
            getType() == someRule1InputEntry1ExpressionType
            getValue() == someRule1InputEntry1ExpressionValue
        }

        final InputEntry rule1InputEntry2 = rule1.getInputEntries().get(1)
        rule1InputEntry2.getName() == someRule1InputEntry2Name

        final Expression rule1InputEntry2Expression = rule1InputEntry2.getExpression()
        with(rule1InputEntry2Expression) {
            getType() == someRule1InputEntry2ExpressionType
            getValue() == someRule1InputEntry2ExpressionValue
        }

        final OutputEntry rule1OutputEntry1 = rule1.getOutputEntries().get(0)
        rule1OutputEntry1.getName() == someRule1OutputEntry1Name

        final Expression rule1OutputEntry1Expression = rule1OutputEntry1.getExpression()
        with(rule1OutputEntry1Expression) {
            getType() == someRule1OutputEntry1ExpressionType
            getValue() == someRule1OutputEntry1ExpressionValue
        }

        final Rule rule2 = decision.getRules().get(1)
        with(rule2) {
            getDescription() == someRule2Description
            getInputEntries().size() == 2
            getOutputEntries().size() == 2
        }

        final InputEntry rule2InputEntry1 = rule2.getInputEntries().get(0)
        rule2InputEntry1.getName() == someRule2InputEntry1Name

        final InputEntry rule2InputEntry2 = rule2.getInputEntries().get(1)
        rule2InputEntry2.getName() == someRule2InputEntry2Name

        final OutputEntry rule2OutputEntry1 = rule2.getOutputEntries().get(0)
        rule2OutputEntry1.getName() == someRule2OutputEntry1Name

        final Expression rule2OutputEntry1Expression = rule2OutputEntry1.getExpression()
        with(rule2OutputEntry1Expression) {
            getType() == ExpressionType.LITERAL
            getValue() == someRule2OutputEntry1ExpressionValue
        }

        final OutputEntry rule2OutputEntry2 = rule2.getOutputEntries().get(1)
        rule2OutputEntry2.getName() == someRule2OutputEntry2Name

        final Expression rule2OutputEntry2Expression = rule2OutputEntry2.getExpression()
        with(rule2OutputEntry2Expression) {
            getType() == ExpressionType.LITERAL
            getValue() == someRule2OutputEntry2ExpressionValue
        }
    }
}
