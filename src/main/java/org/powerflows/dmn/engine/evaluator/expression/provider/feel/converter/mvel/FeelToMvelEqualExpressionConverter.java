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

/**
 * Converts FEEL equality check into MVEL equality check.
 */
public class FeelToMvelEqualExpressionConverter implements ExpressionConverter {

    @Override
    public String convert(final String feelExpression, final String inputName) {
        if (inputName == null) {
            return feelExpression;
        }

        return inputName + "==" + feelExpression;
    }

    @Override
    public boolean isConvertible(final String feelExpression) {
        return true;
    }
}