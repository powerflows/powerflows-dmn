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

import java.text.SimpleDateFormat

class DefaultDecisionEngineConfigurationFeelAndJuelSpec extends Specification {

    @Shared
    private DecisionEngine decisionEngine

    @Shared
    private DecisionEngineConfiguration decisionEngineConfiguration

    @Shared
    public SimpleDateFormat format

    void setupSpec() {
        decisionEngineConfiguration = new DefaultDecisionEngineConfiguration()
        decisionEngine = decisionEngineConfiguration.configure()

        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone('UTC'))
    }

    void 'should evaluate no results'() {
        given:
        final DecisionReader decisionReader = new YamlDecisionReader()
        final String decisionFileName = 'reference-single-feel-and-juel.yml'
        final InputStream decisionInputStream = this.class.getResourceAsStream(decisionFileName)
        final Decision decision = decisionReader.read(decisionInputStream).get()

        final Map<String, Serializable> variables = [:]
        variables.put('age', 18)
        variables.put('sexExpression', 'man')
        variables.put('activeLoansNumber', 1)
        variables.put('startDate', format.parse('2016-11-30T12:00:00'))
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            !isSingleRuleResult()
            isCollectionRulesResult()
            getCollectionRulesResult().isEmpty()
        }
    }

    void 'should evaluate single results'() {
        given:
        final DecisionReader decisionReader = new YamlDecisionReader()
        final String decisionFileName = 'reference-single-feel-and-juel.yml'
        final InputStream decisionInputStream = this.class.getResourceAsStream(decisionFileName)
        final Decision decision = decisionReader.read(decisionInputStream).get()

        final Map<String, Serializable> variables = [:]
        variables.put('age', 18)
        variables.put('sexExpression', 'woman')
        variables.put('activeLoansNumber', 0)
        variables.put('startDate', format.parse('2015-11-30T12:00:00'))
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            isSingleRuleResult()
            !isCollectionRulesResult()
            getSingleRuleResult().getEntryResults()[0].value == 9000
            getSingleRuleResult().getEntryResults()[0].name == 'loanAmount'
            getSingleRuleResult().getEntryResults()[1].value == 10
            getSingleRuleResult().getEntryResults()[1].name == 'loanTerm'
        }
    }

    void 'should evaluate two collection results'() {
        given:
        final DecisionReader decisionReader = new YamlDecisionReader()
        final String decisionFileName = 'reference-single-feel-and-juel.yml'
        final InputStream decisionInputStream = this.class.getResourceAsStream(decisionFileName)
        final Decision decision = decisionReader.read(decisionInputStream).get()

        final Map<String, Serializable> variables = [:]
        variables.put('age', 19)
        variables.put('sexExpression', 'man')
        variables.put('activeLoansNumber', 0)
        variables.put('startDate', format.parse('2015-11-30T12:00:00'))
        final DecisionVariables decisionVariables = new DecisionVariables(variables)

        when:
        final DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables)

        then:
        decisionResult != null

        with(decisionResult) {
            !isSingleRuleResult()
            isCollectionRulesResult()
            getCollectionRulesResult()[0].getEntryResults()[0].value == 20000
            getCollectionRulesResult()[0].getEntryResults()[0].name == 'loanAmount'
            getCollectionRulesResult()[0].getEntryResults()[1].value == 12
            getCollectionRulesResult()[0].getEntryResults()[1].name == 'loanTerm'

            getCollectionRulesResult()[1].getEntryResults()[0].value == 25000
            getCollectionRulesResult()[1].getEntryResults()[0].name == 'loanAmount'
            getCollectionRulesResult()[1].getEntryResults()[1].value == 6
            getCollectionRulesResult()[1].getEntryResults()[1].name == 'loanTerm'
        }
    }

}
