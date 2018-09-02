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

import java.io.Serializable;
import java.util.function.Consumer;

@EqualsAndHashCode
@ToString
public class Output implements Serializable {

    private static final long serialVersionUID = 1;

    private String name;
    private String description;
    private ValueType type;

    private Output() {
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

    public static <P extends AbstractBuilder> FluentBuilder<P> fluentBuilder(P parentBuilder, Consumer<Output> outputConsumer) {
        return new FluentBuilder<>(parentBuilder, outputConsumer);
    }

    public static Builder builder() {
        return new Builder();
    }

    private static abstract class OutputBuilder<B extends Output.OutputBuilder<B>> extends AbstractBuilder<Output> {
        @Override
        protected void initProduct() {
            this.product = new Output();
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
    }

    public static final class Builder extends OutputBuilder<Builder> {

    }

    public static final class FluentBuilder<P extends AbstractBuilder> extends Output.OutputBuilder<Output.FluentBuilder<P>> {
        private final P parentBuilder;
        private final Consumer<Output> callback;

        private FluentBuilder(final P parentBuilder, final Consumer<Output> callback) {
            this.parentBuilder = parentBuilder;
            this.callback = callback;
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