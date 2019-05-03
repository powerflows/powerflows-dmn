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

package org.powerflows.dmn.engine.evaluator.expression.type.converter

import org.powerflows.dmn.engine.evaluator.exception.EvaluationException
import org.powerflows.dmn.engine.evaluator.type.converter.TypeConverter
import org.powerflows.dmn.engine.evaluator.type.converter.TypeConverterFactory
import org.powerflows.dmn.engine.evaluator.type.value.IntegerValue
import org.powerflows.dmn.engine.model.decision.field.ValueType
import spock.lang.Specification

class IntegerConverterSpec extends Specification {

    private TypeConverter<Integer> integerTypeConverter


    void setup() {
        final TypeConverterFactory converterFactory = new TypeConverterFactory()
        integerTypeConverter = converterFactory.getInstance(ValueType.INTEGER)
    }


    void 'should convert single null value'() {
        given:
        final Object singleValue = null
        final Integer expectedSingleValue = null

        when:
        final IntegerValue integerValue = integerTypeConverter.convert(singleValue) as IntegerValue

        then:
        integerValue != null
        integerValue.isSingleValue()
        integerValue.getValue() == expectedSingleValue
        integerValue.getValues() == null
    }

    void 'should convert single integer value'() {
        given:
        final Object singleValue = 5
        final Integer expectedSingleValue = 5

        when:
        final IntegerValue integerValue = integerTypeConverter.convert(singleValue) as IntegerValue

        then:
        integerValue != null
        integerValue.isSingleValue()
        integerValue.getValue() == expectedSingleValue
        integerValue.getValues() == null
    }

    void 'should convert collection integer value'() {
        given:
        final Object collectionValue = [1, 3, 7]
        final List<Integer> expectedCollectionValue = [1, 3, 7]

        when:
        final IntegerValue integerValue = integerTypeConverter.convert(collectionValue) as IntegerValue

        then:
        integerValue != null
        !integerValue.isSingleValue()
        integerValue.getValue() == null
        integerValue.getValues() == expectedCollectionValue
    }

    void 'should convert array integer value'() {
        given:
        final Object arrayValue = [1, 3, 7].toArray()
        final List<Integer> expectedCollectionValue = [1, 3, 7]

        when:
        final IntegerValue integerValue = integerTypeConverter.convert(arrayValue) as IntegerValue

        then:
        integerValue != null
        !integerValue.isSingleValue()
        integerValue.getValue() == null
        integerValue.getValues() == expectedCollectionValue
    }

    void 'should convert single integer value as string'() {
        given:
        final Object singleValue = '5'
        final Integer expectedSingleValue = 5

        when:
        final IntegerValue integerValue = integerTypeConverter.convert(singleValue) as IntegerValue

        then:
        integerValue != null
        integerValue.isSingleValue()
        integerValue.getValue() == expectedSingleValue
        integerValue.getValues() == null
    }

    void 'should throw exception when number is non-integer value'() {
        given:
        final Object singleValue = 1.1

        when:
        integerTypeConverter.convert(singleValue)

        then:
        final EvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Value $singleValue is not integer"
    }

    void 'should throw exception when string value can not be parsed to integer'() {
        given:
        final Object singleValue = '1x'

        when:
        integerTypeConverter.convert(singleValue) as IntegerValue

        then:
        final EvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Value $singleValue is not integer"
    }

    void 'should throw exception when value is neither number nor string'() {
        given:
        final Object singleValue = true

        when:
        integerTypeConverter.convert(singleValue)

        then:
        final EvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Value $singleValue is not integer"
    }
}
