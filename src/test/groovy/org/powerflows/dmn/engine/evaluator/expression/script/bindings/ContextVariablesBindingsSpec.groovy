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

package org.powerflows.dmn.engine.evaluator.expression.script.bindings

import org.powerflows.dmn.engine.evaluator.context.ModifiableContextVariables
import org.powerflows.dmn.engine.evaluator.expression.script.DefaultScriptEngineProvider
import org.powerflows.dmn.engine.evaluator.expression.script.ScriptEngineProvider
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.evaluation.context.ContextVariables
import org.powerflows.dmn.engine.model.evaluation.context.DecisionContextVariables
import spock.lang.Shared
import spock.lang.Specification

import javax.script.Bindings
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class ContextVariablesBindingsSpec extends Specification {

    @Shared
    private ScriptEngine scriptEngine

    void setupSpec() {
        final ScriptEngineProvider scriptEngineProvider = new DefaultScriptEngineProvider(new ScriptEngineManager());
        scriptEngine = scriptEngineProvider.getScriptEngine(ExpressionType.GROOVY);
    }

    void 'should create ContextVariablesBindings instance'() {
        given:
        final String baseBindingsVariableName = 'x'
        final Object baseBindingsVariableValue = 10
        final Bindings bindings = scriptEngine.createBindings()
        bindings.put(baseBindingsVariableName, baseBindingsVariableValue)

        final String contextVariablesVariableName = 'y'
        final Object contextVariablesVariableValue = 20
        final Map<String, Object> contextVariablesMap = [:]
        contextVariablesMap.put(contextVariablesVariableName, contextVariablesVariableValue)
        final ContextVariables decisionContextVariables = new DecisionContextVariables(contextVariablesMap)
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        when:
        final ContextVariablesBindings contextVariablesBindings = ContextVariablesBindings.create(bindings, contextVariables)

        then:
        with(contextVariablesBindings) {
            !isEmpty()
            size() == 2
            containsValue(baseBindingsVariableValue)
            containsValue(contextVariablesVariableValue)
            get(baseBindingsVariableName) == baseBindingsVariableValue
            get(contextVariablesVariableName) == contextVariablesVariableValue
            keySet() == [baseBindingsVariableName, contextVariablesVariableName] as Set
            entrySet().size() == 2
            values().size() == 2
        }
    }

    void 'should put to ContextVariablesBindings instance'() {
        given:
        final String variableName = 'x'
        final Object variableValue = 10
        final Bindings bindings = scriptEngine.createBindings()

        final Map<String, Object> contextVariablesMap = [:]
        final ContextVariables decisionContextVariables = new DecisionContextVariables(contextVariablesMap)
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        final ContextVariablesBindings contextVariablesBindings = ContextVariablesBindings.create(bindings, contextVariables)

        when:
        contextVariablesBindings.put(variableName, variableValue)

        then:
        with(contextVariablesBindings) {
            !isEmpty()
            size() == 1
            containsValue(variableValue)
            get(variableName) == variableValue
        }
    }

    void 'should put all to ContextVariablesBindings instance'() {
        given:
        final String variableName1 = 'x'
        final Object variableValue1 = 10
        final String variableName2 = 'y'
        final Object variableValue2 = 20
        final Bindings bindings = scriptEngine.createBindings()

        final Map<String, Object> contextVariablesMap = [:]
        final ContextVariables decisionContextVariables = new DecisionContextVariables(contextVariablesMap)
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        final ContextVariablesBindings contextVariablesBindings = ContextVariablesBindings.create(bindings, contextVariables)

        final Map<String, Object> variablesMapToPutAll = [:]
        variablesMapToPutAll.put(variableName1, variableValue1)
        variablesMapToPutAll.put(variableName2, variableValue2)

        when:
        contextVariablesBindings.putAll(variablesMapToPutAll)

        then:
        with(contextVariablesBindings) {
            !isEmpty()
            size() == 2
            containsValue(variableValue1)
            containsValue(variableValue2)
            get(variableName1) == variableValue1
            get(variableName2) == variableValue2
        }
    }

    void 'should remove from ContextVariablesBindings instance'() {
        given:
        final String variableName = 'x'
        final Object variableValue = 10
        final Bindings bindings = scriptEngine.createBindings()

        final Map<String, Object> contextVariablesMap = [:]
        contextVariablesMap.put(variableName, variableValue)
        final ContextVariables decisionContextVariables = new DecisionContextVariables(contextVariablesMap)
        final ModifiableContextVariables contextVariables = new ModifiableContextVariables(decisionContextVariables)

        final ContextVariablesBindings contextVariablesBindings = ContextVariablesBindings.create(bindings, contextVariables)

        when:
        contextVariablesBindings.remove(variableName)

        then:
        with(contextVariablesBindings) {
            isEmpty()
        }
    }
}
