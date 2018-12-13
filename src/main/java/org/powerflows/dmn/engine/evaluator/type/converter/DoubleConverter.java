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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DoubleConverter implements TypeConverter<Double> {

    @Override
    public SpecifiedTypeValue<Double> convert(final Object unspecifiedValue) {
        final SpecifiedTypeValue<Double> doubleTypeValue;

        if (unspecifiedValue instanceof Collection) {
            final List<Double> doubleValues = convertCollection((Collection<Object>) unspecifiedValue);
            doubleTypeValue = new DoubleValue(doubleValues);
        } else if (unspecifiedValue.getClass().isArray()) {
            final List<Double> doubleValues = convertArray((Object[]) unspecifiedValue);
            doubleTypeValue = new DoubleValue(doubleValues);
        } else {
            final Double doubleValue = convertSingleObject(unspecifiedValue);
            doubleTypeValue = new DoubleValue(doubleValue);
        }

        return doubleTypeValue;
    }

    private List<Double> convertCollection(final Collection<Object> unspecifiedValues) {
        return unspecifiedValues
                .stream()
                .map(this::convertSingleObject)
                .collect(Collectors.toList());
    }

    private List<Double> convertArray(final Object[] unspecifiedValues) {
        return convertCollection(new ArrayList<>(Arrays.asList(unspecifiedValues)));
    }

    private Double convertSingleObject(final Object unspecifiedValue) {
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
