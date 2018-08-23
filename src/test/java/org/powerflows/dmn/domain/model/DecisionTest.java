package org.powerflows.dmn.domain.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powerflows.dmn.engine.model.decision.Decision;
import org.powerflows.dmn.engine.model.decision.HitPolicy;
import org.powerflows.dmn.engine.model.decision.expression.Expression;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.field.Output;
import org.powerflows.dmn.engine.model.decision.field.ValueType;
import org.powerflows.dmn.engine.model.decision.rule.Rule;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Copyright (c) 2018-present PowerFlows.org - all rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@RunWith(JUnit4.class)
public class DecisionTest {

    final String someTableId = "some_table_id";
    final String someTableName = "Some Table Name";
    final HitPolicy someHitPolicy = HitPolicy.UNIQUE;
    final ExpressionType someExpressionType = ExpressionType.GROOVY;
    final ValueType someInput1Type = ValueType.INTEGER;
    final String someInput1Name = "Some Input 1 Name";
    final String someInput1Description = "Some Input 1 Description";
    final ExpressionType someInput1Expression1Type = ExpressionType.LITERAL;
    final int someInput1Expression1Value = 5;
    final ValueType someInput2Type = ValueType.COLLECTION;
    final String someInput2Name = "Some Input 2 Name";
    final String someInput2Description = "Some Input 2 Description";
    final ValueType someOutput1Type = ValueType.BOOLEAN;
    final String someOutput1Name = "Some Input 1 Name";
    final String someOutput1Description = "Some Output 1 Description";
    final ExpressionType someOutput1Expression1Type = ExpressionType.GROOVY;
    final String someOutput1Expression1Value = ">= someVariable";
    final ValueType someOutput2Type = ValueType.STRING;
    final String someOutput2Name = "Some Input 2 Name";
    final String someOutput2Description = "Some Output 2 Description";
    final String someRule1Description = "Some Rule 1 Description";
    final String someRule1InputEntry1Name = "Some Rule 1 Input Entry 1 Name";
    final ExpressionType someRule1InputEntry1ExpressionType = ExpressionType.GROOVY;
    final String someRule1InputEntry1ExpressionValue = "> 20";
    final String someRule1InputEntry2Name = "Some Rule 1 Input Entry 2 Name";
    final ExpressionType someRule1InputEntry2ExpressionType = ExpressionType.FEEL;
    final String someRule1InputEntry2ExpressionValue = "not(\"blue\", \"purple\")";
    final String someRule1OutputEntry1Name = "Some Rule 1 Output Entry 1 Name";
    final ExpressionType someRule1OutputEntry1ExpressionType = ExpressionType.GROOVY;
    final String someRule1OutputEntry1ExpressionValue = "someVariable1 || someVariable2";
    final String someRule2Description = "Some Rule 2 Description";
    final String someRule2InputEntry1Name = "Some Rule 2 Input Entry 1 Name";
    final String someRule2InputEntry2Name = "Some Rule 2 Input Entry 2 Name";
    final String someRule2OutputEntry1Name = "Some Rule 2 Output Entry 1 Name";
    final String someRule2OutputEntry2Name = "Some Rule 2 Output Entry 2 Name";

    @Test
    public void shouldCreateDecisionUsingFluentBuilderApi() {
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
                    .withExpression()
                        .type(someOutput1Expression1Type)
                        .value(someOutput1Expression1Value)
                        .and()
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
                        .next()
                        .name(someRule2OutputEntry2Name)
                        .end()
                    .end()
                .build();
        // @formatter:on

        then:
        assertOnDecision(decision);
    }

    @Test
    public void shouldCreateDecisionUsingFunctionalBuilderApi() {
        final Decision decision = Decision.builder()
                .id(someTableId)
                .name(someTableName)
                .hitPolicy(someHitPolicy)
                .withInput(inputsBuilder ->
                        inputsBuilder.name(someInput1Name)
                                .description(someInput1Description)
                                .type(someInput1Type)
                                .withExpression(expressionBuilder ->
                                        expressionBuilder
                                                .type(someInput1Expression1Type)
                                                .value(someInput1Expression1Value)
                                                .build())
                                .build())
                .withInput(inputsBuilder ->
                        inputsBuilder
                                .name(someInput2Name)
                                .description(someInput2Description)
                                .type(someInput2Type)
                                .build())
                .withOutput(outputsBuilder ->
                        outputsBuilder
                                .name(someOutput1Name)
                                .description(someOutput1Description)
                                .type(someOutput1Type)
                                .withExpression(expressionBuilder ->
                                        expressionBuilder
                                                .type(someOutput1Expression1Type)
                                                .value(someOutput1Expression1Value)
                                                .build())
                                .build())
                .withOutput(outputsBuilder ->
                        outputsBuilder
                                .name(someOutput2Name)
                                .description(someOutput2Description)
                                .type(someOutput2Type)
                                .build())
                .withRule(rulesBuilder ->
                        rulesBuilder.description(someRule1Description)
                                .withInputEntry(
                                        inputEntryBuilder ->
                                                inputEntryBuilder
                                                        .name(someRule1InputEntry1Name)
                                                        .withExpression(expressionBuilder ->
                                                                expressionBuilder
                                                                        .type(someRule1InputEntry1ExpressionType)
                                                                        .value(someRule1InputEntry1ExpressionValue)
                                                                        .build())
                                                        .build())
                                .withInputEntry(inputEntryBuilder ->
                                        inputEntryBuilder.name(someRule1InputEntry2Name)
                                                .withExpression(expressionBuilder ->
                                                        expressionBuilder
                                                                .type(someRule1InputEntry2ExpressionType)
                                                                .value(someRule1InputEntry2ExpressionValue)
                                                                .build())
                                                .build())
                                .withOutputEntry(outputEntryBuilder ->
                                        outputEntryBuilder
                                                .name(someRule1OutputEntry1Name)
                                                .withExpression(expressionBuilder ->
                                                        expressionBuilder
                                                                .type(someRule1OutputEntry1ExpressionType)
                                                                .value(someRule1OutputEntry1ExpressionValue)
                                                                .build())
                                                .build())
                                .build())
                .withRule(rulesBuilder ->
                        rulesBuilder
                                .description(someRule2Description)
                                .withInputEntry(inputEntryBuilder ->
                                        inputEntryBuilder.name(someRule2InputEntry1Name)
                                                .build())
                                .withInputEntry(inputEntryBuilder ->
                                        inputEntryBuilder.name(someRule2InputEntry2Name)
                                                .build())
                                .withOutputEntry(outputEntryBuilder ->
                                        outputEntryBuilder.name(someRule2OutputEntry1Name)
                                                .build())
                                .withOutputEntry(outputEntryBuilder ->
                                        outputEntryBuilder.name(someRule2OutputEntry2Name)
                                                .build())
                                .build())
                .build();

        then:
        assertOnDecision(decision);
    }

