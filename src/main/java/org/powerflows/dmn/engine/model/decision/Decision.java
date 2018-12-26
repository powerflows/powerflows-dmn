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

package org.powerflows.dmn.engine.model.decision;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.powerflows.dmn.engine.model.builder.AbstractBuilder;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.field.Output;
import org.powerflows.dmn.engine.model.decision.rule.Rule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Collections.unmodifiableList;

@EqualsAndHashCode
@ToString
public class Decision implements Serializable {

    private static final long serialVersionUID = 1;

    private String id;
    private String name;
    private HitPolicy hitPolicy;
    private ExpressionType expressionType;
    private EvaluationMode evaluationMode;
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

    public EvaluationMode getEvaluationMode() {
        return evaluationMode;
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

    public static FluentBuilder fluentBuilder() {
        return new FluentBuilder();
    }

    public static Builder builder() {
        return new Builder();
    }

    private abstract static class DecisionBuilder<B extends DecisionBuilder<B>> extends AbstractBuilder<Decision> {
        @Override
        protected void initProduct() {
            this.product = new Decision();
        }

        public B id(String id) {
            this.product.id = id;

            return (B) this;
        }

        public B name(String name) {
            this.product.name = name;

            return (B) this;
        }

        public B hitPolicy(HitPolicy hitPolicy) {
            this.product.hitPolicy = hitPolicy;

            return (B) this;
        }

        public B expressionType(ExpressionType expressionType) {
            this.product.expressionType = expressionType;

            return (B) this;
        }

        public B evaluationMode(EvaluationMode evaluationMode) {
            this.product.evaluationMode = evaluationMode;

            return (B) this;
        }

        @Override
        protected Decision assembleProduct() {
            this.product.inputs = unmodifiableList(this.product.inputs);
            this.product.outputs = unmodifiableList(this.product.outputs);
            this.product.rules = unmodifiableList(this.product.rules);

            return this.product;
        }
    }

    public static final class Builder extends DecisionBuilder<Builder> {
        public Builder withInput(final Function<Input.Builder, Input> inputsBuilderConsumer) {
            this.product.inputs.add(inputsBuilderConsumer
                    .apply(Input.builder()));

            return this;
        }

        public Builder withOutput(final Function<Output.Builder, Output> outputsBuilderConsumer) {
            this.product.outputs.add(outputsBuilderConsumer
                    .apply(Output.builder()));

            return this;
        }

        public Builder withRule(final Function<Rule.Builder, Rule> ruleBuilderConsumer) {
            this.product.rules.add(ruleBuilderConsumer.apply(Rule.builder()));

            return this;
        }
    }

    public static final class FluentBuilder extends DecisionBuilder<FluentBuilder> {

        private FluentBuilder() {
        }

        public Input.FluentBuilder<FluentBuilder> withInputs() {
            final Consumer<Input> inputConsumer = this.product.inputs::add;

            return Input.fluentBuilder(this, inputConsumer);
        }

        public Output.FluentBuilder<FluentBuilder> withOutputs() {
            final Consumer<Output> outputConsumer = this.product.outputs::add;

            return Output.fluentBuilder(this, outputConsumer);
        }

        public Rule.FluentBuilder<FluentBuilder> withRules() {
            final Consumer<Rule> ruleConsumer = this.product.rules::add;

            return Rule.fluentBuilder(this, ruleConsumer);
        }
    }
}