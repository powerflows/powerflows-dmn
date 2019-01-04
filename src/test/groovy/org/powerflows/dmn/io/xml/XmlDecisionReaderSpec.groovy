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

package org.powerflows.dmn.io.xml

import org.powerflows.dmn.engine.model.decision.Decision
import org.powerflows.dmn.engine.model.decision.DecisionBuildException
import org.powerflows.dmn.engine.model.decision.HitPolicy
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.ValueType
import org.powerflows.dmn.engine.reader.DecisionReadException
import spock.lang.Shared
import spock.lang.Specification

class XmlDecisionReaderSpec extends Specification {
    final static String omgExampleXml = 'omg-dmn-1.1-example.dmn'
    final static String camundaExampleXml = 'camunda-dmn-1.1-example.dmn'
    final static String camundaExampleXmlBadInputColumns = 'camunda-dmn-1.1-example-bad-input-columns.dmn'
    final static String camundaExampleXmlBadOutputColumns = 'camunda-dmn-1.1-example-bad-output-columns.dmn'
    final static String camundaExampleXmlDuplicateIds = 'camunda-dmn-1.1-example-duplicate-ids.dmn'

    @Shared
    private XmlDecisionReader reader

    void setupSpec() {
        reader = new XmlDecisionReader()
    }

    void 'should fail on invalid XML'() {
        given:
        final String xml = '''<?xml version="1.0" encoding="UTF-8"?>
            <definitions xmlns="http://www.omg.org/spec/DMN/20151101/dmn.xsd" id="Definitions_0ulq5ro" name="DRD" namespace="http://camunda.org/schema/1.0/dmn">
              <decision id="some_table_id_1" name="Some Table Name">
                <decisionTable>
                    <input id="input_1" label="Some Input 1 Description">
                    <inputExpression id="inputExpression_1" typeRef="integer" >
                      <text>&gt; 5</text>
                    </inputExpression>
                  </input>
              </decision>
            </definitions>
            '''

        when:
        reader.readAll(new ByteArrayInputStream(xml.getBytes()))

        then:
        thrown(DecisionReadException)
    }

    void 'should fail on minimal markup with empty data'() {
        given:
        final String xml = '''<?xml version="1.0" encoding="UTF-8"?>
            <definitions xmlns="http://www.omg.org/spec/DMN/20151101/dmn.xsd" id="Definitions_0ulq5ro" name="DRD" namespace="http://camunda.org/schema/1.0/dmn">
              <decision id="some_table_id_1" name="Some Table Name">
                <decisionTable/>
              </decision>
            </definitions>
            '''

        when:
        reader.readAll(new ByteArrayInputStream(xml.getBytes()))

        then:
        thrown(DecisionBuildException)
    }

    void 'should load minimal markup with empty data using strict mode and fail on unexpected element'() {
        given:
        final XmlDecisionReader decisionReader = new XmlDecisionReader(true)
        final String xml = '''<?xml version="1.0" encoding="UTF-8"?>
            <definitions xmlns="http://www.omg.org/spec/DMN/20151101/dmn.xsd" id="Definitions_0ulq5ro" name="DRD" namespace="http://camunda.org/schema/1.0/dmn">
              <decision id="some_table_id_1" name="Some Table Name">
                <decisionTable>
                    <badElement attr="x"/>
                </decisionTable>
              </decision>
            </definitions>
            '''

        when:
        decisionReader.readAll(new ByteArrayInputStream(xml.getBytes()))

        then:
        thrown(DecisionReadException)
    }

    void 'should read all decisions from OMG example stream'() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream(omgExampleXml)

        when:
        final List<Decision> result = reader.readAll(inputStream)

