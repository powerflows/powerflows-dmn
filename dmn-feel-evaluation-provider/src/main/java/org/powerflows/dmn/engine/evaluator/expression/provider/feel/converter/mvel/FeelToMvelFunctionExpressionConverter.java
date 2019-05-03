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
package org.powerflows.dmn.engine.evaluator.expression.provider.feel.converter.mvel;

import org.powerflows.dmn.engine.evaluator.expression.ExpressionEvaluationException;
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.MethodBinding;
import org.powerflows.dmn.engine.evaluator.expression.provider.feel.converter.ExpressionConverter;

import java.util.List;

/**
 * Converts FEEL function expression into MVEL function call.
 */
public class FeelToMvelFunctionExpressionConverter implements ExpressionConverter {

    private final List<MethodBinding> functions;

    FeelToMvelFunctionExpressionConverter(List<MethodBinding> functions) {
        this.functions = functions;
    }

    @Override
    public String convert(final String feelExpression, final String inputName) {
        final String functionName = functions
                .stream()
                .filter(methodBinding -> replaceWhiteChars(feelExpression).startsWith(replaceWhiteChars(methodBinding.name())))
                .findFirst()
                .orElseThrow(() -> new ExpressionEvaluationException("No bound function for expression '" + feelExpression + "'"))
                .name();

        String mvelExpression = feelExpression.replace(functionName, replaceWhiteChars(functionName));
        if (inputName != null) {
            mvelExpression = inputName + "==" + mvelExpression;
        }

        return mvelExpression;
    }

    @Override
    public boolean isConvertible(final String feelExpression) {
        return functions
                .stream()
                .anyMatch(methodBinding -> replaceWhiteChars(feelExpression).startsWith(replaceWhiteChars(methodBinding.name()) + "("));
    }

    private String replaceWhiteChars(final String string) {
        return string.replaceAll("\\s", "");
    }
}