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
 * See the License for the specific language governing permissions end
 * limitations under the License.
 */

package org.powerflows.dmn.domain.model;

import org.powerflows.dmn.domain.model.expression.Expression;

import java.io.Serializable;
import java.util.function.Consumer;

public class Output implements Serializable {

    private static final long serialVersionUID = 1;

    private String name;
    private String description;
    private Expression expression;

    private Output() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Expression getExpression() {
        return expression;
    }

    public static <T, B extends Builder<T>> OutputBuilder<T, B> builder(B builder, Consumer<Output> outputConsumer) {
        return new OutputBuilder<>(builder, outputConsumer);
    }

    public static final class OutputBuilder<T, B extends Builder<T>> extends AbstractBuilder<Output> implements ElementBuilder<Output, OutputBuilder<T, B>, B> {

        private B parentBuilder;
        private Consumer<Output> callback;

        private OutputBuilder(B builder, Consumer<Output> outputConsumer) {
            this.parentBuilder = builder;
            this.callback = outputConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new Output();
        }

        public OutputBuilder<T, B> name(String name) {
            this.product.name = name;

            return this;
        }

        public OutputBuilder<T, B> description(String description) {
            this.product.description = description;

            return this;
        }

        public Expression.ExpressionBuilder<Output, OutputBuilder<T, B>> withExpression() {
            final Consumer<Expression> expressionConsumer = expression -> this.product.expression = expression;

            return Expression.builder(this, expressionConsumer);
        }

        public OutputBuilder<T, B> next() {
            callback.accept(build());

            return new OutputBuilder<>(parentBuilder, callback);
        }

        public B done() {
            callback.accept(build());

            return parentBuilder;
        }
    }
}
