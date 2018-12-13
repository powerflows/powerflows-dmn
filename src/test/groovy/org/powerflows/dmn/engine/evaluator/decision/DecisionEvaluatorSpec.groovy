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

package org.powerflows.dmn.engine.evaluator.decision

import org.powerflows.dmn.engine.evaluator.exception.EvaluationException
import org.powerflows.dmn.engine.evaluator.rule.RuleEvaluator
import org.powerflows.dmn.engine.model.decision.Decision
import org.powerflows.dmn.engine.model.decision.HitPolicy
import org.powerflows.dmn.engine.model.decision.expression.Expression
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.Input
import org.powerflows.dmn.engine.model.decision.rule.Rule
import org.powerflows.dmn.engine.model.evaluation.variable.DecisionVariables
import org.powerflows.dmn.engine.model.evaluation.result.DecisionResult
import org.powerflows.dmn.engine.model.evaluation.result.RuleResult
import spock.lang.Specification

class DecisionEvaluatorSpec extends Specification {

    private final RuleEvaluator ruleEvaluator = Mock()
    private final DecisionEvaluator decisionEvaluator = new DecisionEvaluator(ruleEvaluator)

    void 'should throw exception when decision is null'() {
        given:
        final Decision decision = null
        final DecisionVariables decisionContextVariables = new DecisionVariables([:])

        when:
        decisionEvaluator.evaluate(decision, decisionContextVariables)

        then:
        final NullPointerException exception = thrown()
        exception != null
        exception.getMessage() == 'Decision can not be null'
    }

    void 'should throw exception when context Variables are null'() {
        given:
        final Decision decision = [] as Decision
        final DecisionVariables decisionContextVariables = null

        when:
        decisionEvaluator.evaluate(decision, decisionContextVariables)

        then:
        final NullPointerException exception = thrown()
        exception != null
        exception.getMessage() == 'Decision variables can not be null'
    }

    void 'should throw exception when unique result is expected but non-unique evaluated'() {
        given:
        final List<Input> inputs = [];
        final Rule rule1 = [] as Rule
        final Rule rule2 = [] as Rule
        final List<Rule> rules = [rule1, rule2]
        final DecisionVariables decisionContextVariables = new DecisionVariables([:])

        final Decision decision = [hitPolicy: HitPolicy.UNIQUE,
                                   inputs   : inputs,
                                   rules    : rules] as Decision

        when:
        decisionEvaluator.evaluate(decision, decisionContextVariables)

        then:
        final EvaluationException exception = thrown()
        exception != null
        exception.getMessage() == 'Unique result is expected'

        1 * ruleEvaluator.evaluate(rule1, [:], [:], _) >> RuleResult.builder().entryResults([]).build()
        1 * ruleEvaluator.evaluate(rule2, [:], [:], _) >> RuleResult.builder().entryResults([]).build()
        0 * _
    }

    void 'should throw exception when invalid input names occurred'() {
        given:
        final Expression expression = [value: 'x + 7', type: ExpressionType.GROOVY]
        final String inputName = 'Test Input'
        final Input input = [expression: expression, name: inputName]
        final List<Input> inputs = [input];
        final Rule rule = [] as Rule
        final List<Rule> rules = [rule]

        final Map<String, Object> variables = [:]
        variables.put(inputName, 100)
        final DecisionVariables decisionContextVariables = new DecisionVariables(variables)

        final Decision decision = [hitPolicy: HitPolicy.UNIQUE,
                                   inputs   : inputs,
                                   rules    : rules] as Decision

        when:
        decisionEvaluator.evaluate(decision, decisionContextVariables)

        then:
        final EvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Can not apply decision variables to inputs '$inputName'. Only to inputs with literal expression possible."
    }

    void 'should return decision result when unique result is expected and unique evaluated'() {
        given:
        final List<Input> inputs = [];
        final Rule rule = [] as Rule
        final List<Rule> rules = [rule]
        final DecisionVariables decisionContextVariables = new DecisionVariables([:])

        final Decision decision = [hitPolicy: HitPolicy.UNIQUE,
                                   inputs   : inputs,
                                   rules    : rules] as Decision

        final expectedRuleResult = RuleResult.builder().entryResults([]).build()

        when:
        final DecisionResult decisionResult = decisionEvaluator.evaluate(decision, decisionContextVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            !isCollectionRulesResult()
            isSingleRuleResult()
            !isSingleEntryResult()
        }

        final RuleResult ruleResult = decisionResult.getSingleRuleResult()
        ruleResult == expectedRuleResult

        1 * ruleEvaluator.evaluate(rule, [:], [:], _) >> expectedRuleResult
        0 * _
    }

    void 'should return decision result when non-unique result is expected and non-unique evaluated'() {
        given:
        final List<Input> inputs = [];
        final Rule rule1 = [] as Rule
        final Rule rule2 = [] as Rule
        final List<Rule> rules = [rule1, rule2]
        final DecisionVariables decisionContextVariables = new DecisionVariables([:])

        final Decision decision = [hitPolicy: HitPolicy.COLLECT,
                                   inputs   : inputs,
                                   rules    : rules] as Decision

        final expectedRuleResult1 = RuleResult.builder().entryResults([]).build()
        final expectedRuleResult2 = RuleResult.builder().entryResults([]).build()

        when:
        final DecisionResult decisionResult = decisionEvaluator.evaluate(decision, decisionContextVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            isCollectionRulesResult()
            !isSingleRuleResult()
            !isSingleEntryResult()
        }

        final List<RuleResult> ruleResults = decisionResult.getCollectionRulesResult()
        ruleResults.size() == 2
        ruleResults[0] == expectedRuleResult1
        ruleResults[1] == expectedRuleResult2

        1 * ruleEvaluator.evaluate(rule1, [:], [:], _) >> expectedRuleResult1
        1 * ruleEvaluator.evaluate(rule2, [:], [:], _) >> expectedRuleResult2
        0 * _
    }

    void 'should return decision result when single non-unique result is expected and single non-unique evaluated'() {
        given:
        final List<Input> inputs = [];
        final Rule rule1 = [] as Rule
        final Rule rule2 = [] as Rule
        final List<Rule> rules = [rule1, rule2]
        final DecisionVariables decisionContextVariables = new DecisionVariables([:])

        final Decision decision = [hitPolicy: HitPolicy.FIRST,
                                   inputs   : inputs,
                                   rules    : rules] as Decision

        final expectedRuleResult = RuleResult.builder().entryResults([]).build()

        when:
        final DecisionResult decisionResult = decisionEvaluator.evaluate(decision, decisionContextVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            !isCollectionRulesResult()
            isSingleRuleResult()
            !isSingleEntryResult()
        }

        final RuleResult ruleResult = decisionResult.getSingleRuleResult()
        ruleResult == expectedRuleResult

        1 * ruleEvaluator.evaluate(rule1, [:], [:], _) >> expectedRuleResult
        0 * _
    }
}
