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
package org.powerflows.dmn.io.xml;

import lombok.extern.slf4j.Slf4j;
import org.powerflows.dmn.engine.model.decision.Decision;
import org.powerflows.dmn.engine.model.decision.HitPolicy;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;
import org.powerflows.dmn.engine.model.decision.field.ValueType;
import org.powerflows.dmn.engine.model.decision.rule.Rule;
import org.powerflows.dmn.engine.reader.DecisionReadException;
import org.powerflows.dmn.io.DecisionToExternalModelConverter;
import org.powerflows.dmn.io.xml.model.XMLDecision;
import org.powerflows.dmn.io.xml.model.XMLInput;
import org.powerflows.dmn.io.xml.model.XMLInputEntry;
import org.powerflows.dmn.io.xml.model.XMLOutput;
import org.powerflows.dmn.io.xml.model.XMLOutputEntry;
import org.powerflows.dmn.io.xml.model.XMLRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
public class XMLDecisionConverter implements DecisionToExternalModelConverter<XMLDecision> {
    private static final Map<String, HitPolicy> OMG_HITPOLICY_MAPPING;

    static {
        final Map<String, HitPolicy> mapping = new HashMap<>();
        mapping.put("UNIQUE", HitPolicy.UNIQUE);
        mapping.put("FIRST", HitPolicy.FIRST);
        mapping.put("PRIORITY", HitPolicy.PRIORITY);
        mapping.put("ANY", HitPolicy.ANY);
        mapping.put("COLLECT", HitPolicy.COLLECT);
        mapping.put("RULE ORDER", HitPolicy.RULE_ORDER);
        mapping.put("OUTPUT ORDER", HitPolicy.OUTPUT_ORDER);

        OMG_HITPOLICY_MAPPING = Collections.unmodifiableMap(mapping);
    }

    private static final Map<String, ExpressionType> EXPRESSION_TYPE_MAPPING;

    static {
        final Map<String, ExpressionType> mapping = new HashMap<>();
        mapping.put("feel", ExpressionType.FEEL);
        mapping.put("groovy", ExpressionType.GROOVY);
        mapping.put("javascript", ExpressionType.JAVASCRIPT);
        mapping.put("juel", ExpressionType.JUEL);

        EXPRESSION_TYPE_MAPPING = Collections.unmodifiableMap(mapping);
    }

    private final HitPolicy defaultHitPolicy;
    private final ExpressionType defaultExpressionType;

    public XMLDecisionConverter() {
        this.defaultHitPolicy = HitPolicy.UNIQUE;
        this.defaultExpressionType = ExpressionType.FEEL;
    }

    @Override
    public XMLDecision to(final Decision decision) {
        throw new UnsupportedOperationException("Serialization is not supported");
    }

    @Override
    public Decision from(final XMLDecision xmlDecision) {
        final Decision.Builder builder = Decision.builder()
                .id(xmlDecision.getId())
                .name(xmlDecision.getName())
                .hitPolicy(makeHitPolicy(xmlDecision));

        processRules(builder,
                xmlDecision.getDecisionTable().getInputs(),
                xmlDecision.getDecisionTable().getOutputs(),
                xmlDecision.getDecisionTable().getRules());

        return builder.build();
    }

    private Supplier<String> makeSequenceNameSupplier(final String prefix) {
        return new Supplier<String>() {
            private int count = 0;

            @Override
            public String get() {
                return prefix + count++;
            }

            @Override
            public String toString() {
                return prefix + count;
            }
        };
    }

    private void processRules(final Decision.Builder builder, final List<XMLInput> inputs, final List<XMLOutput> outputs, final List<XMLRule> rules) {
        final Set<String> inputNames = new LinkedHashSet<>();
        final Supplier<String> inputNameSequence = makeSequenceNameSupplier("input_");

        inputs.forEach(input -> {
            final String name = selectOrCreateUniqueName(inputNames, input.getId(), null, inputNameSequence);

            builder.withInput(inputBuilder -> {
                if (input.getInputVariable() != null) {
                    inputBuilder.nameAlias(input.getInputVariable());
                }

                return inputBuilder
                        .description(input.getLabel())
                        .name(name)
                        .withExpression(expressionBuilder ->
                                expressionBuilder
                                        .value(input.getInputExpression().getText())
                                        .type(resolveExpressionType(input.getInputExpression().getExpressionLanguage()))
                                        .build())
                        .type(resolveType(input.getInputExpression().getTypeRef()))
                        .build();
            });
        });

        final Set<String> outputNames = new LinkedHashSet<>();
        final Supplier<String> outputNameSequence = makeSequenceNameSupplier("output_");

        outputs.forEach(output -> {
            final String name = selectOrCreateUniqueName(outputNames, output.getId(), output.getName(), outputNameSequence);

            builder.withOutput(outputBuilder ->
                    outputBuilder
                            .description(output.getLabel())
                            .type(resolveType(output.getTypeRef()))
                            .name(name)
                            .build()
            );
        });

        rules.forEach(this.ruleProcessor(new ArrayList<>(inputNames), new ArrayList<>(outputNames), builder));
    }

