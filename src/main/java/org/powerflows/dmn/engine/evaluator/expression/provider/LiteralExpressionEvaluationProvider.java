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
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;

import java.io.Serializable;


@Slf4j
class LiteralExpressionEvaluationProvider implements ExpressionEvaluationProvider {

    @Override
    public Serializable evaluateInput(final Input input, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of input: {} with evaluation context: {}", input, evaluationContext);

        final Serializable value = evaluationContext.get(input.getName());

        if (value == null) {
            log.warn("Input value is null");
        }

        log.debug("Evaluated input result: {}", value);

        return value;
    }

    @Override
    public Serializable evaluateInputEntry(final InputEntry inputEntry, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of input entry with evaluation context: {}", inputEntry, evaluationContext);

        final Serializable result = inputEntry.getExpression().getValue();

        log.debug("Evaluated input entry result: {}", result);

        return result;
    }

    @Override
    public Serializable evaluateOutputEntry(final OutputEntry outputEntry, final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of output entry with evaluation context: {}", outputEntry, evaluationContext);

        final Serializable result = outputEntry.getExpression().getValue();

        log.debug("Evaluated output entry result: {}", result);

        return result;
    }
}
