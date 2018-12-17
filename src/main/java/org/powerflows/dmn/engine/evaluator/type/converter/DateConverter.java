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
import org.powerflows.dmn.engine.evaluator.type.value.DateValue;
import org.powerflows.dmn.engine.evaluator.type.value.SpecifiedTypeValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DateConverter implements TypeConverter<Date> {

    /**
     * To ensure compatibility with the reading from YAML model, the following patterns are the same like in SnakeYAML library.
     */
    private final Pattern dateRegexp = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)$");

    private final Pattern dateTimeRegexp = Pattern.compile(
            "^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)(?:(?:[Tt]|[ \t]+)([0-9][0-9]?):([0-9][0-9]):([0-9][0-9])(?:\\.([0-9]*))?(?:[ \t]*(?:Z|([-+][0-9][0-9]?)(?::([0-9][0-9])?)?))?)?$");


    @Override
    public SpecifiedTypeValue<Date> convert(final Object unspecifiedValue) {
        final SpecifiedTypeValue<Date> dateTypeValue;

        if (unspecifiedValue == null) {
            final Date dateValue = null;
            dateTypeValue = new DateValue(dateValue);
        } else if (unspecifiedValue instanceof Collection) {
            final List<Date> dateValues = convertCollection((Collection<Object>) unspecifiedValue);
            dateTypeValue = new DateValue(dateValues);
        } else if (unspecifiedValue.getClass().isArray()) {
            final List<Date> dateValues = convertArray((Object[]) unspecifiedValue);
            dateTypeValue = new DateValue(dateValues);
        } else {
            final Date dateValue = convertSingleObject(unspecifiedValue);
            dateTypeValue = new DateValue(dateValue);
        }

        return dateTypeValue;
    }

    private List<Date> convertCollection(final Collection<Object> unspecifiedValues) {
        return unspecifiedValues
                .stream()
                .map(this::convertSingleObject)
                .collect(Collectors.toList());
    }

    private List<Date> convertArray(final Object[] unspecifiedValues) {
        return convertCollection(new ArrayList<>(Arrays.asList(unspecifiedValues)));
    }

    private Date convertSingleObject(final Object unspecifiedValue) {
        final Date dateValue;

        if (unspecifiedValue instanceof Date) {
            dateValue = (Date) unspecifiedValue;
        } else if (unspecifiedValue instanceof String) {
            final String stringDate = (String) unspecifiedValue;
            Matcher match = dateRegexp.matcher(stringDate);

            if (match.matches()) {
                dateValue = buildDate(match);
            } else {
                match = dateTimeRegexp.matcher(stringDate);

                if (!match.matches()) {
                    throw new EvaluationException("Value " + unspecifiedValue + " is not date");
                }

                dateValue = buildDateTime(match);
            }
        } else {
            throw new EvaluationException("Value " + unspecifiedValue + " is not date");
        }

        return dateValue;
    }

    private Date buildDate(final Matcher match) {
        final String year = match.group(1);
        final String month = match.group(2);
        final String day = match.group(3);

        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

        return calendar.getTime();
    }

    private Date buildDateTime(final Matcher match) {
        final String year = match.group(1);
        final String month = match.group(2);
        final String day = match.group(3);
        final String hour = match.group(4);
        final String min = match.group(5);
        final String second;
        final String millisecond = match.group(7);

        final String secondTmp = match.group(6);
        if (millisecond != null) {
            second = secondTmp + "." + millisecond;
        } else {
            second = secondTmp;
        }
        double doubleSecond = Double.parseDouble(second);
        int intSecond = (int) Math.round(Math.floor(doubleSecond));
        int intMillisecond = (int) Math.round((doubleSecond - intSecond) * 1000);

        final String timezoneHour = match.group(8);
        final String timezoneMinute = match.group(9);
        final TimeZone timeZone;
        if (timezoneHour != null) {
            final String time = timezoneMinute != null ? ":" + timezoneMinute : "00";
            timeZone = TimeZone.getTimeZone("GMT" + timezoneHour + time);
        } else {
            timeZone = TimeZone.getTimeZone("UTC");
        }

        final Calendar calendar = Calendar.getInstance(timeZone);
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(min));
        calendar.set(Calendar.SECOND, intSecond);
        calendar.set(Calendar.MILLISECOND, intMillisecond);

        return calendar.getTime();
    }
}
