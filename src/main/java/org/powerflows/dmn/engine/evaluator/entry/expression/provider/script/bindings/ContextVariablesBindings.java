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

package org.powerflows.dmn.engine.evaluator.entry.expression.provider.script.bindings;

import org.powerflows.dmn.engine.evaluator.context.ModifiableContextVariables;

import javax.script.Bindings;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ContextVariablesBindings implements Bindings {

    private final Bindings bindings;

    private ContextVariablesBindings(final Bindings bindings, final ModifiableContextVariables contextVariables) {
        this.bindings = bindings;

        contextVariables
                .getAll()
                .keySet()
                .forEach(variableName -> this.bindings.put(variableName, contextVariables.get(variableName)));
    }

    public static ContextVariablesBindings create(final Bindings bindings, final ModifiableContextVariables contextVariables) {
        return new ContextVariablesBindings(bindings, contextVariables);
    }

    @Override
    public Object put(final String name, final Object value) {
        return bindings.put(name, value);
    }

    @Override
    public void putAll(Map<? extends String, ?> toMerge) {
        toMerge.forEach(this::put);
    }

    @Override
    public boolean containsKey(Object key) {
        return bindings.containsKey(key);
    }

    @Override
    public Object get(final Object key) {
        return bindings.get(key);
    }

    @Override
    public Object remove(final Object key) {
        return bindings.remove(key);
    }

    @Override
    public int size() {
        return bindings.size();
    }

    @Override
    public boolean isEmpty() {
        return bindings.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return bindings.keySet();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return bindings.entrySet();
    }

    @Override
    public Collection<Object> values() {
        return bindings.values();
    }

    @Override
    public void clear() {
        bindings.clear();
    }

    @Override
    public boolean containsValue(final Object value) {
        return bindings.containsValue(value);
    }
}
