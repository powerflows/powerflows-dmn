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

package org.powerflows.dmn.engine.model.evaluation.variable;


import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for decision variable holders.
 */
@ToString
public class AbstractDecisionVariables {

    protected Map<String, Serializable> variables = new HashMap<>();

    public Serializable get(final String name) {
        return variables.get(name);
    }

    public Map<String, Serializable> getAll() {
        return variables;
    }

    public boolean isPresent(final String name) {
        return variables.get(name) != null;
    }
}
