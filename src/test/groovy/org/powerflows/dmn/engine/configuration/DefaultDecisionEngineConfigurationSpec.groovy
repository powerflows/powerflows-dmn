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
import spock.lang.Specification

class DefaultDecisionEngineConfigurationSpec extends Specification {

    private final DecisionReader decisionReader = new YamlDecisionReader()
    private final DecisionEngineConfiguration decisionEngineConfiguration = new DefaultDecisionEngineConfiguration()

    void 'should configure default decision engine'() {
        given:

        when:
        final DecisionEngine decisionEngine = decisionEngineConfiguration.configure()

        then:
        decisionEngine != null
    }

    void 'should evaluate empty collection rules result using default decision engine'() {
        given:
        final DecisionEngine decisionEngine = decisionEngineConfiguration.configure()
        final String decisionFileName = 'decision-with-literal-expressions-only.yml'
        final InputStream decisionInputStream = this.class.getResourceAsStream(decisionFileName)
        final Decision decision = decisionReader.read(decisionInputStream)

        final Map<String, Object> variables = [:]
        variables.put('inputOne', 2)
        variables.put('inputTwo', 'five')
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
        final DecisionEngine decisionEngine = decisionEngineConfiguration.configure()
        final String decisionFileName = 'decision-with-literal-expressions-only.yml'
        final InputStream decisionInputStream = this.class.getResourceAsStream(decisionFileName)
        final Decision decision = decisionReader.read(decisionInputStream)

        final Map<String, Object> variables = [:]
        variables.put('inputOne', 2)
        variables.put('inputTwo', 'three')
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
        final DecisionEngine decisionEngine = decisionEngineConfiguration.configure()
        final String decisionFileName = 'decision-with-literal-expressions-only.yml'
        final InputStream decisionInputStream = this.class.getResourceAsStream(decisionFileName)
        final Decision decision = decisionReader.read(decisionInputStream)

        final Map<String, Object> variables = [:]
        variables.put('inputOne', 5)
        variables.put('inputTwo', 'seven')
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
            getValue() == 'The output'
        }
    }

}
