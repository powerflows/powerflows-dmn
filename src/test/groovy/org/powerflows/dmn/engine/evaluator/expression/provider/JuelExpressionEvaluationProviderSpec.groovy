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
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.ExpressionEvaluationException
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.InstanceMethodBinding
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.MethodBinding
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.StaticMethodBinding
import org.powerflows.dmn.engine.evaluator.expression.provider.sample.MethodSource
import org.powerflows.dmn.engine.model.decision.expression.Expression
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.Input
import org.powerflows.dmn.engine.model.decision.field.ValueType
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry
import org.powerflows.dmn.engine.model.evaluation.variable.DecisionVariables
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.reflect.Method

class JuelExpressionEvaluationProviderSpec extends Specification {

    private ExpressionEvaluationProvider expressionEvaluationProvider

    void setup() {
        expressionEvaluationProvider = new JuelExpressionEvaluationProvider(ExpressionEvaluationConfiguration.simpleConfiguration())
    }

    @Unroll
    void 'should evaluate input entry juel expression value #entryExpressionValue and variables #contextVariable with #expectedEntryResult'(
            final Object entryExpressionValue,
            final Serializable contextVariable, final Serializable expectedEntryResult) {
        given:
        final Expression entryExpression = [value: entryExpressionValue, type: ExpressionType.JUEL]
        final InputEntry inputEntry = [expression: entryExpression, nameAlias: 'cellInput']

        final DecisionVariables decisionVariables = new DecisionVariables([x: contextVariable, TestInputName: true])
        final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

        when:
        final Serializable inputEntryResult = expressionEvaluationProvider.evaluateInputEntry(inputEntry, evaluationContext)

        then:
        inputEntryResult == expectedEntryResult
        0 * _

        where:
        entryExpressionValue | contextVariable || expectedEntryResult
        '2 < x'              | 4               || true
        '2 == x'             | 2               || true
        '2 == x'             | 3               || false
        '(2 + 5) == x'       | 7               || true
        '"abc"'              | 7               || 'abc'
    }

    void 'should evaluate input entry juel expression with default alias usage'() {
        given:
        final Expression expression = [value: "cellInput == 'something'", type: ExpressionType.JUEL]
        final InputEntry inputEntry = [name: 'TestInputName', expression: expression, nameAlias: 'cellInput']

        final DecisionVariables decisionVariables = new DecisionVariables(['TestInputName': 'something'])
        final EvaluationContext contextVariables = new EvaluationContext(decisionVariables)

        when:
        final Serializable inputEntryResult = expressionEvaluationProvider.evaluateInputEntry(inputEntry, contextVariables)

        then:
        inputEntryResult
    }

    @Unroll
    void 'should evaluate output entry juel expression value #entryExpressionValue and variables #contextVariable with #expectedEntryResult'(
            final Object entryExpressionValue,
            final Serializable contextVariable, final Serializable expectedEntryResult) {
        given:
        final Expression entryExpression = [value: entryExpressionValue, type: ExpressionType.JUEL]
        final OutputEntry outputEntry = [expression: entryExpression]

        final DecisionVariables decisionVariables = new DecisionVariables([x: contextVariable, TestInputName: true])
        final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

        when:
        final Serializable outputEntryResult = expressionEvaluationProvider.evaluateOutputEntry(outputEntry, evaluationContext)

        then:
        outputEntryResult == expectedEntryResult
        0 * _

        where:
        entryExpressionValue | contextVariable || expectedEntryResult
        '2 < x'              | 4               || true
        '2 == x'             | 2               || true
        '2 == x'             | 3               || false
        '(2 + 5) == x'       | 7               || true
        '"abc"'              | 7               || 'abc'
    }

    @Unroll
    void 'should evaluate input juel expression value #inputExpression and variables #contextVariable with #expectedInputResult'(
            final Object inputExpression,
            final ValueType inputType, final Serializable contextVariable, final Serializable expectedInputResult) {
        given:
        final Expression expression = [value: inputExpression, type: ExpressionType.JUEL]
        final Input input = [name: 'TestInputName', expression: expression, type: inputType]

        final DecisionVariables decisionVariables = new DecisionVariables([x: contextVariable])
        final EvaluationContext contextVariables = new EvaluationContext(decisionVariables)

        when:
        final Serializable inputEntryResult = expressionEvaluationProvider.evaluateInput(input, contextVariables)

        then:
        inputEntryResult == expectedInputResult

        where:
        inputExpression | inputType         | contextVariable || expectedInputResult
        '2 < x'         | ValueType.BOOLEAN | 4               || true
        '2 + x'         | ValueType.INTEGER | 3               || 5
        '4 * x'         | ValueType.STRING  | 4               || '16'
        '"abc"'         | ValueType.STRING  | 7               || 'abc'
    }

    void 'should bind static method and make it available in expression'() {
        given:
        final Method method = MethodSource.class.getMethod('sampleStaticMethod', String, Integer.TYPE)
        final List<MethodBinding> methodBinding = [new StaticMethodBinding('testMethod', method)]
        final ExpressionEvaluationConfiguration configuration = ExpressionEvaluationConfiguration
                .builder()
                .methodBindings(methodBinding)
                .build()
        final ExpressionEvaluationProvider expressionEvaluationProvider = new JuelExpressionEvaluationProvider(configuration)
        final Expression expression = [value: 'testMethod(x, 1)', type: ExpressionType.JUEL]
        final Input input = [name: 'TestInputName', expression: expression, type: ValueType.STRING]

        final DecisionVariables decisionVariables = new DecisionVariables([x: 'text'])
        final EvaluationContext contextVariables = new EvaluationContext(decisionVariables)

        when:
        final Serializable inputEntryResult = expressionEvaluationProvider.evaluateInput(input, contextVariables)

        then:
        inputEntryResult == 'static-' + decisionVariables.get('x') + '-1'
    }

    void 'should throw exception for instance method binding '() {
        given:
        final MethodSource theInstance = new MethodSource('someValue')
        final Method method = MethodSource.class.getMethod('sampleInstanceMethod', Integer.TYPE, String)
        final List<MethodBinding> methodBinding = [new InstanceMethodBinding('testMethod', method, {
            theInstance
        })]
        final ExpressionEvaluationConfiguration configuration = ExpressionEvaluationConfiguration
                .builder()
                .methodBindings(methodBinding)
                .build()

        when:
        new JuelExpressionEvaluationProvider(configuration)

        then:
        thrown(ExpressionEvaluationException)
    }
}
