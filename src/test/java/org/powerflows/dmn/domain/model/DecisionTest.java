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

        assertEquals(someTableId, decision.getId());
        assertEquals(someTableName, decision.getName());
        assertEquals(someHitPolicy, decision.getHitPolicy());
        assertEquals(2, decision.getInputs().size());
        assertEquals(2, decision.getOutputs().size());
        assertEquals(2, decision.getRules().size());

        final Input input1 = decision.getInputs().get(0);
        assertEquals(someInput1Name, input1.getName());
        assertEquals(someInput1Description, input1.getDescription());
        assertEquals(someInput1Type, input1.getType());

        final Expression input1Expression1 = input1.getExpression();
        assertEquals(someInput1Expression1Type, input1Expression1.getType());
        assertEquals(someInput1Expression1Value, input1Expression1.getValue());

        final Input input2 = decision.getInputs().get(1);
        assertEquals(someInput2Name, input2.getName());
        assertEquals(someInput2Description, input2.getDescription());
        assertEquals(someInput2Type, input2.getType());

        final Output output1 = decision.getOutputs().get(0);
        assertEquals(someOutput1Name, output1.getName());
        assertEquals(someOutput1Description, output1.getDescription());
        assertEquals(someOutput1Type, output1.getType());

        final Output output2 = decision.getOutputs().get(1);
        assertEquals(someOutput2Name, output2.getName());
        assertEquals(someOutput2Description, output2.getDescription());
        assertEquals(someOutput2Type, output2.getType());

        final Rule rule1 = decision.getRules().get(0);
        assertEquals(someRule1Description, rule1.getDescription());
        assertEquals(2, rule1.getInputEntries().size());
        assertEquals(1, rule1.getOutputEntries().size());

        final InputEntry rule1InputEntry1 = rule1.getInputEntries().get(0);
        assertEquals(someRule1InputEntry1Name, rule1InputEntry1.getName());

        final Expression rule1InputEntry1Expression = rule1InputEntry1.getExpression();

        assertEquals(someRule1InputEntry1ExpressionType, rule1InputEntry1Expression.getType());
        assertEquals(someRule1InputEntry1ExpressionValue, rule1InputEntry1Expression.getValue());

        final InputEntry rule1InputEntry2 = rule1.getInputEntries().get(1);
        assertEquals(someRule1InputEntry2Name, rule1InputEntry2.getName());

        final Expression rule1InputEntry2Expression = rule1InputEntry2.getExpression();

        assertEquals(someRule1InputEntry2ExpressionType, rule1InputEntry2Expression.getType());
        assertEquals(someRule1InputEntry2ExpressionValue, rule1InputEntry2Expression.getValue());

        final OutputEntry rule1OutputEntry1 = rule1.getOutputEntries().get(0);
        assertEquals(someRule1OutputEntry1Name, rule1OutputEntry1.getName());

        final Expression rule1OutputEntry1Expression = rule1OutputEntry1.getExpression();
        assertEquals(someRule1OutputEntry1ExpressionType, rule1OutputEntry1Expression.getType());
        assertEquals(someRule1OutputEntry1ExpressionValue, rule1OutputEntry1Expression.getValue());

        final Rule rule2 = decision.getRules().get(1);
        assertEquals(someRule2Description, rule2.getDescription());
        assertEquals(2, rule2.getInputEntries().size());
        assertEquals(2, rule2.getOutputEntries().size());

        final InputEntry rule2InputEntry1 = rule2.getInputEntries().get(0);
        assertEquals(someRule2InputEntry1Name, rule2InputEntry1.getName());

        final InputEntry rule2InputEntry2 = rule2.getInputEntries().get(1);
        assertEquals(someRule2InputEntry2Name, rule2InputEntry2.getName());

        final OutputEntry rule2OutputEntry1 = rule2.getOutputEntries().get(0);
        assertEquals(someRule2OutputEntry1Name, rule2OutputEntry1.getName());

        final OutputEntry rule2OutputEntry2 = rule2.getOutputEntries().get(1);
        assertEquals(someRule2OutputEntry2Name, rule2OutputEntry2.getName());
    }
}