    private void assertOnDecision(final Decision decision) {
        assertNotNull(decision);

        assertEquals(decision.getId(), someTableId);
        assertEquals(decision.getName(), someTableName);
        assertEquals(decision.getHitPolicy(), someHitPolicy);
        assertEquals(decision.getInputs().size(), 2);
        assertEquals(decision.getOutputs().size(), 2);
        assertEquals(decision.getRules().size(), 2);

        final Input input1 = decision.getInputs().get(0);
        assertEquals(input1.getName(), someInput1Name);
        assertEquals(input1.getDescription(), someInput1Description);
        assertEquals(input1.getType(), someInput1Type);

        final Expression input1Expression1 = input1.getExpression();
        assertEquals(input1Expression1.getType(), someInput1Expression1Type);
        assertEquals(input1Expression1.getValue(), someInput1Expression1Value);

        final Input input2 = decision.getInputs().get(1);
        assertEquals(input2.getName(), someInput2Name);
        assertEquals(input2.getDescription(), someInput2Description);
        assertEquals(input2.getType(), someInput2Type);

        final Output output1 = decision.getOutputs().get(0);
        assertEquals(output1.getName(), someOutput1Name);
        assertEquals(output1.getDescription(), someOutput1Description);
        assertEquals(output1.getType(), someOutput1Type);

        final Expression output1Expression1 = output1.getExpression();
        assertEquals(output1Expression1.getType(), someOutput1Expression1Type);
        assertEquals(output1Expression1.getValue(), someOutput1Expression1Value);

        final Output output2 = decision.getOutputs().get(1);
        assertEquals(output2.getName(), someOutput2Name);
        assertEquals(output2.getDescription(), someOutput2Description);
        assertEquals(output2.getType(), someOutput2Type);

        final Rule rule1 = decision.getRules().get(0);
        assertEquals(rule1.getDescription(), someRule1Description);
        assertEquals(rule1.getInputEntries().size(), 2);
        assertEquals(rule1.getOutputEntries().size(), 1);

        final InputEntry rule1InputEntry1 = rule1.getInputEntries().get(0);
        assertEquals(rule1InputEntry1.getName(), someRule1InputEntry1Name);

        final Expression rule1InputEntry1Expression = rule1InputEntry1.getExpression();

        assertEquals(rule1InputEntry1Expression.getType(), someRule1InputEntry1ExpressionType);
        assertEquals(rule1InputEntry1Expression.getValue(), someRule1InputEntry1ExpressionValue);

        final InputEntry rule1InputEntry2 = rule1.getInputEntries().get(1);
        assertEquals(rule1InputEntry2.getName(), someRule1InputEntry2Name);

        final Expression rule1InputEntry2Expression = rule1InputEntry2.getExpression();

        assertEquals(rule1InputEntry2Expression.getType(), someRule1InputEntry2ExpressionType);
        assertEquals(rule1InputEntry2Expression.getValue(), someRule1InputEntry2ExpressionValue);

        final OutputEntry rule1OutputEntry1 = rule1.getOutputEntries().get(0);
        assertEquals(rule1OutputEntry1.getName(), someRule1OutputEntry1Name);

        final Expression rule1OutputEntry1Expression = rule1OutputEntry1.getExpression();
        assertEquals(rule1OutputEntry1Expression.getType(), someRule1OutputEntry1ExpressionType);
        assertEquals(rule1OutputEntry1Expression.getValue(), someRule1OutputEntry1ExpressionValue);

        final Rule rule2 = decision.getRules().get(1);
        assertEquals(rule2.getDescription(), someRule2Description);
        assertEquals(rule2.getInputEntries().size(), 2);
        assertEquals(rule2.getOutputEntries().size(), 2);

        final InputEntry rule2InputEntry1 = rule2.getInputEntries().get(0);
        assertEquals(rule2InputEntry1.getName(), someRule2InputEntry1Name);

        final InputEntry rule2InputEntry2 = rule2.getInputEntries().get(1);
        assertEquals(rule2InputEntry2.getName(), someRule2InputEntry2Name);

        final OutputEntry rule2OutputEntry1 = rule2.getOutputEntries().get(0);
        assertEquals(rule2OutputEntry1.getName(), someRule2OutputEntry1Name);

        final OutputEntry rule2OutputEntry2 = rule2.getOutputEntries().get(1);
        assertEquals(rule2OutputEntry2.getName(), someRule2OutputEntry2Name);
    }
}
