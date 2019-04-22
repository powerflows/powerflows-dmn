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
 * Determines how evaluation of input entry is handled.
 */
public enum EvaluationMode {
    /**
     * Boolean mode is default and represents pure OMG defined behavior.
     * Each not empty entry is evaluated and must produce boolean value.
     * Logical sum of all entry results produces rule result.
     */
    BOOLEAN,
    /**
     * Input comparison behavior treats all entry evaluation results as value to compare to the input.
     * This way expression may evaluate to any literal value including collection.
     */
    INPUT_COMPARISON
}
