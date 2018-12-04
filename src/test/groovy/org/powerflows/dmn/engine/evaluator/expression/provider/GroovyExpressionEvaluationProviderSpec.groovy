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

import org.powerflows.dmn.engine.evaluator.context.ModifiableContextVariables
import org.powerflows.dmn.engine.evaluator.exception.EvaluationException
import org.powerflows.dmn.engine.evaluator.expression.comparator.DefaultObjectsComparator
import org.powerflows.dmn.engine.evaluator.expression.script.DefaultScriptEngineProvider
import org.powerflows.dmn.engine.evaluator.expression.script.ScriptEngineProvider
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

import javax.script.ScriptEngineManager

class GroovyExpressionEvaluationProviderSpec extends Specification {

    private final ScriptEngineProvider scriptEngineProvider = new DefaultScriptEngineProvider(new ScriptEngineManager())
    private final DefaultObjectsComparator defaultObjectsComparator = Mock()
    private final ExpressionEvaluationProvider expressionEvaluationProvider =
            new ScriptExpressionEvaluationProvider(scriptEngineProvider, defaultObjectsComparator)

    @Unroll
    void 'should evaluate input entry groovy expression value #inputEntryExpression and variables #contextVariable with #expectedInputEntryResult'(
            final Object inputEntryExpression, final Object contextVariable, final boolean expectedInputEntryResult) {
        given:
        final Expression expression = [value: inputEntryExpression, type: ExpressionType.GROOVY]
        final InputEntry inputEntry = [name: 'TestInputName', expression: expression]

        final ContextVariables decisionContextVariables = new DecisionContextVariables([x: contextVariable, TestInputName: true])
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        when:
        final boolean inputEntryResult = expressionEvaluationProvider.evaluateInputEntry(inputEntry, contextVariables)

        then:
        inputEntryResult == expectedInputEntryResult
        1 * defaultObjectsComparator.isInputEntryValueEqualInputValue(expectedInputEntryResult, true) >> expectedInputEntryResult
        0 * _

        where:
        inputEntryExpression | contextVariable || expectedInputEntryResult
        '2 < x'              | 4               || true
        '2 == x'             | 2               || true
        '2 == x'             | 3               || false
        '(2 + 5) == x'       | 7               || true
    }

    @Unroll
    void 'should evaluate input groovy expression value #inputExpression and variables #contextVariable with #expectedInputResult'(
            final Object inputExpression, final Object contextVariable, final boolean expectedInputResult) {
        given:
        final Expression expression = [value: inputExpression, type: ExpressionType.GROOVY]
        final Input input = [name: 'TestInputName', expression: expression]

        final ContextVariables decisionContextVariables = new DecisionContextVariables([x: contextVariable])
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        when:
        final boolean inputEntryResult = expressionEvaluationProvider.evaluateInput(input, contextVariables)

        then:
        inputEntryResult == expectedInputResult

        where:
        inputExpression | contextVariable || expectedInputResult
        '2 < x'         | 4               || true
        '2 + x'         | 3               || 5
        '"a" + 4'       | null            || 'a4'
    }

    void 'should evaluate output entry groovy expression value'() {
        given:
        final String outputEntryValue = 'x + 7'
        final String outputEntryName = 'TestOutputName'
        final Expression expression = [value: outputEntryValue, type: ExpressionType.GROOVY]
        final OutputEntry outputEntry = [name: outputEntryName, expression: expression]
        final ContextVariables decisionContextVariables = new DecisionContextVariables([x: 2])
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        when:
        final EntryResult outputEntryResult = expressionEvaluationProvider.evaluateOutputEntry(outputEntry, contextVariables)

        then:
        outputEntryResult != null
        outputEntryResult.getValue() == 9
        outputEntryResult.getName() == outputEntryName
    }

    void 'should throw exception when missing variable evaluating groovy expression'() {
        given:
        final String outputEntryValue = 'x'
        final String outputEntryName = 'TestOutputName'
        final Expression expression = [value: outputEntryValue, type: ExpressionType.GROOVY]
        final OutputEntry outputEntry = [name: outputEntryName, expression: expression]
        final ContextVariables decisionContextVariables = new DecisionContextVariables([:])
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        when:
        expressionEvaluationProvider.evaluateOutputEntry(outputEntry, contextVariables)

        then:
        final EvaluationException exception = thrown()
        exception != null
        exception.getMessage() == 'Script evaluation exception'
    }
}
