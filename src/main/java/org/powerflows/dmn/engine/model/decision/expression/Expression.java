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

package org.powerflows.dmn.engine.model.decision.expression;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.powerflows.dmn.engine.model.builder.AbstractBuilder;

import java.io.Serializable;
import java.util.function.Consumer;

@EqualsAndHashCode
@ToString
public class Expression implements Serializable {

    private static final long serialVersionUID = 1;

    private Serializable value;
    private ExpressionType type;

    private Expression() {
    }

    public Serializable getValue() {
        return value;
    }

    public ExpressionType getType() {
        return type;
    }

    public static <P extends AbstractBuilder> FluentBuilder<P> fluentBuilder(P parentBuilder, Consumer<Expression> expressionConsumer) {
        return new FluentBuilder<>(parentBuilder, expressionConsumer);
    }

    public static Builder builder() {
        return new Builder();
    }

    private abstract static class ExpressionBuilder<B extends Expression.ExpressionBuilder<B>> extends AbstractBuilder<Expression> {

        @Override
        protected void initProduct() {
            this.product = new Expression();
        }

        public B value(Serializable value) {
            this.product.value = value;

            return (B) this;
        }

        public B type(ExpressionType type) {
            this.product.type = type;

            return (B) this;
        }
    }

    public static class Builder extends ExpressionBuilder<Builder> {
    }

    public static class FluentBuilder<P extends AbstractBuilder> extends Expression.ExpressionBuilder<Expression.FluentBuilder<P>> {
        private final P parentBuilder;
        private final Consumer<Expression> callback;

        private FluentBuilder(P parentBuilder, Consumer<Expression> callback) {
            this.parentBuilder = parentBuilder;
            this.callback = callback;
        }

        public P and() {
            callback.accept(build());

            return parentBuilder;
        }
    }
}