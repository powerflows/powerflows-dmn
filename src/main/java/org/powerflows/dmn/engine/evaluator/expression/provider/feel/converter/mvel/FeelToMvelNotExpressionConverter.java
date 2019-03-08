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

public class FeelToMvelNotExpressionConverter implements ExpressionConverter {

    private final Pattern notPattern = Pattern.compile("^not\\((.+)\\)$");

    private final ExpressionConverter feelToMvelRangeExpressionConverter;
    private final ExpressionConverter feelToMvelCollectionExpressionConverter;

    FeelToMvelNotExpressionConverter(ExpressionConverter feelToMvelRangeExpressionConverter,
                                     ExpressionConverter feelToMvelCollectionExpressionConverter) {
        this.feelToMvelRangeExpressionConverter = feelToMvelRangeExpressionConverter;
        this.feelToMvelCollectionExpressionConverter = feelToMvelCollectionExpressionConverter;
    }

    @Override
    public String convert(final String feelExpression, final String inputName) {
        final Matcher matcher = notPattern.matcher(feelExpression);
        final String feelNotArgumentExpression;
        final String mvelExpression;
        if (matcher.matches()) {
            feelNotArgumentExpression = matcher.group(1);
            if (feelToMvelCollectionExpressionConverter.isConvertible(feelNotArgumentExpression)) {
                mvelExpression = "!(" + feelToMvelCollectionExpressionConverter.convert(feelNotArgumentExpression, inputName) + ")";
            } else if (feelToMvelRangeExpressionConverter.isConvertible(feelNotArgumentExpression)) {
                mvelExpression = "!(" + feelToMvelRangeExpressionConverter.convert(feelNotArgumentExpression, inputName) + ")";
            } else {
                mvelExpression = inputName + "!=" + feelNotArgumentExpression;
            }
        } else {
            throw new ExpressionEvaluationException("Can not evaluate feel expression '" + feelExpression + "'");
        }

        return mvelExpression;
    }

    @Override
    public boolean isConvertible(final String feelExpression) {
        return feelExpression.startsWith("not(");
    }
}