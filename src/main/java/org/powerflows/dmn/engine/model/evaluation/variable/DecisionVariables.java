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
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * Variables container used to pass values to decision engine.
 * {@link DecisionVariables} is immutable.
 */
@ToString(callSuper = true)
public class DecisionVariables extends AbstractDecisionVariables implements Serializable {

    private static final long serialVersionUID = 1;

    public DecisionVariables(Map<String, Serializable> variables) {
        super.variables = unmodifiableMap(variables);
    }

}
