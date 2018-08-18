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

package org.powerflows.dmn.domain.model.evaluation

import org.powerflows.dmn.domain.model.evaluation.exception.EvaluationResultException
import spock.lang.Specification

class DecisionResultSpec extends Specification {

    void 'should build decision result and be only collection rules result'() {
        given:
        final String someRule1Entry1Name = 'Some Rule 1 Entry 1 Name'
        final boolean someRule1Entry1Value = true
        final String someRule1Entry2Name = 'Some Rule 1 Entry 2 Name'
        final int someRule1Entry2Value = 7

        final String someRule2Entry1Name = 'Some Rule 2 Entry 1 Name'
        final boolean someRule2Entry1Value = true
        final String someRule2Entry2Name = 'Some Rule 2 Entry 2 Name'
        final int someRule2Entry2Value = 9

        when:
        // @formatter:off
        final DecisionResult decisionResult = DecisionResult.builder()
                .withRuleResults()
                    .withEntryResults()
                        .name(someRule1Entry1Name)
                        .value(someRule1Entry1Value)
                        .next()
                        .name(someRule1Entry2Name)
                        .value(someRule1Entry2Value)
                        .end()
                    .next()
                    .withEntryResults()
                        .name(someRule2Entry1Name)
                        .value(someRule2Entry1Value)
                        .next()
                        .name(someRule2Entry2Name)
                        .value(someRule2Entry2Value)
                        .end()
                    .end()
                .build()
        // @formatter:on

        decisionResult.getSingleRuleResult()

        then:
        decisionResult != null
        decisionResult.isCollectionRulesResult()
        !decisionResult.isSingleRuleResult()
        !decisionResult.isSingleEntryResult()

        decisionResult.getCollectionRulesResult().size() == 2

        final RuleResult ruleResult1 = decisionResult.getCollectionRulesResult().get(0)
        ruleResult1.getEntryResults().size() == 2

        final EntryResult ruleResult1EntryResult1 = ruleResult1.getEntryResults().get(0)
        with(ruleResult1EntryResult1) {
            getName() == someRule1Entry1Name
            getValue() == someRule1Entry1Value
        }

        final EntryResult ruleResult1EntryResult2 = ruleResult1.getEntryResults().get(1)
        with(ruleResult1EntryResult2) {
            getName() == someRule1Entry2Name
            getValue() == someRule1Entry2Value
        }

        final RuleResult ruleResult2 = decisionResult.getCollectionRulesResult().get(1)
        ruleResult2.getEntryResults().size() == 2

        final EntryResult ruleResult2EntryResult1 = ruleResult2.getEntryResults().get(0)
        with(ruleResult2EntryResult1) {
            getName() == someRule2Entry1Name
            getValue() == someRule2Entry1Value
        }

        final EntryResult ruleResult2EntryResult2 = ruleResult2.getEntryResults().get(1)
        with(ruleResult2EntryResult2) {
            getName() == someRule2Entry2Name
            getValue() == someRule2Entry2Value
        }

        final EvaluationResultException exception = thrown()
        exception.message == 'Evaluation has no single result'
    }

    void 'should build decision result and be single entry result and single rule result'() {
        given:
        final String someRuleEntryName = 'Some Rule Entry Name'
        final boolean someRuleEntryValue = true

        when:
        // @formatter:off
        final DecisionResult decisionResult = DecisionResult.builder()
                .withRuleResults()
                    .withEntryResults()
                        .name(someRuleEntryName)
                        .value(someRuleEntryValue)
                        .end()
                    .end()
                .build()
        // @formatter:on

        then:
        decisionResult != null
        decisionResult.isSingleEntryResult()
        decisionResult.isSingleRuleResult()

        final EntryResult entryResult = decisionResult.getSingleEntryResult()
        with(entryResult) {
            getName() == someRuleEntryName
            getValue() == someRuleEntryValue
        }

        final RuleResult ruleResult = decisionResult.getSingleRuleResult()
        ruleResult.getEntryResults().size() == 1

        final EntryResult ruleResultEntryResult = ruleResult.getEntryResults().get(0)
        with(ruleResultEntryResult) {
            getName() == someRuleEntryName
            getValue() == someRuleEntryValue
        }
    }

    void 'should build decision result and should not be single entry result and should be single rule result'() {
        given:
        final String someRuleEntry1Name = 'Some Rule Entry 1 Name'
        final boolean someRuleEntry1Value = true
        final String someRuleEntry2Name = 'Some Rule Entry 2 Name'
        final int someRuleEntry2Value = 7

        when:
        // @formatter:off
        final DecisionResult decisionResult = DecisionResult.builder()
                .withRuleResults()
                    .withEntryResults()
                        .name(someRuleEntry1Name)
                        .value(someRuleEntry1Value)
                        .next()
                        .name(someRuleEntry2Name)
                        .value(someRuleEntry2Value)
                        .end()
                    .end()
                .build()
        // @formatter:on

        decisionResult.getSingleEntryResult()

        then:
        decisionResult != null
        decisionResult.isSingleRuleResult()
        !decisionResult.isSingleEntryResult()

        final RuleResult ruleResult = decisionResult.getSingleRuleResult()
        ruleResult.getEntryResults().size() == 2

        final EntryResult ruleResultEntryResult1 = ruleResult.getEntryResults().get(0)
        with(ruleResultEntryResult1) {
            getName() == someRuleEntry1Name
            getValue() == someRuleEntry1Value
        }

        final EntryResult ruleResultEntryResult2 = ruleResult.getEntryResults().get(1)
        with(ruleResultEntryResult2) {
            getName() == someRuleEntry2Name
            getValue() == someRuleEntry2Value
        }

        final EvaluationResultException exception = thrown()
        exception.message == 'Evaluation has no single entry result'
    }
}
