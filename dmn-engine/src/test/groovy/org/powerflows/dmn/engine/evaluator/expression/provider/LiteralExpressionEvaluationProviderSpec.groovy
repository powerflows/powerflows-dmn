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

package org.powerflows.dmn.engine.evaluator.expression.provider

import org.powerflows.dmn.engine.evaluator.context.EvaluationContext
import org.powerflows.dmn.engine.model.decision.expression.Expression
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.Input
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry
import org.powerflows.dmn.engine.model.evaluation.variable.DecisionVariables
import spock.lang.Specification

class LiteralExpressionEvaluationProviderSpec extends Specification {

    private final ExpressionEvaluationProvider expressionEvaluationProvider =
            new LiteralExpressionEvaluationProvider()

    void 'should evaluate input entry literal expression value'() {
        given:
        final Object inputEntryValue = 5
        final Object contextVariable = 6
        final Expression entryExpression = [value: inputEntryValue, type: ExpressionType.LITERAL]
        final InputEntry inputEntry = [expression: entryExpression] as InputEntry

        final DecisionVariables decisionVariables = new DecisionVariables([TestInputName: contextVariable])
        final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

        when:
        final boolean inputEntryResult = expressionEvaluationProvider.evaluateInputEntry(inputEntry, evaluationContext)

        then:
        inputEntryResult
        0 * _
    }

    void 'should evaluate output entry literal expression value'() {
        given:
        final Object inputEntryValue = 5
        final Object contextVariable = 6
        final Expression entryExpression = [value: inputEntryValue, type: ExpressionType.LITERAL]
        final OutputEntry outputEntry = [expression: entryExpression]

        final DecisionVariables decisionVariables = new DecisionVariables([TestInputName: contextVariable])
        final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

        when:
        final boolean inputEntryResult = expressionEvaluationProvider.evaluateOutputEntry(outputEntry, evaluationContext)

        then:
        inputEntryResult
        0 * _
    }

    void 'should evaluate input and return nonnull for matching names input and variables'() {
        given:
        final String inputValue = 5
        final String inputName = 'TestInputName'
        final Input input = [name: inputName]

        final Map<String, Serializable> contextVariablesMap = [:]
        contextVariablesMap.put(inputName, inputValue)
        contextVariablesMap.put('x', 'y')
        final DecisionVariables decisionVariables = new DecisionVariables(contextVariablesMap)
        final EvaluationContext contextVariables = new EvaluationContext(decisionVariables)

        when:
        final Object result = expressionEvaluationProvider.evaluateInput(input, contextVariables)

        then:
        result == inputValue
    }

    void 'should evaluate input and return null for non matching names input and variables'() {
        given:
        final Input input = [name: 'nonMatchingName']

        final Map<String, Serializable> contextVariablesMap = [:]
        contextVariablesMap.put('q', 5)
        contextVariablesMap.put('x', 'y')
        final DecisionVariables decisionVariables = new DecisionVariables(contextVariablesMap)
        final EvaluationContext contextVariables = new EvaluationContext(decisionVariables)

        when:
        final Object result = expressionEvaluationProvider.evaluateInput(input, contextVariables)

        then:
        result == null
    }
}
