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
import org.powerflows.dmn.engine.model.decision.EvaluationMode;
import org.powerflows.dmn.engine.model.decision.HitPolicy;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.field.Output;
import org.powerflows.dmn.engine.model.decision.rule.Rule;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;
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
        final ExpressionType decisionExpressionType = findDecisionExpressionType(decision);
        final EvaluationMode decisionEvaluationMode = Decision.DEFAULT_EVALUATION_MODE.equals(decision.getEvaluationMode()) ? null : decision.getEvaluationMode();
        final HitPolicy decisionHitPolicy = Decision.DEFAULT_HIT_POLICY.equals(decision.getHitPolicy()) ? null : decision.getHitPolicy();

        final YamlDecision yamlDecision = new YamlDecision();
        yamlDecision.setId(decision.getId());
        yamlDecision.setName(decision.getName());
        yamlDecision.setExpressionType(Decision.DEFAULT_EXPRESSION_TYPE.equals(decisionExpressionType) ? null : decisionExpressionType);
        yamlDecision.setEvaluationMode(decisionEvaluationMode);
        yamlDecision.setHitPolicy(decisionHitPolicy);
        yamlDecision.setFields(createFields(decision.getInputs(), decision.getOutputs(), decisionEvaluationMode, decisionExpressionType));

        final Map<String, Input> inputsMap = decision.getInputs()
                .stream()
                .collect(Collectors.toMap(Input::getName, Function.identity()));

        yamlDecision.setRules(decision.getRules()
                .stream()
                .map(rule -> ruleToYamlRule(rule, inputsMap, decisionExpressionType))
                .collect(Collectors.toList()));

        return yamlDecision;
    }

    private ExpressionType findDecisionExpressionType(final Decision decision) {
        final ExpressionType expressionType = decision.getRules().get(0).getInputEntries().get(0).getExpression().getType();

        for (Rule rule : decision.getRules()) {
            for (InputEntry inputEntry : rule.getInputEntries()) {
                if (!expressionType.equals(inputEntry.getExpression().getType())) {
                    return decision.getExpressionType();
                }
            }
            for (OutputEntry outputEntry : rule.getOutputEntries()) {
                if (!expressionType.equals(outputEntry.getExpression().getType())) {
                    return decision.getExpressionType();
                }
            }
        }

        return expressionType;
    }

    private YamlFields createFields(final List<Input> inputs,
                                    final List<Output> outputs,
                                    final EvaluationMode decisionEvaluationMode,
                                    final ExpressionType decisionExpressionType) {
        final YamlFields yamlFields = new YamlFields();
        final LinkedHashMap<String, YamlInput> in = new LinkedHashMap<>();
        yamlFields.setIn(in);
        final LinkedHashMap<String, YamlOutput> out = new LinkedHashMap<>();
        yamlFields.setOut(out);

        inputs.forEach(input -> {
            final YamlInput yamlInput = new YamlInput();
            yamlInput.setDescription(input.getDescription());
            yamlInput.setType(input.getType());

            if (decisionEvaluationMode != null && !decisionEvaluationMode.equals(input.getEvaluationMode())) {
                yamlInput.setEvaluationMode(input.getEvaluationMode());
            }

            if (!Input.DEFAULT_NAME_ALIAS.equals(input.getNameAlias())) {
                yamlInput.setNameAlias(input.getNameAlias());
            }

            yamlInput.setExpression(input.getExpression().getValue());
            if (!decisionExpressionType.equals(input.getExpression().getType())) {
                yamlInput.setExpressionType(input.getExpression().getType());
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

    private YamlRule ruleToYamlRule(final Rule rule, final Map<String, Input> inputsMap, final ExpressionType decisionExpressionType) {
        final YamlRule yamlRule = new YamlRule();
        yamlRule.setDescription(rule.getDescription());
        final LinkedHashMap<String, YamlInputEntry> in = new LinkedHashMap<>();
        yamlRule.setIn(in);
        final LinkedHashMap<String, YamlOutputEntry> out = new LinkedHashMap<>();
        yamlRule.setOut(out);

        rule.getInputEntries().forEach(inputEntry -> {
            final Input input = inputsMap.get(inputEntry.getName());
            final YamlInputEntry yamlInputEntry = new YamlInputEntry();

            yamlInputEntry.setExpression(inputEntry.getExpression().getValue());

            if (!inputEntry.getExpression().getType().equals(decisionExpressionType)) {
                yamlInputEntry.setExpressionType(inputEntry.getExpression().getType());
            }

            if (!input.getEvaluationMode().equals(inputEntry.getEvaluationMode())) {
                yamlInputEntry.setEvaluationMode(inputEntry.getEvaluationMode());
            }

            if (!inputEntry.getNameAlias().equals(input.getNameAlias())) {
                yamlInputEntry.setNameAlias(inputEntry.getNameAlias());
            }

            in.put(inputEntry.getName(), yamlInputEntry);
        });

        rule.getOutputEntries().forEach(outputEntry -> {
            final YamlOutputEntry yamlOutputEntry = new YamlOutputEntry();
            if (!outputEntry.getExpression().getType().equals(decisionExpressionType)) {
                yamlOutputEntry.setExpressionType(outputEntry.getExpression().getType());
            }

            yamlOutputEntry.setExpression(outputEntry.getExpression().getValue());

            out.put(outputEntry.getName(), yamlOutputEntry);
        });

        return yamlRule;
    }

    @Override
    public Decision from(final YamlDecision model) {
        final Decision.Builder builder = Decision.builder();
        builder.id(model.getId())
                .name(model.getName());

        if (model.getExpressionType() != null) {
            builder.expressionType(model.getExpressionType());
        }

        if (model.getEvaluationMode() != null) {
            builder.evaluationMode(model.getEvaluationMode());
        }

        if (model.getHitPolicy() != null) {
            builder.hitPolicy(model.getHitPolicy());
        }

        model.getFields().getIn().forEach((name, input) -> builder
                .withInput(inputBuilder -> {
                            if (input.getNameAlias() != null) {
                                inputBuilder.nameAlias(input.getNameAlias());
                            }

                            return inputBuilder
                                    .name(name)
                                    .type(input.getType())
                                    .evaluationMode(input.getEvaluationMode())
                                    .description(input.getDescription())
                                    .withExpression(expressionBuilder -> expressionBuilder
                                            .type(input.getExpressionType())
                                            .value((Serializable) input.getExpression())
                                            .build())
                                    .build();
                        }
                )
        );

        model.getFields().getOut().forEach((name, output) -> builder
                .withOutput(outpBuilder -> outpBuilder
                        .name(name)
                        .type(output.getType())
                        .description(output.getDescription())
                        .build()));

        model.getRules().forEach(rule -> builder.withRule(ruleBuilder -> {
            rule.getIn().forEach((name, inputEntry) -> ruleBuilder
                    .withInputEntry(inputEntryBuilder -> inputEntryBuilder
                            .name(name)
                            .nameAlias(inputEntry.getNameAlias())
                            .evaluationMode(inputEntry.getEvaluationMode())
                            .withExpression(expressionBuilder -> expressionBuilder
                                    .type(inputEntry.getExpressionType())
                                    .value((Serializable) inputEntry.getExpression())
                                    .build())
                            .build()));

            rule.getOut().forEach((name, outputEntry) -> ruleBuilder
                    .withOutputEntry(outputEntryBuilder -> outputEntryBuilder
                            .name(name)
                            .withExpression(expressionBuilder -> expressionBuilder
                                    .type(outputEntry.getExpressionType())
                                    .value((Serializable) outputEntry.getExpression())
                                    .build())
                            .build()));

            return ruleBuilder
                    .description(rule.getDescription())
                    .build();
        }));

        return builder.build();
    }
}
