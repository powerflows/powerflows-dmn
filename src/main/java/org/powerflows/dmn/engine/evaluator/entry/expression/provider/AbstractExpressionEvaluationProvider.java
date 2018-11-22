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

public abstract class AbstractExpressionEvaluationProvider implements ExpressionEvaluationProvider {

    public abstract boolean evaluateInputEntry(InputEntry inputEntry, ModifiableContextVariables contextVariables);

    public abstract Object evaluateInput(Input input, ModifiableContextVariables contextVariables);

    public abstract EntryResult evaluateOutputEntry(OutputEntry outputEntry, ModifiableContextVariables contextVariables);

    protected boolean isInputEntryValueEqualsInputValue(final Object inputEntryValue, final Object inputValue) {
        final boolean result;
        if (inputValue instanceof Set && inputEntryValue instanceof Set) {
            result = areSetsEqual((Set<Object>) inputValue, (Set<Object>) inputEntryValue);
        } else if (!(inputValue instanceof Set) && !(inputEntryValue instanceof Set)) {
            result = areObjectsEqual(inputValue, inputEntryValue);
        } else {
            result = false;
        }

        return result;
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
