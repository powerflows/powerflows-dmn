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

import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertySubstitute;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CustomPropertyUtils extends PropertyUtils {

    private Map<Class<?>, Map<String, Property>> propertiesCache = new HashMap<>();

    private static final Map<String, String> PROPERTY_NAME_MAP = new HashMap<>();

    static {
        PROPERTY_NAME_MAP.put("hitPolicy", "hit-policy");
        PROPERTY_NAME_MAP.put("expressionType", "expression-type");
    }

    CustomPropertyUtils() {
        this.setBeanAccess(BeanAccess.FIELD);
    }

    @Override
    protected Set<Property> createPropertySet(final Class<?> type, final BeanAccess beanAccess) {
        final Set<Property> properties = new LinkedHashSet<>();
        for (Property property : getPropertiesMap(type, beanAccess).values()) {
            if (property.isReadable() && (isAllowReadOnlyProperties() || property.isWritable())) {
                properties.add(property);
            }
        }

        return properties;
    }

    @Override
    protected Map<String, Property> getPropertiesMap(final Class<?> type, final BeanAccess beanAccess) {
        if (propertiesCache.containsKey(type)) {
            return propertiesCache.get(type);
        }
        final Map<String, Property> properties = new LinkedHashMap<String, Property>();
        super.getPropertiesMap(type, beanAccess).forEach((name, property) -> {
            if (PROPERTY_NAME_MAP.containsKey(name)) {
                final String newName = PROPERTY_NAME_MAP.get(name);
                final PropertySubstitute substitute = new PropertySubstitute(newName, property.getType());
                substitute.setDelegate(property);
                properties.put(newName, substitute);
            } else {
                properties.put(name, property);
            }
        });

        propertiesCache.put(type, properties);

        return properties;
    }
}