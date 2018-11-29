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

import lombok.extern.slf4j.Slf4j;
import org.powerflows.dmn.engine.evaluator.context.ModifiableContextVariables;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;
import org.powerflows.dmn.engine.model.evaluation.result.EntryResult;


@Slf4j
class LiteralExpressionEvaluationProvider extends AbstractExpressionEvaluationProvider {

    @Override
    public boolean evaluateInputEntry(final InputEntry inputEntry, final ModifiableContextVariables contextVariables) {
        log.debug("Starting evaluation of input entry: {} with context variables: {}", inputEntry, contextVariables);

        final Object inputValue = contextVariables.get(inputEntry.getName());
        final Object inputEntryValue = inputEntry.getExpression().getValue();

        final boolean result = isInputEntryValueEqualsInputValue(inputEntryValue, inputValue);

        log.debug("Evaluated input entry result: {}", result);

        return result;
    }

    @Override
    public Object evaluateInput(final Input input, final ModifiableContextVariables contextVariables) {
        log.debug("Starting evaluation of input: {} with context variables: {}", input, contextVariables);

        final Object value = contextVariables.get(input.getName());

        if (value == null) {
            log.warn("Input value is null");
        }

        log.debug("Evaluated input result: {}", value);

        return value;
    }

    @Override
    public EntryResult evaluateOutputEntry(final OutputEntry outputEntry, final ModifiableContextVariables contextVariables) {
        log.debug("Starting evaluation of output entry: {} with context variables: {}", outputEntry, contextVariables);

        final Object outputEntryValue = outputEntry.getExpression().getValue();

        final EntryResult entryResult = EntryResult.builder().name(outputEntry.getName()).value(outputEntryValue).build();

        log.debug("Evaluated output result: {}", entryResult);

        return entryResult;
    }
}
