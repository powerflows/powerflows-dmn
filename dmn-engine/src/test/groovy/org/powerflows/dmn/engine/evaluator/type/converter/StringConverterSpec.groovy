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

package org.powerflows.dmn.engine.evaluator.type.converter

import org.powerflows.dmn.engine.evaluator.type.value.StringValue
import org.powerflows.dmn.engine.model.decision.field.ValueType
import spock.lang.Specification

class StringConverterSpec extends Specification {

    private TypeConverter<String> stringTypeConverter


    void setup() {
        final TypeConverterFactory converterFactory = new TypeConverterFactory()
        stringTypeConverter = converterFactory.getInstance(ValueType.STRING)
    }

    void 'should convert single null value'() {
        given:
        final Object singleValue = null
        final String expectedSingleValue = null

        when:
        final StringValue stringValue = stringTypeConverter.convert(singleValue) as StringValue

        then:
        stringValue != null
        stringValue.isSingleValue()
        stringValue.getValue() == expectedSingleValue
        stringValue.getValues() == null
    }

    void 'should convert single string value'() {
        given:
        final Object singleValue = 'abc'
        final String expectedSingleValue = 'abc'

        when:
        final StringValue stringValue = stringTypeConverter.convert(singleValue) as StringValue

        then:
        stringValue != null
        stringValue.isSingleValue()
        stringValue.getValue() == expectedSingleValue
        stringValue.getValues() == null
    }

    void 'should convert collection string value'() {
        given:
        final Object collectionValue = ['a', 'b', 'c']
        final List<String> expectedCollectionValue = ['a', 'b', 'c']

        when:
        final StringValue stringValue = stringTypeConverter.convert(collectionValue) as StringValue

        then:
        stringValue != null
        !stringValue.isSingleValue()
        stringValue.getValue() == null
        stringValue.getValues() == expectedCollectionValue
    }

    void 'should convert array string value'() {
        given:
        final Object arrayValue = ['a', 'b', 'c'].toArray()
        final String[] expectedArrayValue = ['a', 'b', 'c'].toArray()

        when:
        final StringValue stringValue = stringTypeConverter.convert(arrayValue) as StringValue

        then:
        stringValue != null
        !stringValue.isSingleValue()
        stringValue.getValue() == null
        stringValue.getValues() == expectedArrayValue
    }
}
