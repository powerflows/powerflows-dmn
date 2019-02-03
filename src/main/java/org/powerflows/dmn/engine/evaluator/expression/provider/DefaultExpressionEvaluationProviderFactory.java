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

package org.powerflows.dmn.engine.evaluator.expression.provider;

import lombok.extern.slf4j.Slf4j;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;

import java.util.EnumMap;
import java.util.Optional;
import java.util.ServiceLoader;

@Slf4j
public class DefaultExpressionEvaluationProviderFactory {

    private static final ServiceLoader<ExpressionEvaluationProviderFactory> serviceLoader = ServiceLoader.load(ExpressionEvaluationProviderFactory.class);

    private final EnumMap<ExpressionType, ExpressionEvaluationProviderFactory> factories = new EnumMap<>(ExpressionType.class);

    private final EnumMap<ExpressionType, ExpressionEvaluationProvider> providers = new EnumMap<>(ExpressionType.class);
    private final ExpressionEvaluationConfiguration configuration;


    public DefaultExpressionEvaluationProviderFactory() {
        this(ExpressionEvaluationConfiguration.simpleConfiguration());
    }

    public DefaultExpressionEvaluationProviderFactory(final ExpressionEvaluationConfiguration configuration) {
        this.configuration = configuration;
        serviceLoader.forEach(provider ->
                provider.supportedExpressionTypes()
                        .forEach(type -> {
                                    log.debug("Found ExpressionEvaluationProvider for type {} - {}", type, provider);
                                    factories.put(type, provider);
                                }
                        )
        );
    }

    public ExpressionEvaluationProvider getInstance(final ExpressionType expressionType) {
        final ExpressionEvaluationProvider expressionEvaluationProvider = providers.computeIfAbsent(expressionType, key -> Optional
                .ofNullable(factories.get(key))
                .map(factory -> factory.createProvider(configuration))
                .orElse(null)
        );

        if (expressionEvaluationProvider == null) {
            throw new IllegalArgumentException("Unknown expression type " + expressionType);
        }

        return expressionEvaluationProvider;
    }
}
