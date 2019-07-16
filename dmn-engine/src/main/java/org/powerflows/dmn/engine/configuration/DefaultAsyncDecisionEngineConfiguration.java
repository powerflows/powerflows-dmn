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
package org.powerflows.dmn.engine.configuration;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.powerflows.dmn.engine.AsyncDecisionEngine;
import org.powerflows.dmn.engine.DefaultAsyncDecisionEngine;
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.MethodBinding;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Default DecisionEngine configurer.
 * Builds and configures {@link DefaultAsyncDecisionEngine} instances.
 * Allows for customisation of method binding configuration.
 * Uses single thread asynchronous executor service by default @see Executors.newSingleThreadExecutor
 *
 * @see MethodBinding
 */
@Accessors(chain = true, fluent = true)
public class DefaultAsyncDecisionEngineConfiguration implements AsyncDecisionEngineConfiguration {
    @Setter
    private DecisionEngineConfiguration engineConfiguration;
    @Setter
    private ExecutorService executorService;
    @Setter
    private List<MethodBinding> methodBindings = Collections.emptyList();

    @Override
    public AsyncDecisionEngine configure() {
        initEngineConfiguration();
        initMethodBindings();
        initExecutorService();

        return new DefaultAsyncDecisionEngine(engineConfiguration.configure(), executorService);
    }

    private void initMethodBindings() {
        engineConfiguration.methodBindings(methodBindings);
    }

    private void initEngineConfiguration() {
        if (engineConfiguration == null) {
            engineConfiguration = new DefaultDecisionEngineConfiguration();
        }
    }

    private void initExecutorService() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
    }
}
