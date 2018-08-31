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

package org.powerflows.dmn.io.yaml;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.MissingProperty;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.util.Map;

public class CustomPropertyUtils extends PropertyUtils {
    @Override
    public Property getProperty(final Class<?> type, final String name, final BeanAccess beanAccess) {
        final Map<String, Property> properties = getPropertiesMap(type, beanAccess);

        if (!properties.containsKey(name) && name.contains("-")) {
            properties.computeIfPresent(toCamelCase(name), (key, property) -> properties.put(name, property));
        }

        properties.computeIfAbsent(name, key -> {
            if (!isSkipMissingProperties()) {
                throw new YAMLException("Unable to find property '" + name + "' on class: " + type.getName());
            }

            return new MissingProperty(key);
        });

        return properties.get(name);
    }

    private String toCamelCase(final String name) {
        final String[] parts = name.split("-");
        final String result;
        if (parts.length > 1) {
            final StringBuilder sb = new StringBuilder(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                sb.append(capitalize(parts[i]));
            }

            result = sb.toString();
        } else {
            result = name;
        }

        return result;
    }

    private char[] capitalize(final String part) {
        final char[] chars = part.toCharArray();
        chars[0] = String.valueOf(chars[0]).toUpperCase().toCharArray()[0];

        return chars;
    }
}
