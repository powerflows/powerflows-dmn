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

package org.powerflows.dmn.engine.evaluator.entry

import org.powerflows.dmn.engine.evaluator.context.ModifiableContextVariables
import org.powerflows.dmn.engine.model.decision.expression.Expression
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.Input
import org.powerflows.dmn.engine.model.decision.field.Output
import org.powerflows.dmn.engine.model.decision.field.ValueType
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry
import org.powerflows.dmn.engine.model.evaluation.context.DecisionContextVariables
import org.powerflows.dmn.engine.model.evaluation.result.EntryResult
import spock.lang.Specification

class EntryEvaluatorSpec extends Specification {

    private final InputEntryEvaluator inputEntryEvaluator = Mock()
    private final OutputEntryEvaluator outputEntryEvaluator = Mock()
    private final EntryEvaluator entryEvaluator = new EntryEvaluator(inputEntryEvaluator, outputEntryEvaluator)

    final Integer inputEntry1Value = 1
    final Integer inputEntry2Value = 2
    final String input1Name = 'x'
    final String input2Name = 'y'

    final Expression expression1 = [value: inputEntry1Value, type: ExpressionType.LITERAL]
    final InputEntry inputEntry1 = [name: input1Name, expression: expression1]
    final Expression expression2 = [value: inputEntry2Value, type: ExpressionType.LITERAL]
    final InputEntry inputEntry2 = [name: input2Name, expression: expression2]

    final Boolean outputEntry1Value = true
    final String outputEntry2Value = 'test'
    final String output1Name = 'z'
    final String output2Name = 'q'
    final Expression expression3 = [value: outputEntry1Value, type: ExpressionType.LITERAL]
    final OutputEntry outputEntry1 = [name: output1Name, expression: expression3]
    final Expression expression4 = [value: outputEntry2Value, type: ExpressionType.LITERAL]
    final OutputEntry outputEntry2 = [name: output2Name, expression: expression4]

    final List<InputEntry> inputEntries = [inputEntry1, inputEntry2]
    final List<OutputEntry> outputEntries = [outputEntry1, outputEntry2]

    final Input input1 = [name: input1Name, type: ValueType.INTEGER]
    final Input input2 = [name: input2Name, type: ValueType.INTEGER]

    final Output output1 = [name: output1Name, type: ValueType.BOOLEAN]
    final Output output2 = [name: output2Name, type: ValueType.STRING]

    void 'should evaluate nonempty list of entry result'() {
        given:
        final Map<String, Object> variables = [:]
        variables.put(input1Name, inputEntry1Value)
        variables.put(input2Name, inputEntry2Value)
        final DecisionContextVariables decisionContextVariables = new DecisionContextVariables(variables)
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        final Map<String, Input> inputsMap = [:]
        inputsMap.put(input1Name, input1)
        inputsMap.put(input2Name, input2)

        final Map<String, Output> outputsMap = [:]
        outputsMap.put(output1Name, output1)
        outputsMap.put(output2Name, output2)

        when:
        final List<EntryResult> entryResults = entryEvaluator.evaluate(inputEntries, outputEntries, inputsMap, outputsMap, contextVariables)

        then:
        entryResults.size() == 2
        final EntryResult entryResult1 = entryResults[0]
        with(entryResult1) {
            getName() == output1Name
            getValue() == outputEntry1Value
        }

        final EntryResult entryResult2 = entryResults[1]
        with(entryResult2) {
            getName() == output2Name
            getValue() == outputEntry2Value
        }

        1 * inputEntryEvaluator.evaluate(inputEntry1, input1, contextVariables) >> true
        1 * inputEntryEvaluator.evaluate(inputEntry2, input2, contextVariables) >> true
        1 * outputEntryEvaluator.evaluate(outputEntry1, output1, contextVariables) >> EntryResult.builder().value(outputEntry1Value).name(output1Name).build()
        1 * outputEntryEvaluator.evaluate(outputEntry2, output2, contextVariables) >> EntryResult.builder().value(outputEntry2Value).name(output2Name).build()
        0 * _
    }

    void 'should evaluate empty list of entry result'() {
        given:
        final nonMatchingInputEntry2Value = 3
        final Map<String, Object> variables = [:]
        variables.put(input1Name, inputEntry1Value)
        variables.put(input2Name, nonMatchingInputEntry2Value)
        final DecisionContextVariables decisionContextVariables = new DecisionContextVariables(variables)
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        final Map<String, Input> inputsMap = [:]
        inputsMap.put(input1Name, input1)
        inputsMap.put(input2Name, input2)

        final Map<String, Output> outputsMap = [:]

        when:
        final List<EntryResult> entryResults = entryEvaluator.evaluate(inputEntries, outputEntries, inputsMap, outputsMap, contextVariables)

        then:
        entryResults.size() == 0

        1 * inputEntryEvaluator.evaluate(inputEntry1, input1, contextVariables) >> true
        1 * inputEntryEvaluator.evaluate(inputEntry2, input2, contextVariables) >> false
        0 * _
    }
}
