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

package org.powerflows.dmn.engine.evaluator.context;


import org.powerflows.dmn.engine.model.evaluation.context.AbstractContextVariables;
import org.powerflows.dmn.engine.model.evaluation.context.ContextVariables;

import java.io.Serializable;
import java.util.HashMap;

public class ModifiableContextVariables extends AbstractContextVariables implements Serializable {

    private static final long serialVersionUID = 1;

    public ModifiableContextVariables(ContextVariables contextVariables) {
        this.variables = new HashMap<>();
        this.variables.putAll(contextVariables.getAll());
    }

    synchronized public void addVariable(final String key, Object value) {
        variables.put(key, value);
    }
}
