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

package org.powerflows.dmn.engine.evaluator.entry.mode

import org.powerflows.dmn.engine.evaluator.entry.mode.provider.EvaluationModeProvider
import org.powerflows.dmn.engine.evaluator.entry.mode.provider.EvaluationModeProviderFactory
import org.powerflows.dmn.engine.evaluator.type.value.BooleanValue
import org.powerflows.dmn.engine.evaluator.type.value.IntegerValue
import org.powerflows.dmn.engine.evaluator.type.value.SpecifiedTypeValue
import org.powerflows.dmn.engine.model.decision.EvaluationMode
import org.powerflows.dmn.engine.model.decision.field.ValueType
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class BooleanEvaluationModeProviderSpec extends Specification {

    @Shared
    private EvaluationModeProvider booleanEvaluationModeProvider

    void setup() {
        final EvaluationModeProviderFactory evaluationModeProviderFactory = new EvaluationModeProviderFactory()
        booleanEvaluationModeProvider = evaluationModeProviderFactory.getInstance(EvaluationMode.BOOLEAN)
    }

    @Unroll
    void 'should compare input entry value #inputEntryValue with result #expectedResult'(
            final Object inputEntryValue, final boolean expectedResult) {
        given:
        final SpecifiedTypeValue<Boolean> specifiedInputEntryValue
        if (inputEntryValue instanceof List) {
            specifiedInputEntryValue = new BooleanValue(inputEntryValue as List<Boolean>)
        } else {
            specifiedInputEntryValue = new BooleanValue(inputEntryValue as Boolean)
        }

        final SpecifiedTypeValue<Integer> specifiedInputValue = null
        final ValueType inputType = null

        when:
        final boolean result = booleanEvaluationModeProvider.isPositive(inputType, specifiedInputEntryValue, specifiedInputValue)

        then:
        result == expectedResult

        where:
        inputEntryValue || expectedResult
        false           || false
        true            || true
        null            || false
        [false]         || false
        [true]          || false
        [null]          || false
    }

    void 'should throw exception when input entry value is null'() {
        given:
        final SpecifiedTypeValue<Integer> specifiedInputEntryValue = null
        final SpecifiedTypeValue<Integer> specifiedInputValue = new IntegerValue(4)

        when:
        booleanEvaluationModeProvider.isPositive(ValueType.INTEGER, specifiedInputEntryValue, specifiedInputValue)

        then:
        final NullPointerException exception = thrown()
        exception != null
        exception.getMessage() == 'Input entry value can not be null'
    }
}
