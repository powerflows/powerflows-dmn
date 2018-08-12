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

package org.powerflows.dmn.domain.model.rule;

import org.powerflows.dmn.domain.model.AbstractBuilder;
import org.powerflows.dmn.domain.model.Builder;
import org.powerflows.dmn.domain.model.ElementBuilder;
import org.powerflows.dmn.domain.model.rule.entry.InputEntry;
import org.powerflows.dmn.domain.model.rule.entry.OutputEntry;

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

    public static <T, B extends Builder<T>> RuleBuilder<T, B> builder(B builder, Consumer<Rule> ruleConsumer) {
        return new RuleBuilder<>(builder, ruleConsumer);
    }

    public static final class RuleBuilder<T, B extends Builder<T>> extends AbstractBuilder<Rule> implements ElementBuilder<Rule, RuleBuilder<T, B>, B> {

        private B parentBuilder;
        private Consumer<Rule> callback;

        private RuleBuilder(B builder, Consumer<Rule> ruleConsumer) {
            this.parentBuilder = builder;
            this.callback = ruleConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new Rule();
        }

        public RuleBuilder<T, B> description(String description) {
            this.product.description = description;

            return this;
        }

        public InputEntry.InputEntryBuilder<Rule, RuleBuilder<T, B>> withInputEntries() {
            final Consumer<InputEntry> inputEntryConsumer = this.product.inputEntries::add;

            return InputEntry.builder(this, inputEntryConsumer);
        }

        public OutputEntry.OutputEntryBuilder<Rule, RuleBuilder<T, B>> withOutputEntries() {
            final Consumer<OutputEntry> outputEntryConsumer = this.product.outputEntries::add;

            return OutputEntry.builder(this, outputEntryConsumer);
        }

        public RuleBuilder<T, B> next() {
            accept();

            return this;
        }

        public B done() {
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
