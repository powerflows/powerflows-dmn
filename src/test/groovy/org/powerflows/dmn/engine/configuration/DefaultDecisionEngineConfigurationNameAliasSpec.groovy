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
import org.powerflows.dmn.engine.model.evaluation.result.DecisionResult
import org.powerflows.dmn.engine.model.evaluation.variable.DecisionVariables
import org.powerflows.dmn.engine.reader.DecisionReader
import org.powerflows.dmn.io.yaml.YamlDecisionReader
import spock.lang.Shared
import spock.lang.Specification

class DefaultDecisionEngineConfigurationNameAliasSpec extends Specification {

    @Shared
    private DecisionEngine decisionEngine

    @Shared
    private DecisionEngineConfiguration decisionEngineConfiguration

    void setupSpec() {
        decisionEngineConfiguration = new DefaultDecisionEngineConfiguration()
        decisionEngine = decisionEngineConfiguration.configure()
    }

    void 'should evaluate with name alias case when no context variables matching to input name'() {
        given:
        final DecisionReader decisionReader = new YamlDecisionReader()
        final String decisionFileName = 'name-alias-no-ctx-var-matching-to-input-name.yml'
        final InputStream decisionInputStream = this.class.getResourceAsStream(decisionFileName)
        final Decision decision = decisionReader.read(decisionInputStream).get()

        final Map<String, Serializable> variables = [:]
        variables.put('inputOneAlias', 2)
        variables.put('inputTwoAlias', true)
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            isSingleEntryResult()
            isSingleRuleResult()
            !isCollectionRulesResult()
            getSingleEntryResult().getName() == 'outputOne'
            getSingleEntryResult().getValue() == 'Result 1'
        }
    }

    void 'should evaluate with name alias when second input has same name alias like first input name and context variable having name of second input is present'() {
        given:
        final DecisionReader decisionReader = new YamlDecisionReader()
        final String decisionFileName = 'name-alias-second-input-has-same-name-alias-like-first-input-name-and-ctx-var-for-second-input-is-present.yml'
        final InputStream decisionInputStream = this.class.getResourceAsStream(decisionFileName)
        final Decision decision = decisionReader.read(decisionInputStream).get()

        final Map<String, Serializable> variables = [:]
        variables.put('inputOne', 2)
        variables.put('inputTwo', 7)
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            isSingleEntryResult()
            isSingleRuleResult()
            !isCollectionRulesResult()
            getSingleEntryResult().getName() == 'outputOne'
            getSingleEntryResult().getValue() == 'Result 1'
        }
    }

    void 'should evaluate with name alias when second input has same name alias like first input name and with no context variable having name of second input'() {
        given:
        final DecisionReader decisionReader = new YamlDecisionReader()
        final String decisionFileName = 'name-alias-second-input-has-same-name-alias-like-first-input-name-and-with no-ctx-var-for-second-input.yml'
        final InputStream decisionInputStream = this.class.getResourceAsStream(decisionFileName)
        final Decision decision = decisionReader.read(decisionInputStream).get()

        final Map<String, Serializable> variables = [:]
        variables.put('inputOne', 2)
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            isSingleEntryResult()
            isSingleRuleResult()
            !isCollectionRulesResult()
            getSingleEntryResult().getName() == 'outputOne'
            getSingleEntryResult().getValue() == 'Result 1'
        }
    }

}
