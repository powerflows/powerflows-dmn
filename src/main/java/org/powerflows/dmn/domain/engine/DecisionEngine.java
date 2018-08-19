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

package org.powerflows.dmn.domain.engine;


import org.powerflows.dmn.domain.model.decision.Decision;
import org.powerflows.dmn.domain.model.evaluation.context.ContextVariables;
import org.powerflows.dmn.domain.model.evaluation.result.DecisionResult;

import java.io.InputStream;

public interface DecisionEngine {

    DecisionResult evaluate(Decision decision, ContextVariables contextVariables);

    DecisionResult evaluate(String decisionId, ContextVariables contextVariables);

    DecisionResult evaluate(InputStream decisionFile, ContextVariables contextVariables);

    Decision toDecision(String decisionId);

    Decision toDecision(InputStream decisionFile);


}
