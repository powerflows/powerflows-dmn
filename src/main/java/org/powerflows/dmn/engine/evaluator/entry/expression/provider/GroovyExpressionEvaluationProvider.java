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
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;
import org.powerflows.dmn.engine.model.evaluation.result.EntryResult;

/**
 * This provider should be moved to external jar as an optional dependency
 */
class GroovyExpressionEvaluationProvider extends AbstractExpressionEvaluationProvider {

    @Override
    public boolean evaluateInputEntry(final InputEntry inputEntry, final ModifiableContextVariables contextVariables) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object evaluateInput(final Input input, final ModifiableContextVariables contextVariables) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntryResult evaluateOutputEntry(final OutputEntry outputEntry, final ModifiableContextVariables contextVariables) {
        throw new UnsupportedOperationException();
    }
}
