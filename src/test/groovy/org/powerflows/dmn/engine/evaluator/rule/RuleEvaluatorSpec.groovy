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

package org.powerflows.dmn.engine.evaluator.rule

import org.powerflows.dmn.engine.evaluator.context.EvaluationContext
import org.powerflows.dmn.engine.evaluator.entry.EntryEvaluator
import org.powerflows.dmn.engine.model.decision.field.Input
import org.powerflows.dmn.engine.model.decision.field.Output
import org.powerflows.dmn.engine.model.decision.rule.Rule
import org.powerflows.dmn.engine.model.evaluation.variable.DecisionVariables
import org.powerflows.dmn.engine.model.evaluation.result.EntryResult
import org.powerflows.dmn.engine.model.evaluation.result.RuleResult
import spock.lang.Specification

class RuleEvaluatorSpec extends Specification {

    private final EntryEvaluator entryEvaluator = Mock()
    private final RuleEvaluator ruleEvaluator = new RuleEvaluator(entryEvaluator)

    final Rule rule = [inputEntries: [], outputEntries: []]
    final Map<String, Input> inputs = [:]
    final Map<String, Output> outputs = [:]
    final DecisionVariables decisionVariables = new DecisionVariables([:])
    final EvaluationContext evaluationContext = new EvaluationContext(decisionVariables)

    void 'should evaluate null rule result'() {
        given:

        when:
        final RuleResult ruleResult = ruleEvaluator.evaluate(rule, inputs, outputs, evaluationContext)

        then:
        ruleResult == null

        1 * entryEvaluator.evaluate(rule.getInputEntries(), rule.getOutputEntries(), inputs, outputs, evaluationContext) >> []
        0 * _
    }

    void 'should evaluate nonnull rule result'() {
        given:

        final String someEntryResultName = 'x'
        final String someEntryResultValue = 'test'
        final EntryResult someEntryResult = [name: someEntryResultName, value: someEntryResultValue]

        final List<EntryResult> someEntryResults = [someEntryResult]

        when:
        final RuleResult ruleResult = ruleEvaluator.evaluate(rule, inputs, outputs, evaluationContext)

        then:
        ruleResult != null

        with(ruleResult) {
            getEntryResults().size() == 1
            getEntryResults()[0].getValue() == someEntryResultValue
            getEntryResults()[0].getName() == someEntryResultName
        }

        1 * entryEvaluator.evaluate(rule.getInputEntries(), rule.getOutputEntries(), inputs, outputs, evaluationContext) >> someEntryResults
        0 * _
    }
}
