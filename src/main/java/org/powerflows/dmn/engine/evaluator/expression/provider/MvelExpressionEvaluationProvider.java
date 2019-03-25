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
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.powerflows.dmn.engine.evaluator.context.EvaluationContext;
import org.powerflows.dmn.engine.evaluator.exception.EvaluationException;
import org.powerflows.dmn.engine.evaluator.expression.ExpressionEvaluationException;
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.InstanceMethodBinding;
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.MethodBinding;
import org.powerflows.dmn.engine.model.decision.expression.Expression;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;

import java.io.Serializable;

/**
 * Provides MVEL expression evaluation.
 *
 * Doesn't support instance method binding.
 */
@Slf4j
class MvelExpressionEvaluationProvider implements ExpressionEvaluationProvider {
    private final VariableResolverFactory functionResolverFactory = new MapVariableResolverFactory();

    MvelExpressionEvaluationProvider(final ExpressionEvaluationConfiguration configuration) {
        configuration.getMethodBindings().forEach(methodBinding -> functionResolverFactory.createVariable(methodBinding.name().replaceAll("\\s", ""), createMethodBinding(methodBinding)));
    }

    private Object createMethodBinding(final MethodBinding methodBinding) {
        if (methodBinding instanceof InstanceMethodBinding) {
            throw new EvaluationException("Instance method binding for MVEL is not supported yet");
        }

        return methodBinding.method();
    }


    @Override
    public Serializable evaluateInput(final Input input, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of input: {} with evaluation context: {}", input, evaluationContext);

        final Serializable result = evaluate(input.getExpression(), evaluationContext);

        log.debug("Evaluated result: {}", result);

        return result;
    }

    @Override
    public Serializable evaluateInputEntry(final InputEntry inputEntry, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of input entry {} with evaluation context: {}", inputEntry, evaluationContext);

        final Serializable result = evaluate(inputEntry, evaluationContext);

        log.debug("Evaluated entry result: {}", result);

        return result;
    }

    @Override
    public Serializable evaluateOutputEntry(final OutputEntry outputEntry, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of output entry {} with evaluation context: {}", outputEntry, evaluationContext);

        final Serializable result = evaluate(outputEntry.getExpression(), evaluationContext);

        log.debug("Evaluated entry result: {}", result);

        return result;
    }

    Serializable evaluate(final InputEntry inputEntry, final EvaluationContext evaluationContext) {
        final VariableResolverFactory mapVariableResolverFactory = new MapVariableResolverFactory();

        fillVariables(evaluationContext, mapVariableResolverFactory);
        mapVariableResolverFactory.createVariable(inputEntry.getNameAlias(), evaluationContext.get(inputEntry.getName()));

        return evaluate(inputEntry.getExpression(), mapVariableResolverFactory);
    }

    final Serializable evaluate(final Expression expression, final EvaluationContext evaluationContext) {
        final VariableResolverFactory mapVariableResolverFactory = new MapVariableResolverFactory();

        fillVariables(evaluationContext, mapVariableResolverFactory);
        return evaluate(expression, mapVariableResolverFactory);
    }

    final void fillVariables(final EvaluationContext evaluationContext, final VariableResolverFactory mapVariableResolverFactory) {
        evaluationContext
                .getAll()
                .keySet()
                .forEach(variableName -> mapVariableResolverFactory.createVariable(variableName, evaluationContext.get(variableName)));
    }

    final Serializable evaluate(final Expression expression, final VariableResolverFactory variableResolverFactory) {
        final Serializable result;
        variableResolverFactory.setNextFactory(functionResolverFactory);

        try {
            result = (Serializable) MVEL.eval((String) expression.getValue(), variableResolverFactory);
        } catch (Exception e) {
            throw new ExpressionEvaluationException("Can not evaluate feel expression '" + expression.getValue() + "'", e);
        }

        return result;
    }
}