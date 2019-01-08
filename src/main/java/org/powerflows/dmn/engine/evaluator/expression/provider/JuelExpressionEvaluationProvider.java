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

import org.powerflows.dmn.engine.evaluator.context.EvaluationContext;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.rule.entry.Entry;

import java.io.Serializable;

class JuelExpressionEvaluationProvider implements ExpressionEvaluationProvider {

    @Override
    public Serializable evaluateInput(final Input input, final EvaluationContext evaluationContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Serializable evaluateEntry(final Entry entry, final EvaluationContext evaluationContext) {
        throw new UnsupportedOperationException();
    }
}
