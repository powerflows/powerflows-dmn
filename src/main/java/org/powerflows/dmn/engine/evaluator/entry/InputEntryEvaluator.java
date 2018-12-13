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
import org.powerflows.dmn.engine.evaluator.context.ModifiableContextVariables;
import org.powerflows.dmn.engine.evaluator.expression.comparator.ObjectsComparator;
import org.powerflows.dmn.engine.evaluator.expression.provider.EvaluationProviderFactory;
import org.powerflows.dmn.engine.evaluator.expression.provider.ExpressionEvaluationProvider;
import org.powerflows.dmn.engine.evaluator.type.converter.TypeConverter;
import org.powerflows.dmn.engine.evaluator.type.converter.TypeConverterFactory;
import org.powerflows.dmn.engine.evaluator.type.value.SpecifiedTypeValue;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.field.ValueType;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;

@Slf4j
public class InputEntryEvaluator {

    private final EvaluationProviderFactory evaluationProviderFactory;
    private final TypeConverterFactory typeConverterFactory;
    private final ObjectsComparator objectsComparator;


    public InputEntryEvaluator(final EvaluationProviderFactory evaluationProviderFactory,
                               final TypeConverterFactory typeConverterFactory,
                               final ObjectsComparator objectsComparator) {
        this.evaluationProviderFactory = evaluationProviderFactory;
        this.typeConverterFactory = typeConverterFactory;
        this.objectsComparator = objectsComparator;
    }

    public boolean evaluate(final InputEntry inputEntry, final Input input, final ModifiableContextVariables contextVariables) {
        final ExpressionEvaluationProvider inputEntryExpressionEvaluator = evaluationProviderFactory.getInstance(inputEntry.getExpression().getType());
        final TypeConverter typeConverter = typeConverterFactory.getInstance(input.getType());

        if (!isInputEvaluated(input, contextVariables)) {
            final ExpressionEvaluationProvider inputExpressionEvaluator = evaluationProviderFactory.getInstance(input.getExpression().getType());
            final Object evaluatedInputValue = inputExpressionEvaluator.evaluateInput(input, contextVariables);

            contextVariables.addVariable(input.getName(), evaluatedInputValue);
        }

        final Object inputValue = contextVariables.get(inputEntry.getName());
        final SpecifiedTypeValue<?> typedInputValue = typeConverter.convert(inputValue);

        final Object inputEntryValue = inputEntryExpressionEvaluator.evaluateEntry(inputEntry.getExpression(), contextVariables);

        final boolean result;

        if (!ValueType.BOOLEAN.equals(input.getType())) {
            if (inputEntryValue.equals(Boolean.TRUE)) {
                result = true;
            } else if (inputEntryValue.equals(Boolean.FALSE)) {
                result = false;
            } else {
                final SpecifiedTypeValue<?> typedInputEntryValue = typeConverter.convert(inputEntryValue);
                result = objectsComparator.isInputEntryValueEqualInputValue(typedInputEntryValue, typedInputValue);
            }
        } else {
            final SpecifiedTypeValue<?> typedInputEntryValue = typeConverter.convert(inputEntryValue);
            result = objectsComparator.isInputEntryValueEqualInputValue(typedInputEntryValue, typedInputValue);
        }

        log.debug("Evaluated input entry result: {}", result);

        return result;
    }

    private boolean isInputEvaluated(final Input input, final ModifiableContextVariables contextVariables) {
        return contextVariables.isPresent(input.getName());
    }

}
