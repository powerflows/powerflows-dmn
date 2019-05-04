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

import java.util.List;

/**
 * Handles value conversion to {@link Boolean} or {@link Boolean} collections.
 */
public class BooleanConverter extends BaseTypeConverter<Boolean> {


    @Override
    protected SpecifiedTypeValue<Boolean> createValue(final Boolean value) {
        return new BooleanValue(value);
    }

    @Override
    protected SpecifiedTypeValue<Boolean> createValue(final List<Boolean> values) {
        return new BooleanValue(values);
    }

    protected Boolean convertSingleObject(final Object unspecifiedValue) {
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
