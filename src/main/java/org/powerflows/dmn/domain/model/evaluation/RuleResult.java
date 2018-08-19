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

package org.powerflows.dmn.domain.model.evaluation;


import org.powerflows.dmn.domain.model.builder.AbstractBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.unmodifiableList;

public class RuleResult implements Serializable {

    private static final long serialVersionUID = 1;

    private List<EntryResult> entryResults = new ArrayList<>();

    private RuleResult() {
    }

    public List<EntryResult> getEntryResults() {
        return entryResults;
    }

    public static <P extends AbstractBuilder> Builder<P> builder(P parentBuilder, Consumer<RuleResult> ruleResultConsumer) {
        return new Builder<>(parentBuilder, ruleResultConsumer);
    }

    public static final class Builder<P extends AbstractBuilder> extends AbstractBuilder<RuleResult> {

        private P parentBuilder;
        private Consumer<RuleResult> callback;

        private Builder(P parentBuilder, Consumer<RuleResult> ruleResultConsumer) {
            this.parentBuilder = parentBuilder;
            this.callback = ruleResultConsumer;
        }

        @Override
        protected void initProduct() {
            this.product = new RuleResult();
        }

        public EntryResult.Builder<Builder<P>> withEntryResults() {
            final Consumer<EntryResult> entryResultConsumer = entryResult -> this.product.entryResults.add(entryResult);

            return EntryResult.builder(this, entryResultConsumer);
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
            this.product.entryResults = unmodifiableList(this.product.entryResults);
            callback.accept(build());
        }
    }
}
