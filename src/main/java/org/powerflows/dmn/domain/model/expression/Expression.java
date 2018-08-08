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

package org.powerflows.dmn.domain.model.expression;

import org.powerflows.dmn.domain.model.AbstractBuilder;
import org.powerflows.dmn.domain.model.Decision;

import java.io.Serializable;
import java.util.function.Consumer;

public class Expression implements Serializable {

    private static final long serialVersionUID = 1;

    private Object value;
    private ExpressionType type;

    private Expression() {
    }

    public Object getValue() {
        return value;
    }

    public ExpressionType getType() {
        return type;
    }

    public static <T extends AbstractBuilder> Builder builder(T builder, Consumer<Expression> outputConsumer) {
        return new Builder(builder, outputConsumer);
    }

    public static final class Builder<T> extends AbstractBuilder<Expression> {

        private T parentBuilder;
        private Consumer<Expression> callback;

        private Builder(T builder, Consumer<Expression> outputConsumer) {
            this.parentBuilder = builder;
            this.callback = outputConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new Expression();
        }

        public Builder value(Object value) {
            this.product.value = value;

            return this;
        }

        public Builder type(ExpressionType type) {
            this.product.type = type;

            return this;
        }

        public T and() {
            callback.accept(build());

            return parentBuilder;
        }
    }
}
