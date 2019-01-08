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

package org.powerflows.dmn.engine.model.decision.rule.entry;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.powerflows.dmn.engine.model.builder.AbstractBuilder;
import org.powerflows.dmn.engine.model.decision.expression.Expression;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OutputEntry extends Entry {

    private static final long serialVersionUID = 1;

    private OutputEntry() {
        super();
    }

    public static <P extends AbstractBuilder> FluentBuilder<P> fluentBuilder(final P parentBuilder, final Consumer<OutputEntry> outputEntryConsumer) {
        return new FluentBuilder<>(parentBuilder, outputEntryConsumer);
    }

    public static Builder builder() {
        return new Builder();
    }

    private abstract static class OutputEntryBuilder<B extends OutputEntryBuilder<B>> extends AbstractBuilder<OutputEntry> {

        @Override
        protected void initProduct() {
            this.product = new OutputEntry();
        }

        public B name(String name) {
            this.product.name = name;

            return (B) this;
        }

        public B withLiteralValue(final Serializable literalValue) {
            this.product.expression = Expression.builder()
                    .type(ExpressionType.LITERAL)
                    .value(literalValue)
                    .build();

            return (B) this;
        }

        @Override
        protected OutputEntry assembleProduct() {
            if (product.expression == null) {
                product.expression = Expression.builder().build();
            }

            validateIsNonNull(product.name, "Name is required");

            return product;
        }
    }

    public static final class Builder extends OutputEntryBuilder<Builder> {

        public Builder withExpression(final Function<Expression.Builder, Expression> expressionBuilderConsumer) {
            this.product.expression = expressionBuilderConsumer.apply(Expression.builder());

            return this;
        }
    }

    public static final class FluentBuilder<P extends AbstractBuilder> extends OutputEntryBuilder<FluentBuilder<P>> {
        private final P parentBuilder;
        private final Consumer<OutputEntry> callback;

        private FluentBuilder(final P parentBuilder, final Consumer<OutputEntry> callback) {
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
