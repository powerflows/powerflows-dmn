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

package org.powerflows.dmn.domain.model.evaluation.result;


import org.powerflows.dmn.domain.model.builder.AbstractBuilder;

import java.io.Serializable;
import java.util.function.Consumer;

public class EntryResult implements Serializable {

    private static final long serialVersionUID = 1;

    private String name;
    private Object value;

    private EntryResult() {
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public static <P extends AbstractBuilder> Builder<P> builder(P parentBuilder, Consumer<EntryResult> entryResultConsumer) {
        return new Builder<>(parentBuilder, entryResultConsumer);
    }

    public static class Builder<P extends AbstractBuilder> extends AbstractBuilder<EntryResult> {

        private P parentBuilder;
        private Consumer<EntryResult> callback;

        private Builder(P parentBuilder, Consumer<EntryResult> entryResultConsumer) {
            this.parentBuilder = parentBuilder;
            this.callback = entryResultConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new EntryResult();
        }

        public Builder<P> name(String name) {
            this.product.name = name;

            return this;
        }

        public Builder<P> value(Object value) {
            this.product.value = value;

            return this;
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
