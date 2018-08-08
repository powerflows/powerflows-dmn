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

package org.powerflows.dmn.domain.model.input;

import org.powerflows.dmn.domain.model.AbstractBuilder;
import org.powerflows.dmn.domain.model.Decision;
import org.powerflows.dmn.domain.model.expression.Expression;

import java.io.Serializable;
import java.util.function.Consumer;

public class Input implements Serializable {

    private static final long serialVersionUID = 1;

    private String name;
    private String description;
    private InputType type;
    private Expression expression;

    private Input() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public InputType getType() {
        return type;
    }

    public Expression getExpression() {
        return expression;
    }

    public static Builder builder(Decision.Builder builder, Consumer<Input> inputConsumer) {
        return new Builder(builder, inputConsumer);
    }

    public static final class Builder extends AbstractBuilder<Input> {

        private Decision.Builder parentBuilder;
        private Consumer<Input> callback;

        private Builder(Decision.Builder builder, Consumer<Input> inputConsumer) {
            this.parentBuilder = builder;
            this.callback = inputConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new Input();
        }

        public Builder name(String name) {
            this.product.name = name;

            return this;
        }

        public Builder description(String description) {
            this.product.description = description;

            return this;
        }

        public Builder type(InputType type) {
            this.product.type = type;

            return this;
        }

        public Expression.Builder withExpression() {
            final Consumer<Expression> expressionConsumer = expression -> this.product.expression = expression;

            return Expression.builder(this, expressionConsumer);
        }

        public Builder next() {
            callback.accept(build());

            return new Builder(parentBuilder, callback);
        }

        public Decision.Builder end() {
            callback.accept(build());

            return parentBuilder;
        }
    }
}
