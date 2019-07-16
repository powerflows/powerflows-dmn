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
package org.powerflows.dmn.engine;

import org.powerflows.dmn.engine.model.decision.Decision;
import org.powerflows.dmn.engine.model.evaluation.result.DecisionResult;
import org.powerflows.dmn.engine.model.evaluation.variable.DecisionVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * Default asynchronous decision engine implementation.
 */
public class DefaultAsyncDecisionEngine implements AsyncDecisionEngine {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAsyncDecisionEngine.class);
    private final DecisionEngine decisionEngine;
    private final ExecutorService executorService;

    public DefaultAsyncDecisionEngine(DecisionEngine decisionEngine, ExecutorService executorService) {
        this.decisionEngine = decisionEngine;
        this.executorService = executorService;
    }

    @Override
    public CompletableFuture<DecisionResult> evaluate(final Decision decision, final DecisionVariables decisionVariables) {
        final CompletableFuture<DecisionResult> result = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                result.complete(decisionEngine.evaluate(decision, decisionVariables));
            } catch (Exception e) {
                LOG.debug("Error completing decision evaluation", e);
                result.completeExceptionally(e);
            }
        });

        return result;
    }
}
