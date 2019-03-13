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

package org.powerflows.dmn.engine.model.decision;

/**
 * Defines how decision result is created from evaluated rules.
 */
public enum HitPolicy {
    /**
     * One single rule can be matched.
     * All rules are evaluated.
     */
    UNIQUE,
    /**
     * First found matching rule is used for result.
     * Rules are evaluated top-down in defined order.
     */
    FIRST,
    /**
     * Single found matching rule is used for result.
     * If decision rules are evaluated in undefined order like in multi threaded environment any matched rule is used.
     */
    ANY,
    /**
     * All matching rules are used for result.
     * May result in multiple results.
     */
    COLLECT,
    /**
     * Equivalent to FIRST.
     */
    PRIORITY,
    /**
     * Equivalent to COLLECT
     */
    RULE_ORDER,
    /**
     * Equivalent to COLLECT
     */
    OUTPUT_ORDER
}
