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

package org.powerflows.dmn.engine.model.evaluation.result;


import lombok.ToString;
import org.powerflows.dmn.engine.model.builder.AbstractBuilder;
import org.powerflows.dmn.engine.model.evaluation.result.exception.EvaluationResultException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * Represents decision evaluation result.
 */
@ToString
public class DecisionResult implements Serializable {

    private static final long serialVersionUID = 1;

    private static final int SINGLE_ITEM_COLLECTION_SIZE = 1;
    private static final int FIRST_ITEM_COLLECTION_INDEX = 0;

    private List<RuleResult> ruleResults = new ArrayList<>();

    private DecisionResult() {
    }

    public boolean isSingleEntryResult() {
        return isSingleRuleResult() && getSingleRuleResult().getEntryResults().size() == SINGLE_ITEM_COLLECTION_SIZE;
    }

    public boolean isSingleRuleResult() {
        return ruleResults.size() == SINGLE_ITEM_COLLECTION_SIZE;
    }

    public boolean isCollectionRulesResult() {
        return !isSingleRuleResult();
    }

    public EntryResult getSingleEntryResult() {
        if (!isSingleEntryResult()) {
            throw new EvaluationResultException("Evaluation has no single entry result");
        }

        return getSingleRuleResult().getEntryResults().get(FIRST_ITEM_COLLECTION_INDEX);
    }

    public RuleResult getSingleRuleResult() {
        if (!isSingleRuleResult()) {
            throw new EvaluationResultException("Evaluation has no single result");
        }

        return ruleResults.get(FIRST_ITEM_COLLECTION_INDEX);
    }

    public List<RuleResult> getCollectionRulesResult() {
        return ruleResults;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends AbstractBuilder<DecisionResult> {

        private Builder() {
        }

        @Override
        protected void initProduct() {
            this.product = new DecisionResult();
        }

        public Builder ruleResults(List<RuleResult> ruleResults) {
            this.product.ruleResults = ruleResults;

            return this;
        }

        @Override
        protected DecisionResult assembleProduct() {
            this.product.ruleResults = unmodifiableList(this.product.ruleResults);

            return this.product;
        }
    }
}
