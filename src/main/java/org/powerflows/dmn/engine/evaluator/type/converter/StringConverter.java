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
import org.powerflows.dmn.engine.evaluator.type.value.StringValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StringConverter implements TypeConverter<String> {

    @Override
    public SpecifiedTypeValue<String> convert(final Object unspecifiedValue) {
        final SpecifiedTypeValue<String> stringTypeValue;
        if (unspecifiedValue == null) {
            final String stringValues = null;
            stringTypeValue = new StringValue(stringValues);
        } else if (unspecifiedValue instanceof Collection) {
            final List<String> stringValues = convertCollection((Collection<Object>) unspecifiedValue);
            stringTypeValue = new StringValue(stringValues);
        } else if (unspecifiedValue.getClass().isArray()) {
            final List<String> stringValues = convertArray((Object[]) unspecifiedValue);
            stringTypeValue = new StringValue(stringValues);
        } else {
            final String stringValue = convertSingleObject(unspecifiedValue);
            stringTypeValue = new StringValue(stringValue);
        }

        return stringTypeValue;
    }

    private List<String> convertCollection(final Collection<Object> unspecifiedValues) {
        return unspecifiedValues
                .stream()
                .map(this::convertSingleObject)
                .collect(Collectors.toList());
    }

    private List<String> convertArray(final Object[] unspecifiedValues) {
        return convertCollection(new ArrayList<>(Arrays.asList(unspecifiedValues)));
    }

    private String convertSingleObject(final Object unspecifiedValue) {
        return String.valueOf(unspecifiedValue);
    }
}
