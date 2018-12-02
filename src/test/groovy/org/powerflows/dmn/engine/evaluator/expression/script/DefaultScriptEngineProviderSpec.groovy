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

package org.powerflows.dmn.engine.evaluator.expression.script

import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import spock.lang.Shared
import spock.lang.Specification

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class DefaultScriptEngineProviderSpec extends Specification {

    @Shared
    private ScriptEngineProvider scriptEngineProvider

    void setupSpec() {
        scriptEngineProvider = new DefaultScriptEngineProvider(new ScriptEngineManager());
    }

    void 'should get script engine instance'() {
        given:
        final ExpressionType supportedScriptLanguage = ExpressionType.GROOVY

        when:
        final ScriptEngine scriptEngine = scriptEngineProvider.getScriptEngine(supportedScriptLanguage)

        then:
        scriptEngine != null
    }

    void 'should throw exception for non supported script language'() {
        given:
        final ExpressionType unsupportedScriptLanguage = ExpressionType.LITERAL

        when:
        scriptEngineProvider.getScriptEngine(unsupportedScriptLanguage)

        then:
        final IllegalArgumentException exception = thrown()
        exception != null
        exception.getMessage() == 'Unsupported ' + unsupportedScriptLanguage
    }
}
