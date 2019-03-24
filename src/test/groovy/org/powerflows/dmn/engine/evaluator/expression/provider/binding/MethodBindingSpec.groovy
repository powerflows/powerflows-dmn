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
package org.powerflows.dmn.engine.evaluator.expression.provider.binding

import org.powerflows.dmn.engine.evaluator.expression.ExpressionEvaluationException
import org.powerflows.dmn.engine.evaluator.expression.provider.sample.MethodSource
import spock.lang.Specification

import java.lang.reflect.Method
import java.util.function.Supplier

class MethodBindingSpec extends Specification {
    void 'StaticMethodBinding should not allow non static methods for configuration'() {
        given:
        final Method method = MethodSource.class.getMethod('sampleInstanceMethod', Integer.TYPE, String)

        when:
        new StaticMethodBinding('test', method)

        then:
        thrown(IllegalArgumentException)
    }

    void 'InstanceMethodBinding should not allow static methods for configuration'() {
        given:
        final Method method = MethodSource.class.getMethod('sampleStaticMethod', String, Integer.TYPE)
        final Supplier<Object> supplier = { -> null }

        when:
        new InstanceMethodBinding('test', method, supplier)

        then:
        thrown(IllegalArgumentException)
    }

    void 'MethodBinding should throw exception on private methods'() {
        given:
        final Method method = MethodSource.class.getDeclaredMethod('privateMethod', String)
        final Supplier<Object> supplier = { -> new MethodSource() }
        final MethodBinding methodBinding = new InstanceMethodBinding('test', method, supplier)

        when:
        methodBinding.execute('test')

        then:
        thrown(ExpressionEvaluationException)
    }

    void 'MethodBinding should throw exception on invalid method arguments'() {
        given:
        final Method method = MethodSource.class.getMethod('sampleInstanceMethod', Integer.TYPE, String)
        final Supplier<Object> supplier = { -> new MethodSource() }
        final MethodBinding methodBinding = new InstanceMethodBinding('test', method, supplier)

        when:
        methodBinding.execute('test')

        then:
        thrown(ExpressionEvaluationException)
    }

    void 'MethodBinding should throw exception on invalid instance source'() {
        given:
        final Method method = MethodSource.class.getMethod('sampleInstanceMethod', Integer.TYPE, String)
        final Supplier<Object> supplier = { -> 'this is string!' }
        final MethodBinding methodBinding = new InstanceMethodBinding('test', method, supplier)

        when:
        methodBinding.execute('test')

        then:
        thrown(ExpressionEvaluationException)
    }

    void 'MethodBinding should throw exception on null instance'() {
        given:
        final Method method = MethodSource.class.getMethod('sampleInstanceMethod', Integer.TYPE, String)
        final Supplier<Object> supplier = { -> null }
        final MethodBinding methodBinding = new InstanceMethodBinding('test', method, supplier)

        when:
        methodBinding.execute('test')

        then:
        thrown(ExpressionEvaluationException)
    }
}
