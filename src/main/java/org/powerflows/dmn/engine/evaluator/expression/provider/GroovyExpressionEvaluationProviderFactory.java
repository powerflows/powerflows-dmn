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

import java.util.Collections;
import java.util.List;

public class GroovyExpressionEvaluationProviderFactory implements ExpressionEvaluationProviderFactory {
    private static final List<ExpressionType> SUPPORTED = Collections.singletonList(ExpressionType.GROOVY);

    @Override
    public ExpressionEvaluationProvider createProvider(final ExpressionEvaluationConfiguration configuration) {
        return new GroovyExpressionEvaluationProvider(configuration);
    }

    @Override
    public List<ExpressionType> supportedExpressionTypes() {
        return SUPPORTED;
    }
}
