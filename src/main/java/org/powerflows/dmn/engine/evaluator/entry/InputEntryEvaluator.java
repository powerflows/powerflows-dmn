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


import lombok.extern.slf4j.Slf4j;
import org.powerflows.dmn.engine.evaluator.context.EvaluationContext;
import org.powerflows.dmn.engine.evaluator.entry.mode.provider.EvaluationModeProvider;
import org.powerflows.dmn.engine.evaluator.entry.mode.provider.EvaluationModeProviderFactory;
import org.powerflows.dmn.engine.evaluator.expression.provider.ExpressionEvaluationProvider;
import org.powerflows.dmn.engine.evaluator.expression.provider.DefaultExpressionEvaluationProviderFactory;
import org.powerflows.dmn.engine.evaluator.type.converter.TypeConverter;
import org.powerflows.dmn.engine.evaluator.type.converter.TypeConverterFactory;
import org.powerflows.dmn.engine.evaluator.type.value.SpecifiedTypeValue;
import org.powerflows.dmn.engine.model.decision.EvaluationMode;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.field.ValueType;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;

import java.io.Serializable;

@Slf4j
public class InputEntryEvaluator {

    private final DefaultExpressionEvaluationProviderFactory expressionEvaluationProviderFactory;
    private final TypeConverterFactory typeConverterFactory;
    private final EvaluationModeProviderFactory evaluationModeProviderFactory;


    public InputEntryEvaluator(final DefaultExpressionEvaluationProviderFactory expressionEvaluationProviderFactory,
                               final TypeConverterFactory typeConverterFactory,
                               final EvaluationModeProviderFactory evaluationModeProviderFactory) {
        this.expressionEvaluationProviderFactory = expressionEvaluationProviderFactory;
        this.typeConverterFactory = typeConverterFactory;
        this.evaluationModeProviderFactory = evaluationModeProviderFactory;
    }

    public boolean evaluate(final InputEntry inputEntry,
                            final Input input,
                            final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of input entry: {} with input: {} and evaluation context: {}", inputEntry, input, evaluationContext);

        final ExpressionEvaluationProvider inputEntryExpressionEvaluator = expressionEvaluationProviderFactory.getInstance(inputEntry.getExpression().getType());
        final TypeConverter typeConverter = typeConverterFactory.getInstance(input.getType());

        if (!isInputEvaluated(input, evaluationContext)) {
            final ExpressionEvaluationProvider inputExpressionEvaluator = expressionEvaluationProviderFactory.getInstance(input.getExpression().getType());
            final Serializable evaluatedInputValue = inputExpressionEvaluator.evaluateInput(input, evaluationContext);

            evaluationContext.addVariable(input.getName(), evaluatedInputValue);
        }

        final Object inputValue = evaluationContext.get(inputEntry.getName());
        final SpecifiedTypeValue<?> typedInputValue = typeConverter.convert(inputValue);
        final Object inputEntryValue = inputEntryExpressionEvaluator.evaluateInputEntry(inputEntry, evaluationContext);

        final SpecifiedTypeValue<?> typedInputEntryValue;
        if (isBoolean(inputEntryValue)) {
            final TypeConverter booleanTypeConverter;
            if (ValueType.BOOLEAN == input.getType()) {
                booleanTypeConverter = typeConverter;
            } else {
                booleanTypeConverter = typeConverterFactory.getInstance(ValueType.BOOLEAN);
            }

            typedInputEntryValue = booleanTypeConverter.convert(inputEntryValue);
        } else {
            typedInputEntryValue = typeConverter.convert(inputEntryValue);
        }

        final EvaluationMode evaluationMode = inputEntry.getEvaluationMode();
        final EvaluationModeProvider evaluationModeProvider = evaluationModeProviderFactory.getInstance(evaluationMode);
        final boolean result = evaluationModeProvider.isPositive(input.getType(), typedInputEntryValue, typedInputValue);

        log.debug("Evaluated input entry result: {}", result);

        return result;
    }

    private boolean isBoolean(final Object value) {
        return Boolean.TRUE.equals(value) || Boolean.FALSE.equals(value);
    }

    private boolean isInputEvaluated(final Input input, final EvaluationContext evaluationContext) {
        return evaluationContext.isPresent(input.getName());
    }

}
