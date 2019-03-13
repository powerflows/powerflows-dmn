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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

/**
 * Configures given instance {@link Method} to be executed as function in expression.
 */
public class InstanceMethodBinding extends AbstractMethodBinding {

    /**
     * @param name             of bound function
     * @param method           the call is delegated to
     * @param instanceSupplier supplies instance on which the method is called
     */
    public InstanceMethodBinding(final String name, final Method method, final Supplier<Object> instanceSupplier) {
        super(name, method, instanceSupplier);
        if ((method.getModifiers() & Modifier.STATIC) != 0) {
            throw new IllegalArgumentException("Provided method must not be static");
        }
    }
}
