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
import org.powerflows.dmn.engine.evaluator.type.value.BooleanValue;
import org.powerflows.dmn.engine.evaluator.type.value.SpecifiedTypeValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BooleanConverter implements TypeConverter<Boolean> {

    @Override
    public SpecifiedTypeValue<Boolean> convert(final Object unspecifiedValue) {
        final SpecifiedTypeValue<Boolean> booleanTypeValue;

        if (unspecifiedValue instanceof Collection) {
            final List<Boolean> booleanValues = convertCollection((Collection<Object>) unspecifiedValue);
            booleanTypeValue = new BooleanValue(booleanValues);
        } else if (unspecifiedValue.getClass().isArray()) {
            final List<Boolean> booleanValues = convertArray((Object[]) unspecifiedValue);
            booleanTypeValue = new BooleanValue(booleanValues);
        } else {
            final Boolean booleanValue = convertSingleObject(unspecifiedValue);
            booleanTypeValue = new BooleanValue(booleanValue);
        }

        return booleanTypeValue;
    }

    private List<Boolean> convertCollection(final Collection<Object> unspecifiedValues) {
        return unspecifiedValues
                .stream()
                .map(this::convertSingleObject)
                .collect(Collectors.toList());
    }

    private List<Boolean> convertArray(final Object[] unspecifiedValues) {
        return convertCollection(new ArrayList<>(Arrays.asList(unspecifiedValues)));
    }

    private Boolean convertSingleObject(final Object unspecifiedValue) {
        final boolean booleanValue;

        if (unspecifiedValue instanceof Boolean) {
            booleanValue = (Boolean) unspecifiedValue;
        } else if (unspecifiedValue instanceof String) {
            if ("true".equalsIgnoreCase((String) unspecifiedValue)) {
                booleanValue = true;
            } else if ("false".equalsIgnoreCase((String) unspecifiedValue)) {
                booleanValue = false;
            } else {
                throw new EvaluationException("Value " + unspecifiedValue + " is not boolean");
            }
        } else {
            throw new EvaluationException("Value " + unspecifiedValue + " is not boolean");
        }

        return booleanValue;
    }
}
