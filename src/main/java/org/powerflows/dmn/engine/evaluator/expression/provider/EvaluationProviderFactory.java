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

import org.powerflows.dmn.engine.evaluator.expression.script.ScriptEngineProvider;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;

import java.util.EnumMap;

public class EvaluationProviderFactory {

    private final EnumMap<ExpressionType, ExpressionEvaluationProvider> factories = new EnumMap<>(ExpressionType.class);

    public EvaluationProviderFactory(final ScriptEngineProvider scriptEngineProvider) {
        final ExpressionEvaluationProvider scriptExpressionEvaluationProvider = new ScriptExpressionEvaluationProvider(scriptEngineProvider);

        factories.put(ExpressionType.LITERAL, new LiteralExpressionEvaluationProvider());
        factories.put(ExpressionType.FEEL, new FeelExpressionEvaluationProvider());
        factories.put(ExpressionType.JUEL, new JuelExpressionEvaluationProvider());
        factories.put(ExpressionType.GROOVY, scriptExpressionEvaluationProvider);
        factories.put(ExpressionType.JAVASCRIPT, scriptExpressionEvaluationProvider);
    }

    public ExpressionEvaluationProvider getInstance(final ExpressionType expressionType) {
        final ExpressionEvaluationProvider expressionEvaluationProvider = factories.get(expressionType);

        if (expressionEvaluationProvider == null) {
            throw new IllegalArgumentException("Unknown expression type");
        }

        return expressionEvaluationProvider;
    }
}
