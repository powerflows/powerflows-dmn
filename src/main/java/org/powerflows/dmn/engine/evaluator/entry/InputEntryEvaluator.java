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

package org.powerflows.dmn.engine.evaluator.entry;


import org.powerflows.dmn.engine.evaluator.context.ModifiableContextVariables;
import org.powerflows.dmn.engine.evaluator.entry.expression.provider.EvaluationProviderFactory;
import org.powerflows.dmn.engine.evaluator.entry.expression.provider.ExpressionEvaluationProvider;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.evaluation.context.DecisionContextVariables;

class InputEntryEvaluator {

    private final EvaluationProviderFactory evaluationProviderFactory;

    public InputEntryEvaluator(EvaluationProviderFactory evaluationProviderFactory) {
        this.evaluationProviderFactory = evaluationProviderFactory;
    }

    boolean evaluate(final InputEntry inputEntry, final Input input, final ModifiableContextVariables contextVariables) {
        final ExpressionEvaluationProvider inputEntryExpressionEvaluator = evaluationProviderFactory.getInstance(inputEntry.getExpression().getType());

        if (!isInputEvaluated(input, contextVariables)) {
            final ExpressionEvaluationProvider inputExpressionEvaluator = evaluationProviderFactory.getInstance(input.getExpression().getType());
            final Object evaluatedInputValue = inputExpressionEvaluator.evaluateInput(input, contextVariables);

            contextVariables.addVariable(input.getName(), evaluatedInputValue);
        }

        return inputEntryExpressionEvaluator.evaluateInputEntry(inputEntry, contextVariables);
    }

    private boolean isInputEvaluated(final Input input, final ModifiableContextVariables contextVariables) {
        return contextVariables.isPresent(input.getName());
    }

}
