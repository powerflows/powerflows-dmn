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

public class InputEntry implements Serializable {

    private static final long serialVersionUID = 1;

    private String name;

    private InputEntry() {
    }

    public String getName() {
        return name;
    }

    public static <T, B extends Builder<T>> InputEntryBuilder<T, B> builder(B builder, Consumer<InputEntry> inputEntryConsumer) {
        return new InputEntryBuilder<>(builder, inputEntryConsumer);
    }

    public static final class InputEntryBuilder<T, B extends Builder<T>> extends AbstractBuilder<InputEntry> implements ElementBuilder<InputEntry, InputEntryBuilder<T, B>, B> {

        private B parentBuilder;
        private Consumer<InputEntry> callback;

        private InputEntryBuilder(B builder, Consumer<InputEntry> inputEntryConsumer) {
            this.parentBuilder = builder;
            this.callback = inputEntryConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new InputEntry();
        }

        public InputEntryBuilder<T, B> name(String name) {
            this.product.name = name;

            return this;
        }

        public InputEntryBuilder<T, B> next() {
            callback.accept(build());

            return this;
        }

        public B done() {
            callback.accept(build());

            return parentBuilder;
        }
    }
}
