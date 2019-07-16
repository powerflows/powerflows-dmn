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

import org.powerflows.dmn.engine.AsyncDecisionEngine
import org.powerflows.dmn.engine.DecisionEngine
import org.powerflows.dmn.engine.model.decision.Decision
import org.powerflows.dmn.engine.model.evaluation.result.DecisionResult
import org.powerflows.dmn.engine.model.evaluation.variable.DecisionVariables
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class DefaultAsyncDecisionEngineConfigurationSpec extends Specification {

    @Shared
    private AsyncDecisionEngineConfiguration asyncDecisionEngineConfiguration

    @Shared
    private DecisionEngineConfiguration decisionEngineConfiguration

    @Shared
    private DecisionEngine decisionEngine

    @Shared
    private DecisionResult decisionResult

    @Shared
    private Decision decision

    void setupSpec() {
        decisionResult = Mock()
        decisionEngine = Mock() {
            evaluate(_, _) >> decisionResult
        }
        decisionEngineConfiguration = Mock() {
            configure() >>
                    decisionEngine

        }
        asyncDecisionEngineConfiguration = new DefaultAsyncDecisionEngineConfiguration(
                engineConfiguration: decisionEngineConfiguration
        )

        decision = Mock()
    }

    void 'should evaluate asynchronously'() {
        given:
        final DecisionVariables decisionVariables = new DecisionVariables([:])

        when:
        final AsyncDecisionEngine asyncDecisionEngine = asyncDecisionEngineConfiguration.configure()

        then:
        asyncDecisionEngine != null

        when:
        final CompletableFuture<DecisionResult> future = asyncDecisionEngine.evaluate(decision, decisionVariables)

        then:
        future != null
        DecisionResult result = future.get()
        result != null

        0 * _
    }
}
