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
package org.powerflows.dmn.engine.evaluator.expression.provider.mvel;

import org.mvel2.integration.VariableResolver;
import org.mvel2.integration.impl.CachingMapVariableResolverFactory;
import org.mvel2.integration.impl.SimpleSTValueResolver;

import java.util.Map;

/**
 * {@link CachingMapVariableResolverFactory} uses exception driven programming instead of null checks so we extend it and change this behavior.
 */
public class FastCachingMapVariableResolverFactory extends CachingMapVariableResolverFactory {

    public FastCachingMapVariableResolverFactory(final Map variables) {
        super(variables);
    }

    @Override
    public VariableResolver createVariable(final String name, Object value) {
        VariableResolver vr = getVariableResolverOrNull(name);
        if (vr == null) {
            vr = new SimpleSTValueResolver(value, null, true);
            addResolver(name, vr);
        } else {
            vr.setValue(value);
        }

        return vr;
    }

    private VariableResolver getVariableResolverOrNull(final String name) {
        VariableResolver vr = variableResolvers.get(name);
        if (vr == null) {
            if (variables.containsKey(name)) {
                vr = new SimpleSTValueResolver(variables.get(name), null);
                variableResolvers.put(name, vr);
            } else if (nextFactory != null) {
                vr = nextFactory.getVariableResolver(name);
            }
        }

        return vr;
    }
}
