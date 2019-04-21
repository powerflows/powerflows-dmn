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

package org.powerflows.dmn.engine.model.decision.expression;

/**
 * Types of expressions that the engine understands.
 */
public enum ExpressionType {
    /**
     * Not an expression.
     * Literal value of given type.
     */
    LITERAL,
    /**
     * Subset of OMG S-FEEL (Friendly Enough Expression Language).
     * https://www.omg.org/spec/DMN/1.1
     */
    FEEL,
    /**
     * Java Unified Expression Language.
     * http://juel.sourceforge.net/
     */
    JUEL,
    /**
     * Groovy executed with {@link javax.script.ScriptEngine}
     * http://groovy-lang.org/
     */
    GROOVY,
    /**
     * Javascript executed with {@link javax.script.ScriptEngine}
     * Since JDK8 its Nashorn https://wiki.openjdk.java.net/display/Nashorn/Main
     */
    JAVASCRIPT,
    /**
     * MVEL Expression language
     * http://mvel.documentnode.com/
     */
    MVEL
}
