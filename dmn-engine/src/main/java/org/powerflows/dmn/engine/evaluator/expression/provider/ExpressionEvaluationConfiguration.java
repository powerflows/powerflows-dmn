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
package org.powerflows.dmn.engine.evaluator.expression.provider;

import lombok.Builder;
import lombok.Getter;
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.MethodBinding;

import javax.script.ScriptEngineManager;
import java.util.Collections;
import java.util.List;

/**
 * Configures expression evaluation.
 */
@Getter
@Builder
public class ExpressionEvaluationConfiguration {

    private final List<MethodBinding> methodBindings;
    private final ScriptEngineManager scriptEngineManager;

    private ExpressionEvaluationConfiguration(final List<MethodBinding> methodBindings, final ScriptEngineManager scriptEngineManager) {
        this.methodBindings = methodBindings == null ? Collections.emptyList() : methodBindings;
        this.scriptEngineManager = scriptEngineManager == null ? new ScriptEngineManager() : scriptEngineManager;
    }

    /**
     * Creates simple configuration using default {@link ScriptEngineManager} and empty {@link MethodBinding} collection.
     * @return configuration instance
     */
    public static ExpressionEvaluationConfiguration simpleConfiguration() {
        return ExpressionEvaluationConfiguration.builder().build();
    }
}