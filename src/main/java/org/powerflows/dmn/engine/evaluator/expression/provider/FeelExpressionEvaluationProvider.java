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
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.powerflows.dmn.engine.evaluator.context.EvaluationContext;
import org.powerflows.dmn.engine.evaluator.expression.provider.feel.converter.ExpressionConverter;
import org.powerflows.dmn.engine.evaluator.expression.provider.feel.converter.mvel.FeelToMvelExpressionConverter;
import org.powerflows.dmn.engine.model.decision.expression.Expression;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;

import java.io.Serializable;

/**
 * Provides S-FEEL expression evaluation.
 */
@Slf4j
class FeelExpressionEvaluationProvider extends MvelExpressionEvaluationProvider {

    private final ExpressionConverter expressionConverter;


    FeelExpressionEvaluationProvider(final ExpressionEvaluationConfiguration configuration) {
        super(configuration);

        expressionConverter = new FeelToMvelExpressionConverter(configuration.getMethodBindings());
    }

    @Override
    public Serializable evaluateInput(final Input input, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of input: {} with evaluation context: {}", input, evaluationContext);

        final String mvelInputExpressionValue = expressionConverter.convert(String.valueOf(input.getExpression().getValue()), null);
        final Expression mvelInputExpression = Expression.builder().type(input.getExpression().getType()).value(mvelInputExpressionValue).build();

        final Serializable result = evaluate(mvelInputExpression, evaluationContext);

        log.debug("Evaluated result: {}", result);

        return result;
    }

    @Override
    public Serializable evaluateOutputEntry(final OutputEntry outputEntry, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of output entry with evaluation context: {}", outputEntry, evaluationContext);

        final String mvelInputExpressionValue = expressionConverter.convert(String.valueOf(outputEntry.getExpression().getValue()), null);
        final Expression mvelInputExpression = Expression.builder().type(outputEntry.getExpression().getType()).value(mvelInputExpressionValue).build();

        final Serializable result = evaluate(mvelInputExpression, evaluationContext);

        log.debug("Evaluated output entry result: {}", result);

        return result;
    }

    @Override
    Serializable evaluate(final InputEntry inputEntry, final EvaluationContext evaluationContext) {
        final VariableResolverFactory mapVariableResolverFactory = new MapVariableResolverFactory();

        fillVariables(evaluationContext, mapVariableResolverFactory);
        mapVariableResolverFactory.createVariable(inputEntry.getNameAlias(), evaluationContext.get(inputEntry.getName()));

        final String mvelExpressionValue = expressionConverter.convert(String.valueOf(inputEntry.getExpression().getValue()), inputEntry.getName());
        final Expression mvelInputEntryExpression = Expression.builder().type(inputEntry.getExpression().getType()).value(mvelExpressionValue).build();

        return evaluate(mvelInputEntryExpression, mapVariableResolverFactory);
    }
}
