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

import org.powerflows.dmn.domain.model.input.Input;
import org.powerflows.dmn.domain.model.rule.Rule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.unmodifiableList;

public class Decision implements Serializable {

    private static final long serialVersionUID = 1;

    private String id;
    private String name;
    private HitPolicy hitPolicy;
    private List<Input> inputs = new ArrayList<>();
    private List<Output> outputs = new ArrayList<>();
    private List<Rule> rules = new ArrayList<>();

    private Decision() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HitPolicy getHitPolicy() {
        return hitPolicy;
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public static DecisionBuilder builder() {
        return new DecisionBuilder();
    }

    public static final class DecisionBuilder extends AbstractBuilder<Decision> implements Builder<Decision> {

        private DecisionBuilder() {
        }

        @Override
        protected void initProduct() {
            this.product = new Decision();
        }

        public DecisionBuilder id(String id) {
            this.product.id = id;

            return this;
        }

        public DecisionBuilder name(String name) {
            this.product.name = name;

            return this;
        }

        public DecisionBuilder hitPolicy(HitPolicy hitPolicy) {
            this.product.hitPolicy = hitPolicy;

            return this;
        }

        public Input.InputBuilder<Decision, DecisionBuilder> withInputs() {
            final Consumer<Input> inputConsumer = this.product.inputs::add;

            return Input.builder(this, inputConsumer);
        }

        public Output.OutputBuilder<Decision, DecisionBuilder> withOutputs() {
            final Consumer<Output> outputConsumer = this.product.outputs::add;

            return Output.builder(this, outputConsumer);
        }

        public Rule.RuleBuilder<Decision, DecisionBuilder> withRules() {
            final Consumer<Rule> ruleConsumer = this.product.rules::add;

            return Rule.builder(this, ruleConsumer);
        }

        public Decision build() {
            this.product.inputs = unmodifiableList(this.product.inputs);
            this.product.outputs = unmodifiableList(this.product.outputs);
            this.product.rules = unmodifiableList(this.product.rules);

            return super.build();
        }
    }

}
