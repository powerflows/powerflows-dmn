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
import org.powerflows.dmn.engine.evaluator.type.value.IntegerValue;
import org.powerflows.dmn.engine.evaluator.type.value.SpecifiedTypeValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles value conversion to {@link Integer} or {@link Integer} collections.
 */
public class IntegerConverter extends BaseTypeConverter<Integer> {

    @Override
    protected SpecifiedTypeValue<Integer> createValue(final Integer value) {
        return new IntegerValue(value);
    }

    @Override
    protected SpecifiedTypeValue<Integer> createValue(final List<Integer> values) {
        return new IntegerValue(values);
    }

    private boolean isInteger(final Number value) {
        final double doubleValue = value.doubleValue();

        return doubleValue == (int) doubleValue;
    }

    @Override
    protected Integer convertSingleObject(final Object unspecifiedValue) {
        final int intValue;

        if (unspecifiedValue instanceof Number) {
            if (isInteger((Number) unspecifiedValue)) {
                intValue = ((Number) unspecifiedValue).intValue();
            } else {
                throw new EvaluationException("Value " + unspecifiedValue + " is not integer");
            }
        } else if (unspecifiedValue instanceof String) {
            try {
                intValue = Integer.parseInt((String) unspecifiedValue);
            } catch (NumberFormatException e) {
                throw new EvaluationException("Value " + unspecifiedValue + " is not integer");
            }
        } else {
            throw new EvaluationException("Value " + unspecifiedValue + " is not integer");
        }

        return intValue;
    }
}
