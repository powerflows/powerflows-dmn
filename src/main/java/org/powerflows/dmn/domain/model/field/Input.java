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

package org.powerflows.dmn.domain.model.field;

import org.powerflows.dmn.domain.model.builder.AbstractBuilder;
import org.powerflows.dmn.domain.model.expression.Expression;

import java.io.Serializable;
import java.util.function.Consumer;

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

    public static <P extends AbstractBuilder> Builder<P> builder(P parentBuilder, Consumer<Input> inputConsumer) {
        return new Builder<>(parentBuilder, inputConsumer);
    }

    public static final class Builder<P extends AbstractBuilder> extends AbstractBuilder<Input> {

        private P parentBuilder;
        private Consumer<Input> callback;

        private Builder(P parentBuilder, Consumer<Input> inputConsumer) {
            this.parentBuilder = parentBuilder;
            this.callback = inputConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new Input();
        }

        public Builder<P> name(String name) {
            this.product.name = name;

            return this;
        }

        public Builder<P> description(String description) {
            this.product.description = description;

            return this;
        }

        public Builder<P> type(ValueType type) {
            this.product.type = type;

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
