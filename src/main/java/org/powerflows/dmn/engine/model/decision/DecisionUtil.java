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

package org.powerflows.dmn.engine.model.decision;

import org.powerflows.dmn.engine.model.decision.expression.Expression;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.rule.Rule;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class DecisionUtil {

    private DecisionUtil() {
    }

    static void assignDefaults(final List<Input> inputs,
                               final List<Rule> rules,
                               final ExpressionType decisionExpressionType,
                               final EvaluationMode decisionEvaluationMode) {
        final Field expressionTypeField = findField(Expression.class, ExpressionType.class);

        expressionTypeField.setAccessible(true);
        assignInputsDefaults(inputs, decisionExpressionType, decisionEvaluationMode, expressionTypeField);
        assignEntriesDefaults(inputs, rules, decisionExpressionType, expressionTypeField);
        expressionTypeField.setAccessible(false);
    }

    private static void assignInputsDefaults(final List<Input> inputs,
                                             final ExpressionType decisionExpressionType,
                                             final EvaluationMode decisionEvaluationMode,
                                             final Field expressionTypeField) {
        final Field inputEvaluationModeField = findField(Input.class, EvaluationMode.class);

        inputEvaluationModeField.setAccessible(true);

        for (Input input : inputs) {
            if (input.getExpression().getType() == null) {
                setValue(expressionTypeField, input.getExpression(), decisionExpressionType);
            }

            if (input.getEvaluationMode() == null) {
                setValue(inputEvaluationModeField, input, decisionEvaluationMode);
            }
        }

        inputEvaluationModeField.setAccessible(false);
    }

    private static void assignEntriesDefaults(final List<Input> inputs,
                                              final List<Rule> rules,
                                              final ExpressionType decisionExpressionType,
                                              final Field expressionTypeField) {
        final Field inputEntryEvaluationModeField = findField(InputEntry.class, EvaluationMode.class);

        inputEntryEvaluationModeField.setAccessible(true);

        final Map<String, Input> inputsMap = inputs
                .stream()
                .collect(Collectors.toMap(Input::getName, Function.identity()));

        for (Rule rule : rules) {
            for (InputEntry inputEntry : rule.getInputEntries()) {
                if (inputEntry.getExpression().getType() == null) {
                    setValue(expressionTypeField,
                            inputEntry.getExpression(),
                            inputsMap.get(inputEntry.getName()).getExpression().getType());
                }

                if (inputEntry.getEvaluationMode() == null) {
                    setValue(inputEntryEvaluationModeField,
                            inputEntry,
                            inputsMap.get(inputEntry.getName()).getEvaluationMode());
                }
            }

            for (OutputEntry outputEntry : rule.getOutputEntries()) {
                if (outputEntry.getExpression().getType() == null) {
                    setValue(expressionTypeField,
                            outputEntry.getExpression(),
                            decisionExpressionType);
                }
            }
        }

        inputEntryEvaluationModeField.setAccessible(false);
    }

    private static Field findField(final Class clazz, final Class fieldClass) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.getType() == fieldClass)
                .findFirst()
                .orElseThrow(() -> new DecisionBuildException("Can not find " + fieldClass + " in " + clazz));
    }

    private static void setValue(final Field field, final Object object, final Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new DecisionBuildException("Can not set value " + value + " for " + object, e);
        }
    }

}