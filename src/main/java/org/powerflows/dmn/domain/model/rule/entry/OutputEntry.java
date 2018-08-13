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

package org.powerflows.dmn.domain.model.rule.entry;

import org.powerflows.dmn.domain.model.AbstractBuilder;
import org.powerflows.dmn.domain.model.Builder;
import org.powerflows.dmn.domain.model.ElementBuilder;

import java.io.Serializable;
import java.util.function.Consumer;

public class OutputEntry implements Serializable {

    private static final long serialVersionUID = 1;

    private String name;

    private OutputEntry() {
    }

    public String getName() {
        return name;
    }

    public static <T, B extends Builder<T>> OutputEntryBuilder<T, B> builder(B builder, Consumer<OutputEntry> outputEntryConsumer) {
        return new OutputEntryBuilder<>(builder, outputEntryConsumer);
    }

    public static final class OutputEntryBuilder<T, B extends Builder<T>> extends AbstractBuilder<OutputEntry> implements ElementBuilder<OutputEntry, OutputEntryBuilder<T, B>, B> {

        private B parentBuilder;
        private Consumer<OutputEntry> callback;

        private OutputEntryBuilder(B builder, Consumer<OutputEntry> outputEntryConsumer) {
            this.parentBuilder = builder;
            this.callback = outputEntryConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new OutputEntry();
        }

        public OutputEntryBuilder<T, B> name(String name) {
            this.product.name = name;

            return this;
        }

        public OutputEntryBuilder<T, B> next() {
            callback.accept(build());

            return this;
        }

        public B done() {
            callback.accept(build());

            return parentBuilder;
        }
    }
}
