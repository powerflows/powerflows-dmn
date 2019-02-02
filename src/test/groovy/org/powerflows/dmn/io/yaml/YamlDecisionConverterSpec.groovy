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
import org.powerflows.dmn.engine.model.decision.EvaluationMode
import org.powerflows.dmn.engine.model.decision.HitPolicy
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.ValueType
import org.powerflows.dmn.io.yaml.model.YamlDecision
import spock.lang.Specification

class YamlDecisionConverterSpec extends Specification {

    private final YamlDecisionConverter converter = new YamlDecisionConverter();

    void 'should convert simple decision to yaml '() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream('converter-simple.yml')
        final Decision decision = new YamlDecisionReader().read(inputStream).get()

        when:
        final YamlDecision yamlDecision = converter.to(decision)

        then:
        yamlDecision != null

        with(yamlDecision) {
            getId() == 'some_table_id'
            getName() == 'Some Table Name'
            getHitPolicy() == null
            getExpressionType() == null
            getEvaluationMode() == null
            getFields().getIn().size() == 2
            getFields().getOut().size() == 1
            getRules().size() == 1
        }

        with(yamlDecision.getFields().getIn().get('inputOne')) {
            getType() == ValueType.INTEGER
            getEvaluationMode() == null
            getExpressionType() == null
            getExpression() == null
            getNameAlias() == null
            getDescription() == null
        }

        with(yamlDecision.getFields().getIn().get('inputTwo')) {
            getType() == ValueType.STRING
            getEvaluationMode() == null
            getExpressionType() == null
            getExpression() == null
            getNameAlias() == null
            getDescription() == null
        }

        with(yamlDecision.getFields().getOut().get('outputOne')) {
            getType() == ValueType.BOOLEAN
            getDescription() == null
        }

        yamlDecision.getRules().get(0).getDescription() == null

        with(yamlDecision.getRules().get(0).getIn().get('inputOne')) {
            getEvaluationMode() == null
            getExpressionType() == null
            getExpression() == '> 20'
            getNameAlias() == null
        }

        with(yamlDecision.getRules().get(0).getIn().get('inputTwo')) {
            getEvaluationMode() == null
            getExpressionType() == null
            getExpression() == 3
            getNameAlias() == null
        }

        with(yamlDecision.getRules().get(0).getOut().get('outputOne')) {
            getExpressionType() == null
            getExpression() == 'someVariable1 || someVariable2'
        }
    }

    void 'should convert simple with expression type decision to yaml '() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream('converter-simple-with-expression-type.yml')
        final Decision decision = new YamlDecisionReader().read(inputStream).get()

        when:
        final YamlDecision yamlDecision = converter.to(decision)

        then:
        yamlDecision != null

        with(yamlDecision) {
            getId() == 'some_table_id'
            getName() == 'Some Table Name'
            getHitPolicy() == null
            getExpressionType() == null
            getEvaluationMode() == null
            getFields().getIn().size() == 2
            getFields().getOut().size() == 1
            getRules().size() == 1
        }

        with(yamlDecision.getFields().getIn().get('inputOne')) {
            getType() == ValueType.INTEGER
            getEvaluationMode() == null
            getExpressionType() == null
            getExpression() == null
            getNameAlias() == null
            getDescription() == null
        }

        with(yamlDecision.getFields().getIn().get('inputTwo')) {
            getType() == ValueType.STRING
            getEvaluationMode() == null
            getExpressionType() == null
            getExpression() == null
            getNameAlias() == null
            getDescription() == null
        }

        with(yamlDecision.getFields().getOut().get('outputOne')) {
            getType() == ValueType.BOOLEAN
            getDescription() == null
        }

        yamlDecision.getRules().get(0).getDescription() == null

        with(yamlDecision.getRules().get(0).getIn().get('inputOne')) {
            getEvaluationMode() == null
            getExpressionType() == ExpressionType.JUEL
            getExpression() == '> 20'
            getNameAlias() == null
        }

        with(yamlDecision.getRules().get(0).getIn().get('inputTwo')) {
            getEvaluationMode() == null
            getExpressionType() == ExpressionType.JUEL
            getExpression() == 3
            getNameAlias() == null
        }

        with(yamlDecision.getRules().get(0).getOut().get('outputOne')) {
            getExpressionType() == ExpressionType.JAVASCRIPT
            getExpression() == 'someVariable1 || someVariable2'
        }
    }

    void 'should convert advanced decision to yaml'() {
        given:
        final InputStream inputStream = this.class.getResourceAsStream('converter-advanced.yml')
        final Decision decision = new YamlDecisionReader().read(inputStream).get()

        when:
        final YamlDecision yamlDecision = converter.to(decision)

        then:
        yamlDecision != null

        with(yamlDecision) {
            getId() == 'some_table_id'
            getName() == 'Some Table Name'
            getHitPolicy() == HitPolicy.COLLECT
            getExpressionType() == ExpressionType.GROOVY
            getEvaluationMode() == EvaluationMode.INPUT_COMPARISON
            getFields().getIn().size() == 2
            getFields().getOut().size() == 1
            getRules().size() == 1
        }

        with(yamlDecision.getFields().getIn().get('inputOne')) {
            getType() == ValueType.INTEGER
            getEvaluationMode() == EvaluationMode.BOOLEAN
            getExpressionType() == ExpressionType.JAVASCRIPT
            getExpression() == null
            getNameAlias() == 'inputOneAlias'
            getDescription() == null
        }

        with(yamlDecision.getFields().getIn().get('inputTwo')) {
            getType() == ValueType.STRING
            getEvaluationMode() == null
            getExpressionType() == ExpressionType.FEEL
            getExpression() == null
            getNameAlias() == 'inputTwoAlias'
            getDescription() == null
        }

        with(yamlDecision.getFields().getOut().get('outputOne')) {
            getType() == ValueType.BOOLEAN
            getDescription() == null
        }

        yamlDecision.getRules().get(0).getDescription() == null

        with(yamlDecision.getRules().get(0).getIn().get('inputOne')) {
            getEvaluationMode() == EvaluationMode.INPUT_COMPARISON
            getExpressionType() == ExpressionType.JUEL
            getExpression() == '> 20'
            getNameAlias() == 'inputOneAliasOverridden'
        }

        with(yamlDecision.getRules().get(0).getIn().get('inputTwo')) {
            getEvaluationMode() == EvaluationMode.BOOLEAN
            getExpressionType() == ExpressionType.JAVASCRIPT
            getExpression() == 3
            getNameAlias() == null
        }

        with(yamlDecision.getRules().get(0).getOut().get('outputOne')) {
            getExpressionType() == ExpressionType.JUEL
            getExpression() == 'someVariable1 || someVariable2'
        }
    }
}
