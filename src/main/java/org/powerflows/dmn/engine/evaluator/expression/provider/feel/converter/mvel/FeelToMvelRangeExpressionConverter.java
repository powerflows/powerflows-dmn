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

import org.powerflows.dmn.engine.evaluator.expression.provider.binding.ExpressionEvaluationException;
import org.powerflows.dmn.engine.evaluator.expression.provider.feel.converter.ExpressionConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeelToMvelRangeExpressionConverter implements ExpressionConverter {
    private final Pattern rangePattern = Pattern.compile("^(\\(|\\]|\\[)(.*[^\\.])\\.\\.(.+)(\\)|\\[|\\])$");

    private final ExpressionConverter feelToMvelFunctionExpressionConverter;

    FeelToMvelRangeExpressionConverter(ExpressionConverter feelToMvelFunctionExpressionConverter) {
        this.feelToMvelFunctionExpressionConverter = feelToMvelFunctionExpressionConverter;
    }

    @Override
    public String convert(final String feelExpression, final String inputName) {
        final Matcher matcher = rangePattern.matcher(feelExpression);
        final String startRangeSymbol;
        String startRangeOperand;
        final String endRangeSymbol;
        String endRangeOperand;
        if (matcher.matches()) {
            startRangeSymbol = matcher.group(1);
            startRangeOperand = matcher.group(2);
            endRangeOperand = matcher.group(3);
            endRangeSymbol = matcher.group(4);
        } else {
            throw new ExpressionEvaluationException("Can not evaluate feel expression '" + feelExpression + "'");
        }

        final String mvelStartComparator = convertToMvelStartComparator(startRangeSymbol);
        final String mvelEndComparator = convertToMvelEndComparator(endRangeSymbol);

        if (feelToMvelFunctionExpressionConverter.isConvertible(startRangeOperand)) {
            startRangeOperand = feelToMvelFunctionExpressionConverter.convert(startRangeOperand, null);
        }

        if (feelToMvelFunctionExpressionConverter.isConvertible(endRangeOperand)) {
            endRangeOperand = feelToMvelFunctionExpressionConverter.convert(endRangeOperand, null);
        }

        return inputName + mvelStartComparator + startRangeOperand + "&&" + inputName + mvelEndComparator + endRangeOperand;
    }

    @Override
    public boolean isConvertible(final String feelExpression) {
        return feelExpression.startsWith("(") || feelExpression.startsWith("[") || feelExpression.startsWith("]");

    }

    private String convertToMvelStartComparator(final String startRangeSymbol) {
        return startRangeSymbol.equals("[") ? ">=" : ">";
    }

    private String convertToMvelEndComparator(final String endRangeSymbol) {
        return endRangeSymbol.equals("]") ? "<=" : "<";
    }
}