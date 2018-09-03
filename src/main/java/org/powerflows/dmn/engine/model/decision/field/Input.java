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

package org.powerflows.dmn.engine.model.decision.field;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.powerflows.dmn.engine.model.builder.AbstractBuilder;
import org.powerflows.dmn.engine.model.decision.expression.Expression;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

@EqualsAndHashCode
@ToString
public class Input implements Serializable {

    private static final long serialVersionUID = 1;

    private String name;
    private String description;
    private ValueType type;
    private Expression expression;

    private Input() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ValueType getType() {
        return type;
    }

    public Expression getExpression() {
        return expression;
    }

    public static <P extends AbstractBuilder> FluentBuilder<P> fluentBuilder(final P parentBuilder, final Consumer<Input> inputConsumer) {
        return new FluentBuilder<>(parentBuilder, inputConsumer);
    }

    public static Builder builder() {
        return new Builder();
    }

    private static abstract class InputBuilder<B extends InputBuilder<B>> extends AbstractBuilder<Input> {

        @Override
        protected void initProduct() {
            this.product = new Input();
        }

        public B name(String name) {
            this.product.name = name;

            return (B) this;
        }

        public B description(String description) {
            this.product.description = description;

            return (B) this;
        }

        public B type(ValueType type) {
            this.product.type = type;

            return (B) this;
        }

        @Override
        protected Input assembleProduct() {
            if (product.expression == null) {
                product.expression = Expression.builder().build();
            }

            return product;
        }
    }

    public static final class Builder extends InputBuilder<Builder> {

        public Builder withExpression(final Function<Expression.Builder, Expression> expressionBuilderConsumer) {
            this.product.expression = expressionBuilderConsumer.apply(Expression.builder());

            return this;
        }
    }

    public static final class FluentBuilder<P extends AbstractBuilder> extends InputBuilder<FluentBuilder<P>> {
        private final P parentBuilder;
        private final Consumer<Input> callback;

        private FluentBuilder(final P parentBuilder, final Consumer<Input> callback) {
            this.parentBuilder = parentBuilder;
            this.callback = callback;
        }

        public Expression.FluentBuilder<FluentBuilder<P>> withExpression() {
            final Consumer<Expression> expressionConsumer = expression -> this.product.expression = expression;

            return Expression.fluentBuilder(this, expressionConsumer);
        }

        public FluentBuilder<P> next() {
            callback.accept(build());

            return fluentBuilder(parentBuilder, callback);
        }

        public P end() {
            callback.accept(build());

            return parentBuilder;
        }
    }
}