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

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import java.util.HashMap;
import java.util.Map;

public class BasicELContext extends ELContext {

    private final ELResolver resolver;
    private final FunctionMapper functionMapper;
    private final VariableMapper variableMapper;

    public BasicELContext(final FunctionMapper functionMapper) {
        final CompositeELResolver localResolver = new CompositeELResolver();
        localResolver.add(new ArrayELResolver(true));
        localResolver.add(new ListELResolver(true));
        localResolver.add(new MapELResolver(true));
        localResolver.add(new ResourceBundleELResolver());
        localResolver.add(new BeanELResolver(true));

        this.resolver = localResolver;
        this.functionMapper = functionMapper;
        this.variableMapper = new Variables();
    }

    @Override
    public ELResolver getELResolver() {
        return resolver;
    }

    @Override
    public FunctionMapper getFunctionMapper() {
        return functionMapper;
    }

    @Override
    public VariableMapper getVariableMapper() {
        return variableMapper;
    }

    public void setVariable(final String name, final ValueExpression valueExpression) {
        variableMapper.setVariable(name, valueExpression);
    }

    static class Variables extends VariableMapper {
        Map<String, ValueExpression> map = new HashMap<>();

        @Override
        public ValueExpression resolveVariable(String variable) {
            return map.get(variable);
        }

        @Override
        public ValueExpression setVariable(String variable, ValueExpression expression) {
            return map.put(variable, expression);
        }
    }
}