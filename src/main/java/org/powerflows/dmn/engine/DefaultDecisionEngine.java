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


import org.powerflows.dmn.engine.evaluator.decision.DecisionEvaluator;
import org.powerflows.dmn.engine.model.decision.Decision;
import org.powerflows.dmn.engine.model.evaluation.context.DecisionContextVariables;
import org.powerflows.dmn.engine.model.evaluation.result.DecisionResult;
import org.powerflows.dmn.engine.reader.DecisionReader;

import java.io.InputStream;
import java.util.List;

public class DefaultDecisionEngine implements DecisionEngine {

    private final DecisionReader decisionReader;
    private final DecisionEvaluator decisionEvaluator;

    public DefaultDecisionEngine(DecisionReader decisionReader,
                                 DecisionEvaluator decisionEvaluator) {
        this.decisionReader = decisionReader;
        this.decisionEvaluator = decisionEvaluator;
    }

    @Override
    public DecisionResult evaluate(final Decision decision, final DecisionContextVariables contextVariables) {
        return decisionEvaluator.evaluate(decision, contextVariables);
    }

    @Override
    public DecisionResult evaluate(final String decisionId, final DecisionContextVariables contextVariables) {
        final Decision decision = toDecision(decisionId);

        return evaluate(decision, contextVariables);
    }

    @Override
    public DecisionResult evaluate(final InputStream decisionFile, final DecisionContextVariables contextVariables) {
        final Decision decision = toDecision(decisionFile);

        return evaluate(decision, contextVariables);
    }

    @Override
    public Decision toDecision(final String decisionId) {
        return decisionReader.read(decisionId);
    }

    @Override
    public Decision toDecision(final InputStream decisionFile) {
        return decisionReader.read(decisionFile);
    }

    @Override
    public List<Decision> toDecisions(final List<String> decisionIds) {
        return decisionReader.readAll(decisionIds);
    }

    @Override
    public List<Decision> toDecisions(final InputStream decisionsFile) {
        return decisionReader.readAll(decisionsFile);
    }

}
