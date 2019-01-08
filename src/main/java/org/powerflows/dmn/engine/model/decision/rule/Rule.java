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

package org.powerflows.dmn.engine.model.decision.rule;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.powerflows.dmn.engine.model.builder.AbstractBuilder;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Collections.unmodifiableList;

@EqualsAndHashCode
@ToString
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

    public static <P extends AbstractBuilder> FluentBuilder<P> fluentBuilder(P parentBuilder, Consumer<Rule> ruleConsumer) {
        return new FluentBuilder<>(parentBuilder, ruleConsumer);
    }

    public static Builder builder() {
        return new Builder();
    }

    private abstract static class RuleBuilder<B extends Rule.RuleBuilder<B>> extends AbstractBuilder<Rule> {

        @Override
        protected void initProduct() {
            this.product = new Rule();
        }

        public B description(String description) {
            this.product.description = description;

            return (B) this;
        }

        @Override
        protected Rule assembleProduct() {
            validateIsNonEmpty(product.inputEntries, "At least one input entry is required");
            validateIsNonEmpty(product.outputEntries, "At least one output entry is required");

            this.product.inputEntries = unmodifiableList(this.product.inputEntries);
            this.product.outputEntries = unmodifiableList(this.product.outputEntries);

            return product;
        }
    }

    public static final class Builder extends RuleBuilder<Builder> {

        public Builder withInputEntry(final Function<InputEntry.Builder, InputEntry> inputEntryBuilderConsumer) {
            this.product.inputEntries.add(inputEntryBuilderConsumer.apply(InputEntry.builder()));

            return this;
        }

        public Builder withOutputEntry(final Function<OutputEntry.Builder, OutputEntry> outputEntryBuilderConsumer) {
            this.product.outputEntries.add(outputEntryBuilderConsumer.apply(OutputEntry.builder()));

            return this;
        }
    }

    public static final class FluentBuilder<P extends AbstractBuilder> extends RuleBuilder<FluentBuilder<P>> {
        private final P parentBuilder;
        private final Consumer<Rule> callback;

        private FluentBuilder(final P parentBuilder, final Consumer<Rule> callback) {
            this.parentBuilder = parentBuilder;
            this.callback = callback;
        }

        public InputEntry.FluentBuilder<FluentBuilder<P>> withInputEntries() {
            final Consumer<InputEntry> inputEntryConsumer = this.product.inputEntries::add;

            return InputEntry.fluentBuilder(this, inputEntryConsumer);
        }

        public OutputEntry.FluentBuilder<FluentBuilder<P>> withOutputEntries() {
            final Consumer<OutputEntry> outputEntryConsumer = this.product.outputEntries::add;

            return OutputEntry.fluentBuilder(this, outputEntryConsumer);
        }

        private void accept() {
            callback.accept(build());
        }

        public FluentBuilder<P> next() {
            accept();

            return fluentBuilder(parentBuilder, callback);
        }

        public P end() {
            accept();

            return parentBuilder;
        }
    }
}
