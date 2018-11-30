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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public abstract class AbstractExpressionEvaluationProvider implements ExpressionEvaluationProvider {

    public abstract boolean evaluateInputEntry(InputEntry inputEntry, ModifiableContextVariables contextVariables);

    public abstract Object evaluateInput(Input input, ModifiableContextVariables contextVariables);

    public abstract EntryResult evaluateOutputEntry(OutputEntry outputEntry, ModifiableContextVariables contextVariables);

    protected boolean isInputEntryValueEqualsInputValue(final Object inputEntryValue, final Object inputValue) {
        final Collection<Object> inputEntryValues = convertObjectToCollection(inputEntryValue);
        final Collection<Object> inputValues = convertObjectToCollection(inputValue);

        return areSubCollections(inputValues, inputEntryValues);
    }

    private Collection<Object> convertObjectToCollection(final Object object) {
        final Collection<Object> objects;

        if (object == null) {
            objects = null;
        } else if (object instanceof Collection) {
            objects = (Collection<Object>) object;
        } else if (object.getClass().isArray()) {
            objects = Arrays.asList((Object[]) object);
        } else {
            objects = Collections.singleton(object);
        }

        return objects;
    }

    private boolean areSubCollections(final Collection<Object> inputCollection, final Collection<Object> entryCollection) {
        final boolean result;

        if (inputCollection == null && entryCollection == null) {
            result = true;
        } else if (inputCollection == null || entryCollection == null) {
            result = false;
        } else if (inputCollection.isEmpty() && !entryCollection.isEmpty()) {
            result = false;
        } else {
            result = entryCollection.containsAll(inputCollection);
        }

        return result;
    }
}
