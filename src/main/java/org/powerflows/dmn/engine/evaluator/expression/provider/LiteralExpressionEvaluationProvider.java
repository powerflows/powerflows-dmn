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
import org.powerflows.dmn.engine.model.decision.expression.Expression;
import org.powerflows.dmn.engine.model.decision.field.Input;


@Slf4j
class LiteralExpressionEvaluationProvider implements ExpressionEvaluationProvider {

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
    public Object evaluateEntry(final Expression entryExpression, final ModifiableContextVariables contextVariables) {
        log.debug("Starting evaluation of entry with expression: {} and context variables: {}", entryExpression, contextVariables);

        final Object result = entryExpression.getValue();

        log.debug("Evaluated entry result: {}", result);

        return result;
    }
}
