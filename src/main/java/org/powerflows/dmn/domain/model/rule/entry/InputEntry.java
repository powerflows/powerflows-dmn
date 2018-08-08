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
import org.powerflows.dmn.domain.model.rule.Rule;

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

    public static Builder builder(Rule.Builder builder, Consumer<InputEntry> inputEntryConsumer) {
        return new Builder(builder, inputEntryConsumer);
    }

    public static final class Builder extends AbstractBuilder<InputEntry> {

        private Rule.Builder parentBuilder;
        private Consumer<InputEntry> callback;

        private Builder(Rule.Builder builder, Consumer<InputEntry> inputEntryConsumer) {
            this.parentBuilder = builder;
            this.callback = inputEntryConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new InputEntry();
        }

        public Builder name(String name) {
            this.product.name = name;

            return this;
        }

        public Builder next() {
            callback.accept(build());

            return this;
        }

        public Rule.Builder end() {
            callback.accept(build());

            return parentBuilder;
        }
    }
}