    private String selectOrCreateUniqueName(final Set<String> names, final String id, final String name, final Supplier<String> nameSequence) {
        final String resultName;

        if (name != null) {
            if (names.contains(name)) {
                log.warn("Names collection {} already contains name {}, using one from sequence {}", names, name, nameSequence);
                resultName = getNextUniqueName(nameSequence, names);
            } else {
                resultName = name.replaceAll("\\s", "_");
            }
        } else if (id != null) {
            if (names.contains(id)) {
                throw new DecisionReadException("Non unique element id: " + id);
            }
            resultName = id;
        } else {
            resultName = getNextUniqueName(nameSequence, names);
        }
        names.add(resultName);

        return resultName;
    }

    private String getNextUniqueName(final Supplier<String> nameSequence, final Set<String> names) {
        while (true) {
            final String name = nameSequence.get();
            if (!names.contains(name)) {
                return name;
            }
        }
    }

    private Consumer<XMLRule> ruleProcessor(final List<String> inputNames, final List<String> outputNames, final Decision.Builder builder) {
        log.debug("Created rule processor for inputs: {}, and outputs: {}", inputNames, outputNames);

        return rule -> {
            log.debug("Processing rule: {}" + rule);

            builder.withRule(ruleBuilder -> {
                processInputEntries(rule.getInputEntries(), inputNames, rule.getId(), ruleBuilder);
                processOutputEntries(rule.getOutputEntries(), outputNames, rule.getId(), ruleBuilder);

                return ruleBuilder
                        .description(rule.getDescription())
                        .build();
            });
        };
    }

    private void processOutputEntries(final List<XMLOutputEntry> outputEntries, final List<String> outputNames, final String id, final Rule.Builder ruleBuilder) {
        if (outputEntries.size() != outputNames.size()) {
            throw new DecisionReadException("Invalid number of outputs in rule " + id);
        }

        for (int idx = 0; idx < outputEntries.size(); idx++) {
            XMLOutputEntry outputEntry = outputEntries.get(idx);
            if (isNotBlank(outputEntry.getExpression())) {
                final String name = outputNames.get(idx);
                ruleBuilder.withOutputEntry(entryBuilder -> entryBuilder
                        .name(name)
                        .withExpression(expressionBuilder ->
                                expressionBuilder
                                        .type(resolveExpressionType(outputEntry.getExpressionLanguage()))
                                        .value(outputEntry.getExpression())
                                        .build())
                        .build());
            }
        }
    }


    private void processInputEntries(final List<XMLInputEntry> inputEntries, final List<String> inputNames, final String id, final Rule.Builder ruleBuilder) {
        if (inputEntries.size() != inputNames.size()) {
            throw new DecisionReadException("Invalid number of inputs in rule " + id);
        }
        for (int idx = 0; idx < inputEntries.size(); idx++) {
            XMLInputEntry outputEntry = inputEntries.get(idx);
            if (isNotBlank(outputEntry.getExpression())) {
                final String name = inputNames.get(idx);
                ruleBuilder.withInputEntry(entryBuilder -> entryBuilder
                        .name(name)
                        .withExpression(expressionBuilder ->
                                expressionBuilder
                                        .type(resolveExpressionType(outputEntry.getExpressionLanguage()))
                                        .value(outputEntry.getExpression())
                                        .build())
                        .build());
            }
        }
    }

    private ExpressionType resolveExpressionType(final String expressionLanguage) {
        if (expressionLanguage == null) {
            return defaultExpressionType;
        } else {
            return EXPRESSION_TYPE_MAPPING.getOrDefault(expressionLanguage.toLowerCase(), defaultExpressionType);
        }
    }

    private boolean isNotBlank(final String text) {
        return text != null && !text.trim().isEmpty();
    }

    private ValueType resolveType(final String typeRef) {
        if (typeRef == null) {
            return ValueType.STRING;
        } else {
            return Arrays.stream(ValueType.values())
                    .filter(v -> v.name().equalsIgnoreCase(typeRef))
                    .findFirst()
                    .orElseGet(() -> {
                        log.debug("Unable to resolve typeRef: {} to PowerFlows Type", typeRef);

                        return null;
                    });
        }
    }

    private HitPolicy makeHitPolicy(final XMLDecision externalModel) {
        final String hitPolicy = externalModel.getDecisionTable().getHitPolicy();

        return OMG_HITPOLICY_MAPPING.getOrDefault(hitPolicy, defaultHitPolicy);
    }
}