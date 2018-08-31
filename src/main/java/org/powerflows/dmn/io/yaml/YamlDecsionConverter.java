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
import org.powerflows.dmn.io.DecisionToExternalModelConverter;
import org.powerflows.dmn.io.yaml.model.YamlDecision;

public class YamlDecsionConverter implements DecisionToExternalModelConverter<YamlDecision> {
    @Override
    public YamlDecision to(final Decision decision) {
        throw new UnsupportedOperationException("This is not supported yet");
    }

    @Override
    public Decision from(final YamlDecision model) {
        final Decision.Builder builder = Decision.builder();
        builder.id(model.getId())
                .name(model.getName())
                .expressionType(model.getExpressionType())
                .hitPolicy(model.getHitPolicy());

        model.getFields().getIn().forEach((name, input) -> builder
                .withInput(inputBuilder -> inputBuilder
                        .name(name)
                        .type(input.getType())
                        .description(input.getDescription())
                        .withExpression(expressionBuilder -> expressionBuilder
                                .type(input.getExpressionType())
                                .value(input.getExpression())
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
                            .withExpression(expressionBuilder -> expressionBuilder
                                    .type(input.getExpressionType())
                                    .value(input.getExpression())
                                    .build())
                            .build()));

            rule.getOut().forEach((name, output) -> ruleBuilder
                    .withOutputEntry(outputEntryBuilder -> outputEntryBuilder
                            .name(name)
                            .withExpression(expressionBuilder -> expressionBuilder
                                    .type(output.getExpressionType())
                                    .value(output.getExpression())
                                    .build())
                            .build()));

            return ruleBuilder
                    .description(rule.getDescription())
                    .build();
        }));

        return builder.build();
    }
}
