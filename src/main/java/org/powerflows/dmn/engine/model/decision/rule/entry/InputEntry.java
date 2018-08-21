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

import org.powerflows.dmn.engine.model.builder.AbstractBuilder;
import org.powerflows.dmn.engine.model.decision.expression.Expression;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

public class InputEntry implements Serializable {

    private static final long serialVersionUID = 1;

    private String name;
    private Expression expression;

    private InputEntry() {
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }

    public static <P extends AbstractBuilder> FluentBuilder<P> fluentBuilder(final P parentBuilder, final Consumer<InputEntry> inputEntryConsumer) {
        return new FluentBuilder<>(parentBuilder, inputEntryConsumer);
    }

    public static Builder builder() {
        return new Builder();
    }

    private static abstract class InputEntryBuilder<B extends InputEntryBuilder<B>> extends AbstractBuilder<InputEntry> {

        @Override
        protected void initProduct() {
            this.product = new InputEntry();
        }

        public B name(String name) {
            this.product.name = name;

            return (B) this;
        }
    }

    public static final class Builder extends InputEntryBuilder<Builder> {

        public Builder withExpression(final Function<Expression.Builder, Expression> expressionBuilderConsumer) {
            this.product.expression = expressionBuilderConsumer.apply(Expression.builder());

            return this;
        }
    }

    public static final class FluentBuilder<P extends AbstractBuilder> extends InputEntryBuilder<FluentBuilder<P>> {
        private final P parentBuilder;
        private final Consumer<InputEntry> callback;

        private FluentBuilder(final P parentBuilder, final Consumer<InputEntry> callback) {
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