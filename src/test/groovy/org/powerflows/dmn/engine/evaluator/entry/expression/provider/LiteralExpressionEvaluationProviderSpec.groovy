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

package org.powerflows.dmn.engine.evaluator.entry.expression.provider

import org.powerflows.dmn.engine.evaluator.context.ModifiableContextVariables
import org.powerflows.dmn.engine.model.decision.expression.Expression
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.Input
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry
import org.powerflows.dmn.engine.model.evaluation.context.ContextVariables
import org.powerflows.dmn.engine.model.evaluation.context.DecisionContextVariables
import org.powerflows.dmn.engine.model.evaluation.result.EntryResult
import spock.lang.Specification
import spock.lang.Unroll

class LiteralExpressionEvaluationProviderSpec extends Specification {

    private final ExpressionEvaluationProvider expressionEvaluationProvider = new LiteralExpressionEvaluationProvider()

    @Unroll
    void 'should evaluate input entry literal expression value #inputEntryVariable and variables #contextVariable with #expectedInputEntryResult'(
            final Object inputEntryVariable, final Object contextVariable, final boolean expectedInputEntryResult) {
        given:
        final Expression expression = [value: inputEntryVariable, type: ExpressionType.LITERAL]
        final InputEntry inputEntry = [name: 'TestInputName', expression: expression]

        final ContextVariables decisionContextVariables = new DecisionContextVariables([TestInputName: contextVariable])
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        when:
        final boolean inputEntryResult = expressionEvaluationProvider.evaluateInputEntry(inputEntry, contextVariables)

        then:
        inputEntryResult == expectedInputEntryResult

        where:
        inputEntryVariable | contextVariable  || expectedInputEntryResult
        4                  | 4                || true
        4                  | 3                || false
        null               | null             || true
        null               | 4                || false
        4                  | null             || false
        [1, 4] as Set      | [1, 4] as Set    || true
        [1, 4] as Set      | [1, 3] as Set    || false
        [] as Set          | [1, 3] as Set    || false
        [1, 4] as Set      | [] as Set        || false
        [] as Set          | [] as Set        || true
        [null] as Set      | [null] as Set    || true
        [4] as Set         | [null] as Set    || false
        [null] as Set      | [4] as Set       || false
        null as Set        | [4] as Set       || false
        [4] as Set         | null as Set      || false
        null as Set        | null as Set      || true
        [1, 4] as Set      | [1, 4] as List   || true
        [1, 4] as Set      | [1, 4].toArray() || true
        [1, 4] as List     | [1, 4] as Set    || true
        [1, 4] as List     | [1, 4].toArray() || true
        [1, 4].toArray()   | [1, 4] as List   || true
        [1, 4].toArray()   | [1, 4] as Set    || true
        [1, 4].toArray()   | [1, 4].toArray() || true
        [1, 4] as List     | [1, 4] as List   || true
        1                  | [1] as List      || true
        1                  | [1] as Set       || true
        1                  | [1].toArray()    || true
        [1] as List        | 1                || true
        [1] as Set         | 1                || true
        [1].toArray()      | 1                || true
        1                  | [1, 2] as List   || false
        1                  | [1, 2] as Set    || false
        1                  | [1, 2].toArray() || false
        [1, 2] as List     | 1                || true
        [1, 2] as Set      | 1                || true
        [1, 2].toArray()   | 1                || true
    }

    void 'should evaluate input and return nonnull for matching names input and variables'() {
        given:
        final String inputValue = 5
        final String inputName = 'TestInputName'
        final Input input = [name: inputName]

        final Map<String, Object> contextVariablesMap = [:]
        contextVariablesMap.put(inputName, inputValue)
        contextVariablesMap.put('x', 'y')
        final ContextVariables decisionContextVariables = new DecisionContextVariables(contextVariablesMap)
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        when:
        final Object result = expressionEvaluationProvider.evaluateInput(input, contextVariables)

        then:
        result == inputValue
    }

    void 'should evaluate input and return null for non matching names input and variables'() {
        given:
        final Input input = [name: 'nonMatchingName']

        final Map<String, Object> contextVariablesMap = [:]
        contextVariablesMap.put('q', 5)
        contextVariablesMap.put('x', 'y')
        final ContextVariables decisionContextVariables = new DecisionContextVariables(contextVariablesMap)
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        when:
        final Object result = expressionEvaluationProvider.evaluateInput(input, contextVariables)

        then:
        result == null
    }

    void 'should evaluate output entry literal expression value'() {
        given:
        final String outputEntryValue = 5
        final String outputEntryName = 'TestOutputName'
        final Expression expression = [value: outputEntryValue, type: ExpressionType.LITERAL]
        final OutputEntry outputEntry = [name: outputEntryName, expression: expression]
        final ContextVariables decisionContextVariables = new DecisionContextVariables([:])
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        when:
        final EntryResult outputEntryResult = expressionEvaluationProvider.evaluateOutputEntry(outputEntry, contextVariables)

        then:
        outputEntryResult != null
        outputEntryResult.getValue() == outputEntryValue
        outputEntryResult.getName() == outputEntryName
    }
}
