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
import org.powerflows.dmn.engine.evaluator.expression.script.DefaultScriptEngineProvider
import org.powerflows.dmn.engine.evaluator.expression.script.ScriptEngineProvider
import org.powerflows.dmn.engine.model.decision.expression.Expression
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.Input
import org.powerflows.dmn.engine.model.evaluation.context.ContextVariables
import org.powerflows.dmn.engine.model.evaluation.context.DecisionContextVariables
import spock.lang.Specification
import spock.lang.Unroll

import javax.script.ScriptEngineManager

class JavaScriptExpressionEvaluationProviderSpec extends Specification {

    private final ScriptEngineProvider scriptEngineProvider = new DefaultScriptEngineProvider(new ScriptEngineManager())
    private final ExpressionEvaluationProvider expressionEvaluationProvider =
            new ScriptExpressionEvaluationProvider(scriptEngineProvider)

    @Unroll
    void 'should evaluate entry javascript expression value #entryExpressionValue and variables #contextVariable with #expectedEntryResult'(
            final Object entryExpressionValue, final Object contextVariable, final boolean expectedEntryResult) {
        given:
        final Expression entryExpression = [value: entryExpressionValue, type: ExpressionType.JAVASCRIPT]

        final ContextVariables decisionContextVariables = new DecisionContextVariables([x: contextVariable, TestInputName: true])
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        when:
        final boolean inputEntryResult = expressionEvaluationProvider.evaluateEntry(entryExpression, contextVariables)

        then:
        inputEntryResult == expectedEntryResult
        0 * _

        where:
        entryExpressionValue | contextVariable || expectedEntryResult
        '2 < x'              | 4               || true
        '2 == x'             | 2               || true
        '2 == x'             | 3               || false
        '(2 + 5) == x'       | 7               || true
    }

    @Unroll
    void 'should evaluate input javascript expression value #inputExpression and variables #contextVariable with #expectedInputResult'(
            final Object inputExpression, final Object contextVariable, final boolean expectedInputResult) {
        given:
        final Expression expression = [value: inputExpression, type: ExpressionType.JAVASCRIPT]
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

    void 'should throw exception when missing variable evaluating javascript expression'() {
        given:
        final String outputEntryValue = 'x'
        final Expression entryExpression = [value: outputEntryValue, type: ExpressionType.JAVASCRIPT]
        final ContextVariables decisionContextVariables = new DecisionContextVariables([:])
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        when:
        expressionEvaluationProvider.evaluateEntry(entryExpression, contextVariables)

        then:
        final EvaluationException exception = thrown()
        exception != null
        exception.getMessage() == 'Script evaluation exception'
    }
}
