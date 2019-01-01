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
import org.powerflows.dmn.engine.model.evaluation.variable.DecisionVariables
import org.powerflows.dmn.engine.model.evaluation.result.DecisionResult
import org.powerflows.dmn.engine.model.evaluation.result.EntryResult
import org.powerflows.dmn.engine.reader.DecisionReader
import org.powerflows.dmn.io.yaml.YamlDecisionReader
import spock.lang.Shared
import spock.lang.Specification

class DefaultDecisionEngineConfigurationReferenceSingleSpec extends Specification {

    @Shared
    private DecisionEngine decisionEngine

    @Shared
    private Decision decision

    @Shared
    private DecisionEngineConfiguration decisionEngineConfiguration

    void setupSpec() {
        final DecisionReader decisionReader = new YamlDecisionReader()
        decisionEngineConfiguration = new DefaultDecisionEngineConfiguration()

        decisionEngine = decisionEngineConfiguration.configure()

        final String decisionFileName = 'reference-single.yml'
        final InputStream decisionInputStream = this.class.getResourceAsStream(decisionFileName)
        decision = decisionReader.read(decisionInputStream).get()
    }

    void 'should configure default decision engine'() {
        given:

        when:
        final DecisionEngine decisionEngine = decisionEngineConfiguration.configure()

        then:
        decisionEngine != null
    }

    void 'should evaluate empty collection rules result using default decision engine'() {
        given:
        final Map<String, Serializable> variables = [:]
        variables.put('x', 2)
        variables.put('y', 3)
        variables.put('p', 0)
        variables.put('q', 0)
        variables.put('arrayVar', '')
        variables.put('inputFour', Date.parse('yyyy-MM-dd', '2018-12-18'))
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

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
        final Map<String, Serializable> variables = [:]
        variables.put('x', 1)
        variables.put('y', 1)
        variables.put('p', 0)
        variables.put('q', 0)
        variables.put('arrayVar', '')
        variables.put('inputFour', Date.parse('yyyy-MM-dd', '2018-12-18'))
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

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
        final Map<String, Serializable> variables = [:]
        variables.put('x', 5)
        variables.put('y', 7)
        variables.put('p', 0)
        variables.put('q', 0)
        variables.put('arrayVar', '')
        variables.put('inputFour', Date.parse('yyyy-MM-dd', '2018-12-18'))
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

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
        final Map<String, Serializable> variables = [:]
        variables.put('x', 5)
        variables.put('y', 7)
        variables.put('p', 3)
        variables.put('q', 4)
        variables.put('arrayVar', '')
        variables.put('inputFour', Date.parse('yyyy-MM-dd', '2018-12-18'))
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

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
        final Map<String, Serializable> variables = [:]
        variables.put('x', 10)
        variables.put('y', 20)
        variables.put('p', 0)
        variables.put('q', 0)
        variables.put('arrayVar', 'a,e,c')
        variables.put('inputFour', Date.parse('yyyy-MM-dd', '2018-12-18'))
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

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

    void 'should evaluate single rule on boolean input entry result basis using default decision engine'() {
        given:
        final Map<String, Serializable> variables = [:]
        variables.put('x', 0)
        variables.put('y', 0)
        variables.put('p', 25)
        variables.put('q', 26)
        variables.put('arrayVar', '')
        variables.put('inputFour', Date.parse('yyyy-MM-dd', '2018-12-18'))
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

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
            getValue() == 'The output rule five'
        }
    }

    void 'should evaluate single rule when date input entry is matching using default decision engine'() {
        given:
        final Map<String, Serializable> variables = [:]
        variables.put('x', 0)
        variables.put('y', 0)
        variables.put('p', 7)
        variables.put('q', 7)
        variables.put('arrayVar', '')
        variables.put('inputFour', '2018-12-14')
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

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
            getValue() == 'The output rule six'
        }
    }

    void 'should evaluate single rule when date with time input entry is matching using default decision engine'() {
        given:
        final Map<String, Serializable> variables = [:]
        variables.put('x', 0)
        variables.put('y', 0)
        variables.put('p', 7)
        variables.put('q', 7)
        variables.put('arrayVar', '')
        variables.put('inputFour', '2018-12-14T19:38:26')
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

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
            getValue() == 'The output rule seven'
        }
    }

    void 'should evaluate multiple rule when date with time input entry is matching using default decision engine'() {
        given:
        final Map<String, Serializable> variables = [:]
        variables.put('x', 0)
        variables.put('y', 0)
        variables.put('p', 7)
        variables.put('q', 7)
        variables.put('arrayVar', '')
        variables.put('inputFour', Date.parse('yyyy-MM-dd', '2018-12-13'))
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

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
            getValue() == 'The output rule eight'
        }

        final EntryResult entryResult21 = decisionResult.getCollectionRulesResult().get(1).getEntryResults()[0]
        with(entryResult21) {
            getName() == 'outputOne'
            getValue()
        }

        final EntryResult entryResult22 = decisionResult.getCollectionRulesResult().get(1).getEntryResults()[1]
        with(entryResult22) {
            getName() == 'outputTwo'
            getValue() == 'The output rule nine'
        }
    }
}
