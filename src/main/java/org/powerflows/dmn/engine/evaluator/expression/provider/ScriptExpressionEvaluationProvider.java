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
import org.powerflows.dmn.engine.evaluator.context.ModifiableContextVariables;
import org.powerflows.dmn.engine.evaluator.exception.EvaluationException;
import org.powerflows.dmn.engine.evaluator.expression.comparator.ObjectsComparator;
import org.powerflows.dmn.engine.evaluator.expression.script.ScriptEngineProvider;
import org.powerflows.dmn.engine.evaluator.expression.script.bindings.ContextVariablesBindings;
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
@Slf4j
class ScriptExpressionEvaluationProvider implements ExpressionEvaluationProvider {

    private final ScriptEngineProvider scriptEngineProvider;
    private final ObjectsComparator objectsComparator;

    public ScriptExpressionEvaluationProvider(final ScriptEngineProvider scriptEngineProvider,
                                              final ObjectsComparator objectsComparator) {
        this.scriptEngineProvider = scriptEngineProvider;
        this.objectsComparator = objectsComparator;
    }

    @Override
    public boolean evaluateInputEntry(final InputEntry inputEntry, final ModifiableContextVariables contextVariables) {
        log.debug("Starting evaluation of input entry: {} with context variables: {}", inputEntry, contextVariables);

        final Object inputValue = contextVariables.get(inputEntry.getName());
        final Object inputEntryValue = evaluateValue(inputEntry.getExpression(), contextVariables);

        final boolean result = objectsComparator.isInputEntryValueEqualInputValue(inputEntryValue, inputValue);

        log.debug("Evaluated input entry result: {}", result);

        return result;
    }

    @Override
    public Object evaluateInput(final Input input, final ModifiableContextVariables contextVariables) {
        log.debug("Starting evaluation of input: {} with context variables: {}", input, contextVariables);

        final Object value = evaluateValue(input.getExpression(), contextVariables);

        log.debug("Evaluated input result: {}", value);

        return value;
    }

    @Override
    public EntryResult evaluateOutputEntry(final OutputEntry outputEntry, final ModifiableContextVariables contextVariables) {
        log.debug("Starting evaluation of output entry: {} with context variables: {}", outputEntry, contextVariables);

        final Object outputEntryValue = evaluateValue(outputEntry.getExpression(), contextVariables);

        final EntryResult entryResult = EntryResult.builder().name(outputEntry.getName()).value(outputEntryValue).build();

        log.debug("Evaluated output result: {}", entryResult);

        return entryResult;
    }

    private Object evaluateValue(final Expression expression, final ModifiableContextVariables contextVariables) {
        final ScriptEngine scriptEngine = scriptEngineProvider.getScriptEngine(expression.getType());
        final Bindings bindings = ContextVariablesBindings.create(scriptEngine.createBindings(), contextVariables);

        final Object value;

        try {
            value = scriptEngine.eval((String) expression.getValue(), bindings);
        } catch (ScriptException e) {
            throw new EvaluationException("Script evaluation exception", e);
        }

        return value;
    }
}
