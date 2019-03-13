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

import org.powerflows.dmn.engine.evaluator.exception.EvaluationException
import org.powerflows.dmn.engine.evaluator.type.value.BooleanValue
import org.powerflows.dmn.engine.model.decision.field.ValueType
import spock.lang.Specification

class BooleanConverterSpec extends Specification {

    private TypeConverter<Boolean> booleanTypeConverter


    void setup() {
        final TypeConverterFactory converterFactory = new TypeConverterFactory()
        booleanTypeConverter = converterFactory.getInstance(ValueType.BOOLEAN)
    }


    void 'should convert single null value'() {
        given:
        final Object singleValue = null
        final Boolean expectedSingleValue = null

        when:
        final BooleanValue booleanValue = booleanTypeConverter.convert(singleValue) as BooleanValue

        then:
        booleanValue != null
        booleanValue.isSingleValue()
        booleanValue.getValue() == expectedSingleValue
        booleanValue.getValues() == null
    }

    void 'should convert single boolean value'() {
        given:
        final Object singleValue = true
        final Boolean expectedSingleValue = true

        when:
        final BooleanValue booleanValue = booleanTypeConverter.convert(singleValue) as BooleanValue

        then:
        booleanValue != null
        booleanValue.isSingleValue()
        booleanValue.getValue() == expectedSingleValue
        booleanValue.getValues() == null
    }

    void 'should convert collection boolean value'() {
        given:
        final Object collectionValue = [true, false, true]
        final List<Boolean> expectedCollectionValue = [true, false, true]

        when:
        final BooleanValue booleanValue = booleanTypeConverter.convert(collectionValue) as BooleanValue

        then:
        booleanValue != null
        !booleanValue.isSingleValue()
        booleanValue.getValue() == null
        booleanValue.getValues() == expectedCollectionValue
    }

    void 'should convert array boolean value'() {
        given:
        final Object arrayValue = [true, false, true].toArray()
        final List<Boolean> expectedCollectionValue = [true, false, true]

        when:
        final BooleanValue booleanValue = booleanTypeConverter.convert(arrayValue) as BooleanValue

        then:
        booleanValue != null
        !booleanValue.isSingleValue()
        booleanValue.getValue() == null
        booleanValue.getValues() == expectedCollectionValue
    }

    void 'should convert single boolean true value as string'() {
        given:
        final Object singleValue = 'true'
        final Boolean expectedSingleValue = true

        when:
        final BooleanValue booleanValue = booleanTypeConverter.convert(singleValue) as BooleanValue

        then:
        booleanValue != null
        booleanValue.isSingleValue()
        booleanValue.getValue() == expectedSingleValue
        booleanValue.getValues() == null
    }

    void 'should convert single boolean false value as string'() {
        given:
        final Object singleValue = 'false'
        final Boolean expectedSingleValue = false

        when:
        final BooleanValue booleanValue = booleanTypeConverter.convert(singleValue) as BooleanValue

        then:
        booleanValue != null
        booleanValue.isSingleValue()
        booleanValue.getValue() == expectedSingleValue
        booleanValue.getValues() == null
    }

    void 'should throw exception when string value can not be parsed to boolean'() {
        given:
        final Object singleValue = 'true-false'

        when:
        booleanTypeConverter.convert(singleValue)

        then:
        final EvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Value $singleValue is not boolean"
    }

    void 'should throw exception when value is neither boolean nor string'() {
        given:
        final Object singleValue = 1

        when:
        booleanTypeConverter.convert(singleValue)

        then:
        final EvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Value $singleValue is not boolean"
    }
}
