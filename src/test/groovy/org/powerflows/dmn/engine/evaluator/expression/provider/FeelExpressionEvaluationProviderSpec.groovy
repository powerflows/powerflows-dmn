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
import org.powerflows.dmn.engine.evaluator.expression.ExpressionEvaluationException
import org.powerflows.dmn.engine.model.decision.expression.Expression
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.Input
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry
import org.powerflows.dmn.engine.model.evaluation.variable.DecisionVariables
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.text.SimpleDateFormat

class FeelExpressionEvaluationProviderSpec extends Specification {

    private ExpressionEvaluationProviderFactory evaluationProviderFactory = new FeelExpressionEvaluationProviderFactory()
    private
    final ExpressionEvaluationProvider expressionEvaluationProvider = evaluationProviderFactory.createProvider(ExpressionEvaluationConfiguration.simpleConfiguration())

    @Shared
    public SimpleDateFormat format

    void setupSpec() {
        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone('UTC'))
    }

    @Unroll
    void 'should evaluate input entry feel expression value #entryExpressionValue and variables #contextVariable with #expectedEntryResult'(
            final Object entryExpressionValue, final Object contextVariable, final Serializable expectedEntryResult) {
        given:
        final Expression entryExpression = [value: entryExpressionValue, type: ExpressionType.FEEL]
        final InputEntry inputEntry = [expression: entryExpression, name: 'TestInputName', nameAlias: 'cellInput']

        final DecisionVariables decisionVariables = new DecisionVariables([TestInputName: contextVariable])
        final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

        when:
        final Serializable inputEntryResult = expressionEvaluationProvider.evaluateInputEntry(inputEntry, evaluationContext)

        then:
        inputEntryResult == expectedEntryResult
        0 * _

        where:
        entryExpressionValue                                                           | contextVariable                     || expectedEntryResult
        'not(4)'                                                                       | 4                                   || false
        'not(4)'                                                                       | 2                                   || true
        'not("A")'                                                                     | 'A'                                 || false
        'not("A")'                                                                     | 'B'                                 || true
        'not(1,2,3)'                                                                   | 4                                   || true
        'not(1,2,3)'                                                                   | 3                                   || false
        'not("a","b","c")'                                                             | 'd'                                 || true
        'not("a","b","c")'                                                             | 'c'                                 || false
        'not([1..3])'                                                                  | 4                                   || true
        'not([1..3])'                                                                  | 3                                   || false
        'not(>=3, <=1)'                                                                | 2                                   || true
        'not(>=3, <=1)'                                                                | 1                                   || false
        '4'                                                                            | 4                                   || true
        '4'                                                                            | 2                                   || false
        '"abc"'                                                                        | 'abc'                               || true
        '"abc"'                                                                        | 'abb'                               || false
        '<4'                                                                           | 4                                   || false
        '<4'                                                                           | 3                                   || true
        '<=4'                                                                          | 4                                   || true
        '>4'                                                                           | 4                                   || false
        '>4'                                                                           | 5                                   || true
        '>=4'                                                                          | 4                                   || true
        '> 4'                                                                          | 5                                   || true
        '>= 4'                                                                         | 4                                   || true
        '1,2,3'                                                                        | 4                                   || false
        '1,2,3'                                                                        | 3                                   || true
        '"a","b","c"'                                                                  | 'd'                                 || false
        '"a","b","c"'                                                                  | 'c'                                 || true
        '>10,5,<=3'                                                                    | 4                                   || false
        '>10,5,<=3'                                                                    | 11                                  || true
        '>10,5,<=3'                                                                    | 5                                   || true
        '>10,5 , <=3'                                                                  | 3                                   || true
        '[4..9]'                                                                       | 4                                   || true
        '[4..9]'                                                                       | 9                                   || true
        '[4..9]'                                                                       | 5                                   || true
        '[4..9]'                                                                       | 3                                   || false
        '[4..9]'                                                                       | 10                                  || false
        '(4..9]'                                                                       | 4                                   || false
        ']4..9]'                                                                       | 4                                   || false
        '(4..9]'                                                                       | 5                                   || true
        ']4..9]'                                                                       | 5                                   || true
        '[4..9)'                                                                       | 9                                   || false
        '[4..9['                                                                       | 9                                   || false
        '[4..9)'                                                                       | 8                                   || true
        '[4..9['                                                                       | 8                                   || true
        '[4..9], [14..19]'                                                             | 4                                   || true
        '[4..9], [14..19]'                                                             | 14                                  || true
        '[4..9], [14..19]'                                                             | 11                                  || false
        '(4..9], [14..19]'                                                             | 4                                   || false
        '[4..9], (14..19]'                                                             | 14                                  || false
        'date and time("2015-11-30T12:00:00")'                                         | format.parse('2015-11-30T12:00:00') || true
        '[date and time("2015-11-28T12:00:00")..date and time("2015-11-30T12:00:00")]' | format.parse('2015-11-29T12:00:00') || true
        '[date and time("2015-11-28T12:00:00")..date and time("2015-11-30T12:00:00")]' | format.parse('2015-11-30T12:00:00') || true
        '[date and time("2015-11-28T12:00:00")..date and time("2015-11-30T12:00:00"))' | format.parse('2015-11-30T12:00:00') || false
    }

    @Unroll
    void 'should evaluate input feel expression value #inputExpression with #expectedInputResult'(
            final Object inputExpression, final Serializable expectedInputResult) {
        given:
        final Expression expression = [value: inputExpression, type: ExpressionType.FEEL]
        final Input input = [name: 'TestInputName', expression: expression]

        final DecisionVariables decisionVariables = new DecisionVariables([:])
        final EvaluationContext contextVariables = new EvaluationContext(decisionVariables)

        when:
        final Serializable inputEntryResult = expressionEvaluationProvider.evaluateInput(input, contextVariables)

        then:
        inputEntryResult == expectedInputResult

        where:
        inputExpression                        || expectedInputResult
        '"aaa"'                                || 'aaa'
        'date and time("2015-11-30T12:00:00")' || format.parse('2015-11-30T12:00:00')
    }

    @Unroll
    void 'should evaluate output entry feel expression value #entryExpressionValue with #expectedEntryResult'(
            final Object entryExpressionValue, final Serializable expectedEntryResult) {
        given:
        final Expression entryExpression = [value: entryExpressionValue, type: ExpressionType.FEEL]
        final OutputEntry outputEntry = [expression: entryExpression, name: 'TestOutputName']

        final DecisionVariables decisionVariables = new DecisionVariables([:])
        final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

        when:
        final Serializable inputEntryResult = expressionEvaluationProvider.evaluateOutputEntry(outputEntry, evaluationContext)

        then:
        inputEntryResult == expectedEntryResult
        0 * _

        where:
        entryExpressionValue || expectedEntryResult
        '"some string"'      || 'some string'
        '"not(4)"'           || 'not(4)'
        4                    || 4
        2.3d                 || 2.3d
        true                 || true
        false                || false
    }

    void 'should throw exception for empty value of collection'() {
        given:
        final Expression entryExpression = [value: '1,,', type: ExpressionType.FEEL]
        final InputEntry inputEntry = [expression: entryExpression, name: 'TestInputName', nameAlias: 'cellInput']

        final DecisionVariables decisionVariables = new DecisionVariables([TestInputName: 1])
        final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

        when:
        final Serializable inputEntryResult = expressionEvaluationProvider.evaluateInputEntry(inputEntry, evaluationContext)

        then:
        final ExpressionEvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Can not evaluate feel expression '1,,'"
    }

    void 'should throw exception for invalid syntax of comparison expression'() {
        given:
        final Expression entryExpression = [value: '>==', type: ExpressionType.FEEL]
        final InputEntry inputEntry = [expression: entryExpression, name: 'TestInputName', nameAlias: 'cellInput']

        final DecisionVariables decisionVariables = new DecisionVariables([TestInputName: 1])
        final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

        when:
        final Serializable inputEntryResult = expressionEvaluationProvider.evaluateInputEntry(inputEntry, evaluationContext)

        then:
        final ExpressionEvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Can not evaluate feel expression '>=='"
    }

    void 'should throw exception for evaluate output entry for "not" expression'() {
        given:
        final Expression entryExpression = [value: 'not(4)', type: ExpressionType.FEEL]
        final OutputEntry outputEntry = [expression: entryExpression, name: 'TestOutputName']

        final DecisionVariables decisionVariables = new DecisionVariables([:])
        final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

        when:
        expressionEvaluationProvider.evaluateOutputEntry(outputEntry, evaluationContext)

        then:
        final ExpressionEvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Can not evaluate feel expression 'not(4)', due to applicable only for input entry expressions"
    }

    void 'should throw exception for evaluate output entry for "comparison" expression'() {
        given:
        final Expression entryExpression = [value: '>=4', type: ExpressionType.FEEL]
        final OutputEntry outputEntry = [expression: entryExpression, name: 'TestOutputName']

        final DecisionVariables decisionVariables = new DecisionVariables([:])
        final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

        when:
        expressionEvaluationProvider.evaluateOutputEntry(outputEntry, evaluationContext)

        then:
        final ExpressionEvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Can not evaluate feel expression '>=4', due to applicable only for input entry expressions"
    }

    void 'should throw exception for evaluate output entry for "collection" expression'() {
        given:
        final Expression entryExpression = [value: '1,2', type: ExpressionType.FEEL]
        final OutputEntry outputEntry = [expression: entryExpression, name: 'TestOutputName']

        final DecisionVariables decisionVariables = new DecisionVariables([:])
        final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

        when:
        expressionEvaluationProvider.evaluateOutputEntry(outputEntry, evaluationContext)

        then:
        final ExpressionEvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Can not evaluate feel expression '1,2', due to applicable only for input entry expressions"
    }

    void 'should throw exception for evaluate output entry for "range" expression'() {
        given:
        final Expression entryExpression = [value: '[1..2]', type: ExpressionType.FEEL]
        final OutputEntry outputEntry = [expression: entryExpression, name: 'TestOutputName']

        final DecisionVariables decisionVariables = new DecisionVariables([:])
        final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

        when:
        expressionEvaluationProvider.evaluateOutputEntry(outputEntry, evaluationContext)

        then:
        final ExpressionEvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Can not evaluate feel expression '[1..2]', due to applicable only for input entry expressions"
    }
}