        then:
        result?.size() == 1
    }

    void 'should read single decision from Camunda example stream'() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream(camundaExampleXml)

        when:
        final Optional<Decision> result = reader.read(inputStream)

        then:
        result.isPresent()
        result.ifPresent({ d -> assertFirstCamundaExampleContents(d) })
    }

    void 'should read single decision by id from Camunda example stream'() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream(camundaExampleXml)
        final String decisionId = 'Decision_0replv7'

        when:
        final Optional<Decision> result = reader.read(inputStream, decisionId)

        then:
        result.isPresent()
        result.ifPresent({ d -> assertSecondCamundaExampleContents(d) })
    }

    void 'should read all decisions from Camunda example stream'() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream(camundaExampleXml)

        when:
        final List<Decision> result = reader.readAll(inputStream)

        then:
        result?.size() == 2
        assertFirstCamundaExampleContents(result[0])
    }

    void 'should fail reading xml with wrong number of input entries vs inputs'() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream(camundaExampleXmlBadInputColumns)

        when:
        reader.readAll(inputStream)

        then:
        thrown(DecisionReadException)
    }

    void 'should fail reading xml with wrong number of output entries vs outputs'() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream(camundaExampleXmlBadOutputColumns)

        when:
        reader.readAll(inputStream)

        then:
        thrown(DecisionReadException)
    }

    void 'should fail reading xml with duplicate element ids'() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream(camundaExampleXmlDuplicateIds)

        when:
        reader.readAll(inputStream)

        then:
        thrown(DecisionReadException)
    }

    private void assertSecondCamundaExampleContents(final Decision result) {
        with(result) {
            getId() == 'Decision_0replv7'
            getName() == 'Another Table'
            getHitPolicy() == HitPolicy.UNIQUE
            getInputs().size() == 1
            getOutputs().size() == 2
            getRules().size() == 1
        }
    }

    private void assertFirstCamundaExampleContents(final Decision result) {
        with(result) {
            getId() == 'some_table_id_1'
            getName() == 'Some Table Name'
            getHitPolicy() == HitPolicy.FIRST
            getInputs().size() == 3
            getOutputs().size() == 2
            getRules().size() == 2
        }

        with(result.getInputs()[0]) {
            getName() == 'input_1'
            getDescription() == 'Some Input 1 Description'
            getType() == ValueType.INTEGER
            getExpression().getType() == ExpressionType.FEEL
            getExpression().getValue() == '> 5'
        }

        with(result.getInputs()[1]) {
            getName() == 'InputClause_03bkdz8'
            getType() == ValueType.STRING
            getExpression().getType() == ExpressionType.FEEL
            getExpression().getValue() == '' //How do we handle lists of possible values?
        }

        with(result.getInputs()[2]) {
            getName() == 'InputClause_1beg16w'
            getType() == ValueType.BOOLEAN
            getExpression().getType() == ExpressionType.GROOVY
            getExpression().getValue() == 'someInteger > 10'
        }

        with(result.getOutputs()[0]) {
            getName() == 'output_0'
            getType() == ValueType.BOOLEAN

        }

        with(result.getOutputs()[1]) {
            getName() == 'output_1'
            getType() == ValueType.STRING
        }

        with(result.getRules()[0]) {
            getInputEntries().size() == 2
            getOutputEntries().size() == 1
        }

        with(result.getRules()[0].getInputEntries()[0]) {
            getName() == 'input_1'
            getExpression().getType() == ExpressionType.GROOVY
            getExpression().getValue() == '> 20'
        }

        with(result.getRules()[0].getInputEntries()[1]) {
            getName() == 'InputClause_03bkdz8'
            getExpression().getType() == ExpressionType.FEEL
            getExpression().getValue() == 'not("blue", "purple")'
        }

        with(result.getRules()[0].getOutputEntries()[0]) {
            getName() == 'output_0'
            getExpression().getType() == ExpressionType.GROOVY
            getExpression().getValue() == 'someVariable1 || someVariable2'
        }

        with(result.getRules()[1]) {
            getInputEntries().size() == 2
            getOutputEntries().size() == 1
        }

        with(result.getRules()[1].getInputEntries()[0]) {
            getName() == 'input_1'
            getExpression().getType() == ExpressionType.FEEL
            getExpression().getValue() == '5'
        }

        with(result.getRules()[1].getInputEntries()[1]) {
            getName() == 'InputClause_1beg16w'
            getExpression().getType() == ExpressionType.FEEL
            getExpression().getValue() == 'true'
        }

        with(result.getRules()[1].getOutputEntries()[0]) {
            getName() == 'output_1'
            getExpression().getType() == ExpressionType.FEEL
            getExpression().getValue() == '"The output"'
        }
    }
}