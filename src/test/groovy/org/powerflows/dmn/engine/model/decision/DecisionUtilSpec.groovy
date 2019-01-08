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

package org.powerflows.dmn.engine.model.decision

import org.powerflows.dmn.engine.model.decision.expression.Expression
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.Input
import org.powerflows.dmn.engine.model.decision.field.ValueType
import org.powerflows.dmn.engine.model.decision.rule.Rule
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry
import spock.lang.Specification

import java.lang.reflect.Field

class DecisionUtilSpec extends Specification {

    void 'should assign defaults'() {
        given:
        final String input1Name = 'x'
        final String input2Name = 'y'
        final Expression input1Expression = [type: ExpressionType.FEEL]
        final Expression input2Expression = [type: null]
        final Input input1 = [name: input1Name, type: ValueType.INTEGER, expression: input1Expression, evaluationMode: EvaluationMode.BOOLEAN]
        final Input input2 = [name: input2Name, type: ValueType.INTEGER, expression: input2Expression]

        final Integer inputEntry1Value = 1
        final Integer inputEntry2Value = 2
        final Expression expression1 = [value: inputEntry1Value, type: null]
        final InputEntry inputEntry1 = [name: input1Name, expression: expression1, evaluationMode: EvaluationMode.BOOLEAN]
        final Expression expression2 = [value: inputEntry2Value, type: ExpressionType.LITERAL]
        final InputEntry inputEntry2 = [name: input2Name, expression: expression2]

        final Boolean outputEntry1Value = true
        final String outputEntry2Value = 'test'
        final String output1Name = 'z'
        final String output2Name = 'q'
        final Expression expression3 = [value: outputEntry1Value, type: ExpressionType.LITERAL]
        final OutputEntry outputEntry1 = [name: output1Name, expression: expression3]
        final Expression expression4 = [value: outputEntry2Value, type: null]
        final OutputEntry outputEntry2 = [name: output2Name, expression: expression4]

        final List<InputEntry> inputEntries = [inputEntry1, inputEntry2]
        final List<OutputEntry> outputEntries = [outputEntry1, outputEntry2]

        final Rule rule = [inputEntries: inputEntries, outputEntries: outputEntries]
        final List<Input> inputs = [input1, input2]
        final List<Rule> rules = [rule]
        final ExpressionType decisionExpressionType = ExpressionType.GROOVY
        final EvaluationMode decisionEvaluationMode = EvaluationMode.INPUT_COMPARISON

        when:
        DecisionUtil.assignDefaults(inputs, rules, decisionExpressionType, decisionEvaluationMode)

        then:
        input1.expression.type == ExpressionType.FEEL
        input1.evaluationMode == EvaluationMode.BOOLEAN
        input2.expression.type == ExpressionType.GROOVY
        input2.evaluationMode == EvaluationMode.INPUT_COMPARISON

        inputEntry1.expression.type == ExpressionType.FEEL
        inputEntry1.evaluationMode == EvaluationMode.BOOLEAN
        inputEntry2.expression.type == ExpressionType.LITERAL
        inputEntry2.evaluationMode == EvaluationMode.INPUT_COMPARISON

        outputEntry1.expression.type == ExpressionType.LITERAL
        outputEntry2.expression.type == ExpressionType.GROOVY
    }

    void 'should throw exception when can not set field value'() {
        given:
        final Field field = DecisionUtil.findField(InputEntry.class, EvaluationMode.class);
        final InputEntry inputEntry = []
        final Integer value = 5

        when:
        DecisionUtil.setValue(field, inputEntry, value)

        then:
        final DecisionBuildException exception = thrown()
        exception != null
        exception.getMessage() == 'Can not set value 5 for InputEntry(super=Entry(name=null, expression=null), evaluationMode=null)'
    }

    void 'should throw exception when can not find field by type'() {
        given:
        final Class clazz = InputEntry.class
        final Class fieldClass = HitPolicy.class

        when:
        DecisionUtil.findField(clazz, fieldClass);

        then:
        final DecisionBuildException exception = thrown()
        exception != null
        exception.getMessage() == 'Can not find class org.powerflows.dmn.engine.model.decision.HitPolicy in class org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry'
    }
}
