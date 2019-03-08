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

import org.powerflows.dmn.engine.evaluator.expression.provider.feel.converter.ExpressionConverter;

public class FeelToMvelUnaryExpressionConverter implements ExpressionConverter {

    private final ExpressionConverter feelToMvelComparisonExpressionConverter;
    private final ExpressionConverter feelToMvelRangeExpressionConverter;
    private final ExpressionConverter feelToMvelEqualExpressionConverter;
    private final ExpressionConverter feelToMvelFunctionExpressionConverter;

    FeelToMvelUnaryExpressionConverter(ExpressionConverter feelToMvelComparisonExpressionConverter,
                                       ExpressionConverter feelToMvelRangeExpressionConverter,
                                       ExpressionConverter feelToMvelEqualExpressionConverter,
                                       ExpressionConverter feelToMvelFunctionExpressionConverter) {
        this.feelToMvelComparisonExpressionConverter = feelToMvelComparisonExpressionConverter;
        this.feelToMvelRangeExpressionConverter = feelToMvelRangeExpressionConverter;
        this.feelToMvelEqualExpressionConverter = feelToMvelEqualExpressionConverter;
        this.feelToMvelFunctionExpressionConverter = feelToMvelFunctionExpressionConverter;
    }

    @Override
    public String convert(final String feelExpression, final String inputName) {
        final String mvelExpression;
        if (feelToMvelFunctionExpressionConverter.isConvertible(feelExpression)) {
            mvelExpression = feelToMvelFunctionExpressionConverter.convert(feelExpression, inputName);
        } else if (feelToMvelRangeExpressionConverter.isConvertible(feelExpression)) {
            mvelExpression = feelToMvelRangeExpressionConverter.convert(feelExpression, inputName);
        } else if (feelToMvelComparisonExpressionConverter.isConvertible(feelExpression)) {
            mvelExpression = feelToMvelComparisonExpressionConverter.convert(feelExpression, inputName);
        } else {
            mvelExpression = feelToMvelEqualExpressionConverter.convert(feelExpression, inputName);
        }

        return mvelExpression;
    }

    @Override
    public boolean isConvertible(final String feelExpression) {
        return true;
    }
}