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

package org.powerflows.dmn.domain.model.decision;

import org.powerflows.dmn.domain.model.builder.AbstractBuilder;
import org.powerflows.dmn.domain.model.decision.expression.ExpressionType;
import org.powerflows.dmn.domain.model.decision.field.Input;
import org.powerflows.dmn.domain.model.decision.field.Output;
import org.powerflows.dmn.domain.model.decision.rule.Rule;

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
    private ExpressionType expressionType;
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

    public ExpressionType getExpressionType() {
        return expressionType;
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

    public static <P extends AbstractBuilder> Builder<P> builder() {
        return new Builder<>();
    }

    public static final class Builder<P extends AbstractBuilder> extends AbstractBuilder<Decision> {

        private Builder() {
        }

        @Override
        protected void initProduct() {
            this.product = new Decision();
        }

        public Builder<P> id(String id) {
            this.product.id = id;

            return this;
        }

        public Builder<P> name(String name) {
            this.product.name = name;

            return this;
        }

        public Builder<P> hitPolicy(HitPolicy hitPolicy) {
            this.product.hitPolicy = hitPolicy;

            return this;
        }

        public Builder<P> expressionType(ExpressionType expressionType) {
            this.product.expressionType = expressionType;

            return this;
        }

        public Input.Builder<Builder<P>> withInputs() {
            final Consumer<Input> inputConsumer = input -> this.product.inputs.add(input);

            return Input.builder(this, inputConsumer);
        }

        public Output.Builder<Builder<P>> withOutputs() {
            final Consumer<Output> outputConsumer = output -> this.product.outputs.add(output);

            return Output.builder(this, outputConsumer);
        }

        public Rule.Builder<Builder<P>> withRules() {
            final Consumer<Rule> ruleConsumer = rule -> this.product.rules.add(rule);

            return Rule.builder(this, ruleConsumer);
        }

        @Override
        public Decision build() {
            this.product.inputs = unmodifiableList(this.product.inputs);
            this.product.outputs = unmodifiableList(this.product.outputs);
            this.product.rules = unmodifiableList(this.product.rules);

            return super.build();
        }
    }

}
