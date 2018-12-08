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

package org.powerflows.dmn.engine.evaluator.expression.comparator;

import org.powerflows.dmn.engine.evaluator.type.value.SpecifiedTypeValue;

import java.util.Collection;
import java.util.Collections;

public class DefaultObjectsComparator implements ObjectsComparator {

    @Override
    public <T, P> boolean isInputEntryValueEqualInputValue(final SpecifiedTypeValue<T> inputEntryValue, final SpecifiedTypeValue<P> inputValue) {
        if (inputEntryValue == null) {
            throw new NullPointerException("Input entry value can not be null");
        }

        if (inputValue == null) {
            throw new NullPointerException("Input value can not be null");
        }

        final Collection<T> inputEntryValues = convertObjectToCollection(inputEntryValue);
        final Collection<P> inputValues = convertObjectToCollection(inputValue);

        return areSubCollections(inputEntryValues, inputValues);
    }

    private <X> Collection<X> convertObjectToCollection(final SpecifiedTypeValue<X> object) {
        final Collection<X> objects;

        if (object.isSingleValue()) {
            objects = Collections.singleton(object.getValue());
        } else {
            objects = object.getValues();
        }

        return objects;
    }

    private <T, P> boolean areSubCollections(final Collection<T> entryCollection, final Collection<P> inputCollection) {
        final boolean result;

        if (inputCollection.isEmpty() && !entryCollection.isEmpty()) {
            result = false;
        } else {
            result = entryCollection.containsAll(inputCollection);
        }

        return result;
    }
}
