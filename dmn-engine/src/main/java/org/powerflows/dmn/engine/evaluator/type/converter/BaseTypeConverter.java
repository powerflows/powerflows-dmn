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

import org.powerflows.dmn.engine.evaluator.type.value.SpecifiedTypeValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base converter class for type converters.
 * @param <T> type it targets.
 */
public abstract class BaseTypeConverter<T> implements TypeConverter<T> {

    public SpecifiedTypeValue<T> convert(final Object unspecifiedValue) {
        final SpecifiedTypeValue<T> specifiedTypeValue;
        if (unspecifiedValue == null) {
            specifiedTypeValue = createValue((T) null);
        } else if (unspecifiedValue instanceof Collection<?>) {
            specifiedTypeValue = createValue(convertCollection((Collection<?>) unspecifiedValue));
        } else if (unspecifiedValue.getClass().isArray()) {
            specifiedTypeValue = createValue(convertArray((Object[]) unspecifiedValue));
        } else {
            specifiedTypeValue = createValue(convertSingleObject(unspecifiedValue));
        }

        return specifiedTypeValue;
    }

    protected abstract SpecifiedTypeValue<T> createValue(T value);

    protected abstract SpecifiedTypeValue<T> createValue(List<T> values);

    protected List<T> convertArray(final Object[] unspecifiedValues) {
        return convertCollection(Arrays.asList(unspecifiedValues));
    }

    protected abstract T convertSingleObject(final Object unspecifiedValue);

    protected List<T> convertCollection(final Collection<?> unspecifiedValues) {
        return unspecifiedValues
                .stream()
                .map(this::convertSingleObject)
                .collect(Collectors.toList());
    }
}
