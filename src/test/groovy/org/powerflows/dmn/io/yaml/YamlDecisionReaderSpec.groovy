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

package org.powerflows.dmn.io.yaml

import org.powerflows.dmn.engine.model.decision.Decision
import org.powerflows.dmn.engine.model.decision.HitPolicy
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.ValueType
import org.powerflows.dmn.engine.reader.DecisionReadException
import spock.lang.Specification

class YamlDecisionReaderSpec extends Specification {
    final String singleDecision = 'test-single-decision.yml'
    final String multipleDecisions = 'test-multiple-decisions.yml'
    final String decisionNamePrefix = 'sample_decision_'
    final String decisionId = 'sample_decision_2'

    void 'should wrap exceptions in framework ones when reading single decision'() {
        given:
        final InputStream inputStream = Mock()

        when:
        new YamlDecisionReader().read(inputStream)

        then:
        thrown(DecisionReadException)
    }

    void 'should read decision using InputStream'() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream(singleDecision)

        when:
        final Optional<Decision> result = new YamlDecisionReader().read(inputStream)

        then:
        result != null
        result.isPresent()
        assertDecision(result.get(), decisionNamePrefix + 1)
    }

    void 'should read decision by id using InputStream'() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream(multipleDecisions)

        when:
        final Optional<Decision> result = new YamlDecisionReader().read(inputStream, decisionId)

        then:
        result != null
        result.isPresent()
        assertDecision(result.get(), decisionNamePrefix + 2)
    }

    void 'should wrap exceptions in framework ones when reading multiple decisions'() {
        given:
        final InputStream inputStream = Mock()

        when:
        new YamlDecisionReader().readAll(inputStream)

        then:
        thrown(DecisionReadException)
    }

    void 'should read all decisions using InputStream'() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream(multipleDecisions)

        when:
        final List<Decision> result = new YamlDecisionReader().readAll(inputStream)

        then:
        result?.size() == 2
        assertDecision(result[0], decisionNamePrefix + 1)
        assertDecision(result[1], decisionNamePrefix + 2)
    }

    void assertDecision(final Decision decision, final String id) {
        with(decision) {
            id == id
            name == 'Sample Decision One'
            hitPolicy == HitPolicy.UNIQUE
            expressionType == ExpressionType.GROOVY
            inputs.size() == 2
            outputs.size() == 1
            rules.size() == 6
        }

        with(decision.inputs[0]) {
            name == 'age'
            expression.value == 'toYear(now()) - toYear(birthDate)'
            expression.type == ExpressionType.FEEL
            description == 'This is something about age'
            type == ValueType.INTEGER
        }

        with(decision.inputs[1]) {
            name == 'colour'
            expression.value == null
            expression.type == ExpressionType.GROOVY
            description == 'This is something about colour'
            type == ValueType.STRING
        }

        with(decision.outputs[0]) {
            name == 'allow'
            type == ValueType.BOOLEAN
            description == 'We expect a decision, if we have access to do it'
        }

        with(decision.rules[0]) {
            description == '3 allows always'
            inputEntries[0].name == 'age'
            inputEntries[0].expression.type == ExpressionType.GROOVY
            inputEntries[0].expression.value == 3

            outputEntries[0].name == 'allow'
            outputEntries[0].expression.type == ExpressionType.GROOVY
            outputEntries[0].expression.value == true
        }

        with(decision.rules[1]) {
            description == null
            inputEntries[0].name == 'age'
            inputEntries[0].expression.type == ExpressionType.GROOVY
            inputEntries[0].expression.value == 8

            inputEntries[1].name == 'colour'
            inputEntries[1].expression.type == ExpressionType.GROOVY
            inputEntries[1].expression.value == 'red'

            outputEntries[0].name == 'allow'
            outputEntries[0].expression.type == ExpressionType.GROOVY
            outputEntries[0].expression.value == true
        }

        with(decision.rules[2]) {
            description == 'Green allows always'
            inputEntries[0].name == 'colour'
            inputEntries[0].expression.type == ExpressionType.GROOVY
            inputEntries[0].expression.value == 'green'

            outputEntries[0].name == 'allow'
            outputEntries[0].expression.type == ExpressionType.GROOVY
            outputEntries[0].expression.value == true
        }

        with(decision.rules[3]) {
            description == 'Expression usage'

            inputEntries[0].name == 'colour'
            inputEntries[0].expression.type == ExpressionType.FEEL
            inputEntries[0].expression.value == 'not("blue", "purple")'

            inputEntries[1].name == 'age'
            inputEntries[1].expression.type == ExpressionType.GROOVY
            inputEntries[1].expression.value == 10

            outputEntries[0].name == 'allow'
            outputEntries[0].expression.type == ExpressionType.GROOVY
            outputEntries[0].expression.value == true
        }

        with(decision.rules[4]) {
            description == 'Formatted expression usage'

            inputEntries[0].name == 'colour'
            inputEntries[0].expression.type == ExpressionType.FEEL
            inputEntries[0].expression.value == 'not( "red", "pink" )'

            inputEntries[1].name == 'age'
            inputEntries[1].expression.type == ExpressionType.GROOVY
            inputEntries[1].expression.value == 20

            outputEntries[0].name == 'allow'
            outputEntries[0].expression.type == ExpressionType.GROOVY
            outputEntries[0].expression.value == true
        }

        with(decision.rules[5]) {
            description == 'No input entry'

            inputEntries.isEmpty()
            outputEntries[0].name == 'allow'
            outputEntries[0].expression.type == ExpressionType.GROOVY
            outputEntries[0].expression.value == true
        }
    }
}