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

package org.powerflows.dmn.engine.configuration

import org.powerflows.dmn.engine.DecisionEngine
import org.powerflows.dmn.engine.model.decision.Decision
import org.powerflows.dmn.engine.model.evaluation.context.DecisionContextVariables
import org.powerflows.dmn.engine.model.evaluation.result.DecisionResult
import org.powerflows.dmn.engine.model.evaluation.result.EntryResult
import org.powerflows.dmn.engine.reader.DecisionReader
import org.powerflows.dmn.io.yaml.YamlDecisionReader
import spock.lang.Shared
import spock.lang.Specification

class DefaultDecisionEngineConfigurationGroovySpec extends Specification {
    @Shared
    private DecisionEngine decisionEngine

    @Shared
    private Decision decision

    void setupSpec() {
        final DecisionReader decisionReader = new YamlDecisionReader()
        final DecisionEngineConfiguration decisionEngineConfiguration = new DefaultDecisionEngineConfiguration()

        decisionEngine = decisionEngineConfiguration.configure()

        final String decisionFileName = 'decision-with-groovy-expressions-only.yml'
        final InputStream decisionInputStream = this.class.getResourceAsStream(decisionFileName)
        decision = decisionReader.read(decisionInputStream)
    }

    void 'should evaluate empty collection rules result using default decision engine'() {
        given:
        final Map<String, Object> variables = [:]
        variables.put('x', 2)
        variables.put('y', 3)
        variables.put('p', 0)
        variables.put('q', 0)
        variables.put('arrayVar', '')
        final DecisionContextVariables decisionContextVariables = new DecisionContextVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionContextVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            !isSingleEntryResult()
            !isSingleRuleResult()
            isCollectionRulesResult()
            getCollectionRulesResult().isEmpty()
        }
    }

    void 'should evaluate single rule and single entry result using default decision engine'() {
        given:
        final Map<String, Object> variables = [:]
        variables.put('x', 1)
        variables.put('y', 1)
        variables.put('p', 0)
        variables.put('q', 0)
        variables.put('arrayVar', '')
        final DecisionContextVariables decisionContextVariables = new DecisionContextVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionContextVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            isSingleEntryResult()
            isSingleRuleResult()
            !isCollectionRulesResult()
            getSingleRuleResult().getEntryResults().size() == 1
        }

        final EntryResult entryResult = decisionResult.getSingleRuleResult().getEntryResults()[0]
        with(entryResult) {
            getName() == 'outputOne'
            !getValue()
        }
    }

    void 'should evaluate single rule result using default decision engine'() {
        given:
        final Map<String, Object> variables = [:]
        variables.put('x', 5)
        variables.put('y', 7)
        variables.put('p', 0)
        variables.put('q', 0)
        variables.put('arrayVar', '')
        final DecisionContextVariables decisionContextVariables = new DecisionContextVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionContextVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            !isSingleEntryResult()
            isSingleRuleResult()
            !isCollectionRulesResult()
            getSingleRuleResult().getEntryResults().size() == 2
        }

        final EntryResult entryResult1 = decisionResult.getSingleRuleResult().getEntryResults()[0]
        with(entryResult1) {
            getName() == 'outputOne'
            getValue()
        }

        final EntryResult entryResult2 = decisionResult.getSingleRuleResult().getEntryResults()[1]
        with(entryResult2) {
            getName() == 'outputTwo'
            getValue() == 'The output rule two'
        }
    }

    void 'should evaluate multiple rule result using default decision engine'() {
        given:
        final Map<String, Object> variables = [:]
        variables.put('x', 5)
        variables.put('y', 7)
        variables.put('p', 3)
        variables.put('q', 4)
        variables.put('arrayVar', '')
        final DecisionContextVariables decisionContextVariables = new DecisionContextVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionContextVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            !isSingleEntryResult()
            !isSingleRuleResult()
            isCollectionRulesResult()
            getCollectionRulesResult().get(0).getEntryResults().size() == 2
            getCollectionRulesResult().get(1).getEntryResults().size() == 2
        }

        final EntryResult entryResult11 = decisionResult.getCollectionRulesResult().get(0).getEntryResults()[0]
        with(entryResult11) {
            getName() == 'outputOne'
            getValue()
        }

        final EntryResult entryResult12 = decisionResult.getCollectionRulesResult().get(0).getEntryResults()[1]
        with(entryResult12) {
            getName() == 'outputTwo'
            getValue() == 'The output rule two'
        }

        final EntryResult entryResult21 = decisionResult.getCollectionRulesResult().get(1).getEntryResults()[0]
        with(entryResult21) {
            getName() == 'outputOne'
            getValue()
        }

        final EntryResult entryResult22 = decisionResult.getCollectionRulesResult().get(1).getEntryResults()[1]
        with(entryResult22) {
            getName() == 'outputTwo'
            getValue() == 'The output rule three'
        }
    }

    void 'should evaluate single rule on collection operations basis result using default decision engine'() {
        given:
        final Map<String, Object> variables = [:]
        variables.put('x', 10)
        variables.put('y', 20)
        variables.put('p', 0)
        variables.put('q', 0)
        variables.put('arrayVar', 'a,e,c')
        final DecisionContextVariables decisionContextVariables = new DecisionContextVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionContextVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            !isSingleEntryResult()
            isSingleRuleResult()
            !isCollectionRulesResult()
            getSingleRuleResult().getEntryResults().size() == 2
        }

        final EntryResult entryResult1 = decisionResult.getSingleRuleResult().getEntryResults()[0]
        with(entryResult1) {
            getName() == 'outputOne'
            getValue()
        }

        final EntryResult entryResult2 = decisionResult.getSingleRuleResult().getEntryResults()[1]
        with(entryResult2) {
            getName() == 'outputTwo'
            getValue() == 'The output rule four'
        }
    }

}
