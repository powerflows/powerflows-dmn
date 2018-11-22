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

package org.powerflows.dmn.engine.evaluator.entry.expression.provider;

import org.powerflows.dmn.engine.evaluator.context.ModifiableContextVariables;
import org.powerflows.dmn.engine.evaluator.entry.expression.provider.script.ScriptEngineProvider;
import org.powerflows.dmn.engine.evaluator.entry.expression.provider.script.bindings.ContextVariablesBindings;
import org.powerflows.dmn.engine.evaluator.exception.EvaluationException;
import org.powerflows.dmn.engine.model.decision.expression.Expression;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;
import org.powerflows.dmn.engine.model.evaluation.result.EntryResult;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * This provider should be moved to external jar as an optional dependency
 */
class GroovyExpressionEvaluationProvider extends AbstractExpressionEvaluationProvider {

    private ScriptEngineProvider scriptEngineProvider;

    public GroovyExpressionEvaluationProvider(ScriptEngineProvider scriptEngineProvider) {
        this.scriptEngineProvider = scriptEngineProvider;
    }

    @Override
    public boolean evaluateInputEntry(final InputEntry inputEntry, final ModifiableContextVariables contextVariables) {
        final Object inputValue = contextVariables.get(inputEntry.getName());
        final Object inputEntryValue = evaluateValue(inputEntry.getExpression(), contextVariables);

        return isInputEntryValueEqualsInputValue(inputEntryValue, inputValue);
    }

    @Override
    public Object evaluateInput(final Input input, final ModifiableContextVariables contextVariables) {
        return evaluateValue(input.getExpression(), contextVariables);
    }

    @Override
    public EntryResult evaluateOutputEntry(final OutputEntry outputEntry, final ModifiableContextVariables contextVariables) {
        final Object outputEntryValue = evaluateValue(outputEntry.getExpression(), contextVariables);

        return EntryResult.builder().name(outputEntry.getName()).value(outputEntryValue).build();
    }

    private Object evaluateValue(final Expression expression, final ModifiableContextVariables contextVariables) {
        final ScriptEngine scriptEngine = scriptEngineProvider.getScriptEngine(expression.getType());
        final Bindings bindings = ContextVariablesBindings.create(scriptEngine.createBindings(), contextVariables);

        final Object value;

        try {
            value = scriptEngine.eval((String) expression.getValue(), bindings);
        } catch (ScriptException e) {
            throw new EvaluationException(e);
        }

        return value;
    }
}
