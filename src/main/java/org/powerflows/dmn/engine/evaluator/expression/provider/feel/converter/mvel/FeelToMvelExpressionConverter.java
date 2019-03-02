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

import lombok.extern.slf4j.Slf4j;
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.MethodBinding;
import org.powerflows.dmn.engine.evaluator.expression.provider.feel.converter.ExpressionConverter;

import java.util.List;

@Slf4j
public class FeelToMvelExpressionConverter implements ExpressionConverter {
    private final ExpressionConverter feelToMvelCollectionExpressionConverter;
    private final ExpressionConverter feelToMvelNotExpressionConverter;
    private final ExpressionConverter feelToMvelUnaryExpressionConverter;


    public FeelToMvelExpressionConverter(List<MethodBinding> functions) {
        final ExpressionConverter feelToMvelEqualExpressionConverter = new FeelToMvelEqualExpressionConverter();
        final ExpressionConverter feelToMvelComparisonExpressionConverter = new FeelToMvelComparisonExpressionConverter();
        final ExpressionConverter feelToMvelFunctionExpressionConverter = new FeelToMvelFunctionExpressionConverter(functions);
        final ExpressionConverter feelToMvelRangeExpressionConverter = new FeelToMvelRangeExpressionConverter(feelToMvelFunctionExpressionConverter);
        feelToMvelUnaryExpressionConverter = new FeelToMvelUnaryExpressionConverter(
                feelToMvelComparisonExpressionConverter,
                feelToMvelRangeExpressionConverter,
                feelToMvelEqualExpressionConverter,
                feelToMvelFunctionExpressionConverter
        );

        feelToMvelCollectionExpressionConverter = new FeelToMvelCollectionExpressionConverter(
                feelToMvelUnaryExpressionConverter);

        feelToMvelNotExpressionConverter = new FeelToMvelNotExpressionConverter(
                feelToMvelRangeExpressionConverter,
                feelToMvelCollectionExpressionConverter);
    }

    @Override
    public String convert(final String feelExpression, final String inputName) {
        final String mvelExpression;

        if (feelExpression == null) {
            mvelExpression = null;
        } else if (feelToMvelNotExpressionConverter.isConvertible(feelExpression)) {
            mvelExpression = feelToMvelNotExpressionConverter.convert(feelExpression, inputName);
        } else if (feelToMvelCollectionExpressionConverter.isConvertible(feelExpression)) {
            mvelExpression = feelToMvelCollectionExpressionConverter.convert(feelExpression, inputName);
        } else {
            mvelExpression = feelToMvelUnaryExpressionConverter.convert(feelExpression, inputName);
        }

        return mvelExpression;
    }

    @Override
    public boolean isConvertible(final String expression) {
        return true;
    }
}