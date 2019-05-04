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
import org.powerflows.dmn.engine.evaluator.type.value.DateValue
import org.powerflows.dmn.engine.model.decision.field.ValueType
import spock.lang.Specification

import java.text.SimpleDateFormat

class DateConverterSpec extends Specification {

    private TypeConverter<Date> dateTypeConverter


    void setup() {
        final TypeConverterFactory converterFactory = new TypeConverterFactory()
        dateTypeConverter = converterFactory.getInstance(ValueType.DATE)
    }

    void 'should convert single null value'() {
        given:
        final Object singleValue = null
        final Date expectedSingleValue = null

        when:
        final DateValue dateValue = dateTypeConverter.convert(singleValue) as DateValue

        then:
        dateValue != null
        dateValue.isSingleValue()
        dateValue.getValue() == expectedSingleValue
        dateValue.getValues() == null
    }

    void 'should convert single date value'() {
        given:
        final Object singleValue = Date.parse('yyyy-MM-dd', '2018-12-15')
        final Date expectedSingleValue = Date.parse('yyyy-MM-dd', '2018-12-15')

        when:
        final DateValue dateValue = dateTypeConverter.convert(singleValue) as DateValue

        then:
        dateValue != null
        dateValue.isSingleValue()
        dateValue.getValue() == expectedSingleValue
        dateValue.getValues() == null
    }

    void 'should convert collection date value'() {
        given:
        final Date date1 = Date.parse('yyyy-MM-dd', '2018-12-15')
        final Date date2 = Date.parse('yyyy-MM-dd', '2018-12-16')
        final Date date3 = Date.parse('yyyy-MM-dd', '2018-12-17')
        final Object collectionValue = [date1, date2, date3]
        final List<Date> expectedCollectionValue = [date1, date2, date3]

        when:
        final DateValue dateValue = dateTypeConverter.convert(collectionValue) as DateValue

        then:
        dateValue != null
        !dateValue.isSingleValue()
        dateValue.getValue() == null
        dateValue.getValues() == expectedCollectionValue
    }

    void 'should convert array date value'() {
        given:
        final Date date1 = Date.parse('yyyy-MM-dd', '2018-12-15')
        final Date date2 = Date.parse('yyyy-MM-dd', '2018-12-16')
        final Date date3 = Date.parse('yyyy-MM-dd', '2018-12-17')
        final Object arrayValue = [date1, date2, date3].toArray()
        final List<Date> expectedCollectionValue = [date1, date2, date3]

        when:
        final DateValue dateValue = dateTypeConverter.convert(arrayValue) as DateValue

        then:
        dateValue != null
        !dateValue.isSingleValue()
        dateValue.getValue() == null
        dateValue.getValues() == expectedCollectionValue
    }

    void 'should convert single date value as string'() {
        given:
        final Object singleValue = '2018-12-15'
        final SimpleDateFormat format = new SimpleDateFormat('yyyy-MM-dd');
        format.setTimeZone(TimeZone.getTimeZone('UTC'))
        final Date expectedSingleValue = format.parse('2018-12-15')

        when:
        final DateValue dateValue = dateTypeConverter.convert(singleValue) as DateValue

        then:
        dateValue != null
        dateValue.isSingleValue()
        dateValue.getValue() == expectedSingleValue
        dateValue.getValues() == null
    }

    void 'should convert single datetime value as string'() {
        given:
        final Object singleValue = '2018-12-15T11:22:33'
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone('UTC'))
        final Date expectedSingleValue = format.parse('2018-12-15T11:22:33')

        when:
        final DateValue dateValue = dateTypeConverter.convert(singleValue) as DateValue

        then:
        dateValue != null
        dateValue.isSingleValue()
        dateValue.getValue() == expectedSingleValue
        dateValue.getValues() == null
    }

    void 'should convert single datetime with milliseconds value as string'() {
        given:
        final Object singleValue = '2018-12-15T11:22:33.123'
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        format.setTimeZone(TimeZone.getTimeZone('UTC'))
        final Date expectedSingleValue = format.parse('2018-12-15T11:22:33.123')

        when:
        final DateValue dateValue = dateTypeConverter.convert(singleValue) as DateValue

        then:
        dateValue != null
        dateValue.isSingleValue()
        dateValue.getValue() == expectedSingleValue
        dateValue.getValues() == null
    }

    void 'should convert single datetime with non-Z and with minutes timezone value as string'() {
        given:
        final Object singleValue = '2018-12-14T01:00:00+01:00'
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        format.setTimeZone(TimeZone.getTimeZone('UTC'))
        final Date expectedSingleValue = format.parse('2018-12-14T01:00:00+0100')

        when:
        final DateValue dateValue = dateTypeConverter.convert(singleValue) as DateValue

        then:
        dateValue != null
        dateValue.isSingleValue()
        dateValue.getValue() == expectedSingleValue
        dateValue.getValues() == null
    }

    void 'should convert single datetime with non-Z and without minutes timezone value as string'() {
        given:
        final Object singleValue = '2018-12-14T01:00:00.000+01'
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        format.setTimeZone(TimeZone.getTimeZone('UTC'))
        final Date expectedSingleValue = format.parse('2018-12-14T01:00:00.000+0100')

        when:
        final DateValue dateValue = dateTypeConverter.convert(singleValue) as DateValue

        then:
        dateValue != null
        dateValue.isSingleValue()
        dateValue.getValue() == expectedSingleValue
        dateValue.getValues() == null
    }

    void 'should convert single datetime with Z timezone value as string'() {
        given:
        final Object singleValue = '2018-12-14T01:00:00.000Z'
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        format.setTimeZone(TimeZone.getTimeZone('UTC'))
        final Date expectedSingleValue = format.parse('2018-12-14T01:00:00.000')

        when:
        final DateValue dateValue = dateTypeConverter.convert(singleValue) as DateValue

        then:
        dateValue != null
        dateValue.isSingleValue()
        dateValue.getValue() == expectedSingleValue
        dateValue.getValues() == null
    }

    void 'should throw exception when string value can not be parsed to date'() {
        given:
        final Object singleValue = '2018.12.15'

        when:
        dateTypeConverter.convert(singleValue) as DateValue

        then:
        final EvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Value $singleValue is not date"
    }

    void 'should throw exception when value is neither date nor string'() {
        given:
        final Object singleValue = true

        when:
        dateTypeConverter.convert(singleValue)

        then:
        final EvaluationException exception = thrown()
        exception != null
        exception.getMessage() == "Value $singleValue is not date"
    }
}
