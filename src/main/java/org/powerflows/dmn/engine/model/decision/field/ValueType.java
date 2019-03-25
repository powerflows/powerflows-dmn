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

package org.powerflows.dmn.engine.model.decision.field;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * Lists value types that can be used in expressions.
 */
public enum ValueType {
    STRING(String.class),
    INTEGER(Integer.class),
    DOUBLE(Double.class),
    BOOLEAN(Boolean.class),
    DATE(Date.class);

    private final Class<? extends Serializable> realType;

    ValueType(final Class<? extends Serializable> realType) {
        this.realType = realType;
    }

    public static Optional<ValueType> safeValueOf(final String name) {
        if (name == null) {
            return Optional.empty();
        }

        return Arrays.stream(values())
                .filter(v -> v.name()
                        .equalsIgnoreCase(name))
                .findFirst();
    }

    public Class<? extends Serializable> realType() {
        return realType;
    }
}