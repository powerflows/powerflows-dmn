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

import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;

import java.util.List;

/**
 * Defines Expression evaluation provider factory contract.
 *
 * Implementation must be stateless with default constructor as instances are created by JRE {@link java.util.ServiceLoader} infrastructure.
 */
public interface ExpressionEvaluationProviderFactory {

    /**
     * Creates provider instance with given configuration.
     * @param configuration to use
     * @return expression evaluation provider instance
     */
    ExpressionEvaluationProvider createProvider(ExpressionEvaluationConfiguration configuration);

    /**
     * Enums supported expression types.
     * @return list of expression types
     */
    List<ExpressionType> supportedExpressionTypes();
}
