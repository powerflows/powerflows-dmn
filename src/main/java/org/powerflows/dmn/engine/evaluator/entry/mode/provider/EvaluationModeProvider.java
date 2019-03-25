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
 * Provider for entry result evaluation.
 * Implementation is transforming entry input and entry expression evaluation result into final rule entry result according to configured mode.
 * Implementations must be stateless.
 */
public interface EvaluationModeProvider {

    /**
     * Evaluates entry to boolean value.
     *
     * @param inputType rule input declared type
     * @param inputEntryValue input entry value from decision configuration
     * @param inputValue value from context
     * @param <T> declared type of input entry value
     * @param <P> declared type of input value
     * @return if evaluation result is positive
     */
    <T, P> boolean isPositive(ValueType inputType, SpecifiedTypeValue<T> inputEntryValue, SpecifiedTypeValue<P> inputValue);
}
