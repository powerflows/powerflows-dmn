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

package org.powerflows.dmn.domain.model.decision.expression;

import org.powerflows.dmn.domain.model.builder.AbstractBuilder;

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

    public static <P extends AbstractBuilder> Builder<P> builder(P parentBuilder, Consumer<Expression> expressionConsumer) {
        return new Builder<>(parentBuilder, expressionConsumer);
    }

    public static class Builder<P extends AbstractBuilder> extends AbstractBuilder<Expression> {

        private P parentBuilder;
        private Consumer<Expression> callback;

        private Builder(P parentBuilder, Consumer<Expression> expressionConsumer) {
            this.parentBuilder = parentBuilder;
            this.callback = expressionConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new Expression();
        }

        public Builder<P> value(Object value) {
            this.product.value = value;

            return this;
        }

        public Builder<P> type(ExpressionType type) {
            this.product.type = type;

            return this;
        }

        public P and() {
            callback.accept(build());

            return parentBuilder;
        }
    }

}
