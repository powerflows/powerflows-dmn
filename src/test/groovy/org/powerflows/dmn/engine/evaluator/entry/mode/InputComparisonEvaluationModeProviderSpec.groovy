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

class InputComparisonEvaluationModeProviderSpec extends Specification {

    @Shared
    private EvaluationModeProvider inputComparisonEvaluationModeProvider

    void setup() {
        final EvaluationModeProviderFactory evaluationModeProviderFactory = new EvaluationModeProviderFactory()
        inputComparisonEvaluationModeProvider = evaluationModeProviderFactory.getInstance(EvaluationMode.INPUT_COMPARISON)
    }

    @Unroll
    void 'should compare input entry value #inputEntryValue and input value #inputValue with result #expectedResult'(
            final Object inputEntryValue, final Object inputValue, final boolean expectedResult) {
        given:
        final SpecifiedTypeValue<?> specifiedInputEntryValue

        if (Boolean.TRUE.equals(inputEntryValue) || Boolean.FALSE.equals(inputEntryValue)) {
            specifiedInputEntryValue = new BooleanValue(inputEntryValue)
        } else if (inputEntryValue instanceof List) {
            specifiedInputEntryValue = new IntegerValue(inputEntryValue as List<Integer>)
        } else {
            specifiedInputEntryValue = new IntegerValue(inputEntryValue as Integer)
        }

        final SpecifiedTypeValue<Integer> specifiedInputValue
        if (inputValue instanceof List) {
            specifiedInputValue = new IntegerValue(inputValue as List<Integer>)
        } else {
            specifiedInputValue = new IntegerValue(inputValue as Integer)
        }

        when:
        final boolean result = inputComparisonEvaluationModeProvider.isPositive(ValueType.INTEGER, specifiedInputEntryValue, specifiedInputValue)

        then:
        result == expectedResult

        where:
        inputEntryValue | inputValue     || expectedResult
        4               | 4              || true
        4               | 3              || false
        null            | null           || true
        null            | 4              || false
        4               | null           || false
        [1, 4] as List  | [1, 4] as List || true
        [1, 4] as List  | [1, 3] as List || false
        [] as List      | [1, 3] as List || false
        [1, 4] as List  | [] as List     || false
        [] as List      | [] as List     || true
        [null] as List  | [null] as List || true
        [4] as List     | [null] as List || false
        [null] as List  | [4] as List    || false
        null as List    | [4] as List    || false
        [4] as List     | null as List   || false
        null as List    | null as List   || true
        true            | 1              || true
        false           | 1              || false
    }

    void 'should throw exception when input entry value is null'() {
        given:
        final SpecifiedTypeValue<Integer> specifiedInputEntryValue = null
        final SpecifiedTypeValue<Integer> specifiedInputValue = new IntegerValue(4)

        when:
        inputComparisonEvaluationModeProvider.isPositive(ValueType.INTEGER, specifiedInputEntryValue, specifiedInputValue)

        then:
        final NullPointerException exception = thrown()
        exception != null
        exception.getMessage() == 'Input entry value can not be null'
    }

    void 'should throw exception when input value is null'() {
        given:
        final SpecifiedTypeValue<Integer> specifiedInputEntryValue = new IntegerValue(4)
        final SpecifiedTypeValue<Integer> specifiedInputValue = null

        when:
        inputComparisonEvaluationModeProvider.isPositive(ValueType.INTEGER, specifiedInputEntryValue, specifiedInputValue)

        then:
        final NullPointerException exception = thrown()
        exception != null
        exception.getMessage() == 'Input value can not be null'
    }
}
