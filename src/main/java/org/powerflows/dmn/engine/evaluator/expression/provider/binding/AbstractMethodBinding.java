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
package org.powerflows.dmn.engine.evaluator.expression.provider.binding;

import org.powerflows.dmn.engine.evaluator.expression.ExpressionEvaluationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * Base class for implementors of {@link MethodBinding}.
 * Handles basic method call delegation.
 */
public abstract class AbstractMethodBinding implements MethodBinding {
    private final String name;
    private final Supplier<Object> instanceSupplier;
    private final Method method;

    public AbstractMethodBinding(final String name, final Method method, final Supplier<Object> instanceSupplier) {
        this.name = name;
        this.instanceSupplier = instanceSupplier;
        this.method = method;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Method method() {
        return method;
    }

    @Override
    public Object execute(final Object... args) {
        try {
            return method.invoke(instanceSupplier.get(), args);
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException | NullPointerException e) {
            throw new ExpressionEvaluationException(e);
        }
    }
}
