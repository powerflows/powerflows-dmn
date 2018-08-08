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

package org.powerflows.dmn.domain.model.rule.entry;

import org.powerflows.dmn.domain.model.builder.AbstractBuilder;
import org.powerflows.dmn.domain.model.expression.Expression;

import java.io.Serializable;
import java.util.function.Consumer;

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

    public static <P extends AbstractBuilder> Builder<P> builder(P parentBuilder, Consumer<InputEntry> inputEntryConsumer) {
        return new Builder<>(parentBuilder, inputEntryConsumer);
    }

    public static final class Builder<P extends AbstractBuilder> extends AbstractBuilder<InputEntry> {

        private P parentBuilder;
        private Consumer<InputEntry> callback;

        private Builder(P parentBuilder, Consumer<InputEntry> inputEntryConsumer) {
            this.parentBuilder = parentBuilder;
            this.callback = inputEntryConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new InputEntry();
        }

        public Builder<P> name(String name) {
            this.product.name = name;

            return this;
        }

        public Expression.Builder<Builder<P>> withExpression() {
            final Consumer<Expression> expressionConsumer = expression -> this.product.expression = expression;

            return Expression.builder(this, expressionConsumer);
        }

        public Builder<P> next() {
            callback.accept(build());

            return builder(parentBuilder, callback);
        }

        public P end() {
            callback.accept(build());

            return parentBuilder;
        }
    }
}
