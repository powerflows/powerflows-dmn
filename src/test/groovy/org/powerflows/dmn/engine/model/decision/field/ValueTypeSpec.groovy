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

package org.powerflows.dmn.engine.model.decision.field

import spock.lang.Specification

class ValueTypeSpec extends Specification {


    void 'should return non empty optional for existing name'() {
        given:
        final String valueName = 'InTeGeR'

        when:
        final Optional<ValueType> valueType = ValueType.safeValueOf(valueName)

        then:
        valueType != null
        valueType.get() == ValueType.INTEGER
    }

    void 'should return empty optional for null name'() {
        given:
        final String valueName = null

        when:
        final Optional<ValueType> valueType = ValueType.safeValueOf(valueName)

        then:
        valueType != null
        !valueType.isPresent()
    }

    void 'should return empty optional for non existing name'() {
        given:
        final String valueName = 'test'

        when:
        final Optional<ValueType> valueType = ValueType.safeValueOf(valueName)

        then:
        valueType != null
        !valueType.isPresent()
    }

}
