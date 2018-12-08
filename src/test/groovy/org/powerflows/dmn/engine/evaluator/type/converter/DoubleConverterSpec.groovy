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
import org.powerflows.dmn.engine.evaluator.type.value.DoubleValue
import org.powerflows.dmn.engine.model.decision.field.ValueType
import spock.lang.Specification

class DoubleConverterSpec extends Specification {

    private TypeConverter<Double> doubleTypeConverter


    void setup() {
        final TypeConverterFactory converterFactory = new TypeConverterFactory()
        doubleTypeConverter = converterFactory.getInstance(ValueType.DOUBLE)
    }


    void 'should convert single double value'() {
        given:
        final Object singleValue = 5.48
        final Double expectedSingleValue = 5.48d

        when:
        final DoubleValue doubleValue = doubleTypeConverter.convert(singleValue) as DoubleValue

        then:
        doubleValue != null
        doubleValue.isSingleValue()
        doubleValue.getValue() == expectedSingleValue
        doubleValue.getValues() == null
    }

    void 'should convert collection double value'() {
        given:
        final Object collectionValue = [1.345, 3.556, 7.0]
        final List<Double> expectedCollectionValue = [1.345d, 3.556d, 7.0d]

        when:
        final DoubleValue doubleValue = doubleTypeConverter.convert(collectionValue) as DoubleValue

        then:
        doubleValue != null
        !doubleValue.isSingleValue()
        doubleValue.getValue() == null
        doubleValue.getValues() == expectedCollectionValue
    }

    void 'should convert array double value'() {
        given:
        final Object arrayValue = [1.345, 3.556, 7.0].toArray()
        final List<Double> expectedCollectionValue = [1.345d, 3.556d, 7.0d]

        when:
        final DoubleValue doubleValue = doubleTypeConverter.convert(arrayValue) as DoubleValue

        then:
        doubleValue != null
        !doubleValue.isSingleValue()
        doubleValue.getValue() == null
        doubleValue.getValues() == expectedCollectionValue
    }

    void 'should convert single double value as string'() {
        given:
        final Object singleValue = '1.234'
        final Double expectedSingleValue = 1.234d

        when:
        final DoubleValue doubleValue = doubleTypeConverter.convert(singleValue) as DoubleValue

        then:
        doubleValue != null
        doubleValue.isSingleValue()
        doubleValue.getValue() == expectedSingleValue
        doubleValue.getValues() == null
    }

    void 'should throw exception when value is neither double nor string'() {
        given:
        final Object singleValue = true

        when:
        doubleTypeConverter.convert(singleValue)

        then:
        final EvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Value $singleValue is not double"
    }
}
