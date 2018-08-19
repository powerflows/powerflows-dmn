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

package org.powerflows.dmn.domain.model.decision.rule;

import org.powerflows.dmn.domain.model.builder.AbstractBuilder;
import org.powerflows.dmn.domain.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.domain.model.decision.rule.entry.OutputEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.unmodifiableList;

public class Rule implements Serializable {

    private static final long serialVersionUID = 1;

    private String description;
    private List<InputEntry> inputEntries = new ArrayList<>();
    private List<OutputEntry> outputEntries = new ArrayList<>();

    private Rule() {
    }

    public String getDescription() {
        return description;
    }

    public List<InputEntry> getInputEntries() {
        return inputEntries;
    }

    public List<OutputEntry> getOutputEntries() {
        return outputEntries;
    }

    public static <P extends AbstractBuilder> Builder<P> builder(P parentBuilder, Consumer<Rule> ruleConsumer) {
        return new Builder<>(parentBuilder, ruleConsumer);
    }

    public static final class Builder<P extends AbstractBuilder> extends AbstractBuilder<Rule> {

        private P parentBuilder;
        private Consumer<Rule> callback;

        private Builder(P parentBuilder, Consumer<Rule> ruleConsumer) {
            this.parentBuilder = parentBuilder;
            this.callback = ruleConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new Rule();
        }

        public Builder<P> description(String description) {
            this.product.description = description;

            return this;
        }

        public InputEntry.Builder<Builder<P>> withInputEntries() {
            final Consumer<InputEntry> inputEntryConsumer = inputEntry -> this.product.inputEntries.add(inputEntry);

            return InputEntry.builder(this, inputEntryConsumer);
        }

        public OutputEntry.Builder<Builder<P>> withOutputEntries() {
            final Consumer<OutputEntry> outputEntryConsumer = outputEntry -> this.product.outputEntries.add(outputEntry);

            return OutputEntry.builder(this, outputEntryConsumer);
        }

        public Builder<P> next() {
            accept();

            return builder(parentBuilder, callback);
        }

        public P end() {
            accept();

            return parentBuilder;
        }

        private void accept() {
            this.product.inputEntries = unmodifiableList(this.product.inputEntries);
            this.product.outputEntries = unmodifiableList(this.product.outputEntries);
            callback.accept(build());
        }
    }
}
