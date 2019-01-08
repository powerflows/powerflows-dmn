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
import org.powerflows.dmn.engine.evaluator.context.EvaluationContext;
import org.powerflows.dmn.engine.evaluator.exception.EvaluationException;
import org.powerflows.dmn.engine.evaluator.expression.script.ScriptEngineProvider;
import org.powerflows.dmn.engine.evaluator.expression.script.bindings.ContextVariablesBindings;
import org.powerflows.dmn.engine.model.decision.expression.Expression;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.rule.entry.Entry;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.Serializable;


@Slf4j
class ScriptExpressionEvaluationProvider implements ExpressionEvaluationProvider {

    private final ScriptEngineProvider scriptEngineProvider;

    public ScriptExpressionEvaluationProvider(final ScriptEngineProvider scriptEngineProvider) {
        this.scriptEngineProvider = scriptEngineProvider;
    }

    @Override
    public Serializable evaluateInput(final Input input, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of input: {} with evaluation context: {}", input, evaluationContext);

        final Serializable result = evaluate(input.getExpression(), evaluationContext);

        log.debug("Evaluated result: {}", result);

        return result;
    }

    @Override
    public Serializable evaluateEntry(final Entry entry, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of entry {} with evaluation context: {}", entry, evaluationContext);

        final Serializable result = evaluate(entry, evaluationContext);

        log.debug("Evaluated entry result: {}", result);

        return result;
    }

    private Serializable evaluate(final Entry entry, final EvaluationContext evaluationContext) {
        final ScriptEngine scriptEngine = scriptEngineProvider.getScriptEngine(entry.getExpression().getType());
        final Bindings bindings = ContextVariablesBindings.create(scriptEngine.createBindings(), evaluationContext, entry.getName());

        return evaluate(entry.getExpression(), scriptEngine, bindings);
    }

    private Serializable evaluate(final Expression expression, final EvaluationContext evaluationContext) {
        final ScriptEngine scriptEngine = scriptEngineProvider.getScriptEngine(expression.getType());
        final Bindings bindings = ContextVariablesBindings.create(scriptEngine.createBindings(), evaluationContext);

        return evaluate(expression, scriptEngine, bindings);
    }

    private Serializable evaluate(final Expression expression, final ScriptEngine scriptEngine, final Bindings bindings) {
        final Serializable result;

        try {
            result = (Serializable) scriptEngine.eval((String) expression.getValue(), bindings);
        } catch (ScriptException e) {
            throw new EvaluationException("Script evaluation exception", e);
        }

        return result;
    }
}
