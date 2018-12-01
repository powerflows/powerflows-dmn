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

package org.powerflows.dmn.engine.evaluator.expression.comparator

import spock.lang.Specification
import spock.lang.Unroll

class DefaultObjectsComparatorSpec extends Specification {

    private final ObjectsComparator defaultObjectsComparator = new DefaultObjectsComparator()

    @Unroll
    void 'should compare input entry value #inputEntryValue and input value #inputValue with result #expectedResult'(
            final Object inputEntryValue, final Object inputValue, final boolean expectedResult) {
        given:

        when:
        final boolean result = defaultObjectsComparator.isInputEntryValueEqualInputValue(inputEntryValue, inputValue)

        then:
        result == expectedResult

        where:
        inputEntryValue  | inputValue       || expectedResult
        4                | 4                || true
        4                | 3                || false
        null             | null             || true
        null             | 4                || false
        4                | null             || false
        [1, 4] as Set    | [1, 4] as Set    || true
        [1, 4] as Set    | [1, 3] as Set    || false
        [] as Set        | [1, 3] as Set    || false
        [1, 4] as Set    | [] as Set        || false
        [] as Set        | [] as Set        || true
        [null] as Set    | [null] as Set    || true
        [4] as Set       | [null] as Set    || false
        [null] as Set    | [4] as Set       || false
        null as Set      | [4] as Set       || false
        [4] as Set       | null as Set      || false
        null as Set      | null as Set      || true
        [1, 4] as Set    | [1, 4] as List   || true
        [1, 4] as Set    | [1, 4].toArray() || true
        [1, 4] as List   | [1, 4] as Set    || true
        [1, 4] as List   | [1, 4].toArray() || true
        [1, 4].toArray() | [1, 4] as List   || true
        [1, 4].toArray() | [1, 4] as Set    || true
        [1, 4].toArray() | [1, 4].toArray() || true
        [1, 4] as List   | [1, 4] as List   || true
        1                | [1] as List      || true
        1                | [1] as Set       || true
        1                | [1].toArray()    || true
        [1] as List      | 1                || true
        [1] as Set       | 1                || true
        [1].toArray()    | 1                || true
        1                | [1, 2] as List   || false
        1                | [1, 2] as Set    || false
        1                | [1, 2].toArray() || false
        [1, 2] as List   | 1                || true
        [1, 2] as Set    | 1                || true
        [1, 2].toArray() | 1                || true
    }
}
