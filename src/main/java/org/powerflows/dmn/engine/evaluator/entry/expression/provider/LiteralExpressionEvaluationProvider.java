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

import java.util.Set;

class LiteralExpressionEvaluationProvider extends AbstractExpressionEvaluationProvider {

    @Override
    public boolean evaluateInputEntry(final InputEntry inputEntry, final ModifiableContextVariables contextVariables) {
        final Object contextVariableValue = contextVariables.get(inputEntry.getName());
        final Object inputEntryValue = inputEntry.getExpression().getValue();

        final boolean evaluationResult;

        if (contextVariableValue instanceof Set && inputEntryValue instanceof Set) {
            evaluationResult = areSetsEqual((Set<Object>) contextVariableValue, (Set<Object>) inputEntryValue);
        } else if (!(contextVariableValue instanceof Set) && !(inputEntryValue instanceof Set)) {
            evaluationResult = areObjectsEqual(contextVariableValue, inputEntryValue);
        } else {
            evaluationResult = false;
        }

        return evaluationResult;
    }

    @Override
    public Object evaluateInput(final Input input, final ModifiableContextVariables contextVariables) {
        return contextVariables.get(input.getName());
    }

    @Override
    public EntryResult evaluateOutputEntry(final OutputEntry outputEntry, final ModifiableContextVariables contextVariables) {
        final Object outputEntryValue = outputEntry.getExpression().getValue();

        final EntryResult entryResult = EntryResult.builder().name(outputEntry.getName()).value(outputEntryValue).build();

        //TODO logger will be here

        return entryResult;
    }

    private boolean areObjectsEqual(final Object object1, final Object object2) {
        final boolean result;

        if (object1 == null && object2 == null) {
            result = true;
        } else if (object1 == null) {
            result = false;
        } else {
            result = object1.equals(object2);
        }

        return result;
    }

    private boolean areSetsEqual(final Set<Object> objects1, final Set<Object> objects2) {
        final boolean result;

        if (objects1 == null && objects2 == null) {
            result = true;
        } else if (objects1 == null || objects2 == null) {
            result = false;
        } else if (objects1.size() != objects2.size()) {
            result = false;
        } else {
            result = objects1.containsAll(objects2);
        }

        return result;
    }

}
