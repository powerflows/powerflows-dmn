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

package org.powerflows.dmn.engine.evaluator.type.converter;

import org.powerflows.dmn.engine.evaluator.exception.EvaluationException;
import org.powerflows.dmn.engine.evaluator.type.value.DoubleValue;
import org.powerflows.dmn.engine.evaluator.type.value.SpecifiedTypeValue;

import java.util.List;

/**
 * Handles value conversion to {@link Double} or {@link Double} collections.
 */
public class DoubleConverter extends BaseTypeConverter<Double> {

    @Override
    protected SpecifiedTypeValue<Double> createValue(final Double value) {
        return new DoubleValue(value);
    }

    @Override
    protected SpecifiedTypeValue<Double> createValue(final List<Double> values) {
        return new DoubleValue(values);
    }

    @Override
    protected Double convertSingleObject(final Object unspecifiedValue) {
        final double doubleValue;

        if (unspecifiedValue instanceof Number) {
            doubleValue = ((Number) unspecifiedValue).doubleValue();
        } else if (unspecifiedValue instanceof String) {
            doubleValue = Double.valueOf((String) unspecifiedValue);
        } else {
            throw new EvaluationException("Value " + unspecifiedValue + " is not double");
        }

        return doubleValue;
    }
}
