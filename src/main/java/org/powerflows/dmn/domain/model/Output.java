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

package org.powerflows.dmn.domain.model;

import java.io.Serializable;
import java.util.function.Consumer;

public class Output implements Serializable {

    private static final long serialVersionUID = 1;

    private String name;
    private String description;

    private Output() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static Builder builder(Decision.Builder builder, Consumer<Output> outputConsumer) {
        return new Builder(builder, outputConsumer);
    }

    public static final class Builder extends AbstractBuilder<Output> {

        private Decision.Builder parentBuilder;
        private Consumer<Output> callback;

        private Builder(Decision.Builder builder, Consumer<Output> outputConsumer) {
            this.parentBuilder = builder;
            this.callback = outputConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new Output();
        }

        public Builder name(String name) {
            this.product.name = name;

            return this;
        }

        public Builder description(String description) {
            this.product.description = description;

            return this;
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
