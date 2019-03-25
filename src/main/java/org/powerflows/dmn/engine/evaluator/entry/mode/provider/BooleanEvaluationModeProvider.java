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

package org.powerflows.dmn.engine.evaluator.entry.mode.provider;

import org.powerflows.dmn.engine.evaluator.type.value.SpecifiedTypeValue;
import org.powerflows.dmn.engine.model.decision.field.ValueType;

/**
 * Boolean based entry evaluation provider.
 * Handles default S-FEEL behavior, entry evaluation is expected to be boolean which becomes the result.
 */
class BooleanEvaluationModeProvider implements EvaluationModeProvider {

    @Override
    public <T, P> boolean isPositive(final ValueType inputType, final SpecifiedTypeValue<T> inputEntryValue, final SpecifiedTypeValue<P> inputValue) {
        if (inputEntryValue == null) {
            throw new NullPointerException("Input entry value can not be null");
        }

        return inputEntryValue.isSingleValue() && Boolean.TRUE.equals(inputEntryValue.getValue());
    }
}
