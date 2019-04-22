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

package org.powerflows.dmn.engine.model.decision.builder

import org.powerflows.dmn.engine.model.decision.DecisionBuildException
import org.powerflows.dmn.engine.model.decision.EvaluationMode
import spock.lang.Specification

class AbstractBuilderSpec extends Specification {


    void 'should throw exception when multiple build() called'() {
        given:
        final TestBuilder testBuilder = new TestBuilder();

        when:
        testBuilder.build()
        testBuilder.build()

        then:
        final IllegalStateException exception = thrown()
        exception != null
        exception.getMessage() == 'Only single build() call is allowed'
    }

    void 'should throw exception when validated value is null'() {
        given:
        final TestBuilder testBuilder = new TestBuilder(null as EvaluationMode);

        when:
        testBuilder.build()

        then:
        final DecisionBuildException exception = thrown()
        exception != null
        exception.getMessage() == 'Evaluation mode is required'
    }

    void 'should throw exception when validated value is empty'() {
        given:
        final TestBuilder testBuilder = new TestBuilder([]);

        when:
        testBuilder.build()

        then:
        final DecisionBuildException exception = thrown()
        exception != null
        exception.getMessage() == 'At least one input is required'
    }

}
