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

package org.powerflows.dmn.io.yaml;

import org.powerflows.dmn.engine.model.decision.Decision;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.field.Output;
import org.powerflows.dmn.engine.model.decision.rule.Rule;
import org.powerflows.dmn.io.DecisionToExternalModelConverter;
import org.powerflows.dmn.io.yaml.model.YamlDecision;
import org.powerflows.dmn.io.yaml.model.field.YamlFields;
import org.powerflows.dmn.io.yaml.model.field.YamlInput;
import org.powerflows.dmn.io.yaml.model.field.YamlOutput;
import org.powerflows.dmn.io.yaml.model.rule.YamlRule;
import org.powerflows.dmn.io.yaml.model.rule.entry.YamlInputEntry;
import org.powerflows.dmn.io.yaml.model.rule.entry.YamlOutputEntry;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class YamlDecisionConverter implements DecisionToExternalModelConverter<YamlDecision> {
    @Override
    public YamlDecision to(final Decision decision) {
        final YamlDecision yamlDecision = new YamlDecision();
        yamlDecision.setId(decision.getId());
        yamlDecision.setName(decision.getName());
        yamlDecision.setExpressionType(decision.getExpressionType());
        yamlDecision.setEvaluationMode(decision.getEvaluationMode());
        yamlDecision.setHitPolicy(decision.getHitPolicy());
        yamlDecision.setFields(createFields(decision.getInputs(), decision.getOutputs()));

        final Map<String, Input> inputsMap = decision.getInputs()
                .stream()
                .collect(Collectors.toMap(Input::getName, Function.identity()));

        yamlDecision.setRules(decision.getRules()
                .stream()
                .map(rule -> ruleToYamlRule(rule, inputsMap))
                .collect(Collectors.toList()));

        return yamlDecision;
    }

    private YamlFields createFields(final List<Input> inputs, final List<Output> outputs) {
        final YamlFields yamlFields = new YamlFields();
        final LinkedHashMap<String, YamlInput> in = new LinkedHashMap<>();
        yamlFields.setIn(in);
        final LinkedHashMap<String, YamlOutput> out = new LinkedHashMap<>();
        yamlFields.setOut(out);

        inputs.forEach(input -> {
            final YamlInput yamlInput = new YamlInput();
            yamlInput.setDescription(input.getDescription());
            yamlInput.setType(input.getType());
            yamlInput.setEvaluationMode(input.getEvaluationMode());

            if (!Input.DEFAULT_NAME_ALIAS.equals(input.getNameAlias())) {
                yamlInput.setNameAlias(input.getNameAlias());
            }

            if (input.getExpression() != null && input.getExpression().getValue() != null) {
                yamlInput.setExpression(input.getExpression().getValue());
                yamlInput.setExpressionType(
                        input.getExpression().getType() == ExpressionType.LITERAL ? null : input
                                .getExpression()
                                .getType());

            }

            in.put(input.getName(), yamlInput);
        });

        outputs.forEach(output -> {
            final YamlOutput yamlOutput = new YamlOutput();
            yamlOutput.setDescription(output.getDescription());
            yamlOutput.setType(output.getType());

            out.put(output.getName(), yamlOutput);
        });

        return yamlFields;
    }

    private YamlRule ruleToYamlRule(final Rule rule, final Map<String, Input> inputsMap) {
        final YamlRule yamlRule = new YamlRule();
        yamlRule.setDescription(rule.getDescription());
        final LinkedHashMap<String, YamlInputEntry> in = new LinkedHashMap<>();
        yamlRule.setIn(in);
        final LinkedHashMap<String, YamlOutputEntry> out = new LinkedHashMap<>();
        yamlRule.setOut(out);

        rule.getInputEntries().forEach(inputEntry -> {
            final YamlInputEntry yamlInputEntry = new YamlInputEntry();
            yamlInputEntry.setExpressionType(inputEntry.getExpression().getType());
            yamlInputEntry.setExpression(inputEntry.getExpression().getValue());
            yamlInputEntry.setEvaluationMode(inputEntry.getEvaluationMode());

            final Input input = inputsMap.get(inputEntry.getName());

            if (inputEntry.getNameAlias() != null && !inputEntry.getNameAlias().equals(input.getNameAlias())) {
                yamlInputEntry.setNameAlias(inputEntry.getNameAlias());
            }

            in.put(inputEntry.getName(), yamlInputEntry);
        });

        rule.getOutputEntries().forEach(outputEntry -> {
            final YamlOutputEntry yamlOutputEntry = new YamlOutputEntry();
            yamlOutputEntry.setExpressionType(outputEntry.getExpression().getType());
            yamlOutputEntry.setExpression(outputEntry.getExpression().getValue());

            out.put(outputEntry.getName(), yamlOutputEntry);
        });

        return yamlRule;
    }

    @Override
    public Decision from(final YamlDecision model) {
        final Decision.Builder builder = Decision.builder();
        builder.id(model.getId())
                .name(model.getName())
                .expressionType(model.getExpressionType())
                .evaluationMode(model.getEvaluationMode())
                .hitPolicy(model.getHitPolicy());

        model.getFields().getIn().forEach((name, input) -> builder
                .withInput(inputBuilder -> inputBuilder
                        .name(name)
                        .type(input.getType())
                        .evaluationMode(input.getEvaluationMode())
                        .description(input.getDescription())
                        .withExpression(expressionBuilder -> expressionBuilder
                                .type(input.getExpressionType())
                                .value((Serializable) input.getExpression())
                                .build())
                        .build()));

        model.getFields().getOut().forEach((name, output) -> builder
                .withOutput(outpBuilder -> outpBuilder
                        .name(name)
                        .type(output.getType())
                        .description(output.getDescription())
                        .build()));

        model.getRules().forEach(rule -> builder.withRule(ruleBuilder -> {
            rule.getIn().forEach((name, input) -> ruleBuilder
                    .withInputEntry(inputEntryBuilder -> inputEntryBuilder
                            .name(name)
                            .evaluationMode(input.getEvaluationMode())
                            .withExpression(expressionBuilder -> expressionBuilder
                                    .type(input.getExpressionType())
                                    .value((Serializable) input.getExpression())
                                    .build())
                            .build()));

            rule.getOut().forEach((name, output) -> ruleBuilder
                    .withOutputEntry(outputEntryBuilder -> outputEntryBuilder
                            .name(name)
                            .withExpression(expressionBuilder -> expressionBuilder
                                    .type(output.getExpressionType())
                                    .value((Serializable) output.getExpression())
                                    .build())
                            .build()));

            return ruleBuilder
                    .description(rule.getDescription())
                    .build();
        }));

        return builder.build();
    }
}
