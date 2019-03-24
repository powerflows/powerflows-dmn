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
package org.powerflows.dmn.engine.evaluator.expression.provider.juel;

import lombok.extern.slf4j.Slf4j;
import org.powerflows.dmn.engine.evaluator.expression.ExpressionEvaluationException;
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.InstanceMethodBinding;
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.MethodBinding;
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.StaticMethodBinding;

import javax.el.FunctionMapper;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maps functions from {@link MethodBinding} collection.
 * Only static methods are supported with JUEL.
 */
@Slf4j
public class MethodBindingListFunctionMapper extends FunctionMapper {
    private final Map<String, Method> functions;

    public MethodBindingListFunctionMapper(final List<MethodBinding> methodBindings) {
        functions = new HashMap<>();
        methodBindings.forEach(methodBinding -> {
            if (methodBinding instanceof InstanceMethodBinding) {
                throw new ExpressionEvaluationException("Cannot bind instance method " + methodBinding
                        .name() + " on " + methodBinding.method() + " as instance method binding is not supported for JUEL");
            } else if (methodBinding instanceof StaticMethodBinding) {
                functions.put(methodBinding.name(), methodBinding.method());
            }
        });
    }

    @Override
    public Method resolveFunction(final String prefix, final String localName) {
        return functions.get(localName);
    }
}