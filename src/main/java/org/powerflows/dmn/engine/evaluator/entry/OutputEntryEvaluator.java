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
import org.powerflows.dmn.engine.evaluator.expression.provider.ExpressionEvaluationProvider;
import org.powerflows.dmn.engine.evaluator.expression.provider.DefaultExpressionEvaluationProviderFactory;
import org.powerflows.dmn.engine.evaluator.type.converter.TypeConverter;
import org.powerflows.dmn.engine.evaluator.type.converter.TypeConverterFactory;
import org.powerflows.dmn.engine.model.decision.field.Output;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;
import org.powerflows.dmn.engine.model.evaluation.result.EntryResult;

import java.io.Serializable;

/**
 * Evaluator for output entry expressions.
 */
@Slf4j
public class OutputEntryEvaluator {

    private final DefaultExpressionEvaluationProviderFactory expressionEvaluationProviderFactory;
    private final TypeConverterFactory typeConverterFactory;

    public OutputEntryEvaluator(DefaultExpressionEvaluationProviderFactory expressionEvaluationProviderFactory,
                                final TypeConverterFactory typeConverterFactory) {
        this.expressionEvaluationProviderFactory = expressionEvaluationProviderFactory;
        this.typeConverterFactory = typeConverterFactory;
    }

    /**
     *
     * @param outputEntry output entry expression defined in decision
     * @param output output definition
     * @param evaluationContext decision variable context
     * @return entry evaluation result
     */
    public EntryResult evaluate(final OutputEntry outputEntry, final Output output, final EvaluationContext evaluationContext) {
        final ExpressionEvaluationProvider expressionEvaluator = expressionEvaluationProviderFactory.getInstance(outputEntry.getExpression().getType());
        final TypeConverter typeConverter = typeConverterFactory.getInstance(output.getType());

        final Serializable outputEntryValue = expressionEvaluator.evaluateOutputEntry(outputEntry, evaluationContext);

        //Needed for the output entry value validation.
        //Correct build means the output entry value has a type compatible with the output definition.
        typeConverter.convert(outputEntryValue);

        final EntryResult outputEntryResult = EntryResult.builder().name(outputEntry.getName()).value(outputEntryValue).build();

        log.debug("Evaluated output entry result: {}", outputEntryResult);

        return outputEntryResult;
    }
}
