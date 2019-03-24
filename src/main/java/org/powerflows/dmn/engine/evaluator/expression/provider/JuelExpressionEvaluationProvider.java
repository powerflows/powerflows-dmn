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

import de.odysseus.el.ExpressionFactoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.powerflows.dmn.engine.evaluator.context.EvaluationContext;
import org.powerflows.dmn.engine.evaluator.expression.ExpressionEvaluationException;
import org.powerflows.dmn.engine.evaluator.expression.provider.juel.MethodBindingListFunctionMapper;
import org.powerflows.dmn.engine.evaluator.expression.provider.juel.BasicELContext;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import java.io.Serializable;

/**
 * Provides JUEL expression evaluation.
 * Doesn't support instance method binding.
 */
@Slf4j
class JuelExpressionEvaluationProvider implements ExpressionEvaluationProvider {

    private final ExpressionFactory expressionFactory;
    private final FunctionMapper rootFunctionMapper;

    JuelExpressionEvaluationProvider(final ExpressionEvaluationConfiguration configuration) {
        this.expressionFactory = new ExpressionFactoryImpl();
        rootFunctionMapper = new MethodBindingListFunctionMapper(configuration.getMethodBindings());
    }

    @Override
    public Serializable evaluateInput(final Input input, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of input: {} with evaluation context: {}", input, evaluationContext);

        final Serializable result = evaluate((String) input.getExpression()
                .getValue(), input.getType().realType(), makeContext(evaluationContext));

        log.debug("Evaluated result: {}", result);

        return result;
    }

    @Override
    public Serializable evaluateInputEntry(final InputEntry inputEntry, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of input entry {} with evaluation context: {}", inputEntry, evaluationContext);

        final Serializable result = evaluate((String) inputEntry.getExpression()
                .getValue(), Serializable.class, makeContext(evaluationContext, inputEntry));

        log.debug("Evaluated entry result: {}", result);

        return result;
    }


    @Override
    public Serializable evaluateOutputEntry(final OutputEntry outputEntry, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of output entry {} with evaluation context: {}", outputEntry, evaluationContext);

        final Serializable result = evaluate((String) outputEntry.getExpression()
                .getValue(), Serializable.class, makeContext(evaluationContext));

        log.debug("Evaluated entry result: {}", result);

        return result;
    }

    private BasicELContext makeContext(final EvaluationContext evaluationContext) {
        final BasicELContext context = new BasicELContext(rootFunctionMapper);
        evaluationContext.getAll().forEach((key, value) ->
                context.setVariable(key, expressionFactory.createValueExpression(value, value == null ? Object.class : value
                        .getClass()))
        );

        return context;
    }

    private ELContext makeContext(final EvaluationContext evaluationContext, final InputEntry inputEntry) {
        final BasicELContext context = makeContext(evaluationContext);
        context.setVariable(inputEntry.getNameAlias(), context.getVariableMapper()
                .resolveVariable(inputEntry.getName()));

        return context;
    }

    private Serializable evaluate(final String expressionText, Class<? extends Serializable> targetType, final ELContext evaluationContext) {
        try {
            return (Serializable) expressionFactory
                    .createValueExpression(evaluationContext, "${" + expressionText + "}", targetType)
                    .getValue(evaluationContext);
        } catch (ELException e) {
            throw new ExpressionEvaluationException("Error evaluating expression - " + expressionText, e);
        }
    }
}