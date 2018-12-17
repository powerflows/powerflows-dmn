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

import org.powerflows.dmn.engine.model.decision.field.ValueType;

import java.util.EnumMap;

public class TypeConverterFactory {

    private final EnumMap<ValueType, TypeConverter> factories = new EnumMap<>(ValueType.class);

    public TypeConverterFactory() {
        factories.put(ValueType.STRING, new StringConverter());
        factories.put(ValueType.INTEGER, new IntegerConverter());
        factories.put(ValueType.DOUBLE, new DoubleConverter());
        factories.put(ValueType.BOOLEAN, new BooleanConverter());
        factories.put(ValueType.DATE, new DateConverter());
    }

    public TypeConverter getInstance(final ValueType valueType) {
        final TypeConverter typeConverter = factories.get(valueType);

        if (typeConverter == null) {
            throw new IllegalArgumentException("Unknown value type");
        }

        return typeConverter;
    }
}
